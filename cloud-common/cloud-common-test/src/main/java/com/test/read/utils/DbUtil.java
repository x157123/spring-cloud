package com.test.read.utils;

import com.cloud.common.util.db.bo.ColumnEntity;
import com.cloud.common.util.db.bo.TableEntity;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;
import com.cloud.common.util.db.connection.PostgreSQLDb;
import com.test.read.bo.Database;
import com.test.read.bo.DatabaseParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 */
public class DbUtil {

    private Map<Integer, Database> db = new HashMap<>();

    {
        db.put(1, new Database("pgsql","192.168.10.67", "5432", "tq_scgrid_base", "base_test","base_test", "base_test@123"));
        db.put(2, new Database("pgsql","192.168.10.67", "5432", "tq_scgrid_org", "org_test","org_test", "org_test@123"));
        db.put(3, new Database("pgsql","192.168.10.67", "5432", "postgres", "public", "postgres", "Tianquekeji@123"));
        db.put(4, new Database("pgsql","192.168.10.67", "5432", "tq_scgrid_company", "company_test","company_test", "company_test@123"));

        db.put(5, new Database("mysql","192.168.10.113", "3306", "doraemon-user", "doraemon-user", "root", "tianquekeji"));
        db.put(6, new Database("mysql","192.168.10.113", "3306", "doraemon-propertydict", "doraemon-propertydict", "root", "tianquekeji"));
        db.put(7, new Database("mysql","192.168.10.113", "3306", "doraemon_system", "doraemon_system", "root", "tianquekeji"));

    }

    public void showDbList() {
        db.forEach((k, v) -> {
            System.out.println(k + ":" + v.getDb() + ":" + v.getName());
        });
    }

    public Integer getDb(Integer index) {
        Database database = db.get(index);
        if (database != null) {
            return index;
        }
        return 0;
    }

    public boolean createTableSql(Integer index, String tableName, String tableNameRek, List<DatabaseParam> newList) {
        Database database = db.get(index);
        if (database != null) {
            DbComponent dbComponent = null;
            if(database.getDbType().equals("pgsql")) {
                dbComponent = PostgreSQLDb.createDb(database.getIp(), database.getPort(), database.getDb(), database.getSid(), database.getName(), database.getPwd());
            }else if(database.getDbType().equals("mysql")){
                dbComponent = MySQLDb.createDb(database.getIp(), database.getPort(), database.getDb(), database.getSid(), database.getName(), database.getPwd());
            }
            List<TableEntity> list = dbComponent.getTable(tableName);
            if (list != null && list.size() > 0) {
                TableEntity tableEntity = list.get(0);
                dbComponent.setTableColumn(tableEntity);
                if (tableEntity.getColumns() != null && tableEntity.getColumns().size() > 0) {
                    Map<String, ColumnEntity> map = new HashMap<>();
                    for (ColumnEntity columnEntity : tableEntity.getColumns()) {
                        map.put(columnEntity.getColumnName().replace("_", "").toLowerCase(), columnEntity);
                    }
                    for (DatabaseParam databaseParam : newList) {
                        ColumnEntity columnEntity = map.get(databaseParam.getName().toLowerCase());
                        if (columnEntity != null) {
                            databaseParam.setDbParamName(columnEntity.getColumnName());
                            databaseParam.setType(columnEntity.getDbType());
                            databaseParam.setLength(columnEntity.getLength());
                            databaseParam.setDef(columnEntity.getDefaultVal());

                        }
                    }

                    System.out.println("");System.out.println("");System.out.println("");System.out.println("");
                    //打印 创见
                    {
                        System.out.println("CREATE TABLE " + tableName + " (");
                        boolean b = false;
                        for (DatabaseParam databaseParam : newList) {
                            if (b) {
                                System.out.println(",");
                            } else {
                                b = true;
                            }
                            if (databaseParam.getDef() != null && databaseParam.getDef().length() >= 0) {
                                System.out.print("    " + databaseParam.getName() + " " + databaseParam.getMysqlType() + " NOT NULL DEFAULT '" + databaseParam.getDef() + "' COMMENT '" + databaseParam.getRemark() + "'");
                            } else {
                                System.out.print("    " + databaseParam.getName() + " " + databaseParam.getMysqlType() + " DEFAULT NULL COMMENT '" + databaseParam.getRemark() + "'");
                            }
                        }
                        System.out.println();
                        System.out.println(") COMMENT='" + tableNameRek + "';");
                    }
                    //打印 建表查询语句
                    {
                        System.out.println("select ");
                        boolean b = false;
                        for (DatabaseParam databaseParam : newList) {
                            if (b) {
                                System.out.println(",");
                            } else {
                                b = true;
                            }
                            System.out.print("  " + databaseParam.getDbParamName());
                            System.out.print(" as " + databaseParam.getName());
                        }
                        System.out.println("");
                        System.out.println("from " + tableName);

                    }

                    System.out.println("");System.out.println("");System.out.println("");System.out.println("");
                    return true;
                }
            }
            dbComponent.close();
        }
        return false;
    }


}
