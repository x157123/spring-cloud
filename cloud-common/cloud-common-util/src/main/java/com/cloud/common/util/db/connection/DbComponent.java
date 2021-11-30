package com.cloud.common.util.db.connection;


import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;
import com.cloud.common.util.db.functional.DbReadDataCallBack;

import java.sql.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author liulei
 */
public abstract class DbComponent {

    /**
     * 数据库连接
     */
    private Connection conn = null;

    /**
     * 驱动
     */
    private String driver = "";

    /**
     * 连接地址
     */
    private String url;

    /**
     * ip地址
     */
    private String ip = "";

    /**
     * 端口
     */
    private String port = "";

    /**
     * sid
     */
    String sid = "";

    /**数据库*/
    String database = "";

    /**
     * 用户
     */
    private String name = "";

    /**
     * 密码
     */
    private String pwd = "";

    /**
     * 获取所有表属性
     *
     * @param tables
     * @return
     */
    public abstract List<TableEntity> getTable(String... tables);

    /**
     * 获取表列属性
     *
     * @param tableEntity
     */
    public abstract void setTableColumn(TableEntity tableEntity);

    /**
     * 打印表创建语句sql
     *
     * @param tableEntity
     */
    public abstract void printCreateTableSql(TableEntity tableEntity);

    /**
     * 数据映射
     *
     * @param columnEntity
     * @return
     */
    abstract String dbMapping(ColumnEntity columnEntity);

    /**
     * 初始化数据库连接
     *
     * @param driver
     * @param ip
     * @param port
     * @param sid
     * @param name
     * @param pwd
     * @return
     */
    Connection initConnection(String driver, String url, String ip, String port, String sid, String database, String name, String pwd) {
        this.driver = driver;
        this.url = url;
        this.ip = ip;
        this.port = port;
        this.sid = sid;
        this.database = database;
        this.name = name;
        this.pwd = pwd;
        return this.getJdbcConnection();
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    Connection getJdbcConnection() {
        try {
            if (conn != null) {
                return conn;
            }
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(String.format(url, ip, port, sid), name, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库
     */
    void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据读取
     *
     * @param sql
     * @param s
     * @param target
     * @param readDataCallBack
     * @param <S>
     * @param <T>
     */
    public <S, T> void readJdbcData(String sql, S s, Supplier<T> target, DbReadDataCallBack<S, T> readDataCallBack) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getJdbcConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                if (readDataCallBack != null) {
                    T t = target.get();
                    readDataCallBack.callBack(s, t, rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取查询语句
     *
     * @param tableName 表名
     * @param list      表所有字段
     * @param where     追加条件
     * @return
     */
    String getJdbcQueryData(String tableName, List<ColumnEntity> list, String where) {
        StringBuffer query = new StringBuffer();
        StringBuffer values = new StringBuffer();
        query.append("select ");
        for (int i = 0; i < list.size(); i++) {
            if (values.length() > 0) {
                values.append(",");
            }
            values.append(list.get(i).getColumnName().toLowerCase());
        }
        query.append(values);
        query.append(" from ");
        query.append(tableName);
        if (where != null) {
            query.append(where);
        }
        return query.toString();
    }

    /**
     * 获取插入语句模板
     *
     * @param tableName
     * @param list
     * @return
     */
    String getJDBCInsert(String tableName, List<ColumnEntity> list) {
        StringBuffer sb = new StringBuffer();
        StringBuffer values = new StringBuffer();
        sb.append("INSERT INTO ");
        sb.append(tableName.toLowerCase());
        sb.append("(");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
                values.append(",");
            }
            values.append("?");
            sb.append(list.get(i).getColumnName().toLowerCase());
        }
        sb.append(") VALUES (");
        sb.append(values);
        sb.append(")");
        return sb.toString();
    }

    /**
     * 拼接字符串
     *
     * @param symbol   拼接每个name前后追加符号
     * @param splicing 拼接符号
     * @param names    需要拼接的字符串
     * @return
     */
    String splicingNames(String symbol, String splicing, String... names) {
        StringBuffer queryTables = new StringBuffer();
        if(names.length>0) {
            for (String name : names) {
                if (queryTables != null && queryTables.length() > 0) {
                    queryTables.append(splicing);
                }
                if (symbol != null) {
                    queryTables.append(symbol);
                }
                queryTables.append(name);
                if (symbol != null) {
                    queryTables.append(symbol);
                }
            }
        }
        return queryTables.toString();
    }
}
