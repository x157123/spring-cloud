package com.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 更新用户组织机构层级脚本
 * @author liulei
 */
public class User {

    public static void main(String[] args) {
        String fileName= "德阳罗江区网格员账号调整";
        try {
            DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_user", "doraemon_user", "sichuan", "Tianquekeji@123");
            FileInputStream inperson = new FileInputStream("C:\\Users\\liulei\\Desktop\\"+fileName+".xlsx");
            List<UserOrg> list = upload(inperson, UserOrg.class);
            for (UserOrg userOrg : list) {
                List<Map<String, Object>> userMap = dbComponent.readJdbcData("SELECT organization_id,org_internal_code ,org_level ,org_type FROM doraemon_user.uc_sys_user where user_name = '" + userOrg.getUserName().trim() + "'");
                if (userMap != null && userMap.size() > 0) {
                    userOrg.setOrgId(userMap.get(0).get("organization_id").toString());
                    userOrg.setOrgCode(userMap.get(0).get("org_internal_code").toString());
                }
                List<Map<String, Object>> orgMap = dbComponent.readJdbcData("select id as organization_id,org_internal_code ,org_level ,org_type from doraemon_system.uc_sys_organization uso  where is_deleted=0 and org_full_name = '" + userOrg.getFullOrgName().trim() + "'");
                if (orgMap != null && orgMap.size() > 0) {
                    userOrg.setNewOrgId(orgMap.get(0).get("organization_id").toString());
                    userOrg.setNewOrgCode(orgMap.get(0).get("org_internal_code").toString());
                    userOrg.setOrgLever(orgMap.get(0).get("org_level").toString());
                    userOrg.setOrgType(orgMap.get(0).get("org_type").toString());
                }
                userOrg.setSql("update `doraemon_user`.uc_sys_user set name = '" + userOrg.getName() +"' ,organization_id=" + userOrg.getNewOrgId() + ",org_internal_code='" + userOrg.getNewOrgCode() + "',org_level= " + userOrg.getOrgLever() + ",org_type = " + userOrg.getOrgType() + " where user_name = '" + userOrg.getUserName() + "' and organization_id=" + userOrg.getOrgId() + " and org_internal_code ='" + userOrg.getOrgCode() + "';");
            }
            testWriteExcel(list,fileName+"new");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将Excel文件解析成List
     *
     * @param inputStream 文件
     * @param head        表头
     * @param <T>         泛型
     * @return
     */
    public static <T> List<T> upload(InputStream inputStream, Class<T> head) {
        List<T> list = new ArrayList<>();
        AnalysisEventListener<T> analysisEventListener = new AnalysisEventListener<T>() {
            // 这个每一条数据解析都会来调用
            @Override
            public void invoke(T data, AnalysisContext context) {
                list.add(data);
            }

            // 所有数据解析完成了都会来调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("所有数据解析完成！");
            }
        };
        EasyExcel.read(inputStream, head, analysisEventListener).sheet().doRead();
        return list;
    }

    public static void testWriteExcel(List<UserOrg> list,String fileName) {
        String filename = "X:\\"+fileName+".xlsx";
        // 向Excel中写入数据 也可以通过 head(Class<?>) 指定数据模板
        EasyExcel.write(filename, UserOrg.class)
                .sheet("用户信息")
                .doWrite(list);
    }
}
