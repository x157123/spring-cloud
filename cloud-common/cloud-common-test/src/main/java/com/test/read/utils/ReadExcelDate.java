package com.test.read.utils;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取excel生日数据数据
 *
 * @author liulei
 * <p>
 * 读取 Excel
 */
public class ReadExcelDate {

    private static final Logger logger = LoggerFactory.getLogger(ReadExcelDate.class);


    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";


    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     *
     * @param inputStream 读取文件的输入流
     * @param fileType    文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    private static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public static void main(String[] args) {
        readExcel("D:\\Downloads\\xxx.xlsx");
    }

    public static void readExcel(String filePath) {
        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            // 获取Excel文件
            File excelFile = new File(filePath);
            if (!excelFile.exists()) {
                logger.warn("指定的Excel文件不存在！");
            }
            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);
            // 读取excel中的数据
            parseExcel(workbook);
        } catch (Exception e) {
            logger.warn("解析Excel失败，文件名：" + filePath + " 错误信息：" + e.getMessage());
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
            }
        }

    }

    /**
     * 解析Excel数据
     *
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static void parseExcel(Workbook workbook) {
//            只读取第一个页签
        for (int sheetNum = 0; sheetNum < 1; sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            String workbookSheetName = workbook.getSheetName(sheetNum);
            System.out.println(workbookSheetName);
            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            JSONArray jsonArray = new JSONArray();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (null == row) {
                    continue;
                }
                Map<String, String> map = convertRowToData(row);
                jsonArray.add(map);
                String name = map.get("name");
                String yangli = map.get("yangli");
                String yingli = map.get("yingli");
                if (yangli != null) {
                    Integer yearTmp = Integer.parseInt(yangli.substring(0, 4));
                    Integer year = Integer.parseInt(yangli.substring(0, 4));
                    Integer month = Integer.parseInt(yangli.substring(4, 6));
                    Integer day = Integer.parseInt(yangli.substring(6, 8));
                    while (year < 2100) {
                        if (year >= DateUtil.thisYear()) {
                            System.out.println(name + ":" + (year - yearTmp) + "," + day + "/" + month + "/" + year + ",07:00 AM");
                        }
                        year += 1;
                    }
                } else if (yingli != null) {
                    Integer yearTmp = Integer.parseInt(yingli.substring(0, 4));
                    Integer year = Integer.parseInt(yingli.substring(0, 4));
                    Integer month = Integer.parseInt(yingli.substring(4, 6));
                    Integer day = Integer.parseInt(yingli.substring(6, 8));
                    while (year < 2100) {
                        if (year >= DateUtil.thisYear()) {
                            ChineseDate chineseDate = new ChineseDate(year, month, day);
                            Date dt = chineseDate.getGregorianDate();
                            if (yearTmp <= 2022) {
                                System.out.println(name + ":" + (year - yearTmp) + "," + DateUtil.format(dt, "dd/MM/yyyy") + ",07:00 AM");
                            } else {
                                System.out.println(name + "," + DateUtil.format(dt, "dd/MM/yyyy") + ",07:00 AM");
                            }
                        }
                        year += 1;
                    }
                }
            }

        }
    }


    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     * <p>
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    private static Map<String, String> convertRowToData(Row row) {
        Map<String, String> map = new HashMap<>();
        Cell cell;
        int cellNum = 0;
        // 获取001
        cell = row.getCell(cellNum++);
        String idCard = convertCellValueToString(cell);
        map.put("name", idCard);
        // 获取002
        cell = row.getCell(cellNum++);
        idCard = convertCellValueToString(cell);
        map.put("yingli", idCard);
        // 获取003
        cell = row.getCell(cellNum++);
        idCard = convertCellValueToString(cell);
        map.put("date", idCard);

        // 获取004
        cell = row.getCell(cellNum++);
        idCard = convertCellValueToString(cell);
        map.put("yangli", idCard);
        return map;
    }

    /**
     * 将单元格内容转换为字符串
     *
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:
                //数字
                Double doubleValue = cell.getNumericCellValue();
                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:
                //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:
                // 空值
                break;
            case FORMULA:
                // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:
                // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }
}
