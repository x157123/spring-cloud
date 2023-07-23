package com.cloud.sync.writer;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class DBUtil {


    private static final Map<String, DataSource> dataSourceMap = new HashMap<>();


    private static DataSource getDataSource(String url, String username, String password, String driver) {
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

    public static synchronized Connection getConnect(DataBaseType dataBaseType,String url, String username, String password) {
        try {
            DataSource dataSource = dataSourceMap.get(url);
            if (dataSource == null) {
                dataSource = getDataSource(url, username, password, dataBaseType.getDriverClassName());
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("创建数据库链接错误：" + url);
        }
    }

    /**
     * 获取表字段信息
     *
     * @param conn
     * @param tableName
     * @param column
     * @return
     */
    public static Triple<List<String>, List<Integer>, List<String>> getColumnMetaData(
            Connection conn, String tableName, String column) {
        Statement statement = null;
        ResultSet rs = null;

        Triple<List<String>, List<Integer>, List<String>> columnMetaData = new ImmutableTriple<List<String>, List<Integer>, List<String>>(
                new ArrayList<String>(), new ArrayList<Integer>(),
                new ArrayList<String>());
        try {
            statement = conn.createStatement();
            String queryColumnSql = "select " + column + " from " + tableName
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
