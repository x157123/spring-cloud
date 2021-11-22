package com.cloud.auto.code.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liulei
 */
@Data
public class AutoCodeConfig {

    /**
     * 作者
     */
    private String author;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 项目路径
     */
    private String projectPath;

    /**
     * 包名前缀
     */
    private String packagePrefix;

    /**
     * 生成表前缀
     */
    private String tablePrefix;

    /**
     * 包含表
     */
    private List<Table> tables;
}
