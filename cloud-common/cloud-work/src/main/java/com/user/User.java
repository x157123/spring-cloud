package com.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cloud.common.core.utils.HttpUtil;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;

import java.io.*;
import java.util.*;

/**
 * 更新用户组织机构层级脚本
 *
 * @author liulei
 */
public class User {

    public static void main(String[] args) {
        delUsers();
//        chenge();
    }

    /**
     * 调整账号层级
     */
    public static void chenge() {
        String fileName = "德阳罗江区网格员账号调整";
        try {
            DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_user", "doraemon_user", "sichuan", "Tianquekeji@123");
            FileInputStream inperson = new FileInputStream("C:\\Users\\liulei\\Desktop\\" + fileName + ".xlsx");
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
                userOrg.setSql("update `doraemon_user`.uc_sys_user set name = '" + userOrg.getName() + "' ,organization_id=" + userOrg.getNewOrgId() + ",org_internal_code='" + userOrg.getNewOrgCode() + "',org_level= " + userOrg.getOrgLever() + ",org_type = " + userOrg.getOrgType() + " where user_name = '" + userOrg.getUserName() + "' and organization_id=" + userOrg.getOrgId() + " and org_internal_code ='" + userOrg.getOrgCode() + "';");
            }
            testWriteExcel(list, fileName + "new");
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

    public static void testWriteExcel(List<UserOrg> list, String fileName) {
        String filename = "X:\\" + fileName + ".xlsx";
        // 向Excel中写入数据 也可以通过 head(Class<?>) 指定数据模板
        EasyExcel.write(filename, UserOrg.class)
                .sheet("用户信息")
                .doWrite(list);
    }


    public static void delUsers() {
        List<Long> users = Arrays.asList(107266L, 107267L, 107269L, 107270L, 107271L, 107273L, 107274L, 107275L, 107276L, 107277L, 107278L, 107279L, 107280L, 107281L, 107282L, 107283L, 107284L, 107285L, 107286L, 107287L, 107288L, 107289L, 108066L, 107290L, 107291L, 107292L, 107293L, 107294L, 107295L, 107296L, 107297L, 107298L, 146397L, 107300L, 107301L, 107302L, 107303L, 107304L, 107305L, 107306L, 107307L, 107308L, 107319L, 107320L, 107321L, 107332L, 107341L, 107350L, 107351L, 107357L, 107358L, 107359L, 107360L, 107361L, 107367L, 107368L, 107369L, 107371L, 107380L, 107381L, 107382L, 107383L, 107384L, 107385L, 143878L, 107379L, 107387L, 107388L, 107390L, 107391L, 107413L, 107412L, 107389L, 107434L, 107398L, 107414L, 107415L, 107416L, 107435L, 107417L, 107418L, 107419L, 107420L, 107421L, 96762L, 96761L, 96763L, 96783L, 96784L, 96779L, 96780L, 96781L, 96782L, 96759L, 96760L, 67395L, 67396L, 67407L, 67409L, 67410L, 67411L, 67412L, 67413L, 67414L, 96713L, 96714L, 96715L, 96716L, 96717L, 96718L, 96719L, 96733L, 96734L, 96736L, 96737L, 96738L, 96753L, 96754L, 96755L, 96756L, 96757L, 96758L, 36509L, 36510L, 36511L, 36512L, 36513L, 36514L, 36515L, 36516L, 36518L, 36519L, 36520L, 36521L, 36522L, 36523L, 36524L, 36525L, 36526L, 36527L);
        for(Long lon: users) {
            String url = "http://10.0.188.11:9999/api/doraemon-user/user/deleteByIds";
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("ids[]", lon.toString());

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("auth", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIwMDAwMDAiLCJjb2RlIjoyMDAsInJvbGVfY29kZXMiOltdLCJ1c2VyX25hbWUiOiJsaXVsZWkiLCJhdXRob3JpdGllcyI6WyIxNTMzNzEyMTQzOTg2MjAwNjU5Il0sImNsaWVudF9pZCI6InVzZXJjZW50ZXIiLCJyb2xlX2lkcyI6WzE1MzM3MTIxNDM5ODYyMDA2NTldLCJhZG1pbmlzdHJhdG9yIjp0cnVlLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJ1c2VyX2lkIjoiMTg4NTcwIiwib3JnX2lkIjoiMSIsInN1Y2Nlc3MiOnRydWUsInNjb3BlIjpbImFsbCJdLCJvYXV0aF9pZCI6IiIsImV4cCI6MTY1ODcxMTE1NiwianRpIjoiZWQwOTNmMzMtNWE0MC00YzQyLWE3YjAtMGNlMTIxZjFjYTZlIn0.UUynhnPOQVcAMeL1Zdy3RE0tP-8EXQYHH-HeMYTB9qw");
            HttpUtil.sendPost(url, paramsMap, headerMap);
        }
    }
}
