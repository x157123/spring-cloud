package org.cloud;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.cloud.common.util.http.OkHttpUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class Main {

    /**
     * 解析所有的接口
     *
     * @param swaggerJson
     * @return
     */
    public static List<PathInfoVo> getPathInfo(JSONObject swaggerJson) {
        JSONObject paths = swaggerJson.getJSONObject("paths");
        List<PathInfoVo> list = new ArrayList<>();
        //所有的definitions
        Map<String, JSONObject> refMap = getDefinitions(swaggerJson);
        if (paths != null) {
            Iterator<Map.Entry<String, Object>> it = paths.entrySet().iterator();
            while (it.hasNext()) {
                PathInfoVo pathInfo = new PathInfoVo();
                Map.Entry<String, Object> path = it.next();
                String pathUrl = path.getKey();
                pathInfo.setPathUrl(pathUrl);
                //请求方式
                JSONObject pathJson = paths.getJSONObject(pathUrl);
                Set<String> methodSet = pathJson.keySet();
                //当方法支持多种请求方式时也只取第一种
                if (CollectionUtils.isNotEmpty(methodSet)) {
                    String httpMethod = methodSet.iterator().next();
                    pathInfo.setHttpMethod(httpMethod);
                    JSONObject methodJson = pathJson.getJSONObject(httpMethod);
                    String summary = methodJson.getString("summary");
                    String operationId = methodJson.getString("operationId");
                    String description = methodJson.getString("description");
                    pathInfo.setDescription(description);
                    pathInfo.setOperationId(operationId);
                    pathInfo.setSummary(summary);
                    JSONArray parameters = methodJson.getJSONArray("parameters");
                    JSONObject responses = methodJson.getJSONObject("responses");
                    List<ParameterVo> reqParameters = getParameter(parameters, refMap);
                    pathInfo.setReqList(reqParameters);
                    List<ResponseVo> respList = getResponse(responses, refMap);
                    pathInfo.setRespList(respList);
                }
                list.add(pathInfo);
            }
        }
        return list;
    }

    /**
     * 解析响应数据
     *
     * @param responses
     * @param refMap
     * @return
     */
    public static List<ResponseVo> getResponse(JSONObject responses, Map<String, JSONObject> refMap) {
        List<ResponseVo> respParameters = new ArrayList<>();
        if (responses != null && responses.containsKey("200")) {
            //只解析200的数据
            JSONObject successJson = responses.getJSONObject("200");
            if (successJson.containsKey("schema")) {
                JSONObject schema = successJson.getJSONObject("schema");
                String schemaType = schema.getString("type");
                String ref = "";
                if (schema.containsKey("$ref")) {
                    ref = schema.getString("$ref");
                }
                if ("array".equalsIgnoreCase(schemaType)) {
                    JSONObject items = schema.getJSONObject("items");
                    if (items.containsKey("$ref")) {
                        ref = schema.getString("$ref");
                    } else {
                        ResponseVo resp = new ResponseVo();
                        resp.setName("");
                        resp.setType(items.getString("type"));
                        respParameters.add(resp);
                    }
                }
                if (StringUtils.isNotBlank(ref)) {
                    String def = ref.substring(14);
                    JSONObject defJson = refMap.get(def);
                    //    String type = defJson.getString("type");
                    JSONObject properties = defJson.getJSONObject("properties");
                    Set<String> respKeys = properties.keySet();
                    for (String key : respKeys) {
                        JSONObject respMap = properties.getJSONObject(key);
                        ResponseVo resp = new ResponseVo();
                        resp.setName(key);
                        resp.setFormat(respMap.getString("format"));
                        resp.setDescription(respMap.getString("description"));
                        String respType = respMap.getString("type");
                        resp.setType(StringUtils.isBlank(respType) ? "object" : respType);
                        resp.setRequired(respMap.getBooleanValue("required"));
                        if (respMap.containsKey("$ref")) {
                            String childRef = respMap.getString("$ref");
                            String childDef = childRef.substring(14);
                            JSONObject childDefJson = refMap.get(childDef);
                            JSONObject childProperties = childDefJson.getJSONObject("properties");
                            getRef(refMap, childProperties, resp, childDef, childDefJson);
                        } else if ("array".equalsIgnoreCase(respType)) {
                            JSONObject items = respMap.getJSONObject("items");
                            if (items.containsKey("$ref")) {
                                String itemRef = items.getString("$ref");
                                String itemDef = itemRef.substring(14);
                                JSONObject itemDefJson = refMap.get(itemDef);
                                JSONObject childProperties = itemDefJson.getJSONObject("properties");
                                if (childProperties != null) {
                                    getRef(refMap, childProperties, resp, itemDef, itemDefJson);
                                }
                                resp.setFormat(itemDef);
                            }
                        }
                        respParameters.add(resp);
                    }
                }
            }
        }
        return respParameters;
    }

    /**
     * 递归响应数据
     *
     * @param refMap       所有的definitions
     * @param parentVoName 上级ref的名称，与上级相同不继续递归（树结构）
     * @return
     */
    public static List<ResponseVo> getRef(Map<String, JSONObject> refMap, JSONObject childProperties, ResponseVo parentResp, String parentVoName, JSONObject childJson) {
        Set<String> childSet = childProperties.keySet();
        List<ResponseVo> childResp = new ArrayList<>();
        for (String key : childSet) {
            JSONObject childMap = childProperties.getJSONObject(key);
            ResponseVo resp = new ResponseVo();
            resp.setName(key);
            resp.setFormat(childMap.getString("format"));
            resp.setDescription(childMap.getString("description"));
            String childType = childMap.getString("type");
            resp.setType(StringUtils.isNotBlank(childType) ? childType : childJson.getString("type"));
            resp.setRequired(childMap.getBooleanValue("required"));
            childResp.add(resp);
            parentResp.setChildren(childResp);
            if (childMap.containsKey("$ref")) {
                String childRef = childMap.getString("$ref");
                String childDef = childRef.substring(14);
                JSONObject childDefJson = refMap.get(childDef);
                JSONObject pro = childDefJson.getJSONObject("properties");
                //additionalProperties
                if (pro != null && !childDef.equalsIgnoreCase(parentVoName)) {
                    getRef(refMap, pro, resp, childDef, childDefJson);
                }
            } else if ("array".equalsIgnoreCase(childType)) {
                JSONObject items = childMap.getJSONObject("items");
                if (items.containsKey("$ref")) {
                    String itemRef = items.getString("$ref");
                    String itemDef = itemRef.substring(14);
                    JSONObject itemDefJson = refMap.get(itemDef);
                    JSONObject pro = itemDefJson.getJSONObject("properties");
                    if (pro != null && !itemDef.equalsIgnoreCase(parentVoName)) {
                        getRef(refMap, pro, resp, itemDef, itemDefJson);
                    }
                    resp.setFormat(itemDef);
                }
            }
        }
        return childResp;
    }

    /**
     * 解析请求参数
     *
     * @param parameters
     * @param refMap
     * @return
     */
    public static List<ParameterVo> getParameter(JSONArray parameters, Map<String, JSONObject> refMap) {
        List<ParameterVo> reqParameters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(parameters)) {
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject paramJson = parameters.getJSONObject(i);
                ParameterVo param = JSON.parseObject(JSON.toJSONString(paramJson), ParameterVo.class);
                if (paramJson.containsKey("schema")) {
                    JSONObject schema = paramJson.getJSONObject("schema");
                    String schemaType = schema.getString("type");
                    String ref = "";
                    if (schema.containsKey("$ref")) {
                        ref = schema.getString("$ref");
                    }
                    if ("array".equalsIgnoreCase(schemaType)) {
                        JSONObject items = schema.getJSONObject("items");
                        if (items.containsKey("$ref")) {
                            ref = schema.getString("$ref");
                        } else {
                            List<ParameterVo> childParamList = new ArrayList<>();
                            ParameterVo childParam = new ParameterVo();
                            childParam.setName("");
                            childParam.setType(items.getString("type"));
                            childParamList.add(childParam);
                            param.setChildren(childParamList);
                        }
                    } else {
                        param.setType(schemaType);
                    }
                    if (StringUtils.isNotBlank(ref)) {
                        String def = ref.substring(14);
                        JSONObject defJson = refMap.get(def);
                        if (defJson != null) {
                            param.setType(defJson.getString("type"));
                            JSONObject properties = defJson.getJSONObject("properties");
                            Set<String> propertiesSet = properties.keySet();
                            List<ParameterVo> childParamList = new ArrayList<>();
                            for (String key : propertiesSet) {
                                ParameterVo childParam = new ParameterVo();
                                childParam.setName(key);
                                JSONObject proMap = properties.getJSONObject(key);
                                //根据type判断是否是array
                                String type = proMap.getString("type");
                                childParam.setDescription(StringUtils.isNotBlank(proMap.getString("description")) ? proMap.getString("description") : "");
                                childParam.setType(StringUtils.isBlank(type) ? "object" : type);
                                childParam.setFormat(proMap.getString("format"));
                                childParam.setRequired(proMap.getBooleanValue("required"));
                                if (proMap.containsKey("$ref")) {
                                    String childRef = proMap.getString("$ref");
                                    String childDef = childRef.substring(14);
                                    JSONObject childDefJson = refMap.get(childDef);
                                    JSONObject childProperties = childDefJson.getJSONObject("properties");
                                    if (childProperties != null) {
                                        getParamRef(refMap, childProperties, childParam, childDef, childDefJson);
                                    }
                                } else if ("array".equalsIgnoreCase(type)) {
                                    JSONObject items = proMap.getJSONObject("items");
                                    if (items.containsKey("$ref")) {
                                        String itemRef = items.getString("$ref");
                                        String itemDef = itemRef.substring(14);
                                        JSONObject itemDefJson = refMap.get(itemDef);
                                        JSONObject pro = itemDefJson.getJSONObject("properties");
                                        if (pro != null) {
                                            getParamRef(refMap, pro, childParam, itemDef, itemDefJson);
                                        }
                                        childParam.setFormat(itemDef);
                                    }
                                }
                                childParamList.add(childParam);
                                param.setChildren(childParamList);
                            }
                        }
                    }
                }
                reqParameters.add(param);
            }
        }
        return reqParameters;
    }

    public static List<ParameterVo> getParamRef(Map<String, JSONObject> refMap, JSONObject childProperties, ParameterVo parentResp, String parentVoName, JSONObject childJson) {
        List<ParameterVo> paramList = new ArrayList<>();
        Set<String> childSet = childProperties.keySet();
        for (String key : childSet) {
            JSONObject childMap = childProperties.getJSONObject(key);
            ParameterVo resp = new ParameterVo();
            resp.setName(key);
            resp.setFormat(childMap.getString("format"));
            resp.setDescription(childMap.getString("description"));
            String childType = childMap.getString("type");
            resp.setType(StringUtils.isNotBlank(childType) ? childType : childJson.getString("type"));
            resp.setRequired(childMap.getBooleanValue("required"));
            paramList.add(resp);
            parentResp.setChildren(paramList);
            if (childMap.containsKey("$ref")) {
                String childRef = childMap.getString("$ref");
                String childDef = childRef.substring(14);
                JSONObject childDefJson = refMap.get(childDef);
                JSONObject pro = childDefJson.getJSONObject("properties");
                //additionalProperties
                if (pro != null && !childDef.equalsIgnoreCase(parentVoName)) {
                    getParamRef(refMap, pro, resp, childDef, childDefJson);
                }
            } else if ("array".equalsIgnoreCase(childType)) {
                JSONObject items = childMap.getJSONObject("items");
                if (items.containsKey("$ref")) {
                    String itemRef = items.getString("$ref");
                    String itemDef = itemRef.substring(14);
                    JSONObject itemDefJson = refMap.get(itemDef);
                    JSONObject pro = itemDefJson.getJSONObject("properties");
                    if (pro != null && !itemDef.equalsIgnoreCase(parentVoName)) {
                        getParamRef(refMap, pro, resp, itemDef, itemDefJson);
                    }
                }
            }
        }
        return paramList;
    }

    /**
     * 获取所有的关联参数对象
     *
     * @param swaggerJson
     * @return
     */
    public static Map<String, JSONObject> getDefinitions(JSONObject swaggerJson) {
        Map<String, JSONObject> map = new HashMap<>();
        JSONObject definitions = swaggerJson.getJSONObject("definitions");
        Set<String> definitionSet = definitions.keySet();
        for (String def : definitionSet) {
            map.put(def, definitions.getJSONObject(def));
        }
        return map;
    }

    public static void main(String[] args) {

        String respStr = OkHttpUtils.builder().url("http://192.168.10.34:1000/api/tq-scgrid-openapi-service/openapi/v2/api-docs").get().sync();
        JSONObject swaggerJson = JSONObject.parseObject(respStr);
        List<PathInfoVo> pa = getPathInfo(swaggerJson);
        for (PathInfoVo pathInfoVo : pa) {
            if (pathInfoVo.getPathUrl().equals("/openapi/issue/dealIssue")) {
                System.out.println(JSONObject.toJSONString(pathInfoVo));
            }
        }
    }
}