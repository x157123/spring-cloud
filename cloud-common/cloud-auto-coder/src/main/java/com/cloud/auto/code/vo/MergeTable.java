package com.cloud.auto.code.vo;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * @author liulei
 * 中间关联表信息
 */
@Data
public class MergeTable {
    /**
     * 关联表
     */
    private String leftTable;

    /**
     * 关联列
     */
    private String leftTableColumn;

    /**
     * 关联中间表
     */
    private String mergeTable;

    /**
     * 关联列
     */
    private String leftMergeTableColumn;

    /**
     * 关联列
     */
    private String rightMergeTableColumn;

    /**
     * 关联表
     */
    private String rightTable;

    /**
     * 关联列
     */
    private String rightTableColumn;

    /**
     * 说明
     */
    private String comment;

    /**
     * 扩展包路径
     */
    private String packagePath;

    /**
     * 表前缀
     */
    private List<String> prefix;

    public String getTableNameClass() {
        String className = rightTable;
        if (prefix != null && prefix.size() > 0) {
            for (String pre : prefix) {
                int i = className.indexOf(pre);
                if(i == 0){
                    className = className.substring(pre.length());
                }
            }
        }
        return StringUtil.dbToClassName(className);
    }
}
