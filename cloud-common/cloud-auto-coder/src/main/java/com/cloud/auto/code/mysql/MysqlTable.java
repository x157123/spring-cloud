package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author liulei
 * SELECT TABLE_NAME as name, TABLE_COMMENT as comment FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'testuser' order by CREATE_TIME asc;
 */
@Data
public class MysqlTable {

    /**
     * 名称
     */
    private String name;

    /**
     * 设置java类名
     */
    private String nameClass;

    /**
     * java路径
     */
    private String javaPath;

    /**
     * 或者路径
     */
    private String expandPackage;

    /**
     * 类中属性
     */
    private List<MysqlColumn> column;

    /**
     * 关联表
     */
    private List<MysqlForeignKey> foreignKeys;

    /**
     * 经过关联中间表关联的表
     */
    private List<MysqlMergeTable> mergeTables;

    /**
     * 关联表详细信息
     */
    private MysqlMergeTable mergeTable;

    /**
     * 关联表
     */
    private List<MysqlJoinKey> mysqlJoinKeys;

    /**
     * 功能说明
     * 中文备注->包指定目录    如：   人员信息->person
     */
    private String comment;

    private Config config;

    public void setForeignKeys(List<MysqlForeignKey> foreignKeys) {
        if (foreignKeys != null && foreignKeys.size() > 0) {
            List<MysqlForeignKey> uniqueList = foreignKeys.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getTableName()))), ArrayList::new));
            this.foreignKeys = uniqueList;
        }
    }

    public void setMergeTables(List<MysqlMergeTable> mergeTables) {
        if (mergeTables != null && mergeTables.size() > 0) {
            List<MysqlMergeTable> uniqueList = mergeTables.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getRightTable()))), ArrayList::new));
            this.mergeTables = uniqueList;
        }
    }

    public MysqlTable(String name, String comment, String packagePath, List<String> prefix) {
        this.name = name;
        this.nameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(name, prefix));
        this.javaPath = StringUtil.getPackagePath(packagePath, comment);
        this.expandPackage = StringUtil.getPackagePath(comment);
        this.comment = StringUtil.getComment(comment);
        this.foreignKeys = new ArrayList<>();
        this.mergeTables = new ArrayList<>();
        this.mysqlJoinKeys = new ArrayList<>();
        this.column = new ArrayList<>();
    }

    public MysqlTable(Config config){
        this.config = config;
    }
}
