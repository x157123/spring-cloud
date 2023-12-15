package com.cloud.common.util.db.connection;


import com.cloud.common.util.bean.BeanUtil;
import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;
import com.cloud.common.util.db.functional.DbReadDataCallBack;
import com.cloud.common.util.db.functional.DbReadDataCallBackFun;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * 数据库
     */
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
    public Connection getJdbcConnection() {
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
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 数据读取转为List Map
     *
     * @param sql
     */
    public List<Map<String, Object>> readJdbcData(String sql) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        try {
            ps = getJdbcConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            list = new ArrayList<>(rs.getRow());
            // 获取结果集的数据源
            ResultSetMetaData result = rs.getMetaData();
            // 获取结果集中的字段数
            int count = result.getColumnCount();
            while (rs.next()) {
                Map map = new HashMap<String, Object>();
                // 循环取出个字段的名字以及他们的值并将其作为值赋给对应的实体对象的属性
                for (int k = 0; k < count; k++) {
                    try {
                        // 获取字段名
                        String name = result.getColumnName(k + 1);
                        String properties = dbNameToClassName(name);
                        Object value = rs.getObject(name);
                        if (value != null) {
                            // 将结果集中的值赋给相应的对象实体的属性
                            if ("java.math.BigDecimal".equals(value.getClass().getName())
                                    || "java.lang.Integer".equals(value.getClass().getName())
                                    || "java.lang.Long".equals(value.getClass().getName())) {
                                value = Long.parseLong(value.toString());
                            }
                        }
                        map.put(name, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            list = new ArrayList<>();
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
            return list;
        }
    }

    /**
     * 数据读取转为对象
     *
     * @param sql
     * @param target
     * @param readDataCallBack
     * @param <T>
     */
    public <T> List<T> readJdbcData(String sql, Supplier<T> target, DbReadDataCallBackFun<T> readDataCallBack) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            ps = getJdbcConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            list = new ArrayList<>(rs.getRow());
            // 获取结果集的数据源
            ResultSetMetaData result = rs.getMetaData();
            // 获取结果集中的字段数
            int count = result.getColumnCount();
            while (rs.next()) {
                T t = target.get();
                // 循环取出个字段的名字以及他们的值并将其作为值赋给对应的实体对象的属性
                for (int k = 0; k < count; k++) {
                    try {
                        // 获取字段名
                        String name = result.getColumnName(k + 1);
                        String properties = dbNameToClassName(name);
                        Object value = rs.getObject(name);
                        if (value != null) {
                            // 将结果集中的值赋给相应的对象实体的属性
                            if ("java.math.BigDecimal".equals(value.getClass().getName())) {
                                value = Long.parseLong(value.toString());
                            }else if ("java.math.BigInteger".equals(value.getClass().getName())) {
                                value = Long.parseLong(value.toString());
                            }else{
                                value = value.toString();
                            }
                            BeanUtil.setProperties(t, properties, value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (readDataCallBack != null) {
                    readDataCallBack.callBack(t, rs);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            list = new ArrayList<>();
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
            return list;
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
     * 插入数据
     *
     * @param tableName
     * @param columnList
     * @param listData
     * @return
     */
    public boolean insert(String tableName, List<ColumnEntity> columnList, List<Map<String, Object>> listData) {
        String insertSql = getJDBCInsert(tableName, columnList);
        Connection connection = this.getJdbcConnection();
        try {
            PreparedStatement prest = connection
                    .prepareStatement(insertSql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
            connection.setAutoCommit(false);
            Integer index;
            for (Map<String, Object> map : listData) {
                index = 1;
                for (ColumnEntity columnEntity : columnList) {
                    prest.setString(index, map.get(columnEntity.getColumnName()).toString());
                    prest.setObject(index, map.get(columnEntity.getColumnName()));
                    index++;
                }
                prest.addBatch();
            }
            prest.executeBatch();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        if (names.length > 0) {
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


    String dbNameToClassName(String tableName) {
        StringBuilder properties = new StringBuilder();
        String[] names = tableName.split("_");
        if (names.length > 0) {
            for (int g = 0; g < names.length; g++) {
                String str = names[g].toLowerCase();
                if (g != 0) {
                    properties.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
                } else {
                    properties.append(str);
                }
            }
        }
        return properties.toString();
    }
}
