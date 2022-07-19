package com.dldorg.org;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author liulei
 */
public class WritData {

    private static Logger logger = Logger.getLogger(WritData.class.getName());

    public static void writ(List<OrgCompareAll> orgCompareAll) {

        // 写入数据到工作簿对象内
        Workbook workbook = ExcelWriter.exportData(Arrays.asList("县（市、区）", "乡（镇、街道）", "村（社区）", "网格"), orgCompareAll);

        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "X:/writeExample.xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }
            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            logger.warning("输出Excel时发生错误，错误原因：" + e.getMessage());
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                logger.warning("关闭输出流时发生错误，错误原因：" + e.getMessage());
            }
        }

    }

}
