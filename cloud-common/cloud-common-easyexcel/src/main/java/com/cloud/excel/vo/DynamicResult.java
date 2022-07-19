package com.cloud.excel.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 */
@Data
public class DynamicResult {

    /**
     * 表头
     */
    private TitleVO titleVO;

    /**
     * 表头
     */
    private List<TitleVO> columns;

    /**
     * 结果
     */
    private List<Map<String, Object>> data;

    /**
     * 文件目录
     */
    private String fileContent;


    public DynamicResult(TitleVO titleVO, List<Map<String, Object>> data) {
        this.titleVO = titleVO;
        this.columns = titleVO.getChildren();
        this.data = data;
    }

    /**
     * 获取结果中的表头用于导出
     *
     * @return
     */
    @Transient
    public List<List<String>> getTitleList() {
        return this.getTitleList(null);
    }

    /**
     * 获取结果中的表头用于导出
     *
     * @return
     */
    @Transient
    public List<List<String>> getTitleList(List<String> exportList) {
        return this.getTitleVO().getHead(exportList);
    }

    /**
     * 根据表头 获取结果数据  用于导出
     *
     * @return
     */
    @Transient
    public List<List<String>> getDateList() {
        List<List<String>> dateList = new ArrayList<>();
        List<String> list = this.getTitleVO().getHeadKey();
        for (Map<String, Object> map : getData()) {
            List<String> date = new ArrayList<>();
            for (String key : list) {
                date.add(getValue(map, key));
            }
            dateList.add(date);
        }
        return dateList;
    }

    @Transient
    public TitleVO getTitleVO() {
        return this.titleVO;
    }

    /**
     * 获取map中的值
     *
     * @param map
     * @param key
     * @return
     */
    private String getValue(Map<String, Object> map, String key) {
        return map.get(key) == null ? "" : map.get(key).toString();
    }


    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Transient
    public JSONObject getJsonObj() {
        JSONObject jsonObj = JSONObject.parseObject(this.toString());
        return jsonObj;
    }
}
