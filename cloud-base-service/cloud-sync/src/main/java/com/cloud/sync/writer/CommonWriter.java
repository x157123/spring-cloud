package com.cloud.sync.writer;

import com.cloud.sync.vo.ColumnConfigVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonWriter {

    private Long connectionId;

    private String writeRecordSql;

    private int columnNumber = 0;

    private Triple<List<String>, List<Integer>, List<String>> resultSetMetaData;

    private DataBaseType dataBaseType;

    private Map<String, String> associateColumn;

    private final Logger LOG = LoggerFactory.getLogger(CommonWriter.class);

    public static CommonWriter getCommonWriter(Long connectionId, String tableName, List<ColumnConfigVo> columnConfigVoList
            , Triple<List<String>, List<Integer>, List<String>> resultSetMetaData, DataBaseType dataBaseType, Map<String, String> associateColumn) {
        CommonWriter commonWriter = new CommonWriter();
        commonWriter.connectionId = connectionId;
        commonWriter.resultSetMetaData = resultSetMetaData;
        commonWriter.dataBaseType = dataBaseType;
        commonWriter.columnNumber = columnConfigVoList.size();
        commonWriter.getWriteRecordSql(tableName, columnConfigVoList);
        commonWriter.associateColumn = associateColumn;
        return commonWriter;
    }


    public void doBatchInsert(List<Map<String, String>> buffer)
            throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnect(this.connectionId);
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);
            for (Map<String, String> record : buffer) {
                preparedStatement = fillPreparedStatement(
                        preparedStatement, record);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("回滚此次写入, 采用每次写入一行方式提交. 因为:" + e.getMessage());
            connection.rollback();
            doOneInsert(connection, buffer);
        } catch (Exception e) {
            System.out.println("系统级别错误");
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, null);
        }
    }

    private void doOneInsert(Connection connection, List<Map<String, String>> buffer) {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(true);
            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);
            for (Map<String, String> record : buffer) {
                try {
                    preparedStatement = fillPreparedStatement(
                            preparedStatement, record);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    LOG.debug(e.toString());

                } finally {
                    // 最后不要忘了关闭 preparedStatement
                    preparedStatement.clearParameters();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库写入错误");
        } finally {
            DBUtil.closeDBResources(preparedStatement, null);
        }
    }


    // 直接使用了两个类变量：columnNumber,resultSetMetaData
    private PreparedStatement fillPreparedStatement(PreparedStatement preparedStatement, Map<String, String> record)
            throws SQLException {
        for (int i = 0; i < this.columnNumber; i++) {
            int columnSqltype = this.resultSetMetaData.getMiddle().get(i);
            String typeName = this.resultSetMetaData.getRight().get(i);
            String name = this.resultSetMetaData.getLeft().get(i);
            preparedStatement = fillPreparedStatementColumnType(preparedStatement, i, columnSqltype, typeName, record.get(associateColumn.get(name)));
        }
        return preparedStatement;
    }

    private PreparedStatement fillPreparedStatementColumnType(PreparedStatement preparedStatement, int columnIndex,
                                                              int columnSqltype, String typeName, String column) throws SQLException {
        java.util.Date utilDate;
        switch (columnSqltype) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                preparedStatement.setString(columnIndex + 1, column);
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                if (null == column || "".equals(column.trim())) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, column);
                }
                break;
            case Types.TINYINT:
                Long longValue = Long.parseLong(column);
                if (null == longValue) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, longValue.toString());
                }
                break;
            case Types.DATE:
                if (typeName == null) {
                    typeName = this.resultSetMetaData.getRight().get(columnIndex);
                }
                if (typeName.equalsIgnoreCase("year")) {
                    if (column == null) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {
                        preparedStatement.setInt(columnIndex + 1, Integer.parseInt(column));
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    try {
                        utilDate = getData(column);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("Date 类型转换错误：[%s]", column));
                    }

                    if (null != utilDate) {
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    }
                    preparedStatement.setDate(columnIndex + 1, sqlDate);
                }
                break;

            case Types.TIME:
                java.sql.Time sqlTime = null;
                try {
                    utilDate = getData(column);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("TIME 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                preparedStatement.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                try {
                    utilDate = getData(column);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("TIMESTAMP 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTimestamp = new java.sql.Timestamp(
                            utilDate.getTime());
                }
                preparedStatement.setTimestamp(columnIndex + 1, sqlTimestamp);
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                preparedStatement.setBytes(columnIndex + 1, column.getBytes());
                break;

            case Types.BOOLEAN:
                preparedStatement.setBoolean(columnIndex + 1, Boolean.parseBoolean(column));
                break;

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                if (this.dataBaseType == DataBaseType.MySql) {
                    preparedStatement.setBoolean(columnIndex + 1, Boolean.parseBoolean(column));
                } else {
                    preparedStatement.setString(columnIndex + 1, column);
                }
                break;
            default:
                throw new RuntimeException(String.format(
                        "数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], 字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                        this.resultSetMetaData.getLeft()
                                .get(columnIndex),
                        this.resultSetMetaData.getMiddle()
                                .get(columnIndex),
                        this.resultSetMetaData.getRight()
                                .get(columnIndex)));
        }
        return preparedStatement;
    }

    private void getWriteRecordSql(String tableName, List<ColumnConfigVo> columnConfigVoList) {
        List<String> columns = columnConfigVoList.stream().map(ColumnConfigVo::getColumnName).collect(Collectors.toList());
        String writeDataSqlTemplate;
        if (dataBaseType == DataBaseType.MySql) {
            writeDataSqlTemplate = new StringBuilder()
                    .append("REPLACE INTO ").append(tableName).append(" (").append(StringUtils.join(columns, ","))
                    .append(") VALUES(")
                    .append(String.join(", ", Collections.nCopies(columns.size(), "?")))
                    .append(")").toString();

        }
        if (dataBaseType == DataBaseType.PostgreSQL) {
            List<String> keys = columnConfigVoList.stream().filter(b -> b.getColumnPrimaryKey() > 0).map(ColumnConfigVo::getColumnName).collect(Collectors.toList());
            //update只在mysql下使用
            StringBuilder stringBuilder = new StringBuilder()
                    .append("INSERT INTO ").append(tableName).append(" (").append(StringUtils.join(columns, ","))
                    .append(") VALUES(")
                    .append(String.join(", ", Collections.nCopies(columns.size(), "?")))
                    .append(")")
                    .append("ON CONFLICT (" + String.join(",", keys) + ")")
                    .append(")")
                    .append("DO UPDATE SET ");
            for (ColumnConfigVo columnConfigVo : columnConfigVoList) {
                if (!keys.contains(columnConfigVo.getColumnName())) {
                    stringBuilder.append(columnConfigVo.getColumnName() + "= EXCLUDED." + columnConfigVo.getColumnName() + ",");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            writeDataSqlTemplate = stringBuilder.toString();

        } else {
            List<String> keys = columnConfigVoList.stream().filter(b -> b.getColumnPrimaryKey() > 0).map(ColumnConfigVo::getColumnName).collect(Collectors.toList());
            //update只在mysql下使用
            writeDataSqlTemplate = new StringBuilder()
                    .append("INSERT INTO ").append(tableName).append(" (").append(StringUtils.join(columns, ","))
                    .append(") VALUES(")
                    .append(String.join(", ", Collections.nCopies(columns.size(), "?")))
                    .append(")")
                    .append(onDuplicateKeyUpdateString(keys))
                    .toString();
        }
        this.writeRecordSql = writeDataSqlTemplate;
    }

    private static String onDuplicateKeyUpdateString(List<String> columnHolders) {
        if (columnHolders == null || columnHolders.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ON DUPLICATE KEY UPDATE ");
        boolean first = true;
        for (String column : columnHolders) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(column);
            sb.append("=VALUES(");
            sb.append(column);
            sb.append(")");
        }

        return sb.toString();
    }

    private static Date getData(String str) throws ParseException {
        if (str != null && str.length() > 0) {
            return DateUtils.parseDate(str, new String[]{"yyyy-MM-dd HH:mm:ss", "HH:mm:ss"});
        }
        return null;
    }
}
