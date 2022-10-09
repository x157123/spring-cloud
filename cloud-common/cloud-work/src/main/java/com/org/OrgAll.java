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
                if((orgName.equals("金华村民委员会0728") && org.getOrgName().equals("金华村村民委员会")) ||
                        orgName.equals("金华村村民委员会") && org.getOrgName().equals("金华村民委员会0728")){
                    System.out.println("--");
                }
                String orgNameNews = getStr(org.getOrgName());
                if (unExte(orgName, org.getOrgName()) && compare(orgNames, orgNameNews)) {
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

    private static boolean unExte(String a,String b){
        if(isSg(a) || isBg(b) || isBg(a) || isSg(b)){
            return false;
        }
        return true;
    }

    private static boolean isSg(String str){
        String strs = ",五宝社区居委会,五宝村委会,凤鸣社区居委会,凤鸣村委会,凤凰社区居委会,凤皇村委会,三多寨社区居委会,三多寨村委会,董家社区居委会,董家村委会,庙坝社区居委会," +
                "庙坝村委会,金胜社区居委会,金胜村委会,茴新社区居委会,茴新村委会,济公社区居委会,济公村委会,王井街社区居委会,王井村村委会," +
                "九洪街社区居委会,九洪村村委会,双古场社区居民委员会,双古村委会,高石社区居委会,高石村委会,和平社区居委会,和平村委会," +
                "五道河社区居民委员会,五道河村民委员会,沙沟社区居民委员会,沙沟村民委员会,平地街社区居民委员会,平地村民委员会," +
                "得石社区居民委员会,得石社区村民委员会,禹王宫社区居民委员会,禹王宫社区村民委员会,垭口社区居民委员会,垭口村村民委员会," +
                "挂榜社区居民委员会,挂榜村村民委员会,金江社区居民委员会,金江村民委员会,方山社区居民委员会,方山村村民委员会," +
                "中峰社区居民委员会,中峰村民委员会,玉龙社区居民委员会,玉龙村民委员会,五通社区居民委员会,五通村民委员会,木广社区居民委员会," +
                "木广村民委员会,后山社区居民委员会,后山村民委员会,大石社区居民委员会,大石村民委员会,永乐街社区居民委员会,永乐村民委员会," +
                "双河村民委员会,双河村民委员会[鱼化镇],土城街社区居民委员会,土城村民委员会,石宝街社区居民委员会,石宝村民委员会,白沙街社区居民委员会," +
                "白沙村民委员会,皇华街社区居民委员会,皇华村民委员会,黄荆社区居民委员会,黄荆村村民委员会,玄武社区居委会,玄武村村民委员会," +
                "北塔社区居委会,北塔村村民委员会,龙华社区居委会,龙华村村民委员会,兴隆街社区居委会,兴隆村村民委员会,铜山街社区居委会," +
                "铜山村村民委员会,积金场镇社区居委会,积金村村民委员会,左街社区居委会,左街村村民委员会,柏树村村民委员会,柏树乡村委会," +
                "白果乡村委会,白果村村民委员会,永丰乡村委会,永丰村村民委员会,通山乡村委会,通山村村民委员会,罗汉场社区居民委员会," +
                "罗汉村民委员会,紫阳社区居民委员会,紫阳社区委员会,凤凰场社区居民委员会,凤凰村民委员会,朝真场社区居民委员会,朝真村民委员会," +
                "解放街社区居民委员会,解放村村民委员会,古井场社区居民委员会,古井村村民委员会,八洞场社区居民委员会,八洞村村民委员会," +
                "中太场社区居民委员会,中太村村民委员会,王渡村民委员会,王渡社区居民委员会,天官村民委员会,天观社区居民委员会," +
                "两河社区居民委员会,两河村民委员会,登高社区居民委员会,登高村民委员会,陈江社区,陈江村村民委员会,明月路社区,明月村," +
                "圣莲岛社区居民委员会,圣莲岛,保石镇社区,保石村村民委员会网格,中兴镇社区,中心场村村民委员会,云丰社区居委会,云峰村村民委员会," +
                "狮子村村民委员会,柿子村村民委员会,常乐社区居委会,常乐村村民委员会,回龙社区居民委员会,回龙村村民委员会,梨子坝社区管委会," +
                "梨子坝村委会,同意村,同意村村委会,龙门社区,龙门村,田溪口社区居委会,田溪口村村委会,三河街社区居委会,三河村村委会,龙会场居委会," +
                "龙会村村委会,大井场社区居委会,大井村,宋家场社区居委会,宋家村,鱼溪场社区居委会,鱼溪村,石鹅场社区居委会,石鹅村村民委员会," +
                "谭坝社区,谭坝村,沫江村,沫江社区,踏水社区居委会,踏水村委会,轸溪社区居委会,轸溪村委会,余溪社区,余溪村,茶花社区居委会,茶花村委会," +
                "南阳社区居委会,南阳村委会,泉水社区居委会,泉水村委会,新场社区居委会,新场村委会,敖家场社区居委会,敖家村委会,马踏街社区居委会," +
                "马踏村委会,周坡街社区居委会,周坡村委会,大佛街社区居委会,大佛村委会,千佛街社区居委会,千佛村委会,磨池社区居委会,磨池村委会," +
                "三江街社区居委会,三江村委会,高凤街社区居委会,高凤村委会,五渡社区居民委员会,五渡村民委员会,晓霞路社区居民委员会," +
                "晓霞村村委会,新华路社区居民委员会,新华村村委会,石垭社区居民委员会,石垭农村社区,洛阳社区居民委员会,洛阳村委会," +
                "悦来社区居民委员会,悦来村委会,龙凤社区居民委员会,龙凤场村委会,飞凤居民委员会,飞凤村民委员会,牟坪社区居民委员会," +
                "牟坪村民委员会,柏杨社区居民委员会,白洋村民委员会,孔滩社区居民委员会,孔滩村民委员会,白龙社区居民委员会,白龙村民委员会," +
                "新华社区村民委员会,新华村,凉风社区居民委员会,良丰村民委员会,伏龙村民委员会,复龙社区居民委员会,金象村民委员会,金象村," +
                "商州社区居民委员会,商州村民委员会,豆坝社区居民委员会,豆坝村民委员会,双龙社区居民委员会,双龙村民委员会,征远村民委员会," +
                "征远社区居民委员会,古柏社区居民委员会,古柏村民委员会,重兴村民委员会,中心村民委员会,迎安社区居民委员会,迎安村村民委员会," +
                "底蓬社区居民委员会,底蓬村民委员会,蟠龙社区居民委员会,蟠龙村民委员会,大井社区居民委员会,大井村民委员会,腾龙社区居民委员会," +
                "腾龙村民委员会,东升社区居民委员会,东升村民委员会,蒿坝社区居民委员会,蒿坝村民委员会,僰王社区居民委员会,博望村民委员会," +
                "石海社区居民委员会,石海村民委员会,金鹅池社区居民委员会,金鹅池村,麒麟苗族乡社区,麒麟村民委员会,三河口社区居民委员会," +
                "三河口村民委员会,明月街道社区居委会,明月村委会,高兴街道社区居委会,高兴村委会,农村网格化工作站,农村网格化工作站[双河镇]," +
                "农村网格化工作站,农村网格化工作站[花池乡],农村网格化工作站,农村网格化工作站[凉风乡],农村网格化工作站,农村网格化工作站[凤林乡]," +
                "农村网格化工作站,农村网格化工作站[漆碑乡],农村网格化工作站,农村网格化工作站[凤鸣乡],农村网格化工作站,农村网格化工作站[庆云乡]," +
                "社区,社区[木头乡],涌兴社区居民委员会,永兴社区居民委员会,八庙社区居民委员会,八庙村民委员会,阁溪社区,阁溪村,合并村,合并,枣林社区,枣林村,渔江村,渔江社区,寺岭社区,寺岭村,冉家坝街道居民委员会," +
                "冉家坝村民委员会,龙凤场街道居民委员会,龙凤村民委员会,河坝场社区居民委员会,河坝场村民委员会,兴隆场社区居委会,兴隆村民委员会,烟溪场社区居委会,烟溪村民委员会,朝阳社区居民委员会,朝阳村村民委员会,黄金社区居民委员会,黄金村村民委员会," +
                "宝塔社区居民委员会,宝塔村,凤仪社区居民委员会,凤仪村,朱公社区居民委员会,朱公村,桃园社区居民委员会,桃源村,石矿社区居民委员会,石矿村,流坝社区居民委员会,流坝村,红岩社区,红岩村,燕山村,燕山社区,高塔社区居民委员会,高塔村,金盆村,金盆社区,双桂社区,双桂村,黑潭社区居民委员会,黑潭村,和平社区居民委员会,和平村,平岗社区居民委员会,平岗村,仁和社区居民委员会,仁和村,桥亭社区居民委员会,桥亭村,上两社区居民委员会,上两村,长沟社区居民委员会,长沟村,新立村,新立社区,沙坝社区居民委员会,沙坝村,关坝社区居民委员会,关坝村,茅河村村委会,茅河社区居委会,新村村委会,新村社区,场镇社区居委会,场镇社区居民委员会,中和场社区,中和村村民委员会,石岭场社区,石岭村村民委员会,东峰场社区,东峰村村民委员会,孔雀社区居民委员会,孔雀村村民委员会,七盘沟社区居委会,七盘沟村委会,耿达镇社区居民委员会,耿达村村民委员会,漳腊社区居民委员会,漳腊村民委员会,梨花社区居委会,梨花村村民委员会,漳扎社区居委会,漳扎村村民委员会,彭丰社区居委会,彭丰村村民委员会,新街社区居民委员会," +
                "新街村民委员会,营盘社区居民委员会,营盘村民委员会,夺巴村民委员会,夺巴村村民委员会,然罗村民委员会,然洛村民委员会,沙坝社区,沙坝村,兴隆街道居民委员会,兴隆村民委员会,斜卡乡雪洼村（纳布场定居点）,斜卡乡雪洼村 （雪洼定居点）,乃渠乡七日村（五一桥电站）,乃渠乡七日村（乃渠乡花椒油加工厂）,汤古村民委员会,汤古乡（猎塔湖旅游开发有限公司）,俄古村民委员会,俄估村民委员会,斯俄村,斯俄乡,呷拉村民委员会,呷拉乡,来马村民委员会,来玛乡,四通达村民委员会,四通达乡,甘孜寺管理局,甘孜寺,洛须镇居民委员会,洛须居民委员会,混查村民委员会,浑查村民委员会,作尼村民委员会,作尼村民委员会[上木拉乡],乃沙村民委员会,乃沙村民委员会[上木拉乡],马五村民委员会,马武村民委员会,康宁社区,康宁村村民委员会," +
                "太平街社区,太平村村民委员会,平川社区居民委员会,平川村村民委员会,堵格社区居民委员会,堵格村村民委员会,姜州街社区居民委员会,姜州村村民委员会,新街社区居民委员会,新街村村民委员会,南坪社区居民委员会,南坪村民委员会,乃拖社区居民委员会,乃托村民委员会,西宁镇社区居民委员会,西宁村民委员会,汶水镇社区居民委员会,汶水村民委员会,回龙村民委员会,回龙场村民委员会,莫红村民委员会,莫红社区居民委员会";
        if(strs.indexOf(str) > 0){
            return true;
        }
        return false;
    }

    private static boolean isBg(String str){
        String strs = ",梨花村村民委员会,梨花社区居民委员会,彭丰社区居民委员会,彭丰村村民委员会,漳扎村村民委员会,漳扎社区居民委员会,龙康村村民委员会,隆康社区居民委员会,营盘社区居民委员会,营盘村村民委员会,漳腊村村民委员会,漳腊社区居民委员会,七盘沟社区居民委员会,七盘沟村村民委员会,柏杨社区居民委员会,白洋村村民委员会,蒿坝村村民委员会,蒿坝社区居民委员会,石海社区居民委员会,石海村村民委员会,金鹅池社区居民委员会,金鹅池村村民委员会,商州社区居民委员会,商州村村民委员会,征远社区居民委员会,征远村村民委员会,凉风社区居民委员会,良丰村村民委员会,古罗社区居民委员会,古罗村村民委员会,复龙社区居民委员会,伏龙村村民委员会,古柏社区居民委员会,古柏村村民委员会,双龙社区居民委员会,双龙村村民委员会,豆坝村村民委员会,豆坝社区居民委员会,东升村村民委员会,东升社区居民委员会,底蓬社区居民委员会,底蓬村村民委员会,蟠龙社区居民委员会,蟠龙村村民委员会,大井社区居民委员会,大井村村民委员会,重兴村村民委员会,中心村村民委员会,迎安社区居民委员会,迎安村村民委员会,烟溪村村民委员会,烟溪场社区居民委员会,城南村村民委员会,城南社区居民委员会,鹦哥嘴社区居民委员会,鹦哥嘴村村民委员会,沙坝子村村民委员会,沙坝子社区居民委员会,冉家坝村村民委员会,冉家坝社区居民委员会,兴隆场社区居民委员会,兴隆村村民委员会,龙凤场社区居民委员会,龙凤村村民委员会,河坝场社区居民委员会,河坝场村村民委员会,阳光村村民委员会,阳光社区居民委员会,东垭社区居民委员会,东垭村村民委员会,关坝社区居民委员会,关坝村村民委员会,凉水村村民委员会,凉水社区居民委员会,燕山社区居民委员会,燕山村村民委员会,平岗村村民委员会,平岗社区居民委员会,和平社区居民委员会,和平村村民委员会,瓦池村村民委员会,瓦池社区居民委员会,双桂社区居民委员会,双桂村村民委员会,沙坝社区居民委员会,沙坝村村民委员会,仁和社区居民委员会,仁和村村民委员会,桥亭社区居民委员会,桥亭村村民委员会,长河村村民委员会,长河社区居民委员会,上两村村民委员会,上两社区居民委员会,朝阳村村民委员会,朝阳社区居民委员会,新立社区居民委员会,新立村村民委员会,凤仪村村民委员会,凤仪社区居民委员会,荆江社区居委会,荆江村村民委员会,长滩村村民委员会,长滩社区居民委员会,朱公社区居民委员会,朱公村村民委员会,宝塔社区居民委员会,宝塔村村民委员会,花桥社区居民委员会,花桥村村民委员会,金盆社区居民委员会,金盆村村民委员会,高塔社区居民委员会,高塔村村民委员会,黑潭村村民委员会,黑潭社区居民委员会,红岩社区居民委员会,红岩村村民委员会,桥坝村村民委员会,桥坝社区居民委员会,石矿社区居民委员会,石矿村村民委员会,流坝社区居民委员会,流坝村村民委员会,石滩社区居民委员会,石滩村村民委员会,雪花寺村村民委员会,雪花寺社区居民委员会,菩船社区居民委员会,菩船村村民委员会,铁炉坝村村民委员会,铁炉坝社区居民委员会,槐树社区居民委员会,槐树村村民委员会,桃源村村民委员会,桃园社区居民委员会,沙坝村村民委员会,沙坝场社区居民委员会,八庙村村民委员会,八庙社区居民委员会,永兴社区居民委员会,涌兴社区居民委员会,瓦卡村居民委员会,瓦卡村村民委员会,城区村村民委员会,城区居民委员会,子耳村村民委员会,子耳社区居民委员会,沙坝村村民委员会,沙坝社区居民委员会,洛须村村民委员会,洛须社区居民委员会,金华村村民委员会,金花村村民委员会,解放街社区居民委员会,解放村村民委员会,八洞村村民委员会,八洞场社区居民委员会,会龙村村民委员会,回龙村村民委员会,古井场社区居民委员会,古井村村民委员会,中太村村民委员会,中太场社区居民委员会,凤凰村村民委员会,凤凰场社区居民委员会,朝真村村民委员会,朝真场社区居民委员会,红江社区居民委员会,红江村村民委员会,翰林村村民委员会,翰林社区居民委员会,栖凤村村民委员会,栖凤社区居民委员会,梨子坝村村民委员会,梨子坝社区居民委员会,红旗村村民委员会,红旗社区居民委员会,回龙村村民委员会,回龙社区居民委员会,常乐社区居民委员会,常乐村村民委员会,挂榜社区居民委员会,挂榜村村民委员会,垭口村村民委员会,垭口社区居民委员会,禹王宫村村民委员会,禹王宫社区居民委员会,得石村村民委员会,得石社区居民委员会,五道河社区居民委员会,五道河社区村民委员会,平地街社区居民委员会,平地社区村民委员会,金江社区村民委员会,金江社区居民委员会,沙沟社区村民委员会,沙沟社区居民委员会,踏水村村民委员会,踏水社区居民委员会,沫江社区居民委员会,沫江村村民委员会,谭坝社区居民委员会,谭坝村村民委员会,余溪社区居民委员会,余溪村村民委员会,轸溪社区居民委员会,轸溪村村民委员会,三江村村民委员会,三江街社区居民委员会,大佛社区居民委员会,大佛村村民委员会,周坡村村民委员会,周坡街社区居民委员会,千佛街社区居民委员会,千佛村村民委员会," +
                "马踏社区居民委员会,马踏村村民委员会,高凤街社区居民委员会,高凤村村民委员会,磨池社区居民委员会,磨池村村民委员会,敖家场社区居民委员会,敖家村村民委员会,泉水社区居民委员会,泉水村村民委员会,茶花村村民委员会,茶花社区居民委员会,五渡村村民委员会,五渡社区居民委员会,金胜社区居民委员会,金胜村村民委员会,茴新村村民委员会,茴新社区居民委员会,三多寨村村民委员会,三多寨社区居民委员会,董家村村民委员会,董家社区居民委员会,庙坝社区居民委员会,庙坝村村民委员会,济公社区居民委员会,济公村村民委员会,王井街社区居民委员会,王井村村民委员会,九洪村村民委员会,九洪社区居民委员会,高石社区居民委员会,高石村村民委员会,和平社区居民委员会,和平村村民委员会,双古场社区居民委员会,双古村村民委员会,五宝社区居民委员会,五宝村村民委员会,凤凰村村民委员会,凤凰社区居民委员会,凤鸣村村民委员会,凤鸣社区居民委员会,井田村村民委员会,井田社区居民委员会,陈江村村民委员会,陈江社区居民委员会,两河村村民委员会,两河社区居民委员会,登高社区居民委员会,登高村村民委员会,王渡村村民委员会,王渡社区居民委员会,天观社区居民委员会,天官村村民委员会,康宁村村民委员会,康宁社区居民委员会,乃托社区居民委员会,乃托村村民委员会,瓦吉木村村民委员会,瓦吉木社区居民委员会,新街社区居民委员会,新街村村民委员会,姜州街社区居民委员会,姜州村村民委员会,堵格社区居民委员会,堵格村村民委员会,汶水村村民委员会,汶水社区居民委员会,回龙场村村民委员会,回龙村村民委员会,莫红村村民委员会,莫红社区居民委员会,南坪村村民委员会,南坪社区居民委员会,平川村村民委员会,平川街道社区居民委员会,太平街社区居民委员会,太平村村民委员会,龙门社区居民委员会,龙门村村民委员会,龙会村村民委员会,龙会场社区居民委员会,三河社区居民委员会,三河路社区居民委员会,鱼溪村村民委员会,鱼溪场社区居民委员会,宋家社区村民委员会,宋家场社区居民委员会,大井场社区居民委员会,大井村村民委员会,田溪口社区居民委员会,田溪口村村民委员会,石鹅村村民委员会,石鹅场社区居民委员会,晓霞路社区居民委员会,晓霞社区居民委员会,新华社区居民委员会,新华路社区居民委员会,龙凤场农村社区居民委员会,龙凤社区居民委员会,洛阳社区居民委员会,洛阳村民委员会,石垭农村社区居民委员会,石垭社区居民委员会,悦来社区居民委员会,悦来村民委员会,孔雀村村民委员会,孔雀村社区居民委员会,东峰场社区居民委员会,东峰村村民委员会,石岭场社区居民委员会,石岭村村民委员会,中和场社区居民委员会,中和村村民委员会,五通社区居民委员会,五通村村民委员会,木广村村民委员会,木广社区居民委员会,黄荆村村民委员会,黄荆社区居民委员会,皇华村村民委员会,皇华社区居民委员会,石宝街社区居民委员会,石宝村村民委员会,白沙村村民委员会,白沙街社区居民委员会,土城村村民委员会,土城街社区居民委员会　,后山村村民委员会,后山社区居民委员会,大石社区居民委员会,大石村村民委员会,分水村村民委员会,分水社区居民委员会,中峰村村民委员会,中峰街社区居民委员会,方山社区居民委员会,方山村村民委员会,罗汉村村民委员会,罗汉场社区居民委员会,会龙镇,回龙镇,会龙村村民委员会,回龙村村民委员会,铜山社区居民委员会,铜山村村民委员会,兴隆村村民委员会,兴隆街社区居民委员会,龙华社区居民委员会,龙华村村民委员会,北塔社区居民委员会,北塔村村民委员会,玄武社区居民委员会,玄武村村民委员会,茅河社区居民委员会,茅河村村民委员会,花滩社区居委会,花滩村村民委员会";
        if(strs.indexOf(str) > 0){
            return true;
        }
        return false;
    }

    private static String getStr(String st) {
        String orgNameNews = st.trim();
//        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）|0[0-9]*$|0[0-9]*号$|[0-9]*社$|[0-9]*组$|村.*?组$|[0-9]*网格", "");
        orgNameNews = orgNameNews.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");

        orgNameNews = orgNameNews.replaceAll("四川", "");

        orgNameNews = orgNameNews.replaceAll("[^\\u4e00-\\u9fa5]", "");

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
        orgNameNews = orgNameNews.replaceAll("居委会", "");
        orgNameNews = orgNameNews.replaceAll("管理局", "");
        orgNameNews = orgNameNews.replaceAll("管委会", "");
        orgNameNews = orgNameNews.replaceAll("街道办事处", "");
        orgNameNews = orgNameNews.replaceAll("办事处", "");
        orgNameNews = orgNameNews.replaceAll("办事处", "");
        orgNameNews = orgNameNews.replaceAll("村委会", "");
        orgNameNews = orgNameNews.replaceAll("街道办", "");
        orgNameNews = orgNameNews.replaceAll("村网格", "");
        orgNameNews = orgNameNews.replaceAll("苗族乡", "");
        orgNameNews = orgNameNews.replaceAll("藏族乡", "");


        orgNameNews = orgNameNews.replaceAll("保留", "");
        orgNameNews = orgNameNews.replaceAll("居委", "");

        orgNameNews = orgNameNews.replaceAll("街街道", "");
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
