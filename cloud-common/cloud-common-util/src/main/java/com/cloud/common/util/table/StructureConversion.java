package com.cloud.common.util.table;

import com.cloud.common.util.db.bo.TableEntity;
import com.cloud.common.util.db.connection.ClickHouseDb;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.PostgreSQLDb;

import java.util.List;

/**
 * @author liulei
 */
public class StructureConversion {


//    public static void main(String[] args) {
//        readPostgreSQLConversion();
//    }


    private static void pgsql2Clickhouse() {

    }


    private static void oracle2Pgsql() {

    }

    /**
     * 读取oracle数据库表结构
     */
    private static void readOracleConversion() {

    }

    /**
     * 读入mysql数据表结构
     */
    private static void readMysqlConversion() {

    }

    /**
     * 读取pgsql数据库表结构
     */
    private static void readPostgreSQLConversion() {
        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.67", "5432",
                "postgres", "public", "postgres", "Tianquekeji@123");
        ClickHouseDb clickHouse = new ClickHouseDb();
        List<TableEntity> tableEntities = dbComponent.getTable(
                "sg_et_issue_attachfile"
        );
        for (TableEntity tableEntity : tableEntities) {
            dbComponent.setTableColumn(tableEntity);
            System.out.println("");
            //System.out.println("");
            //System.out.println("DROP TABLE IF EXISTS " + tableEntity.getTableName() + ";");
            System.out.println("");
            System.out.println("-----" + tableEntity.getTableName() + "   " + tableEntity.getComments());
            System.out.println("");
            clickHouse.printCreateTableSql(tableEntity);
        }
        System.out.println("");
        System.out.println("");
    }
}


