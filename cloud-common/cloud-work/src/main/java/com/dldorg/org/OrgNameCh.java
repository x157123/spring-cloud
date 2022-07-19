package com.dldorg.org;

import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class OrgNameCh {


    /**
     * 获取数据库组织机构信息
     */
    public static Map<Long, List<OrgTest>> getDbOrg(String orgCode) {
        Long start = 0L;
        Long page = 2000L;
        List<OrgTest> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
//        DbComponent dbComponent = MySQLDb.createDb("192.168.10.113", "3306", "doraemon_system_v5", "doraemon_system_v5", "root", "tianquekeji");
        try {
            String queryTable = "SELECT id, parent_id, org_name, remark,seq FROM `doraemon_system`.uc_sys_organization where org_internal_code like '%s' and org_level = '90170001' and org_type = '90180001' and is_deleted =0 LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<OrgTest> list = dbComponent.readJdbcData(String.format(queryTable, orgCode + "%", start), OrgTest::new, null);
                all.addAll(list);
                start += page;
                if (list.size() < page) {
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbComponent.close();

        Map<Long,List<OrgTest>> tmpMap = all.stream().collect(Collectors.groupingBy(OrgTest::getParentId));

        //获取上一级组织机构信息
        Map<Long, OrgTest> orgTestMap = getDbAftOrg(orgCode);


        for(Long key: tmpMap.keySet()){
            OrgTest orgTest = orgTestMap.get(key);
            List<OrgTest> list = tmpMap.get(key);
            if(orgTest!=null) {
                for (OrgTest t : list) {
                    String str = replace(t.getOrgName(), orgTest);
                    t.setOrgNewName(str);
                }
            }
        }
        return tmpMap;
    }


    /**
     * 获取数据库组织机构信息
     */
    public static Map<Long, OrgTest> getDbAftOrg(String orgCode) {
        Long start = 0L;
        Long page = 2000L;
        List<OrgTest> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
        try {
            String queryTable = "SELECT id, parent_id, org_name, remark,seq FROM `doraemon_system`.uc_sys_organization where id in (SELECT parent_id FROM `doraemon_system`.uc_sys_organization where org_internal_code like '%s' and org_level = '90170001' and org_type = '90180001' and is_deleted =0) LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<OrgTest> list = dbComponent.readJdbcData(String.format(queryTable, orgCode + "%", start), OrgTest::new, null);
                all.addAll(list);
                start += page;
                if (list.size() < page) {
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbComponent.close();
        Map<Long, OrgTest> map = new HashMap<>();
        for (OrgTest org : all) {
            map.put(org.getId(), org);
        }
        return map;
    }

    private static String replace(String str, OrgTest orgTest) {
        String tmp = str.replace(" ", "");

        //替换网格名称保护上一级组织机构名称问题
        String[] strs = orgTest.getOrgName().split("");
        for (String s : strs) {
            tmp = tmp.replace(s, "");
        }

        tmp = tmp.replace("(", "");
        tmp = tmp.replace(")", "");
        tmp = tmp.replace("（", "");
        tmp = tmp.replace("）", "");
        tmp = tmp.replace("第", "");
        tmp = tmp.replaceAll("十一", "11");
        tmp = tmp.replaceAll("十二", "12");
        tmp = tmp.replaceAll("十三", "13");
        tmp = tmp.replaceAll("十四", "14");
        tmp = tmp.replaceAll("十五", "15");
        tmp = tmp.replaceAll("十六", "16");
        tmp = tmp.replaceAll("十七", "17");
        tmp = tmp.replaceAll("十八", "18");
        tmp = tmp.replaceAll("十九", "19");
        tmp = tmp.replaceAll("二十一", "21");
        tmp = tmp.replaceAll("二十二", "22");
        tmp = tmp.replaceAll("二十三", "23");
        tmp = tmp.replaceAll("二十四", "24");
        tmp = tmp.replaceAll("二十五", "25");
        tmp = tmp.replaceAll("二十六", "26");
        tmp = tmp.replaceAll("二十七", "27");
        tmp = tmp.replaceAll("二十八", "28");
        tmp = tmp.replaceAll("二十九", "29");
        tmp = tmp.replaceAll("三十一", "31");
        tmp = tmp.replaceAll("三十二", "32");
        tmp = tmp.replaceAll("三十三", "33");
        tmp = tmp.replaceAll("三十四", "34");
        tmp = tmp.replaceAll("三十五", "35");
        tmp = tmp.replaceAll("三十六", "36");
        tmp = tmp.replaceAll("三十七", "37");
        tmp = tmp.replaceAll("三十八", "38");
        tmp = tmp.replaceAll("三十九", "39");
        tmp = tmp.replaceAll("四十一", "41");
        tmp = tmp.replaceAll("四十二", "42");
        tmp = tmp.replaceAll("四十三", "43");
        tmp = tmp.replaceAll("四十四", "44");
        tmp = tmp.replaceAll("四十五", "45");
        tmp = tmp.replaceAll("四十六", "46");
        tmp = tmp.replaceAll("四十七", "47");
        tmp = tmp.replaceAll("四十八", "48");
        tmp = tmp.replaceAll("四十九", "49");
        tmp = tmp.replaceAll("五十一", "51");
        tmp = tmp.replaceAll("五十二", "52");
        tmp = tmp.replaceAll("五十三", "53");
        tmp = tmp.replaceAll("五十四", "54");
        tmp = tmp.replaceAll("五十五", "55");
        tmp = tmp.replaceAll("五十六", "56");
        tmp = tmp.replaceAll("五十七", "57");
        tmp = tmp.replaceAll("五十八", "58");
        tmp = tmp.replaceAll("五十九", "59");
        tmp = tmp.replaceAll("二十", "20");
        tmp = tmp.replaceAll("三十", "30");
        tmp = tmp.replaceAll("四十", "40");
        tmp = tmp.replaceAll("五十", "50");
        tmp = tmp.replaceAll("一", "1");
        tmp = tmp.replaceAll("二", "2");
        tmp = tmp.replaceAll("三", "3");
        tmp = tmp.replaceAll("四", "4");
        tmp = tmp.replaceAll("五", "5");
        tmp = tmp.replaceAll("六", "6");
        tmp = tmp.replaceAll("七", "7");
        tmp = tmp.replaceAll("八", "8");
        tmp = tmp.replaceAll("九", "9");
        tmp = tmp.replaceAll("十", "10");
        tmp = exchangeNumber(tmp);
        tmp = tmp.replaceAll("网格", "");
        return tmp;
    }


    private static String exchangeNumber(String s) {
        if (s.equals("网格") || s.trim().length() <= 0) {
            return s;
        }
        String str = s.replaceAll("[0-9]*网格$", "");
        if (str.length() <= 0) {
            //匹配非数字字符，然后全部替换为空字符，剩下的自然只有数字啦
            String sss = Pattern.compile("[^0-9]").matcher(s).replaceAll("");
            //打印结果 100
            String news = s.replace(sss, "");
            return news + Long.parseLong(sss);
        }
        return s;
    }


    public static void main(String[] args) {

        //获取组织机构
        Map<Long, List<OrgTest>> map = getDbOrg(".1.2.");

        int i = 1;

        for (Long orgId : map.keySet()) {
            List<OrgTest> orgTests = map.get(orgId);
            Map<String, OrgTest> oldData = new HashMap<>();
            Map<String, OrgTest> newData = new HashMap<>();
            for (OrgTest test : orgTests) {
                if (test.getRemark() != null && test.getRemark().equals("自动新增")) {
                    newData.put(test.getOrgNewName(), test);
                } else {
                    oldData.put(test.getOrgNewName(), test);
                }
            }
            for (String key : newData.keySet()) {
                OrgTest org = oldData.get(key);
                if (org != null) {
                    OrgTest tmp = newData.get(key);
                    i++;
                    if (tmp != null && tmp.getId() != null) {
                        //删除自动新增数据
//                        System.out.println(tmp.getId() + "------" + tmp.getOrgName() + "------======---" + org.getId() + "---------" + org.getOrgName());
                        OrtAdd.deleteOrg(tmp.getId());
                        OrtAdd.updateOrgName(org.getId(), tmp.getOrgName());
                    }
                }
            }
        }

        map.forEach((k, v) -> {
            if (v.size() > 0) {
                v.forEach(orgTest -> {
                    //System.out.println(k + "---" + orgTest);
                });
            }
        });
    }


}
