package com.dldorg.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.core.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 */
public class OrtAdd {


    private static Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("auth", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIwMDAwMDAiLCJjb2RlIjoyMDAsInJvbGVfY29kZXMiOltdLCJ1c2VyX25hbWUiOiJsaXVsZWkiLCJhdXRob3JpdGllcyI6WyIxNTMzNzEyMTQzOTg2MjAwNjU5Il0sImNsaWVudF9pZCI6InVzZXJjZW50ZXIiLCJyb2xlX2lkcyI6WzE1MzM3MTIxNDM5ODYyMDA2NTldLCJhZG1pbmlzdHJhdG9yIjp0cnVlLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJ1c2VyX2lkIjoiMTg4NTcwIiwib3JnX2lkIjoiMSIsInN1Y2Nlc3MiOnRydWUsInNjb3BlIjpbImFsbCJdLCJvYXV0aF9pZCI6IiIsImV4cCI6MTY2MTQzMzc2OCwianRpIjoiZWQ2OWM4NDAtMjFlNC00ZGQyLTg1ODAtYjFlYzc2ZWU5MDZkIn0.oyAHTyAhd3hzjvvRPL1igjaHU7PLv5jfBCoWcL4QI5Q");
        return headerMap;
    }


    public static Map<String, Long> addXzOrg(Long parentId, String orgName, Long start) {
        Map<String, String> map = new HashMap<>(8);
        JSONObject jsonObject = getOrg(parentId);
        Integer orgLevel = jsonObject.getInteger("orgLevel");
        String departmentNo = jsonObject.getString("departmentNo");
        start = validateDepartmentNo(departmentNo, parentId, start);
        map.put("orgName", orgName);
        map.put("departmentNo", departmentNo + start);
        map.put("parentId", parentId.toString());
        map.put("parentOrgLevel", orgLevel.toString());
        map.put("orgLevelMark", getLevelMark(orgLevel.toString()));
        map.put("orgLevel", (--orgLevel).toString());

        map.put("remark", "自动新增");
        map.put("orgType", "90180001");
        String url = "http://10.0.188.11:9999/api/doraemon-system/organization/save";
        try {
            HttpUtil.sendPost(url, map, getHeaderMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = getAddId(parentId, orgName);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        data.put("dept", start + 1);
        return data;
    }

    public static Map<String, Long> addZnOrg(Long parentId, String orgName, Long start) {
        Map<String, String> map = new HashMap<>(8);
        JSONObject jsonObject = getOrg(parentId);
        Integer orgLevel = jsonObject.getInteger("orgLevel");
        String departmentNo = jsonObject.getString("departmentNo");
        start = validateDepartmentNo(departmentNo, parentId, start);
        map.put("orgName", orgName);
        map.put("departmentNo", departmentNo + start);
        map.put("parentId", parentId.toString());
        map.put("parentOrgLevel", orgLevel.toString());
        map.put("orgLevelMark", getLevelMark(orgLevel.toString()));
        map.put("orgLevel", (--orgLevel).toString());

        map.put("remark", "自动新增");
        map.put("orgType", "90180002");
        map.put("functionalOrgType", "90190048");
        String url = "http://10.0.188.11:9999/api/doraemon-system/organization/save";
        try {
            HttpUtil.sendPost(url, map, getHeaderMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = getAddId(parentId, orgName);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        data.put("dept", start + 1);
        return data;
    }

    private static Long validateDepartmentNo(String departmentNo, Long orgId, Long start) {
        if (start == null || start <= 10) {
            if (departmentNo.length() < 6) {
                start = 10L;
            } else {
                start = 100L;
            }
        }
        String url = "http://10.0.188.11:9999/api/doraemon-system/organization/validateDepartmentNo?departmentNo=" + departmentNo + start + "&parentId=" + orgId;
        String json = HttpUtil.sendGet(url, null, getHeaderMap());
        JSONObject jsonObject = JSONObject.parseObject(json);
        Boolean obj = jsonObject.getBoolean("data");
        if (obj) {
            start += 1;
            start = validateDepartmentNo(departmentNo, orgId, new Long(start));
        }
        return start;
    }


    private static String getLevelMark(String orgLevel) {
        if (orgLevel.equals("90170005")) {
            return "1046001";
        } else if (orgLevel.equals("90170004")) {
            return "1047001";
        } else if (orgLevel.equals("90170003")) {
            return "1048001";
        } else if (orgLevel.equals("90170002")) {
            return "1049001";
        } else if (orgLevel.equals("90170001")) {
            return "1050001";
        }
        return "";
    }

    private static JSONObject getOrg(Long id) {
        String url = "http://10.0.188.11:9999/api/doraemon-system/organization/getVOById?id=" + id;
        String json = HttpUtil.sendGet(url, null, getHeaderMap());
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject obj = jsonObject.getJSONObject("data");
        return obj;
    }


    public static Long getAddId(Long id, String orgName) {
        String url = "http://10.0.188.11:9999/api/doraemon-system/organization/listChild?id=" + id;
        String json = HttpUtil.sendGet(url, null, getHeaderMap());
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject js = jsonArray.getJSONObject(i);
            if (orgName.equals(js.getString("orgName"))) {
                return js.getLong("id");
            }
        }
        return null;
    }

    public static void updateOrgName(Long id, String orgName) {
        Map<String, String> map = new HashMap<>(2);
        map.put("id", id.toString());
        map.put("orgName", orgName);
        try {
            HttpUtil.sendPost("http://10.0.188.11:1000/api/tq-scgrid-doraemon-system-extend/organizationExtend/editOrg", map, getHeaderMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrg(Long id){
        Map<String, String> map = new HashMap<>(2);
        map.put("id", id.toString());
        try {
            HttpUtil.sendPost("http://10.0.188.11:9999/api/doraemon-system/organization/deleteById", map, getHeaderMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        addXzOrg(2704L,"鸳鸯社区",105L);
//        Map<String,Long> zn = addZnOrg(1533768071712346189L,"张三职能机构2",1L);
//        Map<String,Long> zn1 = addZnOrg(1533768071712346189L,"张三职能机构3",1L);
//        OrtAdd.updateOrgName(2186L,"长沟社区居委会1");

    }
}
