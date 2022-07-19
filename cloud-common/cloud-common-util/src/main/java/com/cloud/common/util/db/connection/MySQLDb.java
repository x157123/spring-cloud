package com.cloud.common.util.db.connection;


import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 */
public class MySQLDb extends DbComponent {

    /**
     * 默认数据库连接地址
     */
    private static final String defUrl = "jdbc:mysql://%s:%s/%s?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" +
            "&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%%2B8&nullCatalogMeansCurrent=true&useOldAliasMetadataBehavior=true";

    /**
     * 默认驱动地址
     */
    private static final String defDriver = "com.mysql.cj.jdbc.Driver";

    /**
     * 查询表
     */
    private String queryTable = "SELECT TABLE_NAME as name, TABLE_COMMENT as comment " +
            "FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s' and table_name = %s order by CREATE_TIME asc";

    /**
     * 查询表字段
     */
    private String queryTableColumn = "select COLUMN_NAME as column_name,IS_NULLABLE as required,DATA_TYPE as data_type," +
            "ifnull( CHARACTER_MAXIMUM_LENGTH ,NUMERIC_PRECISION) as length,COLUMN_KEY as constr," +
            "NUMERIC_SCALE as accuracy,COLUMN_COMMENT as comment,column_default as defaultVal " +
            "FROM information_schema.COLUMNS WHERE table_name = '%s' and table_schema = '%s' order by ORDINAL_POSITION asc";

    private MySQLDb() {
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
        MySQLDb clickHouseDb = new MySQLDb();
        clickHouseDb.initConnection(driver, url, ip, port, sid, database, name, pwd);
        return clickHouseDb;
    }

    @Override
    public List<TableEntity> getTable(String... tables) {
        List<TableEntity> list = new ArrayList<>();
        try {
            readJdbcData(String.format(queryTable, database, this.splicingNames("'", ",", tables).toString()), list, TableEntity::new, (k, b, rs) -> {
                b.setTableName(rs.getString(1));
                b.setComments(rs.getString(2));
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
            readJdbcData(String.format(queryTableColumn, tableEntity.getTableName(), database, tableEntity.getTableName()), list, ColumnEntity::new, (k, b, rs) -> {
                try {
                    b.setColumnName(rs.getString("column_name"));
                    b.setType(rs.getString("data_type"));
                    String length = rs.getString("length");
                    if (length != null) {
                        if (length.indexOf(",") > 0) {
                            b.setLength(Integer.parseInt(length.split(",")[0]));
                        } else {
                            b.setLength(rs.getInt("length"));
                        }
                    }
                    b.setScale(rs.getInt("accuracy"));
                    b.setRequired("YES".equals(rs.getString("required")) ? Boolean.TRUE : Boolean.FALSE);
                    b.setDefaultVal(rs.getString("defaultVal"));
                    b.setComments(rs.getString("comment"));
                    k.add(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    }

    /**
     * 数据映射
     *
     * @param columnEntity
     * @return
     */
    @Override
    String dbMapping(ColumnEntity columnEntity) {
        return null;
    }

}
