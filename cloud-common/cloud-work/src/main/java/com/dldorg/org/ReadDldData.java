package com.dldorg.org;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;
import com.dldorg.user.User;
import com.dldorg.user.UserCompare;
import com.dldorg.user.Users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class ReadDldData {

    public static void main(String[] args) throws FileNotFoundException {
        OrgAll orgAll = new OrgAll("四川省");


        Map<String, List<Org>> orgDldMap = getDldDbOrg();
        getOrgAll(orgAll, orgDldMap, "510101", Boolean.TRUE);

        System.out.println("读取大联动组织机构完成");

        Map<String, List<Org>> orgMap = getDbOrg();
        getOrgAll(orgAll, orgMap, "2", Boolean.FALSE);

        System.out.println("读取社管组织机构完成,开始读取用户");

//
//        Map<String, List<Users>> dldUser = getDldDbUser();
//
//        System.out.println("大联动 读取用户完成");
//
//        Map<String, List<Users>> sgUser = getSgDbUser();
//
//        System.out.println("读取用户完成");

//        List<User> list = new ArrayList<>();
//        setList(list, orgAll.getOrgs(), dldUser, sgUser);

//        写入文件
        List<OrgCompareAll> list = getOrgCompareAll(orgAll);
        WritData.writ(list);

//        list.forEach(System.out::println);
    }

    public static void setList(List<User> list, List<OrgAll> orgs, Map<String, List<Users>> dldUser, Map<String, List<Users>> sgUser) {
        for (OrgAll orgAll : orgs) {
            List<User> li = UserCompare.getCompare(orgAll.getOrgFullNameNew(), dldUser.get(orgAll.getId()), sgUser.get(orgAll.getNewId()));
            list.addAll(li);
            List<OrgAll> orgAllList = orgAll.getOrgs();
            if (orgAllList != null && orgAllList.size() > 0) {
                setList(list, orgAllList, dldUser, sgUser);
            }
        }
    }

    public static List<OrgCompareAll> getOrgCompareAll(OrgAll orgAll) {
        List<OrgCompareAll> list = new ArrayList<>();
        List<OrgAll> orgList = orgAll.getOrgs();
        for (OrgAll org : orgList) {
            OrgCompareAll orgCompareAll = new OrgCompareAll();
            orgCompareAll.setCity(org.getOrgName());
            orgCompareAll.setOrgCompare(new ArrayList<>());
            setOrg(orgCompareAll.getOrgCompare(), org);
            list.add(orgCompareAll);
        }
        return list;
    }

    public static void setOrg(List<OrgCompare> orgCompareList, OrgAll org) {
        if (org.getOrgs() == null || org.getOrgs().size() <= 0) {
            OrgCompare orgCompare = new OrgCompare();
            OrgAll orgTmp = org;
            while (orgTmp != null) {
                if (orgTmp.getLevel() == 5) {
                    orgCompare.setGrid(orgTmp);
                } else if (orgTmp.getLevel() == 4) {
                    orgCompare.setVillage(orgTmp);
                } else if (orgTmp.getLevel() == 3) {
                    orgCompare.setTown(orgTmp);
                } else if (orgTmp.getLevel() == 2) {
                    orgCompare.setCounty(orgTmp);
                }
                orgTmp = orgTmp.getFatherOrgAll();
            }
            orgCompareList.add(orgCompare);
        } else {
            List<OrgAll> orgAlls = org.getOrgs();

            //组织机构排序，错误的显示到最后
            Collections.sort(orgAlls, (o1, o2) -> o1.getSort() < o2.getSort() ? -1 : (o1.getSort().longValue() == o2.getSort().longValue() ? 0 : 1));

            for (OrgAll orgAll : orgAlls) {
                setOrg(orgCompareList, orgAll);
            }
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
    public static <T> List<T> getData(InputStream inputStream, Class<T> head, String sheetName) {
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
        EasyExcel.read(inputStream, head, analysisEventListener).sheet(sheetName).doRead();
        return list;
    }

    /**
     * 获取大联动组织机构列表
     *
     * @return
     */
    public static Map<String, List<Org>> getDldDbOrg() {
        Long start = 0L;
        Long page = 2000L;
        List<Org> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("10.1.235.26", "23333", "dld", "dld", "wanggh", "123456");
        try {
            String queryTable = "SELECT CONCAT(ssqx,'-',ORGID) as id,CASE WHEN PORGID = 510111 THEN 510111 ELSE CONCAT(ssqx,'-',PORGID) END as parent_id,ORGNAME org_name,ORGID as seq ,ORGTYPE as org_type, ORGNAMEPATH as org_full_name FROM dld.wgh_org " +
                    " where ORGIDPATH like '%s' and effective='0' and destory is null LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Org> list = dbComponent.readJdbcData(String.format(queryTable, "1/510101/%", start), Org::new, (b, t) -> b.setSg(Boolean.TRUE));
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
        Map<String, List<Org>> map = all.stream().collect(Collectors.groupingBy(Org::getParentId));
        return map;
    }

    /**
     * 获取数据库组织机构信息
     */
    public static Map<String, List<Org>> getDbOrg() {
        Long start = 0L;
        Long page = 2000L;
        List<Org> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
//        DbComponent dbComponent = MySQLDb.createDb("192.168.10.113", "3306", "doraemon_system_v5", "doraemon_system_v5", "root", "tianquekeji");
        try {
            String queryTable = "SELECT id,parent_id ,org_name, org_full_name,seq FROM `doraemon_system`.uc_sys_organization where is_deleted =0 and org_internal_code like '%s' LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Org> list = dbComponent.readJdbcData(String.format(queryTable, ".1.2.%", start), Org::new, (b, t) -> b.setSg(Boolean.TRUE));
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
        Map<String, List<Org>> map = all.stream().collect(Collectors.groupingBy(Org::getParentId));
        return map;
    }


    /**
     * 获取大联动组织机构列表
     *
     * @return
     */
    public static Map<String, List<Users>> getDldDbUser() {
        Long start = 0L;
        Long page = 2000L;
        List<Users> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("10.1.235.26", "23333", "dld", "dld", "wanggh", "123456");
        try {
            String queryTable = "SELECT USERID as id,NAME as name,TEL as tel,SEX as sex,DIRECTORGID as org_id FROM dld.wgh_user where DIRECTORGID is not null LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Users> list = dbComponent.readJdbcData(String.format(queryTable, start), Users::new, null);
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
        Map<Long, List<Users>> map = all.stream().collect(Collectors.groupingBy(Users::getOrgId));
        Map<String, List<Users>> maps = new HashMap<>();
        for(Long k: map.keySet()){
            maps.put(k.toString(),map.get(k));
        }
        return maps;
    }


    /**
     * 获取数据库组织机构信息
     */
    public static Map<String, List<Users>> getSgDbUser() {
        Long start = 0L;
        Long page = 2000L;
        List<Users> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
        try {
            String queryTable = "SELECT id,name,user_name ,mobile as tel,organization_id as org_id FROM doraemon_user.uc_sys_user where is_deleted =0 and org_internal_code like '%s' LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Users> list = dbComponent.readJdbcData(String.format(queryTable, ".1.2.%", start), Users::new, null);
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
        Map<Long, List<Users>> map = all.stream().collect(Collectors.groupingBy(Users::getOrgId));
        Map<String, List<Users>> maps = new HashMap<>();
        for(Long k: map.keySet()){
            maps.put(k.toString(),map.get(k));
        }
        return maps;
    }

    private static void getOrgAll(OrgAll orgAll, Map<String, List<Org>> map, String paramId, boolean bool) {
        List<Org> orgs = getOrg(map, paramId);
        if (orgs != null && orgs.size() > 0) {
            for (Org org : orgs) {
                OrgAll tmp;
                if (bool) {
                    tmp = orgAll.addOrg(org.getOrgName(), org.getSeq(), Boolean.TRUE, org.getId(), bool, org.getOrgType(), org.getOrgFullName(),null, org.getDept());
                } else {
                    tmp = orgAll.addOrg(org.getOrgName(), Boolean.FALSE, org.getId(), bool, org.getOrgFullName());
                }
                getOrgAll(tmp, map, org.getId(), bool);
            }
        }
    }


    private static List<Org> getOrg(Map<String, List<Org>> map, String key) {
        List<Org> list = map.get(key);
        if (list != null && list.size() > 0) {
            Collections.sort(list, (o1, o2) -> o1.getSeq() < o2.getSeq() ? -1 : (o1.getSeq().longValue() == o2.getSeq().longValue() ? 0 : 1));
        }
        return list;
    }
}
