package com.cloud.auto.code.table;

import com.cloud.auto.code.mysql.Config;
import com.cloud.auto.code.mysql.MysqlColumn;
import com.cloud.auto.code.mysql.MysqlTable;
import com.cloud.auto.code.mysql.ReadMysqlTable;
import kotlin.collections.EmptyList;

import java.sql.*;
import java.util.*;

public class TableAutoJoin {
    public static void main(String[] args) throws SQLException {

        Config config = new Config("tests", "jdbc:mysql://localhost:3306/universe_mediation_join", "root", "123456", "com.cloud.test", "E:\\IdeaProjects\\spring-cloud\\cloud-apps\\", "E:\\code\\web\\cloud-angular-web\\src\\app\\module\\", "liulei", "2023-12-14");
        List<String> column = Arrays.asList("info_id", "user_id", "data_id", "aid_id", "tag_id", "type_id", "step_id", "business_id", "apply_id", "room_id", "person_id", "risk_id");

        //读取数据库表

        //读取表字段

        try (Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())) {

            //  获取表结果集
            List<MysqlTable> tables = ReadMysqlTable.getTables(conn, config.getPackagePath(), new ArrayList<>());

            //  设置表属性
            ReadMysqlTable.setColumn(conn, tables);

            Map<String,List<String>> map = new HashMap<>();

            int num = 1;

            for (MysqlTable mysqlTable : tables) {
                Set<String> columns = new HashSet<>();
                for (MysqlColumn column1 : mysqlTable.getColumn()) {
                    if (column.contains(column1.getName())) {
//                        System.out.println(mysqlTable.getName()+"--->"+column1.getName());
                        columns.add(column1.getName());
                    }
                }
                for (MysqlTable tmp : tables) {
                    if (mysqlTable.getName().equals(tmp.getName()) || mysqlTable.getName().equals("md_dispute_file") || tmp.getName().equals("md_dispute_file")) {
                        continue;
                    }
                    if (!columns.isEmpty()) {
                        for (String col : columns) {
                            String queryColumnSql = "select count(1) as total from " + tmp.getName() + " where id in (" +
                                    "select " + col + " from " + mysqlTable.getName() + " where " + col + " is not null and " + col + " >100" +
                                    ")";

                            Statement statement = conn.createStatement();

                            ResultSet rs = statement.executeQuery(queryColumnSql);
                            while (rs.next()) {
                                long total = rs.getLong("total");
                                if (total > 0) {
                                    String pk = mysqlTable.getName()+"__"+col+"_FK";
                                    String qu = "ALTER TABLE universe_mediation_tmp."+mysqlTable.getName()+" ADD CONSTRAINT "+mysqlTable.getName()+"__"+col+"_FK FOREIGN KEY ("+col+") REFERENCES universe_mediation_tmp."+tmp.getName()+"(id);";
                                    List<String> list = map.get(pk);
                                    if(list == null){
                                        list = new ArrayList<>();
                                        list.add(qu);
                                        map.put(pk, list);
                                    }else{
                                        list.add(qu);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            map.forEach((k,v)->{
                if(v.size()>1){
                    for(String tmp : v) {
                        System.out.println(tmp);
                    }
                }
            });
        }
        //

    }
}
