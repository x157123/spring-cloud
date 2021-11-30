package com.cloud.excel.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.PostgreSQLDb;
import com.cloud.excel.util.ExportExcelStyle;
import com.cloud.excel.util.MapUtils;
import com.cloud.excel.vo.DynamicResult;
import com.cloud.excel.vo.TitleVO;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author liulei
 */
public class IssueData {

    /**
     * 获取一级大类
     */
    private Map<String, Object> getTagName(DbComponent dbComponent, Long type) {
        String sql = "select id,tag_name,tag_code from issue_prod.sg_et_tag set2 where id = %s";
        List<Map<String, Object>> list = getData(dbComponent, String.format(sql, type), Arrays.asList("id", "tag_name", "tag_code"));
        if (list.size() >= 1) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    /**
     * 获取小类
     */
    private List<Map<String, Object>> getTags(DbComponent dbComponent, Long type, String tagCode) {
        String sql = "select id,tag_name from issue_prod.sg_et_tag set2" +
                " where id != %s and is_deleted = 0  and  tag_code like '%s' order by id asc ";
        List<Map<String, Object>> list = getData(dbComponent, String.format(sql, type, tagCode + "%"), Arrays.asList("id", "tag_name"));
        return list;
    }

    private List<Map<String, Object>> getOrg(DbComponent dbComponent) {
        String sql = "select orgname as org_name, orgcode as org_code,seq from issue_prod.sg_org_tmp sot order by seq asc";
        List<Map<String, Object>> list = getData(dbComponent, sql, Arrays.asList("org_name", "org_code", "seq"));
        Collections.sort(list, (o1, o2) -> {
            if (Long.parseLong(o1.get("seq").toString()) < Long.parseLong(o2.get("seq").toString())) {
                return -1;
            } else {
                return 1;
            }
        });
        return list;
    }

    /**
     * 获取办件事件
     */
    public void getIssue(String orgCode,String start, String end, Long type) {

        List<Map<String, Object>> res = new ArrayList<>();

        DbComponent dbComponent = getDB();

        Map<String, Object> tag = getTagName(dbComponent, type);

        String tagCode = tag.get("tag_code").toString();

        List<Map<String, Object>> orgs = getOrg(dbComponent);

        List<Map<String, Object>> titles = getTags(dbComponent, type, tagCode);

        TitleVO titleVO = TitleVO.createTitleVO("办件-"+tag.get("tag_name").toString() + "  " + start + "-" + end);
        titleVO.append(new TitleVO("区域", "org_name", Boolean.TRUE));

        for (Map<String, Object> map : titles) {
            titleVO.append(new TitleVO(map.get("tag_name").toString(), MapUtils.getKey(map.get("id").toString()), Boolean.TRUE));
        }

        List<String> keys = titleVO.getHeadKey();
        keys.remove("org_name");

        for (Map<String, Object> map : orgs) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("org_name", map.get("org_name").toString());
            tmp.put("org_code", map.get("org_code").toString());
            for (String key : keys) {
                tmp.put(key, 0);
            }
            res.add(tmp);
        }

        String sql = "select org_code,tag_id,count(1) total from ( " +
                " select concat(substring_index(create_org_code ,'.',3),'.') org_code,tag.sys_tag_id as tag_id,issue.id  " +
                " from issue_prod.sg_et_issue issue  " +
                " left join issue_prod.sg_et_issue_tag tag on issue.id = tag.issue_id  " +
                " where issue.create_date >= '%s 00:00:00' and issue.create_date <= '%s 23:59:59' " +
                " and issue.create_org_code like '%s'  and tag.sys_tag_root_id = %s" +
                " and issue.is_deleted = 0 and issue.is_enabled = 1 and tag.is_deleted = 0 " +
                " )t group by org_code,tag_id ";

        List<Map<String, Object>> list = getData(dbComponent, String.format(sql, start, end, orgCode + "%", type), Arrays.asList("org_code", "tag_id", "total"));

        MapUtils.total(res, "org_code", list, "tag_id", null, Arrays.asList("total"));

        MapUtils.addTotalRow(res, "org_name", keys);

        DynamicResult dynamicResult = new DynamicResult(titleVO, res);

        exportListData(dynamicResult, null);
    }

    /**
     * 导出数据
     *
     * @param dynamicResult
     * @throws IOException
     */
    public void exportListData(DynamicResult dynamicResult, List<String> exportList) {
        try {
            // 指定文件输出位置
            File file = new File("E:/create/" + dynamicResult.getTitleVO().getTitle() + ".xlsx");
            List<List<String>> headTitles = dynamicResult.getTitleList(exportList);
            //表数据
            List<List<String>> rowList = dynamicResult.getDateList();
            //写入表头，表数据
            EasyExcel.write(file).excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new ExportExcelStyle(dynamicResult.getTitleVO().getHeadDepth()))
                    .head(headTitles).sheet(dynamicResult.getTitleVO().getTitle()).doWrite(rowList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        IssueData issueData = new IssueData();
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 1L);
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 2L);
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 3L);
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 4L);
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 5L);
        issueData.getCheckIssue(".","2021-11-15", "2021-11-21", 6L);

        issueData.getIssue(".","2021-11-15", "2021-11-21", 1L);
        issueData.getIssue(".","2021-11-15", "2021-11-21", 2L);
        issueData.getIssue(".","2021-11-15", "2021-11-21", 3L);
        issueData.getIssue(".","2021-11-15", "2021-11-21", 4L);
        issueData.getIssue(".","2021-11-15", "2021-11-21", 5L);
        issueData.getIssue(".","2021-11-15", "2021-11-21", 6L);

        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 1L);
        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 2L);
        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 3L);
        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 4L);
        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 5L);
        issueData.getCheckIssue(".","2021-11-22", "2021-11-28", 6L);

        issueData.getIssue(".","2021-11-22", "2021-11-28", 1L);
        issueData.getIssue(".","2021-11-22", "2021-11-28", 2L);
        issueData.getIssue(".","2021-11-22", "2021-11-28", 3L);
        issueData.getIssue(".","2021-11-22", "2021-11-28", 4L);
        issueData.getIssue(".","2021-11-22", "2021-11-28", 5L);
        issueData.getIssue(".","2021-11-22", "2021-11-28", 6L);
    }


    /**
     * 获取阅件事件导出
     */
    public void getCheckIssue(String orgCode,String start, String end, Long type) {


        List<Map<String, Object>> res = new ArrayList<>();

        DbComponent dbComponent = getDB();

        Map<String, Object> tag = getTagName(dbComponent, type);

        String tagCode = tag.get("tag_code").toString();

        List<Map<String, Object>> orgs = getOrg(dbComponent);

        List<Map<String, Object>> titles = getTags(dbComponent, type, tagCode);

        TitleVO titleVO = TitleVO.createTitleVO("阅件-"+tag.get("tag_name").toString() + "  " + start + "-" + end);
        titleVO.append(new TitleVO("区域", "org_name", Boolean.TRUE));

        for (Map<String, Object> map : titles) {
            titleVO.append(new TitleVO(map.get("tag_name").toString(), MapUtils.getKey(map.get("id").toString()), Boolean.TRUE));
        }

        List<String> keys = titleVO.getHeadKey();
        keys.remove("org_name");

        for (Map<String, Object> map : orgs) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("org_name", map.get("org_name").toString());
            tmp.put("org_code", map.get("org_code").toString());
            for (String key : keys) {
                tmp.put(key, 0);
            }
            res.add(tmp);
        }

        String sql = "select org_code,tag_id,count(1) total from (" +
                " select concat(substring_index(create_org_code ,'.',3),'.') org_code,tag.small_tag_id as tag_id,issue.id " +
                " from issue_prod.sg_et_check_issue issue " +
                " left join issue_prod.sg_et_check_issue_tag tag on issue.id = tag.issue_id " +
                " where issue.create_date >= '%s 00:00:00' and issue.create_date <= '%s 23:59:59'" +
                " and issue.create_org_code like '%s'  and tag.big_tag_id = %s" +
                " and issue.is_deleted = 0 and issue.is_enabled = 1 and tag.is_deleted = 0" +
                " )t group by org_code,tag_id ";

        List<Map<String, Object>> list = getData(dbComponent, String.format(sql, start, end, orgCode + "%", type), Arrays.asList("org_code", "tag_id", "total"));

        MapUtils.total(res, "org_code", list, "tag_id", null, Arrays.asList("total"));

        MapUtils.addTotalRow(res, "org_name", keys);

        DynamicResult dynamicResult = new DynamicResult(titleVO, res);

        exportListData(dynamicResult, null);


    }

    /**
     * 查询数据
     *
     * @param dbComponent
     * @param sql
     * @param keys
     */
    public List<Map<String, Object>> getData(DbComponent dbComponent, String sql, List<String> keys) {
        List<Map<String, Object>> list = new ArrayList<>();
        System.out.println(sql);
        dbComponent.readJdbcData(sql, list, HashMap<String, Object>::new, (k, b, rs) -> {
            for (String key : keys) {
                b.put(key, rs.getString(key));
            }
            k.add(b);
        });
        return list;
    }

    /**
     * 获取DB
     *
     * @return
     */
    private DbComponent getDB() {
        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.49", "5432",
                "tq_scgrid_event", "postgres", "postgres", "Tianquekeji@123");
        return dbComponent;
    }

}
