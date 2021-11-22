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

    /**
     * 查询表字段
     */
    private String queryTableColumn = "select  " +
            "             tb.column_name as column_name, " +
            "             td.data_type, " +
            "             td.length, " +
            "             numeric_scale as scale, " +
            "             case tb.is_nullable when 'NO' then 1 else 0 end as required, " +
            "             tb.column_default as defaultVal, " +
            "             td.comments " +
            "            from information_schema.columns tb left join ( " +
            "            select  " +
            "    a.attname AS column_name, " +
            "    t.typname as data_type, " +
            "    SUBSTRING(format_type(a.atttypid,a.atttypmod) from '\\((.+)\\)') as length, " +
            "    d.description AS comments " +
            "    from pg_class c, pg_attribute a , pg_type t, pg_description d  " +
            "    where  c.relname = '%s' " +
            "    and a.attnum>0  " +
            "    and a.attrelid = c.oid  " +
            "    and a.atttypid = t.oid  " +
            "    and  d.objoid=a.attrelid " +
            "    and d.objsubid=a.attnum " +
            "            )td on tb.column_name = td.column_name  " +
            "            where table_schema='%s' and table_name='%s' order by ordinal_position asc";


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
                b.setColumnName(rs.getString("column_name"));
                b.setType(rs.getString("data_type"));
                String length = rs.getString("length");
                if(length!=null){
                    if(length.indexOf(",")>0){
                        b.setLength(Integer.parseInt(length.split(",")[0]));
                    }else{
                        b.setLength(rs.getInt("length"));
                    }
                }
                b.setScale(rs.getInt("scale"));
                b.setRequired(rs.getBoolean("required"));
                b.setDefaultVal(rs.getString("defaultVal"));
                b.setComments(rs.getString("comments"));
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
