package com.dldorg.org;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;
import com.cloud.common.util.db.connection.PostgreSQLDb;
import com.dldorg.bean.DockingMapping;
import com.dldorg.user.User;
import com.dldorg.user.UserCompare;
import com.dldorg.user.Users;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class ReadDldNewData {

    private static long id = 1435295512763183122L;

    public static void repeat(Map<String, List<Org>> map) {
        for (String key : map.keySet()) {
            List<Org> list = new ArrayList<>();
            List<Org> orgs = map.get(key);
            Map<String, List<Org>> newMap = orgs.stream().collect(Collectors.groupingBy(Org::getOrgName));
            for (String orgName : newMap.keySet()) {
                List<Org> orgList = newMap.get(orgName);
                if (orgList != null && orgList.size() > 0) {
                    list.add(orgList.get(0));
                    if (orgList.size() > 1) {
                        for (int i = 1; i < orgList.size(); i++) {
                            orgList.get(0).addSameOrgId(orgList.get(i).getId());
                        }
                    }
                }
            }
            map.put(key, list);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        OrgAll orgAll = new OrgAll("四川省");

        Map<String, List<Org>> orgDldMap = getDldDbOrg();

        repeat(orgDldMap);
        //合并相同orgName

        getOrgAll(orgAll, orgDldMap, "510101", Boolean.TRUE);

        System.out.println("读取大联动组织机构完成");

        Map<String, List<Org>> orgMap = getDbOrg();
        getOrgAll(orgAll, orgMap, "2", Boolean.FALSE);

        //------------------------------处理用户-----------------
//        System.out.println("读取社管组织机构完成,开始读取用户");
//
//        List<Long> unGrid = getDldGridDbUser();
//
//        Map<String, List<Users>> dldUser = getDldDbUser(unGrid);
//
//
//        System.out.println("大联动 读取用户完成");
//
//        Map<String, List<Users>> sgUser = getSgDbUser();
//
//        System.out.println("读取用户完成");
//        List<User> list = new ArrayList<>();
//        setList(list, orgAll.getOrgs(), dldUser, sgUser);
        //------------------------------处理用户-----------------


//        写入文件
        List<OrgCompareAll> list = getOrgCompareAll(orgAll);
        WritData.writ(list);


        List<DockingMapping> all = new ArrayList<>();

        print(orgAll, all);


        startWriteWithConnection(all);


    }


    /**
     * 写入组织机构关联数据
     *
     * @param dataList
     */
    public static void startWriteWithConnection(List<DockingMapping> dataList) {

        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.62", "5432",
                "tq_scgrid_base", "base_prod", "postgres", "Tianquekeji@123");

        Connection connection = dbComponent.getJdbcConnection();

        PreparedStatement prepareStatement = null;

        String writeRecordSql = "INSERT INTO base_prod.sg_org_dockingmapping (id, org_id, org_code, other_org_name, org_no, docking_type, create_user, create_date, update_date, is_deleted) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            prepareStatement = connection.prepareStatement(writeRecordSql);
            for (DockingMapping bo : dataList) {
                prepareStatement.setLong(1, bo.getId());
                prepareStatement.setLong(2, bo.getOrgId());
                prepareStatement.setString(3, bo.getOrgCode());
                prepareStatement.setString(4, bo.getOtherOrgName());
                prepareStatement.setString(5, bo.getOrgNo());
                prepareStatement.setInt(6, bo.getDockingType());
                prepareStatement.setString(7, bo.getCreateUser());
                prepareStatement.setDate(8, bo.getCreateDate());
                prepareStatement.setDate(9, bo.getUpdateDate());
                prepareStatement.setInt(10, bo.getIsDeleted());
                prepareStatement.addBatch();
            }
            prepareStatement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbComponent.close();
        }
    }


    private static void print(OrgAll org, List<DockingMapping> all) {
        if (org != null) {
            id += 1;
            if (org.getNewId() != null && org.getId() != null) {
                all.add(new DockingMapping(id, Long.parseLong(org.getNewId()), org.getOrgInternalCode(), org.getOrgName(), org.getDept()));
            }
            if (org.getOrgs() != null && org.getOrgs().size() > 0) {
                for (OrgAll orgAll : org.getOrgs()) {
                    print(orgAll, all);
                }
            }
        }
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

        Org orgc = new Org();
        orgc.setId("510101");
        orgc.setParentId("1");
        orgc.setOrgName("四川省");
        orgc.setSeq(510101L);
        orgc.setOrgType("1");
        all.add(orgc);

        Org org = new Org();
        org.setId("510111");
        org.setParentId("510101");
        org.setOrgName("成都市");
        org.setSeq(510111L);
        org.setOrgType("1");
        all.add(org);

        Org orga = new Org();
        orga.setId("510108-110357");
        orga.setParentId("510111");
        orga.setOrgName("成华区");
        orga.setSeq(110357L);
        orga.setOrgType("1");
        all.add(orga);

        DbComponent dbComponent = MySQLDb.createDb("10.1.235.26", "23333", "dld", "dld", "wanggh", "123456");
        try {
            String queryTable = "SELECT CONCAT(ssqx,'-',ORGID) as id,CASE WHEN PORGID = 510111 THEN 510111 ELSE CONCAT(ssqx,'-',PORGID) END as parent_id,COSTOMNO dept,ORGNAME org_name,ORGID as seq ,ORGTYPE as org_type, ORGNAMEPATH as org_full_name FROM dld.wgh_org " +
                    " where (ORGIDPATH like '%s' or ORGIDPATH like '%s') and effective='0' and destory is null LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Org> list = dbComponent.readJdbcData(String.format(queryTable, "1/510101/%","1/110357/%", start), Org::new, (b, t) -> {
                    b.setSg(Boolean.TRUE);
                    if(b.getId().equals("510104-5101400022") ){
                        b.setParentId("510111");
                    }
                });
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
            String queryTable = "SELECT CONCAT(id,'') as id,CONCAT(parent_id,'') as parent_id,org_internal_code,org_name, org_full_name,seq FROM `doraemon_system`.uc_sys_organization where is_deleted =0 and org_internal_code like '%s' LIMIT %s ,2000";
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
    public static List<Long> getDldGridDbUser() {
        Long start = 0L;
        Long page = 2000L;
        List<Users> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("10.1.235.26", "23333", "dld", "dld", "wanggh", "123456");
        try {
            String queryTable = "SELECT USERID as id FROM dld.wgh_wgy where SFWGY != '01' or DEL_FLAG = 1 LIMIT %s ,2000";
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
        List<Long> list = all.stream().map(Users::getId).collect(Collectors.toList());
        return list;
    }


    /**
     * 获取大联动组织机构列表
     *
     * @return
     */
    public static Map<String, List<Users>> getDldDbUser(List<Long> unGrid) {
        Long start = 0L;
        Long page = 2000L;
        List<Users> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("10.1.235.26", "23333", "dld", "dld", "wanggh", "123456");
        try {
            String queryTable = "SELECT USERID as id,NAME as name,TEL as tel,SEX as sex,DIRECTORGID as org_id FROM dld.wgh_user where DESTORY is null and EFFECTIVE = 0 LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Users> list = dbComponent.readJdbcData(String.format(queryTable, start), Users::new, null);
                list.forEach(users -> {
                    if (!unGrid.contains(users.getId())) {
                        all.add(users);
                    }
                });
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
        for (Long k : map.keySet()) {
            maps.put(k.toString(), map.get(k));
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
        for (Long k : map.keySet()) {
            maps.put(k.toString(), map.get(k));
        }
        return maps;
    }

    private static void getOrgAll(OrgAll orgAll, Map<String, List<Org>> map, String paramId, boolean bool) {
        List<Org> orgs = getOrg(map, paramId);
        if (orgs != null && orgs.size() > 0) {
            for (Org org : orgs) {
                OrgAll tmp;
                if (bool) {
                    tmp = orgAll.addOrg(org.getOrgName(), org.getSeq(), Boolean.TRUE, org.getId(), bool, org.getOrgType(), org.getOrgFullName(), null, org.getDept());
                } else {
                    tmp = orgAll.addOrgAndCode(org.getOrgName(), Boolean.FALSE, org.getId(), bool, org.getOrgFullName(), org.getOrgInternalCode());
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
