package com.stat.export;

import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.OracleDb;
import com.cloud.common.util.db.connection.PostgreSQLDb;
import com.cloud.common.util.map.MapUtils;
import com.cloud.excel.util.ExportUtil;
import com.cloud.excel.vo.DynamicResult;
import com.cloud.excel.vo.TitleVO;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件导出数据库
 *
 * @author liulei
 */
public class Issue {

    @Data
    class Tag {
        private String name;
        private String id;

        public Tag(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public static void main(String[] args) {
        Issue issue = new Issue();

//        List<Map<String,Object>> maps = issue.getOrgs(2L, ".1.", 2021, Arrays.asList("01","02","03","04","05","06"));
        issue.init(2L, ".1.", 2022, Arrays.asList("01","02","03","04","05","06"),"四川省");
        issue.banjie(2L, ".1.", 2022, Arrays.asList("01","02","03","04","05","06"),"四川省");
        issue.bigTag(2L, ".1.", 2022, Arrays.asList("01","02","03","04","05","06"),"四川省");

//        for(Map<String,Object> map : maps){
//            issue.init(Long.parseLong(map.get("ID").toString()), map.get("ORG_CODE").toString(), 2021, Arrays.asList("01","02","03","04","05","06"), map.get("ORG_NAME").toString());
//            issue.banjie(Long.parseLong(map.get("ID").toString()), map.get("ORG_CODE").toString(), 2021, Arrays.asList("01","02","03","04","05","06"), map.get("ORG_NAME").toString());
//            issue.bigTag(Long.parseLong(map.get("ID").toString()), map.get("ORG_CODE").toString(), 2021, Arrays.asList("01","02","03","04","05","06"), map.get("ORG_NAME").toString());
//        }

    }





    public void banjie(Long orgId, String orgCode, Integer year, List<String> months,String orgName) {
        String sql = "SELECT org.org_name,ADD_ISSUE_QTY,ADD_ISSUE_Checkissue ,(ADD_ISSUE_QTY + ADD_ISSUE_Checkissue) AS total,valid_qty  " +
                "FROM SG_STAT_ORGANIZATION_" + year + "_" + months.get(months.size() - 1) + " org LEFT JOIN ( " +
                "SELECT nvl(ORG_CODE,'" + orgCode + "') as org_code " +
                ",sum(ADD_ISSUE_QTY) AS ADD_ISSUE_QTY " +
                ",sum(ADD_ISSUE_Checkissue) AS ADD_ISSUE_Checkissue " +
                "    ,SUM(valid_qty) AS valid_qty " +
                "     from(" +
                "SELECT SUBSTR(ORG_CODE, 0, INSTR(ORG_CODE, '.', LENGTH('" + orgCode + "') + 2)) org_code, " +
                "  (nvl(ADD_ISSUE_QTY,0) - nvl(month_add_invalid_issue,0)) AS ADD_ISSUE_QTY " +
                "  ,(nvl(ADD_CHECKISSUE_QTY,0) - nvl(MONTH_ADD_INVALID_CHECKISSUE,0)) AS ADD_ISSUE_Checkissue " +
                "    ,(nvl(ADD_ISSUE_QTY,0) - nvl(month_add_invalid_issue,0) - nvl(valid_qty,0)) AS un_valid  " +
                "    ,nvl(valid_qty_month_all,0) as valid_qty_month_all, " +
                "    UNCOMPLETE_LASTMONTH, valid_qty " +
                "FROM sg_et_issue_org_stat_new " +
                "WHERE year = " + year + " AND ORG_CODE LIKE '" + orgCode + "%' " +
                " and MONTH in (" + months.stream().map(s->"'"+s+"'").collect(Collectors.joining(",")) + ") " +
                ") GROUP BY ORG_CODE  " +
                ")iss ON org.ORG_INTERNAL_CODE = iss.org_code WHERE org.parent_id = " + orgId + " AND org.org_type = 90180001 ORDER BY org.seq";

        DbComponent oracleComponent = OracleDb.createDb("192.168.10.99", "1521", "scgrid", "scgrid_statistics", "scgrid_statistics", "scgrid_!@#123");
        List<Map<String, Object>> dataMap = oracleComponent.readJdbcData(sql);
        oracleComponent.close();
        Long cou = 0L;
        try {
            for (Map<String, Object> map : dataMap) {
                if(map.get("VALID_QTY")==null){
                    map.put("VALID_QTY", 0);
                }
                if(map.get("ADD_ISSUE_QTY")==null){
                    map.put("ADD_ISSUE_QTY", 0);
                }
                map.put("VALID_QT", MapUtils.getProportion(Long.parseLong(map.get("VALID_QTY").toString()), Long.parseLong(map.get("ADD_ISSUE_QTY").toString())));
            }
        }catch (Exception e){
            System.out.println( "错误Sql：" + sql);
        }

        TitleVO titleVO = TitleVO.createTitleVO("事件综合统计");
        titleVO.append(new TitleVO("区域", "ORG_NAME", Boolean.TRUE));
        titleVO.append(new TitleVO("办件事件新增", "ADD_ISSUE_QTY", Boolean.TRUE));
        titleVO.append(new TitleVO("阅件事件新增", "ADD_ISSUE_CHECKISSUE", Boolean.TRUE));
        titleVO.append(new TitleVO("合计", "TOTAL", Boolean.TRUE));
        titleVO.append(new TitleVO("办件事件办结量", "VALID_QTY", Boolean.TRUE));
        titleVO.append(new TitleVO("办结率", "VALID_QT", Boolean.TRUE));

        DynamicResult dynamicResult = new DynamicResult(titleVO, dataMap);
        dynamicResult.setFileContent(orgName);
        ExportUtil.exportListData(dynamicResult, null);
    }


    public void bigTag(Long orgId, String orgCode, Integer year, List<String> months,String orgName) {
        List<Map<String, Object>> mapList = getTag();
        Map<String, Map<String, Object>> mapData = new HashMap<>();
        for (Map<String, Object> map : mapList) {
            mapData.put(map.get("id").toString(), map);
        }
        String sql = "SELECT TAG_ROOT_ID as ID" +
                " ,sum(nvl(ADD_ISSUE_QTY,0) - nvl(month_add_invalid_issue,0)) AS ADD_ISSUE_QTY " +
                " ,SUM(valid_qty) AS VALID_QTY " +
                " FROM SG_ET_ISSUE_ORG_BIG_TAG_STAT seiobts " +
                " WHERE year = " + year + " AND ORG_CODE LIKE '" + orgCode + "%' " +
                "    and MONTH in (" + months.stream().map(s->"'"+s+"'").collect(Collectors.joining(",")) + ") " +
                "     GROUP BY TAG_ROOT_ID";

        DbComponent oracleComponent = OracleDb.createDb("192.168.10.99", "1521", "scgrid", "scgrid_statistics", "scgrid_statistics", "scgrid_!@#123");
        List<Map<String, Object>> dataMap = oracleComponent.readJdbcData(sql);
        oracleComponent.close();
        Long cou = 0L;
        for (Map<String, Object> map : dataMap) {
            String key = map.get("ID").toString();
            Map<String, Object> maps = mapData.get(key);
            if (maps != null) {
                maps.put("add_issue_qty", Long.parseLong(maps.get("add_issue_qty").toString()) + Long.parseLong(map.get("ADD_ISSUE_QTY").toString()));
                maps.put("valid_qty", Long.parseLong(maps.get("valid_qty").toString()) + Long.parseLong(map.get("VALID_QTY").toString()));
                maps.put("valid_qt", MapUtils.getProportion(Long.parseLong(maps.get("valid_qty").toString()), Long.parseLong(maps.get("add_issue_qty").toString())));
                cou += Long.parseLong(maps.get("add_issue_qty").toString());
            }
        }
        for (Map<String, Object> map : mapList) {
            map.put("auus", MapUtils.getProportion(Long.parseLong(map.get("add_issue_qty").toString()), cou));
        }

        TitleVO titleVO = TitleVO.createTitleVO("分类统计");
        titleVO.append(new TitleVO("类型", "tag_name", Boolean.TRUE));
        titleVO.append(new TitleVO("办件事件新增", "add_issue_qty", Boolean.TRUE));
        titleVO.append(new TitleVO("办件事件办结数", "valid_qty", Boolean.TRUE));
        titleVO.append(new TitleVO("办件事件办结率", "valid_qt", Boolean.TRUE));
        titleVO.append(new TitleVO("事件占比", "auus", Boolean.TRUE));

        DynamicResult dynamicResult = new DynamicResult(titleVO, mapList);
        dynamicResult.setFileContent(orgName);

        ExportUtil.exportListData(dynamicResult, null);
    }

    public void init(Long orgId, String orgCode, Integer year, List<String> months,String orgName) {

        String tagBig = "SELECT id,tag_name,tag_code FROM issue_prod.sg_et_tag where parent_id = 0 and is_deleted = 0";

        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.49", "5432", "tq_scgrid_event", "postgres", "postgres", "Tianquekeji@123");

        List<Map<String, Object>> tagBigMap = dbComponent.readJdbcData(tagBig);
        tagBigMap.forEach(map -> {
            String tag_name = map.get("tag_name").toString();
            String tag_code = map.get("tag_code").toString();
            String tagSim = "select tag_name,id from issue_prod.sg_et_tag where tag_code like '" + tag_code + "%' and tag_code != '" + tag_code + "' and enabled = 1 and is_deleted = 0 order by id";
            List<Map<String, Object>> tagSimMap = dbComponent.readJdbcData(tagSim);
            List<Tag> tags = new ArrayList<>();
            tagSimMap.forEach(tag -> {
                tags.add(new Tag(tag.get("tag_name").toString(), tag.get("id").toString()));
            });
            smallTag(orgId, orgCode, year, months, tags, tag_name, orgName);
        });
        dbComponent.close();
    }


    /**
     * @param orgId   区域Id
     * @param orgCode 区域code
     * @param year    年
     * @param months  月
     * @param tags    小类标签id
     */
    public void smallTag(Long orgId, String orgCode, Integer year, List<String> months, List<Tag> tags, String fileName,String orgName) {
        List<String> tagList = new ArrayList<>();
        tags.forEach(b -> tagList.add(b.getId()));
        DbComponent dbComponent = OracleDb.createDb("192.168.10.99", "1521", "scgrid", "scgrid_statistics", "scgrid_statistics", "scgrid_!@#123");
        String sql = "SELECT nvl(org_code,'"+orgCode+"') as ORG_CODE,tag_id,sum(nvl(ADD_ISSUE_QTY,0)) as add_issue,sum(nvl(valid_qty,0)) as valid FROM ( " +
                "SELECT SUBSTR(ORG_CODE, 0, INSTR(ORG_CODE, '.', LENGTH('" + orgCode + "') + 2)) org_code,TAG_ID, " +
                "   (nvl(ADD_ISSUE_QTY,0) - nvl(month_add_invalid_issue,0)) AS ADD_ISSUE_QTY,valid_qty " +
                " FROM SG_ET_ISSUE_ORG_TAG_STAT " +
                " WHERE year = " + year + " AND ORG_CODE LIKE '" + orgCode + "%' " +
                "    and MONTH in (" + months.stream().map(s->"'"+s+"'").collect(Collectors.joining(",")) + ") " +
                " AND TAG_ID IN (" + tagList.stream().collect(Collectors.joining(",")) + ") " +
                "     ) " +
                "GROUP BY org_code,TAG_ID";

        String orgSql = "SELECT org.ORG_NAME,ORG_INTERNAL_CODE AS org_code,seq FROM SG_STAT_ORGANIZATION_" + year + "_" + months.get(months.size() - 1) + " org WHERE org.PARENT_ID = " + orgId + " AND org.ORG_TYPE = 90180001 ORDER BY org.seq";

        List<Map<String, Object>> org = dbComponent.readJdbcData(orgSql);

        List<Map<String, Object>> maps = dbComponent.readJdbcData(sql);


        List<Map<String, Object>> res = new ArrayList<>();

        TitleVO titleVO = TitleVO.createTitleVO(fileName);
        titleVO.append(new TitleVO("区域", "org_name", Boolean.TRUE));

        List<String> tagIds = new ArrayList<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }
        for (Map<String, Object> map : org) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("org_code", map.get("ORG_CODE"));
            tmp.put("org_name", map.get("ORG_NAME"));
            for (Tag tag : tags) {
                tmp.put("add_" + tag.getId(), 0);
                tmp.put("valid_" + tag.getId(), 0);
                tmp.put("bjl_" + tag.getId(), 0);
            }
            {
                tmp.put("add_all", 0);
                tmp.put("valid_all", 0);
                tmp.put("bjl_all", 0);

            }
            res.add(tmp);
        }

        Map<String, Map<String, Object>> mapByOrg = new HashMap<>();

        for (Map<String, Object> map : res) {
            mapByOrg.put(map.get("org_code").toString(), map);
        }

        for (Map<String, Object> map : maps) {
            String org_code = map.get("ORG_CODE").toString();
            String tag_Id = map.get("TAG_ID").toString();
            String add = map.get("ADD_ISSUE").toString();
            String valid = map.get("VALID").toString();
            String bjl = MapUtils.getProportion(Long.parseLong(valid), Long.parseLong(add));
            Map<String, Object> thisMap = mapByOrg.get(org_code);
            if(thisMap!=null) {
                thisMap.put("add_" + tag_Id, add);
                thisMap.put("valid_" + tag_Id, valid);
                thisMap.put("bjl_" + tag_Id, bjl);
            }else{
                System.out.println( "错误org数据" + org_code);
            }
        }

        for (Tag tag : tags) {
            TitleVO titleVO1 = new TitleVO(tag.getName(), tag.getId(), Boolean.TRUE);
            TitleVO add = new TitleVO("新增数量", "add_" + tag.getId(), Boolean.TRUE);
            TitleVO valid = new TitleVO("办结数量", "valid_" + tag.getId(), Boolean.TRUE);
            TitleVO bjl = new TitleVO("办结率", "bjl_" + tag.getId(), Boolean.TRUE);
            titleVO1.append(add);
            titleVO1.append(valid);
            titleVO1.append(bjl);
            titleVO.append(titleVO1);
        }

        {
            TitleVO titleVO1 = new TitleVO("合计", "all", Boolean.TRUE);
            TitleVO add = new TitleVO("新增数量", "add_all", Boolean.TRUE);
            TitleVO valid = new TitleVO("办结数量", "valid_all", Boolean.TRUE);
            TitleVO bjl = new TitleVO("办结率", "bjl_all", Boolean.TRUE);
            titleVO1.append(add);
            titleVO1.append(valid);
            titleVO1.append(bjl);
            titleVO.append(titleVO1);
        }

        Map<String, Long> add = new HashMap<>();
        Map<String, Long> valid = new HashMap<>();
        for (Tag tag : tags) {
            add.put(tag.getId(), 0L);
            valid.put(tag.getId(), 0L);
        }

        for (Map<String, Object> map : res) {
            for (String tagId : tagIds) {
                map.put("add_all", Long.parseLong(map.get("add_" + tagId).toString()) + Long.parseLong(map.get("add_all").toString()));
                map.put("valid_all", Long.parseLong(map.get("valid_" + tagId).toString()) + Long.parseLong(map.get("valid_all").toString()));
                add.put(tagId, Long.parseLong(map.get("add_" + tagId).toString()) + Long.parseLong(add.get(tagId).toString()));
                valid.put(tagId, Long.parseLong(map.get("valid_" + tagId).toString()) + Long.parseLong(valid.get(tagId).toString()));
            }
            map.put("bjl_all", MapUtils.getProportion(Long.parseLong(map.get("valid_all").toString()), Long.parseLong(map.get("add_all").toString())));
        }


        //添加列表合计
        {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("org_code", ".");
            tmp.put("org_name", "合计");
            Long addCount = 0L;
            Long validCount = 0L;
            for (Tag tag : tags) {
                addCount += Long.parseLong(add.get(tag.getId()).toString());
                validCount += Long.parseLong(valid.get(tag.getId()).toString());
                tmp.put("add_" + tag.getId(), add.get(tag.getId()));
                tmp.put("valid_" + tag.getId(), valid.get(tag.getId()));
                tmp.put("bjl_" + tag.getId(), MapUtils.getProportion(Long.parseLong(valid.get(tag.getId()).toString()), Long.parseLong(add.get(tag.getId()).toString())));
            }
            {
                tmp.put("add_all", addCount);
                tmp.put("valid_all", validCount);
                tmp.put("bjl_all", MapUtils.getProportion(validCount, addCount));

            }
            res.add(tmp);
        }

        DynamicResult dynamicResult = new DynamicResult(titleVO, res);
        dynamicResult.setFileContent(orgName);
        ExportUtil.exportListData(dynamicResult, null);
    }


    private List<Map<String,Object>> getOrgs(Long orgId, String orgCode, Integer year, List<String> months){
        String orgSql = "SELECT org.id,org.ORG_NAME,ORG_INTERNAL_CODE AS org_code,seq FROM SG_STAT_ORGANIZATION_" + year + "_" + months.get(months.size() - 1) + " org WHERE org.PARENT_ID = " + orgId + " AND org.ORG_TYPE = 90180001 ORDER BY org.seq";
        DbComponent dbComponent = OracleDb.createDb("192.168.10.99", "1521", "scgrid", "scgrid_statistics", "scgrid_statistics", "scgrid_!@#123");
        List<Map<String, Object>> mapList = dbComponent.readJdbcData(orgSql);
        dbComponent.close();
        return mapList;
    }


    private List<Map<String, Object>> getTag() {
        String tagBig = "SELECT ID,tag_name,0 as ADD_ISSUE_QTY, 0 as VALID_QTY FROM issue_prod.sg_et_tag where parent_id = 0 and is_deleted = 0";
        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.49", "5432", "tq_scgrid_event", "postgres", "postgres", "Tianquekeji@123");
        List<Map<String, Object>> mapList = dbComponent.readJdbcData(tagBig);
        dbComponent.close();
        return mapList;
    }
}
