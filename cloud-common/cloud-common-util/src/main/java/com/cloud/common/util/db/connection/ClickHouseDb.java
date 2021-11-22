package com.cloud.common.util.db.connection;


import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liulei
 */
public class ClickHouseDb extends DbComponent {

    /**
     * 默认数据库连接地址
     */
    private static String defUrl = "jdbc:clickhouse://%s:%s/%s";

    /**
     * 默认驱动地址
     */
    private static String defDriver = "ru.yandex.clickhouse.ClickHouseDriver";

    /**
     * 查询表
     */
    private String queryTable = "SELECT name FROM system.tables where name in (%s)";

    /**
     * 查询表字段
     */
    private String queryTableColumn = "DESCRIBE table %s";


    public ClickHouseDb() {
    }


    /**
     * 初始化数据库 并 创建数据库连接
     *
     * @param ip
     * @param port
     * @param sid
     * @param name
     * @param pwd
     * @return
     */
    public static DbComponent createDb(String ip, String port, String sid, String database, String name, String pwd) {
        return createDb(defDriver, defUrl, ip, port, sid, database, name, pwd);
    }

    /**
     * 初始化数据库 并 创建数据库连接
     *
     * @param driver
     * @param url
     * @param ip
     * @param port
     * @param sid
     * @param name
     * @param pwd
     * @return
     */
    public static DbComponent createDb(String driver, String url, String ip, String port, String sid, String database, String name, String pwd) {
        ClickHouseDb clickHouseDb = new ClickHouseDb();
        clickHouseDb.initConnection(driver, url, ip, port, sid, database, name, pwd);
        return clickHouseDb;
    }


    @Override
    public List<TableEntity> getTable(String... tables) {
        List<TableEntity> list = new ArrayList<>();
        try {
            readJdbcData(String.format(queryTable, this.splicingNames("'", ",", tables).toString()), list, TableEntity::new, (k, b, rs) -> {
                b.setTableName(rs.getNString("name"));
                k.add(b);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void setTableColumn(TableEntity tableEntity) {
        List<ColumnEntity> list = new ArrayList<>();
        try {
            readJdbcData(String.format(queryTableColumn, tableEntity.getTableName()), list, ColumnEntity::new, (k, b, rs) -> {
                b.setColumnName(rs.getNString("name"));
                b.setComments(rs.getNString("comment"));
                b.setType(rs.getNString("type"));
                k.add(b);
            });
            tableEntity.setColumns(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印表创建语句sql
     *
     * @param tableEntity
     */
    @Override
    public void printCreateTableSql(TableEntity tableEntity) {
        List<ColumnEntity> columnEntityList = tableEntity.getColumns();
        String tableName = tableEntity.getTableName().toLowerCase();
        System.out.println("CREATE TABLE IF NOT EXISTS db_test_pg_sync." + tableName + "(");
        String orderBy = "";
        boolean bool = false;
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < columnEntityList.size(); i++) {
            ColumnEntity obj = columnEntityList.get(i);
            String type = dbMapping(obj);
            String columnName = obj.getColumnName().toLowerCase();
            if (keys.contains(columnName)) {
                continue;
            }
            if(i>=1){
                System.out.print(",");
                System.out.println("");
            }
            keys.add(columnName);
            if ("create_date".equals(columnName)) {
                bool = true;
            }
            if ("org_code".equals(columnName)
                    || "create_org_code".equals(columnName) || "create_date".equals(columnName)
                    || "org_internal_code".equals(columnName) || "org_type".equals(columnName)) {
                if (orderBy.length() > 0) {
                    orderBy += ",";
                }
                orderBy += columnName;
            } else {
                if (!"id".equals(columnName)) {
                    type = "Nullable(" + type + ")";
                }
            }
            if (obj.getComments() == null || columnName.equals(obj.getComments())) {
                System.out.print("  " + columnName + " " + type);
            } else {
                System.out.print("  " + columnName + " " + type + " COMMENT '" + obj.getComments() + "'");
            }
        }
        System.out.println(")");
        System.out.println("ENGINE = MergeTree");
        if (bool) {
//            分区
            System.out.println("PARTITION BY toYYYYMM(create_date)");
        }
        System.out.println("ORDER BY (" + orderBy + ")");
        System.out.println("SETTINGS index_granularity = 8192;");
    }

    /**
     * 数据映射
     *
     * @param columnEntity
     * @return
     */
    @Override
    String dbMapping(ColumnEntity columnEntity) {
        switch (columnEntity.getType()) {
            case DATE:
                return "DateTime64";
            case STRING:
                return "String";
            case LONG:
                return "Int64";
            case INTEGER:
                if(columnEntity.getLength()<=2){
                    return "Int8";
                }else if(columnEntity.getLength()<=4){
                    return "Int32";
                }
                return "Int64";
            case DOUBLE:
                return "Decimal(" + columnEntity.getLength() + "," + columnEntity.getScale() + ")";
        }
        return null;
    }

}
