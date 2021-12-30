package com.cloud.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.cloud.excel.vo.DynamicResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class ExportUtil {

    private static final Logger log = LoggerFactory.getLogger(ExportUtil.class);

    /**
     * 导出数据
     *
     * @param request
     * @param response
     * @param dynamicResult
     * @throws IOException
     */
    public static void exportListData(HttpServletRequest request, HttpServletResponse response,
                                      DynamicResult dynamicResult, List<String> exportList) {
        try {
            String sheetName = URLEncoder.encode(dynamicResult.getTitleVO().getTitle(), "UTF-8");
            String fileName = sheetName.concat(".xlsx");
            List<List<String>> headTitles = dynamicResult.getTitleList(exportList);
            //表数据
            List<List<String>> rowList = dynamicResult.getDateList();
            //response输出文件流
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new ExportExcelStyle(dynamicResult.getTitleVO().getHeadDepth()))
                    .head(headTitles).sheet(dynamicResult.getTitleVO().getTitle()).doWrite(rowList);
        } catch (Exception e) {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("导出失败");
            } catch (IOException e1) {
                log.error("下载文件IO异常:{}", e);
            }
        }
    }

    public static void exportListData(HttpServletRequest request, HttpServletResponse response,
                                      List exportData, Class clas, Integer headLength, String exportFileName) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(exportFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), clas)
                    .registerWriteHandler(new ExportExcelStyle(headLength))
                    .autoCloseStream(Boolean.FALSE).sheet(exportFileName)
                    .doWrite(exportData);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                log.error("下载文件IO异常:{}", e);
            }
        }
    }

    public static void exportObjList(String fileName, List<String> fieldList, List<?> data,
                                     Class<?> tClass, HttpServletResponse response, String title) {
        try {
            // 过滤隐藏的表头 增加大表头
            List<List<String>> header = handlerBaseModule(fieldList, tClass, title);
            // 数据封装未map
            List<Map<String, String>> list = coverToMapList(data);
            // 过滤隐藏的数据
            List<List<String>> dataList = handlerData(fieldList, list, tClass);

            String ecFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            //response输出文件流
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename=" + ecFileName + ExcelTypeEnum.XLSX.getValue());
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new ExportExcelStyle(3))
                    .head(header)
                    .sheet(fileName)
                    .doWrite(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("导出失败");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private static List<List<String>> handlerData(List<String> fieldList, List<Map<String, String>> list, Class<?> tClass) {
        List<List<String>> collect = new ArrayList<>();
        if (CollectionUtils.isEmpty(fieldList)) {
            // 未传递字段导出全字段
            list.forEach(m -> {
                try {
                    List<String> collect1 = Arrays.stream(tClass.getDeclaredFields())
                            .filter(f -> Objects.nonNull(f.getAnnotation(ExcelProperty.class)))
                            .map(x -> m.get(x.getName()))
                            .collect(Collectors.toList());
                    collect.add(collect1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        // 数据
        list.forEach(m -> {
            try {
                List<String> collect1 = Arrays.stream(tClass.getDeclaredFields())
                        .filter(f -> Objects.nonNull(f.getAnnotation(ExcelProperty.class)))
                        .filter(f -> fieldList.contains(f.getName()))
                        .map(x -> m.get(x.getName()))
                        .collect(Collectors.toList());
                collect.add(collect1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return collect;
    }

    private static List<List<String>> handlerBaseModule(List<String> fieldList, Class<?> tClass, String title) {
        if (CollectionUtils.isEmpty(fieldList)) {
            // 未传递字段导出全字段
            return Arrays.stream(tClass.getDeclaredFields())
                    .filter(field -> Objects.nonNull(field.getAnnotation(ExcelProperty.class)))
                    .map(m -> {
                        if (ObjectUtils.isEmpty(title)) {
                            return Arrays.stream(m.getAnnotation(ExcelProperty.class).value()).filter(Objects::nonNull).collect(Collectors.toList());
                        } else {
                            List<String> temp = new ArrayList<>();
                            temp.add(title);
                            String[] value = m.getAnnotation(ExcelProperty.class).value();
                            // 不要用asList !!!
                            for (String v : value) {
                                temp.add(v);
                            }
                            return temp;
                        }
                    }).collect(Collectors.toList());
        }
        //表头
        return Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> Objects.nonNull(field.getAnnotation(ExcelProperty.class)))
                .filter(f -> fieldList.contains(f.getName()))
                .map(m -> {
                    if (ObjectUtils.isEmpty(title)) {
                        return Arrays.stream(m.getAnnotation(ExcelProperty.class).value()).collect(Collectors.toList());
                    } else {
                        List<String> temp = new ArrayList<>();
                        temp.add(title);
                        String[] value = m.getAnnotation(ExcelProperty.class).value();
                        // 不要用asList !!!
                        for (String v : value) {
                            temp.add(v);
                        }
                        return temp;
                    }
                }).collect(Collectors.toList());
    }

    private static List<Map<String, String>> coverToMapList(List<?> list) {
        // 数据封装未map
        return list.stream().map(m -> {
            try {
                return BeanUtils.describe(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}