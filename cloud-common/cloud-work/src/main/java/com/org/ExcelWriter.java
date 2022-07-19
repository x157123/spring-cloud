package com.org;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * Description: 生成Excel并写入数据
 *
 * @author liulei
 */
public class ExcelWriter {

    /**
     * 生成Excel并写入数据信息
     *
     * @param orgCompareAll 数据列表
     * @return 写入数据后的工作簿对象
     */
    public static Workbook exportData(List<String> heads, List<OrgCompareAll> orgCompareAll) {
        // 生成xlsx的Excel
        Workbook workbook = new SXSSFWorkbook();


        // 如需生成xls的Excel，请使用下面的工作簿对象，注意后续输出时文件后缀名也需更改为xls
        //Workbook workbook = new HSSFWorkbook();

        for (OrgCompareAll compareAll : orgCompareAll) {

            // 生成Sheet表，写入第一行的列头
            Sheet sheet = buildDataSheet(heads, workbook, compareAll.getCity());

            //设置背景颜色
            CellStyle roseStyle = buildHeadCellStyle(sheet.getWorkbook(), IndexedColors.ROSE.getIndex());
            CellStyle greenStyle = buildHeadCellStyle(sheet.getWorkbook(), IndexedColors.LIGHT_GREEN.getIndex());
            CellStyle blueStyle = buildHeadCellStyle(sheet.getWorkbook(), IndexedColors.SKY_BLUE.getIndex());

            //构建每行的数据内容
            int rowNum = 1;
            for (OrgCompare data : compareAll.getOrgCompare()) {
                if (data == null) {
                    continue;
                }
                //输出行数据
                Row row = sheet.createRow(rowNum++);
                convertDataToRow(data, row, roseStyle, greenStyle, blueStyle);
            }
        }
        return workbook;
    }

    /**
     * 生成sheet表，并写入第一行数据（列头）
     *
     * @param heads    工作簿对象
     * @param workbook 工作簿对象
     * @return 已经写入列头的Sheet
     */
    private static Sheet buildDataSheet(List<String> heads, Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        // 设置列头宽度
        for (int i = 0; i < heads.size(); i++) {
            sheet.setColumnWidth(i, 10000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row head = sheet.createRow(0);
        for (int i = 0; i < heads.size(); i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(heads.get(i));
            cell.setCellStyle(cellStyle);
        }
        // 单独设置 第1 列为文本格式
        CellStyle textCellStyle = workbook.createCellStyle();
        textCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        sheet.setDefaultColumnStyle(0, textCellStyle);
        // 单独设置 第2 列为整数格式
        CellStyle intNumCellStyle = workbook.createCellStyle();
        intNumCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        sheet.setDefaultColumnStyle(1, intNumCellStyle);
        // 单独设置 第3 列为两位小数格式
        CellStyle floatNumCellStyle = workbook.createCellStyle();
        floatNumCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        sheet.setDefaultColumnStyle(2, floatNumCellStyle);
        return sheet;
    }

    /**
     * 设置第一行列头的样式
     *
     * @param workbook 工作簿对象
     * @return 单元格样式对象
     */
    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        // 下边框
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        // 左边框
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        // 右边框
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        // 上边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 字体设置
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }

    private static CellStyle buildHeadCellStyle(Workbook workbook, short bg) {
        CellStyle style = workbook.createCellStyle();
        // 边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        // 下边框
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        // 左边框
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        // 右边框
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        // 上边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 设置背景颜色
        style.setFillForegroundColor(bg);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 将数据转换成行
     *
     * @param data       源数据
     * @param row        行对象
     * @param roseStyle  行对象
     * @param greenStyle 行对象
     * @return
     */
    private static void convertDataToRow(OrgCompare data, Row row, CellStyle roseStyle, CellStyle greenStyle, CellStyle blueStyle) {
        int cellNum = 0;
        Cell cell;
        //区县
        /**
         * 县
         */
        OrgAll county = data.getCounty();
        cell = row.createCell(cellNum++);
        setCell(cell, county, roseStyle, greenStyle, blueStyle);

        /**
         * 乡镇
         */
        OrgAll town = data.getTown();
        cell = row.createCell(cellNum++);
        setCell(cell, town, roseStyle, greenStyle, blueStyle);

        /**
         * 村 社区代码
         */
        OrgAll village = data.getVillage();
        cell = row.createCell(cellNum++);
        setCell(cell, village, roseStyle, greenStyle, blueStyle);
    }

    private static void setCell(Cell cell, OrgAll village, CellStyle roseStyle, CellStyle greenStyle, CellStyle blueStyle) {
        if(village!=null) {
            if (village.getCom() <= 1) {
                cell.setCellValue(village.getOrgName());
                if (village.getType()) {
                    cell.setCellStyle(blueStyle);
                } else {
                    cell.setCellStyle(roseStyle);
                }
            } else {
                if (village.getNewOrgName() != null && village.getNewOrgName().length() > 0) {
                    cell.setCellValue(village.getOrgName() + "/" + village.getNewOrgName());
                    cell.setCellStyle(greenStyle);
                } else {
                    cell.setCellValue(village.getOrgName());
                }
            }
        }
    }

}
