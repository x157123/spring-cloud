package com.cloud.common.util.db.connection;


import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 */
public class PostgreSQLDb extends DbComponent {

    /**
     * 默认数据库连接地址
     */
    private static String defUrl = "jdbc:postgresql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8";

    /**
     * 默认驱动地址
     */
    private static String defDriver = "org.postgresql.Driver";

    /**
     * 查询表
     * select relname as tabname,cast(obj_description(relfilenode,'pg_class') as varchar) as comment from pg_class c
     * where relname in (select tablename from pg_tables where schemaname='public' and position('_2' in tablename)=0);
     */
    private String queryTable = "select relname as name,cast(obj_description(relfilenode,'pg_class') as varchar) as comments from pg_class c" +
            " where relname in (select tablename from pg_tables where schemaname='%s' and tablename in (%s))";


    private String queryTableAll = "select relname as name,cast(obj_description(relfilenode,'pg_class') as varchar) as comments from pg_class c" +
            " where relname in (SELECT tablename FROM pg_tables T inner JOIN pg_class C on T.tablename = C.relname WHERE T.schemaname = '%s' and C.relispartition = 'f')";
    /**
     * 查询表字段
     */
    private String queryTableColumn = "select " +
            "        a.attname as column_name, " +
            "        concat_ws('', t.typname) as data_type, " +
            "        (case when a.attlen > 0 then a.attlen when t.typname='bit' then a.atttypmod else a.atttypmod - 4 end) as length, " +
            "        numeric_scale as scale,  " +
            "        (case when a.attnotnull = true then 1 else 0 end) as required, " +
            "        (case " +
            "                when ( " +
            "                select " +
            "                        count(pg_constraint.*) " +
            "                from " +
            "                        pg_constraint " +
            "                inner join pg_class on " +
            "                        pg_constraint.conrelid = pg_class.oid " +
            "                inner join pg_attribute on " +
            "                        pg_attribute.attrelid = pg_class.oid " +
            "                        and pg_attribute.attnum = any(pg_constraint.conkey) " +
            "                inner join pg_type on " +
            "                        pg_type.oid = pg_attribute.atttypid " +
            "                where " +
            "                        pg_class.relname = c.relname " +
            "                        and pg_constraint.contype = 'p' " +
            "                        and pg_attribute.attname = a.attname) > 0 then 1 " +
            "                else 0 end) as keys, " +
            "         col.is_identity        as zizheng, " +
            "         col.column_default        as defaultVal, " +
            "        (select description from pg_description where objoid = a.attrelid and objsubid = a.attnum) as comments " +
            "from " +
            "        pg_class c, " +
            "        pg_attribute a , " +
            "        pg_type t, " +
            "        information_schema.columns as col " +
            "where " +
            "        table_schema = '%s' " +
            "        and c.relname = '%s' " +
            "        and a.attnum>0 " +
            "        and a.attrelid = c.oid " +
            "        and a.atttypid = t.oid " +
            "        and col.table_name=c.relname and col.column_name=a.attname " +
            "order by c.relname desc,a.attnum asc";


    private PostgreSQLDb() {
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
        PostgreSQLDb clickHouseDb = new PostgreSQLDb();
        clickHouseDb.initConnection(driver, url, ip, port, sid, database, name, pwd);
        return clickHouseDb;
    }


    @Override
    public List<TableEntity> getTable(String... tables) {
        List<TableEntity> list = new ArrayList<>();
        try {
            if(tables!=null && tables.length>0){
                readJdbcData(String.format(queryTable, database, this.splicingNames("'", ",", tables).toString()), list, TableEntity::new, (k, b, rs) -> {
                    b.setTableName(rs.getString(1));
                    b.setComments(rs.getString(2));
                    k.add(b);
                });
            }else{
                readJdbcData(String.format(queryTableAll, database, this.splicingNames("'", ",", tables).toString()), list, TableEntity::new, (k, b, rs) -> {
                    b.setTableName(rs.getString(1));
                    b.setComments(rs.getString(2));
                    k.add(b);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void setTableColumn(TableEntity tableEntity) {
        List<ColumnEntity> list = new ArrayList<>();
        try {
            readJdbcData(String.format(queryTableColumn, database, tableEntity.getTableName()), list, ColumnEntity::new, (k, b, rs) -> {
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
                    b.setScale(rs.getInt("scale"));
                    b.setRequired(rs.getBoolean("required"));
                    b.setDefaultVal(rs.getString("defaultVal"));
                    b.setComments(rs.getString("comments"));
                    k.add(b);
                }catch (Exception e){
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
