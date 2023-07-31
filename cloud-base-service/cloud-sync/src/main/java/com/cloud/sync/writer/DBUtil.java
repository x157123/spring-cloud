package com.cloud.sync.writer;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {


    private static final Map<Long, HikariDataSource> dataSourceMap = new HashMap<>();


    private static HikariDataSource getDataSource(String url, String username, String password, String driver) {
        // 创建一个 HikariDataSource 实例。
        HikariDataSource dataSource = new HikariDataSource();
        // 设置 HikariDataSource 的属性。
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        dataSource.setMinimumIdle(3);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }

    public static synchronized Connection getConnect(Long connectionId) {
        try {
            HikariDataSource dataSource = dataSourceMap.get(connectionId);
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("获取数据库链接错误：" + connectionId);
        }
    }

    public static synchronized Connection getConnect(Long connectionId, String url, String username, String password, String driver) {
        try {
            HikariDataSource dataSource = dataSourceMap.get(connectionId);
            if (dataSource == null || dataSource.isClosed()) {
                dataSource = getDataSource(url, username, password, driver);
                dataSourceMap.put(connectionId, dataSource);
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("创建数据库链接错误：" + url);
        }
    }

    public static synchronized void close(Long connectionId) {
        try {
            HikariDataSource dataSource = dataSourceMap.get(connectionId);
            if (dataSource == null) {
                dataSource.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("关闭数据库连接错误");
        }
    }

    /**
     * 获取表字段信息
     *
     * @param conn
     * @param tableName
     * @param columns
     * @return
     */
    public static Triple<List<String>, List<Integer>, List<String>> getColumnMetaData(
            Connection conn, String tableName, List<String> columns) {
        Statement statement = null;
        ResultSet rs = null;

        Triple<List<String>, List<Integer>, List<String>> columnMetaData = new ImmutableTriple<List<String>, List<Integer>, List<String>>(
                new ArrayList<String>(), new ArrayList<Integer>(),
                new ArrayList<String>());
        try {
            statement = conn.createStatement();
            String queryColumnSql = "select " + String.join(", ", columns) + " from " + tableName
                    + " where 1=2";

            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                columnMetaData.getLeft().add(rsMetaData.getColumnName(i + 1));
                columnMetaData.getMiddle().add(rsMetaData.getColumnType(i + 1));
                columnMetaData.getRight().add(
                        rsMetaData.getColumnTypeName(i + 1));
            }
            return columnMetaData;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("获取表:%s 的字段的元信息时失败. 请联系 DBA 核查该库、表信息.", tableName));
        } finally {
            DBUtil.closeDBResources(rs, statement, null);
        }
    }


    /**
     * Close {@link ResultSet}, {@link Statement} referenced by this
     * {@link ResultSet}
     *
     * @param rs {@link ResultSet} to be closed
     * @throws IllegalArgumentException
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (null != rs) {
                Statement stmt = rs.getStatement();
                if (null != stmt) {
                    stmt.close();
                    stmt = null;
                }
                rs.close();
            }
            rs = null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void closeDBResources(ResultSet rs, Statement stmt,
                                        Connection conn) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException unused) {
            }
        }

        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException unused) {
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException unused) {
            }
        }
    }

    public static void closeDBResources(Statement stmt, Connection conn) {
        closeDBResources(null, stmt, conn);
    }
}
