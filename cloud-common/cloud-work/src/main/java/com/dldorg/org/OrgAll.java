package com.dldorg.org;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Data
public class OrgAll {

    /**
     * 组织机构ID
     */
    private String id;

    /**
     * 组织机构ID
     */
    private String newId;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 新组织机构名称
     */
    private String newOrgName;

    /**
     * 排序
     */
    private Long seq;

    /**
     * 下级org
     */
    private List<OrgAll> orgs;

    /**
     * 数据来源类型，True社管  False 其他
     */
    private Boolean type;

    /**
     * 上一级org
     */
    private OrgAll fatherOrgAll;

    /**
     * 层级
     */
    private int level;

    /**
     * 新增次数
     */
    private int com;

    /**
     * 最大数据
     */
    private Long maxDept;

    /**
     * 是否已处理
     */
    private Boolean dealWith;

    /**
     * 其他地方的组织机构
     */
    private String orgType;


    private String orgFullName;


    private String orgFullNameNew;

    private String orgInternalCode;

    private String dept;

    public Long getSort() {
        Long sort = seq;
        if (com > 1) {
            if (newOrgName != null && newOrgName.length() > 1) {
                sort *= 10;
            }
        } else {
            sort *= 100;
        }
        return sort;
    }

    public OrgAll(String orgName) {
        this.orgName = orgName;
        this.newOrgName = "";
        this.com = 1;
        this.type = Boolean.TRUE;
        this.orgs = new ArrayList<>();
    }

    public OrgAll addOrgAndCode(String orgName, Boolean type, String orgId, boolean bool, String orgFullName,String orgCode) {
        return this.addOrg(orgName, null, type, orgId, bool, null, orgFullName,orgCode,null);
    }

    public OrgAll addOrg(String orgName, Boolean type, String orgId, boolean bool, String orgFullName) {
        return this.addOrg(orgName, null, type, orgId, bool, null, orgFullName,null,null);
    }


    public OrgAll addOrg(String orgName, Long seq, Boolean type, String orgId, boolean bool, String orgType, String orgFullName,String orgCode,String dept) {
        OrgAll orgAll = null;
        if (!bool) {
            //全路径匹配
            for (OrgAll org : orgs) {
                if (org.getOrgName().equals(orgName) || org.getNewOrgName().equals(orgName)) {
                    org.setCom(org.getCom() + 1);
                    org.setNewId(orgId.toString());
                    org.setOrgFullNameNew(orgFullName);
                    org.setOrgInternalCode(orgCode);
                    return org;
                }
            }
            List<String> str = new ArrayList<>();
            //截取匹配
            int cou = 0;
            String orgNames;
            if (this.getLevel() == 4) {
                orgNames = getStr(orgName, this.orgName);
            } else {
                orgNames = getStr(orgName);
            }
            for (OrgAll org : orgs) {
                try {
                    if(getStr(exchangeNumber(org.getOrgName())).equals(orgNames)){
                        orgAll = org;
                        org.setCom(org.getCom() + 1);
                        cou += 1;
                        str.add(org.getOrgName());
                    }else {
                        String orgNameNews = getStr(org.getOrgName());
                        if (compare(orgNames, orgNameNews)) {
                            orgAll = org;
                            org.setCom(org.getCom() + 1);
                            cou += 1;
                            str.add(org.getOrgName());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常数据" + orgName);
                }
            }
            //拼音匹配
//            if (cou <= 0) {
//                for (OrgAll org : orgs) {
//                    try {
//                        String orgNameNews = getStr(org.getOrgName());
//                        if (comparePinYin(orgNames, orgNameNews)) {
//                            orgAll = org;
//                            org.setCom(org.getCom() + 1);
//                            cou += 1;
//                            str.add(org.getOrgName());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("异常数据" + orgName);
//                    }
//                }
//            }
            if (cou <= 1 && orgAll != null) {
                orgAll.setNewOrgName(orgName);
                orgAll.setNewId(orgId.toString());
                orgAll.setOrgInternalCode(orgCode);
                return orgAll;
            } else if (cou > 1) {
                System.out.println("出错组织机构：-->" + this.getOrgName() + "/" + this.getNewOrgName() + ":->" + str.stream().collect(Collectors.joining("--")));
            }
        }
        orgAll = new OrgAll(orgName);
        orgAll.setOrgType(orgType);
        if (seq == null) {
            seq = 0L;
            for (OrgAll org : orgs) {
                seq = org.getSeq() > seq ? org.getSeq() : seq;
            }
        }
        if (bool) {
            orgAll.setId(orgId.toString());
            orgAll.setOrgFullName(orgFullName);
            orgAll.setDept(dept);
        } else {
            orgAll.setNewId(orgId.toString());
            orgAll.setOrgInternalCode(orgCode);
            orgAll.setOrgFullNameNew(orgFullName);
        }
        orgAll.setSeq(seq + 1);
        orgAll.setType(type);
        orgAll.setLevel(this.getLevel() + 1);
        orgAll.setFatherOrgAll(this);
        orgs.add(orgAll);
        return orgAll;
    }

    private static String getStr(String st, String upStr) {
        String stNew = getStr(st);
        String upStrNew = getStr(upStr);
        try {
            String tmp = stNew.replace(upStrNew, "");
            return tmp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return stNew;
    }

    private static boolean compare(String a, String b) {
        if (a.trim().equals(b.trim())) {
            return true;
        }
        return false;
    }

    private static boolean comparePinYin(String a, String b) {
        if (Pinyin4jUtils.getFullPinyinStr(a).equals(Pinyin4jUtils.getFullPinyinStr(b))) {
            return true;
        }
        return false;
    }

    private static String getStr(String st) {
        return st.trim();
    }

    private static String getStrBak(String st) {
        String orgNameNews = st.trim();
//        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）|0[0-9]*$|0[0-9]*号$|[0-9]*社$|[0-9]*组$|村.*?组$|[0-9]*网格", "");
        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
        orgNameNews = orgNameNews.replace("综合执法第一分局", "");
        orgNameNews = orgNameNews.replace("村村民村村民委员会", "");
        orgNameNews = orgNameNews.replace("村村民委员会委会", "");
        orgNameNews = orgNameNews.replace("村村民委员会网格", "");
        orgNameNews = orgNameNews.replace("社区居委会筹委会", "");
        orgNameNews = orgNameNews.replace("中心村村民委员会", "");
        orgNameNews = orgNameNews.replace("农村社区村委会", "");
        orgNameNews = orgNameNews.replace("社区居民委员会", "");
        orgNameNews = orgNameNews.replace("社区居民居委会", "");
        orgNameNews = orgNameNews.replace("社区筹委居委会", "");
        orgNameNews = orgNameNews.replace("社区村民委员会", "");
        orgNameNews = orgNameNews.replace("村民居民委员会", "");
        orgNameNews = orgNameNews.replace("社区民委员会", "");
        orgNameNews = orgNameNews.replace("社区居民委会", "");
        orgNameNews = orgNameNews.replace("社区居委员会", "");
        orgNameNews = orgNameNews.replace("社居民委员会", "");
        orgNameNews = orgNameNews.replace("村村民委员会", "");
        orgNameNews = orgNameNews.replace("路社区居委会", "");
        orgNameNews = orgNameNews.replace("居民委员委会", "");
        orgNameNews = orgNameNews.replace("景区管理局", "");
        orgNameNews = orgNameNews.replace("村村民委员", "");
        orgNameNews = orgNameNews.replace("社区委员会", "");
//        orgNameNews = orgNameNews.replace("社区居委会", "");
        orgNameNews = orgNameNews.replace("筹委居委会", "");
        orgNameNews = orgNameNews.replace("居民委员会", "");
        orgNameNews = orgNameNews.replace("居名委员会", "");
        orgNameNews = orgNameNews.replace("村村民委会", "");
        orgNameNews = orgNameNews.replace("村民委员会", "");
        orgNameNews = orgNameNews.replace("村民居委会", "");
        orgNameNews = orgNameNews.replace("村民委員會", "");
        orgNameNews = orgNameNews.replace("管理委员会", "");
        orgNameNews = orgNameNews.replace("旅游风景区", "");
        orgNameNews = orgNameNews.replace("成都直管区", "");

        orgNameNews = orgNameNews.replace("社区保留", "社区");
        orgNameNews = orgNameNews.replace("网格保留", "网格");

        orgNameNews = orgNameNews.replace("村民委会", "");
        orgNameNews = orgNameNews.replace("村委员会", "");
        orgNameNews = orgNameNews.replace("村村委会", "");
        orgNameNews = orgNameNews.replace("社区委会", "");
        orgNameNews = orgNameNews.replace("社区涉农", "");
        orgNameNews = orgNameNews.replace("农村社区", "");
        orgNameNews = orgNameNews.replace("小区社区", "");
        orgNameNews = orgNameNews.replace("管理局", "");
        orgNameNews = orgNameNews.replace("管委会", "");
        orgNameNews = orgNameNews.replace("办事处", "");
        orgNameNews = orgNameNews.replace("办事处", "");
        orgNameNews = orgNameNews.replace("村委会", "");
        orgNameNews = orgNameNews.replace("街道办", "");
        orgNameNews = orgNameNews.replace("村网格", "");
        orgNameNews = orgNameNews.replace("苗族乡", "");
        orgNameNews = orgNameNews.replace("藏族乡", "");


        orgNameNews = orgNameNews.replace("保留", "");
        orgNameNews = orgNameNews.replace("居委", "");

        orgNameNews = orgNameNews.replace("街道", "");
//        orgNameNews = orgNameNews.replace("社区", "");
        orgNameNews = orgNameNews.replace("街", "");
        orgNameNews = orgNameNews.replace("乡", "");
        orgNameNews = orgNameNews.replace("村", "");
        orgNameNews = orgNameNews.replace("镇", "");
        orgNameNews = orgNameNews.replace("路", "");
        orgNameNews = orgNameNews.replace("场", "");
        orgNameNews = orgNameNews.replace("第", "");
        orgNameNews = orgNameNews.replace("城", "成");
        orgNameNews = orgNameNews.replace("波", "坡");
        orgNameNews = orgNameNews.replace("角", "桷");
        orgNameNews = orgNameNews.replace("河", "和");
        orgNameNews = orgNameNews.replace("坪", "平");
        orgNameNews = orgNameNews.replace("禅", "蝉");
        orgNameNews = orgNameNews.replace("檫", "擦");
        orgNameNews = orgNameNews.replace("岭", "嶺");
        orgNameNews = orgNameNews.replace("钟", "中");
        orgNameNews = orgNameNews.replace("棠", "堂");
        orgNameNews = orgNameNews.replace("湾", "塆");
        orgNameNews = orgNameNews.replace("中", "忠");
        orgNameNews = orgNameNews.replace("良", "梁");
        orgNameNews = orgNameNews.replace("观", "关");
        orgNameNews = orgNameNews.replace("心", "兴");
        orgNameNews = orgNameNews.replace("颈", "井");
        orgNameNews = orgNameNews.replace("鳡", "鱤");
        orgNameNews = orgNameNews.replace("庙", "妙");
        orgNameNews = orgNameNews.replace("荆", "井");
        orgNameNews = orgNameNews.replace("僳", "傈");
        orgNameNews = orgNameNews.replace("岩", "崖");
        orgNameNews = orgNameNews.replace("石", "实");
        orgNameNews = orgNameNews.replace("森", "燊");
        orgNameNews = orgNameNews.replace("青", "清");
        orgNameNews = orgNameNews.replace("丛", "从");
        orgNameNews = orgNameNews.replace("咀", "嘴");
        orgNameNews = orgNameNews.replace("丘", "秋");
        orgNameNews = orgNameNews.replace("蓠", "篱");
        orgNameNews = orgNameNews.replace("骑永胜", "永胜");
        orgNameNews = orgNameNews.replace("骑柑坳", "柑坳");
        orgNameNews = orgNameNews.replace("鞍", "安");
        orgNameNews = orgNameNews.replace("卷", "棬");
        orgNameNews = orgNameNews.replace("泥", "坭");
        orgNameNews = orgNameNews.replace("竹湖园", "竹湖");
        orgNameNews = orgNameNews.replace("龙", "隆");
        orgNameNews = orgNameNews.replace("渔", "鱼");
        orgNameNews = orgNameNews.replace("伏", "福");
        orgNameNews = orgNameNews.replace("篷", "蓬");
        orgNameNews = orgNameNews.replace("梁", "樑");
        orgNameNews = orgNameNews.replace("贯", "罐");
        orgNameNews = orgNameNews.replace("加", "嘉");
        orgNameNews = orgNameNews.replace("柏", "白");
        orgNameNews = orgNameNews.replace("震", "正");
        orgNameNews = orgNameNews.replace("保", "宝");
        orgNameNews = orgNameNews.replace("(部分)", "");
        orgNameNews = orgNameNews.replace("壁", "碧");
        orgNameNews = orgNameNews.replace("埝", "堰");
        orgNameNews = orgNameNews.replace("胡", "葫");
        orgNameNews = orgNameNews.replace("议", "义");
        orgNameNews = orgNameNews.replace("览", "笕");
        orgNameNews = orgNameNews.replace("粱", "梁");
        orgNameNews = orgNameNews.replace("净", "静");
        orgNameNews = orgNameNews.replace("房", "坊");

        orgNameNews = orgNameNews.replace("冈", "杠");
        orgNameNews = orgNameNews.replace("胡", "葫");

        orgNameNews = orgNameNews.replace("议", "义");
        orgNameNews = orgNameNews.replace("堡", "宝");
        orgNameNews = orgNameNews.replace("来", "莱");
        orgNameNews = orgNameNews.replace("谭", "潭");

        orgNameNews = orgNameNews.replace("风", "凤");
        orgNameNews = orgNameNews.replace("峰", "锋");
        orgNameNews = orgNameNews.replace("洋", "羊");
        orgNameNews = orgNameNews.replace("所", "院");

        orgNameNews = orgNameNews.replace("第", "");

        orgNameNews = orgNameNews.replace("01", "一");
        orgNameNews = orgNameNews.replace("02", "二");
        orgNameNews = orgNameNews.replace("03", "三");
        orgNameNews = orgNameNews.replace("04", "四");
        orgNameNews = orgNameNews.replace("05", "五");
        orgNameNews = orgNameNews.replace("06", "六");
        orgNameNews = orgNameNews.replace("07", "七");
        orgNameNews = orgNameNews.replace("08", "八");
        orgNameNews = orgNameNews.replace("09", "九");

        orgNameNews = orgNameNews.replace("1", "一");
        orgNameNews = orgNameNews.replace("2", "二");
        orgNameNews = orgNameNews.replace("3", "三");
        orgNameNews = orgNameNews.replace("4", "四");
        orgNameNews = orgNameNews.replace("5", "五");
        orgNameNews = orgNameNews.replace("6", "六");
        orgNameNews = orgNameNews.replace("7", "七");
        orgNameNews = orgNameNews.replace("8", "八");
        orgNameNews = orgNameNews.replace("9", "九");
        orgNameNews = orgNameNews.replace("10", "十");
        orgNameNews = orgNameNews.replace("11", "十一");
        orgNameNews = orgNameNews.replace("12", "十二");
        orgNameNews = orgNameNews.replace("13", "十三");
        orgNameNews = orgNameNews.replace("14", "十四");
        orgNameNews = orgNameNews.replace("15", "十五");

        orgNameNews = orgNameNews.replace("畴江社区", "寿江");
        orgNameNews = orgNameNews.replace("寿阝江", "寿江");
        orgNameNews = orgNameNews.replace("寿阝", "寿江");

        orgNameNews = orgNameNews.replaceAll("\uD844\uDF9A", "扁");


        orgNameNews = orgNameNews.replace("柏林桥", "柏林");
        orgNameNews = orgNameNews.replace("龚巷村第", "");

        return orgNameNews;
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
}
