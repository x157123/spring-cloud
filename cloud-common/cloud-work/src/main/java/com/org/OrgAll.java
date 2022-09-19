package com.org;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    public OrgAll addOrg(String orgName, Boolean type, Long orgId) {
        return this.addOrg(orgName, null, type, orgId);
    }

    public OrgAll addOrg(String orgName, Long seq, Boolean type, Long orgId) {
        //全路径匹配
        for (OrgAll org : orgs) {
            if (org.getOrgName().equals(orgName) || org.getNewOrgName().equals(orgName)) {
                org.setCom(org.getCom() + 1);
                return org;
            }
        }
        //模糊匹配
        int cou = 0;
        OrgAll orgAll = null;
        String orgNames = getStr(orgName);
        for (OrgAll org : orgs) {
            try {
                String orgNameNews = getStr(org.getOrgName());
                if (compare(orgNames, orgNameNews)) {
                    orgAll = org;
                    org.setCom(org.getCom() + 1);
                    cou += 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("异常数据" + orgName);
            }
        }
        if (cou <= 1 && orgAll != null) {
            orgAll.setNewOrgName(orgName);
            return orgAll;
        } else if (cou > 1) {
            System.out.println("出错组织机构：-->" + orgName);
        }
        orgAll = new OrgAll(orgName);
        if (seq == null) {
            seq = 0L;
            for (OrgAll org : orgs) {
                seq = org.getSeq() > seq ? org.getSeq() : seq;
            }
        }
        if (orgId != null) {
            orgAll.setId(orgId.toString());
        }
        orgAll.setSeq(seq + 1);
        orgAll.setType(type);
        orgAll.setLevel(this.getLevel() + 1);
        orgAll.setFatherOrgAll(this);
        orgs.add(orgAll);
        return orgAll;
    }

    private static boolean compare(String a, String b) {
        if (a.trim().equals(b.trim()) || Pinyin4jUtils.getFullPinyinStr(a).equals(Pinyin4jUtils.getFullPinyinStr(b))) {
            return true;
        }
        return false;
    }

    private static String getStr(String st) {
        String orgNameNews = st.trim();
//        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）|0[0-9]*$|0[0-9]*号$|[0-9]*社$|[0-9]*组$|村.*?组$|[0-9]*网格", "");
        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");

        orgNameNews = orgNameNews.replaceAll("四川", "");

        orgNameNews = orgNameNews.replaceAll("成都高新技术产业开发区", "高新区");
        orgNameNews = orgNameNews.replaceAll("四川", "");
        orgNameNews = orgNameNews.replaceAll("成都高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("成都东部新区", "东部新区");
        orgNameNews = orgNameNews.replaceAll("自贡高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("攀枝花高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("泸州高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("德阳高新区", "高新区");

        orgNameNews = orgNameNews.replaceAll("绵阳高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("绵阳科学城办事处", "科学城区");
        orgNameNews = orgNameNews.replaceAll("绵阳科创业园区", "科技城新区");
        orgNameNews = orgNameNews.replaceAll("绵阳经开区", "经开区");
        orgNameNews = orgNameNews.replaceAll("绵阳仙海管理委员会", "仙海管委会");

        orgNameNews = orgNameNews.replaceAll("经济技术开发区", "经开区");

        orgNameNews = orgNameNews.replaceAll("广元高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("遂宁高新区", "高新区");

        orgNameNews = orgNameNews.replaceAll("国家经济技术开发区", "遂宁经济技术开发区");

        orgNameNews = orgNameNews.replaceAll("内江高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("乐山高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("南充高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("宜宾高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("广安高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("达州高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("巴中高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("雅安高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("眉山高新区", "高新区");
        orgNameNews = orgNameNews.replaceAll("资阳高新区", "高新区");
//        orgNameNews = orgNameNews.replaceAll("竹友场社区居民委员会", "竹友社区");


//
//
        orgNameNews = orgNameNews.replaceAll("村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("居民委员会", "");
        orgNameNews = orgNameNews.replaceAll("居委会", "");
        orgNameNews = orgNameNews.replaceAll("街街道", "");
        orgNameNews = orgNameNews.replaceAll("街道办", "");
        orgNameNews = orgNameNews.replaceAll("街道", "");

        orgNameNews = orgNameNews.replaceAll("综合执法第一分局", "");
        orgNameNews = orgNameNews.replaceAll("村村民村村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("村村民委员会委会", "");
        orgNameNews = orgNameNews.replaceAll("村村民委员会网格", "");
        orgNameNews = orgNameNews.replaceAll("社区居委会筹委会", "");
        orgNameNews = orgNameNews.replaceAll("中心村村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("农村社区村委会", "");
        orgNameNews = orgNameNews.replaceAll("社区居民委员会", "");
        orgNameNews = orgNameNews.replaceAll("社区居民居委会", "");
        orgNameNews = orgNameNews.replaceAll("社区筹委居委会", "");
        orgNameNews = orgNameNews.replaceAll("社区村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("村民居民委员会", "");
        orgNameNews = orgNameNews.replaceAll("社区民委员会", "");
        orgNameNews = orgNameNews.replaceAll("社区居民委会", "");
        orgNameNews = orgNameNews.replaceAll("社区居委员会", "");
        orgNameNews = orgNameNews.replaceAll("社居民委员会", "");
        orgNameNews = orgNameNews.replaceAll("村村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("路社区居委会", "");
        orgNameNews = orgNameNews.replaceAll("居民委员委会", "");
        orgNameNews = orgNameNews.replaceAll("景区管理局", "");
        orgNameNews = orgNameNews.replaceAll("村村民委员", "");
        orgNameNews = orgNameNews.replaceAll("社区委员会", "");
        orgNameNews = orgNameNews.replaceAll("社区居委会", "");
        orgNameNews = orgNameNews.replaceAll("筹委居委会", "");
        orgNameNews = orgNameNews.replaceAll("居民委员会", "");
        orgNameNews = orgNameNews.replaceAll("居名委员会", "");
        orgNameNews = orgNameNews.replaceAll("村村民委会", "");
        orgNameNews = orgNameNews.replaceAll("村民委员会", "");
        orgNameNews = orgNameNews.replaceAll("村民居委会", "");
        orgNameNews = orgNameNews.replaceAll("村民委員會", "");
        orgNameNews = orgNameNews.replaceAll("管理委员会", "");
        orgNameNews = orgNameNews.replaceAll("旅游风景区", "");
        orgNameNews = orgNameNews.replaceAll("成都直管区", "");

        orgNameNews = orgNameNews.replaceAll("社区保留", "社区");
        orgNameNews = orgNameNews.replaceAll("网格保留", "网格");



        orgNameNews = orgNameNews.replaceAll("村民委会", "");
        orgNameNews = orgNameNews.replaceAll("村委员会", "");
        orgNameNews = orgNameNews.replaceAll("村村委会", "");
        orgNameNews = orgNameNews.replaceAll("社区委会", "");
        orgNameNews = orgNameNews.replaceAll("社区涉农", "");
        orgNameNews = orgNameNews.replaceAll("农村社区", "");
        orgNameNews = orgNameNews.replaceAll("小区社区", "");
        orgNameNews = orgNameNews.replaceAll("管理局", "");
        orgNameNews = orgNameNews.replaceAll("管委会", "");
        orgNameNews = orgNameNews.replaceAll("办事处", "");
        orgNameNews = orgNameNews.replaceAll("办事处", "");
        orgNameNews = orgNameNews.replaceAll("村委会", "");
        orgNameNews = orgNameNews.replaceAll("街道办事处", "");
        orgNameNews = orgNameNews.replaceAll("街道办", "");
        orgNameNews = orgNameNews.replaceAll("村网格", "");
        orgNameNews = orgNameNews.replaceAll("苗族乡", "");
        orgNameNews = orgNameNews.replaceAll("藏族乡", "");


        orgNameNews = orgNameNews.replaceAll("保留", "");
        orgNameNews = orgNameNews.replaceAll("居委", "");

        orgNameNews = orgNameNews.replaceAll("街道", "");
        orgNameNews = orgNameNews.replaceAll("社区", "");
        orgNameNews = orgNameNews.replaceAll("街", "");
        orgNameNews = orgNameNews.replaceAll("乡", "");
        orgNameNews = orgNameNews.replaceAll("村", "");
        orgNameNews = orgNameNews.replaceAll("镇", "");
        orgNameNews = orgNameNews.replaceAll("路", "");
        orgNameNews = orgNameNews.replaceAll("场", "");
        orgNameNews = orgNameNews.replaceAll("第", "");
        orgNameNews = orgNameNews.replaceAll("城", "成");
        orgNameNews = orgNameNews.replaceAll("波", "坡");
        orgNameNews = orgNameNews.replaceAll("角", "桷");
        orgNameNews = orgNameNews.replaceAll("河", "和");
        orgNameNews = orgNameNews.replaceAll("坪", "平");
        orgNameNews = orgNameNews.replaceAll("禅", "蝉");
        orgNameNews = orgNameNews.replaceAll("檫", "擦");
        orgNameNews = orgNameNews.replaceAll("岭", "嶺");
        orgNameNews = orgNameNews.replaceAll("钟", "中");
        orgNameNews = orgNameNews.replaceAll("棠", "堂");
        orgNameNews = orgNameNews.replaceAll("湾", "塆");
        orgNameNews = orgNameNews.replaceAll("中", "忠");
        orgNameNews = orgNameNews.replaceAll("良", "梁");
        orgNameNews = orgNameNews.replaceAll("观", "关");
        orgNameNews = orgNameNews.replaceAll("心", "兴");
        orgNameNews = orgNameNews.replaceAll("颈", "井");
        orgNameNews = orgNameNews.replaceAll("鳡", "鱤");
        orgNameNews = orgNameNews.replaceAll("庙", "妙");
        orgNameNews = orgNameNews.replaceAll("荆", "井");
        orgNameNews = orgNameNews.replaceAll("僳", "傈");
        orgNameNews = orgNameNews.replaceAll("岩", "崖");
        orgNameNews = orgNameNews.replaceAll("石", "实");
        orgNameNews = orgNameNews.replaceAll("森", "燊");
        orgNameNews = orgNameNews.replaceAll("青", "清");
        orgNameNews = orgNameNews.replaceAll("丛", "从");
        orgNameNews = orgNameNews.replaceAll("咀", "嘴");
        orgNameNews = orgNameNews.replaceAll("丘", "秋");
        orgNameNews = orgNameNews.replaceAll("蓠", "篱");
        orgNameNews = orgNameNews.replaceAll("骑永胜", "永胜");
        orgNameNews = orgNameNews.replaceAll("骑柑坳", "柑坳");
        orgNameNews = orgNameNews.replaceAll("鞍", "安");
        orgNameNews = orgNameNews.replaceAll("卷", "棬");
        orgNameNews = orgNameNews.replaceAll("泥", "坭");
        orgNameNews = orgNameNews.replaceAll("竹湖园", "竹湖");
        orgNameNews = orgNameNews.replaceAll("龙", "隆");
        orgNameNews = orgNameNews.replaceAll("渔", "鱼");
        orgNameNews = orgNameNews.replaceAll("伏", "福");
        orgNameNews = orgNameNews.replaceAll("篷", "蓬");
        orgNameNews = orgNameNews.replaceAll("梁", "樑");
        orgNameNews = orgNameNews.replaceAll("贯", "罐");
        orgNameNews = orgNameNews.replaceAll("加", "嘉");
        orgNameNews = orgNameNews.replaceAll("柏", "白");
        orgNameNews = orgNameNews.replaceAll("震", "正");
        orgNameNews = orgNameNews.replaceAll("保", "宝");
        orgNameNews = orgNameNews.replaceAll("(部分)", "");
        orgNameNews = orgNameNews.replaceAll("壁", "碧");
        orgNameNews = orgNameNews.replaceAll("埝", "堰");
        orgNameNews = orgNameNews.replaceAll("胡", "葫");
        orgNameNews = orgNameNews.replaceAll("议", "义");
        orgNameNews = orgNameNews.replaceAll("览", "笕");
        orgNameNews = orgNameNews.replaceAll("粱", "梁");
        orgNameNews = orgNameNews.replaceAll("净", "静");
        orgNameNews = orgNameNews.replaceAll("房", "坊");

        orgNameNews = orgNameNews.replaceAll("冈", "杠");
        orgNameNews = orgNameNews.replaceAll("胡", "葫");

        orgNameNews = orgNameNews.replaceAll("议", "义");
        orgNameNews = orgNameNews.replaceAll("堡", "宝");
        orgNameNews = orgNameNews.replaceAll("来", "莱");
        orgNameNews = orgNameNews.replaceAll("谭", "潭");

        orgNameNews = orgNameNews.replaceAll("风", "凤");
        orgNameNews = orgNameNews.replaceAll("峰", "锋");
        orgNameNews = orgNameNews.replaceAll("洋", "羊");
        orgNameNews = orgNameNews.replaceAll("所", "院");

        orgNameNews = orgNameNews.replaceAll("第", "");

        orgNameNews = orgNameNews.replaceAll("01", "一");
        orgNameNews = orgNameNews.replaceAll("02", "二");
        orgNameNews = orgNameNews.replaceAll("03", "三");
        orgNameNews = orgNameNews.replaceAll("04", "四");
        orgNameNews = orgNameNews.replaceAll("05", "五");
        orgNameNews = orgNameNews.replaceAll("06", "六");
        orgNameNews = orgNameNews.replaceAll("07", "七");
        orgNameNews = orgNameNews.replaceAll("08", "八");
        orgNameNews = orgNameNews.replaceAll("09", "九");

        orgNameNews = orgNameNews.replaceAll("1", "一");
        orgNameNews = orgNameNews.replaceAll("2", "二");
        orgNameNews = orgNameNews.replaceAll("3", "三");
        orgNameNews = orgNameNews.replaceAll("4", "四");
        orgNameNews = orgNameNews.replaceAll("5", "五");
        orgNameNews = orgNameNews.replaceAll("6", "六");
        orgNameNews = orgNameNews.replaceAll("7", "七");
        orgNameNews = orgNameNews.replaceAll("8", "八");
        orgNameNews = orgNameNews.replaceAll("9", "九");
        orgNameNews = orgNameNews.replaceAll("10", "十");
        orgNameNews = orgNameNews.replaceAll("11", "十一");
        orgNameNews = orgNameNews.replaceAll("12", "十二");
        orgNameNews = orgNameNews.replaceAll("13", "十三");
        orgNameNews = orgNameNews.replaceAll("14", "十四");
        orgNameNews = orgNameNews.replaceAll("15", "十五");

        orgNameNews = orgNameNews.replaceAll("畴江社区", "寿江");
        orgNameNews = orgNameNews.replaceAll("寿阝江", "寿江");
        orgNameNews = orgNameNews.replaceAll("寿阝", "寿江");

        orgNameNews = orgNameNews.replaceAll("\uD844\uDF9A", "扁");


        orgNameNews = orgNameNews.replaceAll("柏林桥", "柏林");
        orgNameNews = orgNameNews.replaceAll("龚巷村第", "");



        return orgNameNews;
    }
}
