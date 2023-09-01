package com.cloud.sync.writer;

import org.apache.commons.lang.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

public enum DataBaseType {
    MONgdb(0L, "mongodb", "", "com.mysql.jdbc.Driver", "io.debezium.connector.mongodb.MongoDbConnector"),
    MySql(1L, "mysql", "jdbc:mysql://${hostname}:${prot}/${database}", "com.mysql.jdbc.Driver", "io.debezium.connector.mysql.MySqlConnector"),
    Oracle(2L, "oracle", "", "oracle.jdbc.OracleDriver", "io.debezium.connector.oracle.OracleConnector"),
    SQLServer(3L, "sqlserver", "", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "io.debezium.connector.sqlserver.SqlServerConnector"),
    PostgreSQL(4L, "postgresql", "jdbc:postgresql://${hostname}:${prot}/${database}", "org.postgresql.Driver", "io.debezium.connector.postgresql.PostgresConnector"),
    DB2(5L, "db2", "", "com.ibm.db2.jcc.DB2Driver", "io.debezium.connector.db2.Db2Connector"),
    Tddl(6L, "mysql", "", "com.mysql.jdbc.Driver", ""),
    DRDS(7L, "drds", "", "com.mysql.jdbc.Driver", ""),
    RDBMS(8L, "rdbms", "", "com.alibaba.datax.plugin.rdbms.util.DataBaseType", ""),
    ADB(9L, "adb", "", "com.mysql.jdbc.Driver", ""),
    ADS(10L, "ads", "", "com.mysql.jdbc.Driver", ""),
    ClickHouse(11L, "clickhouse", "", "ru.yandex.clickhouse.ClickHouseDriver", ""),
    KingbaseES(12L, "kingbasees", "", "com.kingbase8.Driver", ""),
    Oscar(13L, "oscar", "", "com.oscar.Driver", ""),
    OceanBase(14L, "oceanbase", "", "com.alipay.oceanbase.jdbc.Driver", ""),
    StarRocks(15L, "starrocks", "", "com.mysql.jdbc.Driver", ""),
    Databend(16L, "databend", "", "com.databend.jdbc.DatabendDriver", "");

    private Long type;
    private String typeName;
    private String jdbcUrl;
    private String driverClassName;
    private String debeziumConnector;

    DataBaseType(Long type, String typeName, String jdbcUrl, String driverClassName, String debeziumConnector) {
        this.type = type;
        this.typeName = typeName;
        this.jdbcUrl = jdbcUrl;
        this.driverClassName = driverClassName;
        this.debeziumConnector = debeziumConnector;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getDebeziumConnector() {
        return this.debeziumConnector;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public Long getType() {
        return this.type;
    }

    public static DataBaseType getDataBaseType(Long type) {
        DataBaseType[] dataBaseTypes = DataBaseType.values();
        for (DataBaseType dataBaseType : dataBaseTypes) {
            if (dataBaseType.getType().equals(type)) {
                return dataBaseType;
            }
        }
        return null;
    }

    public String getJdbcUrl(String hostname, Integer prot, String database) {
        Map<String, Object> valuesMap = new HashMap();
        valuesMap.put("hostname", hostname);
        valuesMap.put("prot", prot);
        valuesMap.put("database", database);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String content = sub.replace(this.jdbcUrl);
        return content;
    }
}
