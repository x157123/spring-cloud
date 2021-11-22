package com.cloud.common.util.db.connection;


import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 */
public class OracleDb extends DbComponent {


    /**
     * 默认数据库连接地址
     */
    private static String defUrl = "jdbc:oracle:thin:@%s:%s:%s";

    /**
     * 默认驱动地址
     */
    private static String defDriver = "oracle.jdbc.driver.OracleDriver";

    /**
     * 查询表
     */
    private String queryTable = "select *  from user_tab_comments where table_name in (%s)";

    /**
     * 查询表字段
     */
    private String queryTableColumn = "SELECT ut.COLUMN_NAME, uc.comments, ut.DATA_TYPE, ut.DATA_LENGTH, ut.NULLABLE " +
            "FROM user_tab_columns ut INNER JOIN user_col_comments uc " +
            "ON ut.TABLE_NAME = uc.table_name AND ut.COLUMN_NAME = uc.column_name " +
            "WHERE ut.Table_Name = '%s'";

    private OracleDb() {
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
        OracleDb oracleDb = new OracleDb();
        oracleDb.initConnection(driver, url, ip, port, sid, database, name, pwd);
        return oracleDb;
    }

    @Override
    public List<TableEntity> getTable(String... tables) {
        List<TableEntity> list = new ArrayList<>();
        try {
            readJdbcData(String.format(queryTable, splicingNames("'", ",", tables).toString()), list, TableEntity::new, (k, b, res) -> {
                b.setTableName(res.getNString("TABLE_NAME"));
                b.setComments(res.getNString("COMMENTS"));
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
                b.setColumnName(rs.getNString("COLUMN_NAME"));
                b.setComments(rs.getNString("COMMENTS"));
                b.setType(rs.getNString("DATA_TYPE"));
                b.setLength(rs.getInt("DATA_LENGTH"));
                String st = rs.getNString("NULLABLE");
                if ("N".equals(st)) {
                    b.setRequired(true);
                }
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
