package com.org;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class ReadData {

    public static void main(String[] args) throws FileNotFoundException {
        OrgAll orgAll = new OrgAll("四川省");

//        Map<Long, List<Org>> orgMap = getDbOrg();

//        getOrgAll(orgAll, orgMap, 2L);

        Map<String, Map<String, Map<String, Set<String>>>> excelMap = getExcel();
        excelMap.forEach((k, v) -> {
            OrgAll orgAll1 = orgAll.addOrg(k, Boolean.FALSE, null);
            v.forEach((kk, vv) -> {
                if(kk == null){
                    System.out.println("--->"+ k);
                }else {
                    OrgAll orgAll2 = orgAll1.addOrg(kk, Boolean.FALSE, null);
                    vv.forEach((kkk, vvv) -> {
                        if (kkk == null) {
                            System.out.println("--->"+ k + "-" + kk);
                        } else {
                            OrgAll orgAll3 = orgAll2.addOrg(kkk, Boolean.FALSE, null);
                            for (String str : vvv) {
                                if (str == null) {
                                    System.out.println("--->"+ k + "-" + kk + "-" + kkk);
                                } else {
                                    orgAll3.addOrg(str, Boolean.FALSE, null);
                                }
                            }
                        }
                    });
                }
            });
        });


//        detectOrg("中国",orgAll);

        List<OrgCompareAll> list = getOrgCompareAll(orgAll);

        //写入文件
        WritData.writ(list);
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
            int i = 3;
            while (i > 0) {
                if (orgTmp.getLevel() == 4) {
                    orgCompare.setVillage(orgTmp);
                } else if (orgTmp.getLevel() == 3) {
                    orgCompare.setTown(orgTmp);
                } else if (orgTmp.getLevel() == 2) {
                    orgCompare.setCounty(orgTmp);
                }
                orgTmp = orgTmp.getFatherOrgAll();
                i--;
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


    public static void detectOrg(String str, OrgAll orgAll) {
        if (orgAll.getOrgs() != null && orgAll.getOrgs().size() > 0) {
            for (OrgAll tmp : orgAll.getOrgs()) {
                String strs = str + "/" + orgAll.getOrgName();
                detectOrg(strs, tmp);
            }
        } else {
            System.out.println(str + "/" + orgAll.getOrgName());
        }
    }


    /**
     * 获取数据
     */
    public static Map<String, Map<String, Map<String, Set<String>>>> getExcel() throws FileNotFoundException {
        File file = new File("D://四川省网格划分统计总表.xls");
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(file);
        ExcelReader excelReader = excelReaderBuilder.build();
        List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
        Map<String, Map<String, Map<String, Set<String>>>> map = new HashMap<>();
        for (ReadSheet sheet : sheets) {
            map.put(sheet.getSheetName(), new HashMap<>());
            Map<String, Map<String, Set<String>>> xian = map.get(sheet.getSheetName());
            List<OrgStr> orgs = getData(new FileInputStream("D://四川省网格划分统计总表.xls"), OrgStr.class, sheet.getSheetName());
            for (OrgStr str : orgs) {
                Map<String, Set<String>> xiangMap = xian.get(str.getXian());
                if (xiangMap == null) {
                    xiangMap = new HashMap<>();
                    xian.put(str.getXian(), xiangMap);
                }
                Set<String> xiangList = xiangMap.get(str.getXiang());
                if (xiangList == null) {
                    xiangList = new HashSet<>();
                    xiangMap.put(str.getXiang(), xiangList);
                }
                xiangList.add(str.getSq());
            }
        }
        return map;
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
     * 获取数据库组织机构信息
     */
    public static Map<Long, List<Org>> getDbOrg() {
        Long start = 0L;
        Long page = 2000L;
        List<Org> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
        try {
            String queryTable = "SELECT id,parent_id ,org_name,seq FROM `doraemon_system`.uc_sys_organization where is_deleted =0 and org_internal_code like '%s' and org_internal_code != '.1.' and org_level != 90170001 and org_type = 90180001 LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Org> list = dbComponent.readJdbcData(String.format(queryTable, ".1.%", start), Org::new, (b, t) -> b.setSg(Boolean.TRUE));
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
        Map<Long, List<Org>> map = all.stream().collect(Collectors.groupingBy(Org::getParentId));
        return map;
    }

    private static void getOrgAll(OrgAll orgAll, Map<Long, List<Org>> map, Long paramId) {
        List<Org> orgs = getOrg(map, paramId);
        if (orgs != null && orgs.size() > 0) {
            for (Org org : orgs) {
                OrgAll tmp = orgAll.addOrg(org.getOrgName(), org.getSeq(), Boolean.TRUE, org.getId());
                getOrgAll(tmp, map, org.getId());
            }
        }
    }


    private static List<Org> getOrg(Map<Long, List<Org>> map, Long key) {
        List<Org> list = map.get(key);
        if (list != null && list.size() > 0) {
            Collections.sort(list, (o1, o2) -> o1.getSeq() < o2.getSeq() ? -1 : (o1.getSeq().longValue() == o2.getSeq().longValue() ? 0 : 1));
        }
        return list;
    }
}
