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
     * 生成启动项目入口
     */
    private Boolean startApp;

    /**
     * mapper 路径
     */
    private String mapperPath;


    /**
     * web页面保存路径
     */
    private String webPath;

    /**
     * 服务名称
     */
    private String serveName;

    /**
     * 包含表
     */
    private List<Table> tables;

    public AutoCodeConfig(){
        //设置署名
        this.author = "lei.liu";
        //设置创建时间
        this.createTime = new Date();
    }
}
