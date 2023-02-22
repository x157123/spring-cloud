package com.cloud.common.util.db.connection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchInsertDataToTable {
    public static void test() {
        // 数据库连接参数
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        // 表名和要插入的数据
        String tableName = "test";

        List<Map<String, Object>> dataList = createData();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // 构造SQL语句
            String sql = "INSERT INTO " + tableName + " ("
                    + String.join(", ", dataList.get(0).keySet()) + ") VALUES ("
                    + String.join(", ", Collections.nCopies(dataList.get(0).size(), "?")) + ")";
            // 执行批量插入
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Map<String, Object> data : dataList) {
                int i = 1;
                for (Object value : data.values()) {
                    stmt.setObject(i++, value);
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test();
    }


    public static List<Map<String, Object>> createData() {
        String jsonStr = "[{\"name\":\"John Smith\",\"age\":30,\"birthdate\":\"2023-02-13T14:30:00.000Z\"},{\"name\":\"John Smithsss\",\"age\":31,\"birthdate\":\"2023-02-14T15:30:20.000Z\"}]";
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = null;
        try {
            list = mapper.readValue(jsonStr, new TypeReference<>() {});
            for(Map<String,Object> map : list) {
                for (String key : map.keySet()) {
                    if (isDate(map.get(key).toString())) {
                        map.put(key, dateFormat(map.get(key).toString()));
                    }
                }
            }
        }catch (Exception e){

        }
        return list;
    }

    public static boolean isDate(String dateString ){
        String[] regexes = {
                "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", // yyyy-MM-dd HH:mm:ss
                "^\\d{4}-\\d{2}-\\d{2}$", // yyyy-MM-dd
                "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$", // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
                "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", // yyyy-MM-dd HH:mm
        };
        boolean matched = false;
        for (String regex : regexes) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(dateString);
            if (matcher.matches()) {
                matched = true;
                break;
            }
        }
        return matched;
    }

    public static Date dateFormat(String dateString) {
        DateFormat[] formats = new DateFormat[] {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("MM/dd/yyyy")
        }; // 时间格式数组
        Date date = null;
        for (DateFormat format : formats) {
            try {
                date = format.parse(dateString);
                return date;
            } catch (ParseException e) {
                // 格式不匹配，继续尝试下一个格式
            }
        }
        return null;
    }
}