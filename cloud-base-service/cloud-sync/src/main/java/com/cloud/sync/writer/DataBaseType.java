package com.cloud.sync.writer;

public enum DataBaseType {
    MySql(1L, "mysql", "com.mysql.jdbc.Driver", "io.debezium.connector.mysql.MySqlConnector"),
    Tddl(2L, "mysql", "com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
    DRDS(3L, "drds", "com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
    Oracle(4L, "oracle", "oracle.jdbc.OracleDriver", "oracle.jdbc.OracleDriver"),
    SQLServer(5L, "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    PostgreSQL(6L, "postgresql", "org.postgresql.Driver", "org.postgresql.Driver"),
    RDBMS(7L, "rdbms", "com.alibaba.datax.plugin.rdbms.util.DataBaseType", "com.alibaba.datax.plugin.rdbms.util.DataBaseType"),
    DB2(8L, "db2", "com.ibm.db2.jcc.DB2Driver", "com.ibm.db2.jcc.DB2Driver"),
    ADB(9L, "adb", "com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
    ADS(10L, "ads", "com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
    ClickHouse(11L, "clickhouse", "ru.yandex.clickhouse.ClickHouseDriver", "ru.yandex.clickhouse.ClickHouseDriver"),
    KingbaseES(12L, "kingbasees", "com.kingbase8.Driver", "com.kingbase8.Driver"),
    Oscar(13L, "oscar", "com.oscar.Driver", "com.oscar.Driver"),
    OceanBase(14L, "oceanbase", "com.alipay.oceanbase.jdbc.Driver", "com.alipay.oceanbase.jdbc.Driver"),
    StarRocks(15L, "starrocks", "com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
    Databend(16L, "databend", "com.databend.jdbc.DatabendDriver", "com.databend.jdbc.DatabendDriver");

    private Long type;
    private String typeName;
    private String driverClassName;

    private String debeziumConnector;

    DataBaseType(Long type, String typeName, String driverClassName, String debeziumConnector) {
        this.type = type;
        this.typeName = typeName;
        this.driverClassName = driverClassName;
        this.debeziumConnector = debeziumConnector;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getDebeziumConnector() {
        return this.debeziumConnector;
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
}
