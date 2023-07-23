package com.cloud.sync.writer;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class CommonWriter {


    protected String table;

    protected List<String> columns;

    protected String writeRecordSql;

    protected int columnNumber = 0;

    protected Triple<List<String>, List<Integer>, List<String>> resultSetMetaData;

    protected boolean emptyAsNull;

    protected DataBaseType dataBaseType;

    protected static final Logger LOG = LoggerFactory
            .getLogger(CommonWriter.class);

    protected void init(){

    }

    protected void doBatchInsert(Connection connection, List<Record> buffer)
            throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);
            for (Record record : buffer) {
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
        } finally {
            DBUtil.closeDBResources(preparedStatement, null);
        }
    }

    protected void doOneInsert(Connection connection, List<Record> buffer) {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(true);
            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);
            for (Record record : buffer) {
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
    protected PreparedStatement fillPreparedStatement(PreparedStatement preparedStatement, Record record)
            throws SQLException {
        for (int i = 0; i < this.columnNumber; i++) {
            int columnSqltype = this.resultSetMetaData.getMiddle().get(i);
            String typeName = this.resultSetMetaData.getRight().get(i);
            preparedStatement = fillPreparedStatementColumnType(preparedStatement, i, columnSqltype, typeName, record.getColumn(i));
        }

        return preparedStatement;
    }

    protected PreparedStatement fillPreparedStatementColumnType(PreparedStatement preparedStatement, int columnIndex,
                                                                int columnSqltype, String typeName, Column column) throws SQLException {
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
                preparedStatement.setString(columnIndex + 1, column
                        .asString());
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                String strValue = column.asString();
                if (emptyAsNull && "".equals(strValue)) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, strValue);
                }
                break;

            //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue = column.asLong();
                if (null == longValue) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, longValue.toString());
                }
                break;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                if (typeName == null) {
                    typeName = this.resultSetMetaData.getRight().get(columnIndex);
                }

                if (typeName.equalsIgnoreCase("year")) {
                    if (column.asBigInteger() == null) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {
                        preparedStatement.setInt(columnIndex + 1, column.asBigInteger().intValue());
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    try {
                        utilDate = column.asDate();
                    } catch (Exception e) {
                        throw new RuntimeException(String.format(
                                "Date 类型转换错误：[%s]", column));
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
                    utilDate = column.asDate();
                } catch (Exception e) {
                    throw new RuntimeException(String.format(
                            "TIME 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                preparedStatement.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                try {
                    utilDate = column.asDate();
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
                preparedStatement.setBytes(columnIndex + 1, column
                        .asBytes());
                break;

            case Types.BOOLEAN:
                preparedStatement.setBoolean(columnIndex + 1, column.asBoolean());
                break;

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                if (this.dataBaseType == DataBaseType.MySql) {
                    preparedStatement.setBoolean(columnIndex + 1, column.asBoolean());
                } else {
                    preparedStatement.setString(columnIndex + 1, column.asString());
                }
                break;
            default:
                throw new RuntimeException(String.format(
                        "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], 字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                        this.resultSetMetaData.getLeft()
                                .get(columnIndex),
                        this.resultSetMetaData.getMiddle()
                                .get(columnIndex),
                        this.resultSetMetaData.getRight()
                                .get(columnIndex)));
        }
        return preparedStatement;
    }

}
