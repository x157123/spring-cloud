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


    public static HikariDataSource getDataSource(String url, String username, String password, String driver) {
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
            HikariDataSource dataSource;
            if (connectionId != null) {
                dataSource = dataSourceMap.get(connectionId);
                if (dataSource == null || dataSource.isClosed()) {
                    dataSource = getDataSource(url, username, password, driver);
                    dataSourceMap.put(connectionId, dataSource);
                }
            } else {
                dataSource = getDataSource(url, username, password, driver);
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
     * 设工作表属性
     *
     * @param conn
     * @param tableName
     */
    public static List<ColumnData> getColumnDatas(Connection conn, String tableName) {
        List<ColumnData> list = new ArrayList<>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();
            try (ResultSet rs = metaData.getColumns(catalog, null, tableName, "%")) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String columnType = getTypeName(rs.getInt("DATA_TYPE"));
                    Boolean required = !"YES".equals(rs.getString("IS_NULLABLE")) ? Boolean.TRUE : Boolean.FALSE;
                    int columnSize = rs.getInt("COLUMN_SIZE");
                    String columnComment = rs.getString("REMARKS");
                    ResultSet index = conn.createStatement().executeQuery("SHOW COLUMNS FROM " + catalog + "." + tableName + " WHERE Field='" + columnName + "'");
                    Boolean uni = false;
                    if (index.next()) {
                        String key = index.getString("Key");
                        if ("UNI".equals(key)) {
                            // columnName字段为唯一索引
                            uni = true;
                        }
                    }
                    list.add(new ColumnData(columnName, columnType, required, uni, columnSize, columnComment));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取表列表
     *
     * @param conn
     * @return
     */
    public static List<String> getTables(Connection conn) {
        List<String> tables = new ArrayList<>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();
            //metaData 获取列表
            try (ResultSet rs = metaData.getTables(catalog, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableComment = rs.getString("REMARKS");
                    tables.add(tableName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * 获取类型名称
     *
     * @param dataType
     * @return
     */
    private static String getTypeName(int dataType) {
        switch (dataType) {
            case 4:
            case 5:
                return "Integer";
            case 8:
                return "Double";
            case -7:
            case -6:
            case -5:
                return "Long";
            case 1:
            case -1:
            case 12:
                return "String";
            case 91:
            case 92:
            case 93:
                return "Date";
            default:
                System.out.println("未知类型：" + dataType);
                return "Object";
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
