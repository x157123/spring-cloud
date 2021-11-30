package com.cloud.excel.util;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;


/**
 * @author liulei
 */
public class ExportExcelStyle extends AbstractCellStyleStrategy {

    private Integer titleSize;

    private static final int MAX_COLUMN_WIDTH = 255;

    public ExportExcelStyle(Integer titleSize) {
        this.titleSize = titleSize;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

        // 设置列宽
        if (aBoolean && cell.getRowIndex() != 0) {
            int columnWidth = cell.getStringCellValue().getBytes().length;
            if (columnWidth > MAX_COLUMN_WIDTH) {
                columnWidth = MAX_COLUMN_WIDTH;
            } else {
                columnWidth = columnWidth + 3;
            }
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256<=5000?5000:100 + columnWidth * 256);
        }
        // 设置行高
        writeSheetHolder.getSheet().getRow(cell.getRowIndex()).setHeight((short) (500));
        // 获取workbook
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        // 获取样式实例
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 获取字体实例
        WriteFont headWriteFont = new WriteFont();
        // 设置字体样式
        headWriteFont.setFontName("等线");
        // 设置字体大小
        if(cell.getRowIndex()<1){
            //第一行 设置字体
            headWriteFont.setFontHeightInPoints((short) 12);
        }else {
            headWriteFont.setFontHeightInPoints((short) 11);
        }
        // 设置是否加粗
        headWriteFont.setBold(false);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 设置背景颜色为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        if(cell.getRowIndex() != titleSize) {
            headWriteCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        }
        headWriteCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 获取样式实例
        CellStyle cellStyle = StyleUtil.buildHeadCellStyle(workbook, headWriteCellStyle);
        // 单元格设置样式
        cell.setCellStyle(cellStyle);
    }

    /**
     * Initialization cell style
     *
     * @param workbook
     */
    @Override
    protected void initCellStyle(Workbook workbook) {

    }

    /**
     * Sets the cell style of header
     *
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

    /**
     * Sets the cell style of content
     *
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }
}
