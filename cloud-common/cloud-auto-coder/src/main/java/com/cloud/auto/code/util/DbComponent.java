package com.cloud.auto.code.util;


import com.cloud.common.core.utils.BeanUtil;

import java.sql.*;
import java.util.ArrayList;
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

    /**连接地址*/
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
    private String sid = "";

    /**
     * 用户
     */
    private String name = "";

    /**
     * 密码
     */
    private String pwd = "";


    /**
     * 初始化数据库连接
     * @param driver
     * @param ip
     * @param port
     * @param sid
     * @param name
     * @param pwd
     * @return
     */
    Connection initConnection(String driver,String url,String ip,String port,String sid,String name,String pwd){
        this.driver = driver;
        this.url = url;
        this.ip = ip;
        this.port = port;
        this.sid = sid;
        this.name = name;
        this.pwd = pwd;
        return this.getJdbcConnection();
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    private Connection getJdbcConnection() {
        try {
            if(conn!=null){
                return conn;
            }
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(String.format(url,ip,port,sid), name, pwd);
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
     * @param target
     * @param readDataCallBack
     * @param <T>
     */
    <T> List<T> readJdbcData(String sql, Supplier<T> target, DbReadDataCallBack<T> readDataCallBack) {
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
                        String name = result.getColumnName(k+1);
                        String properties = dbNameToClassName(name);
                        Object value = rs.getObject(name);
                        if (value != null) {
                            // 将结果集中的值赋给相应的对象实体的属性
                            if ("java.math.BigDecimal".equals(value.getClass().getName())) {
                                value = Long.parseLong(value.toString());
                            }
                            BeanUtil.setProperties(t, properties, value);
                        }
                    }catch (Exception e){
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
     * @param target
     * @param <T>
     */
    <T> List<T> readJdbcData(String sql, Supplier<T> target) {
        return this.readJdbcData(sql,target,null);
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
        return queryTables.toString();
    }


    String dbNameToClassName(String tableName){
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
