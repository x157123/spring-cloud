package com.cloud.auto.code.util;

import com.cloud.auto.code.vo.AutoCodeConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 */
public class CodeUtil {


    /**
     * 文件写入
     * @param autoCodeConfig
     * @param ftlPath
     */
    public static void writer(AutoCodeConfig autoCodeConfig,String ftlPath) {
        Map<String, Object> map = new HashMap<>(2);
        List<String> ftps = getFtlList(ftlPath);

        //获取所有table的指定包
        Map<String,String> tables = new HashMap<>(autoCodeConfig.getTables().size());
        Map<String,String> comment = new HashMap<>(autoCodeConfig.getTables().size());
        autoCodeConfig.getTables().forEach(table -> {
            tables.put(table.getName(),table.getPackagePath());
            comment.put(table.getName(),table.getComment());
        });

        autoCodeConfig.getTables().forEach(table -> {
            if(table.getForeignKeys()!=null){
                table.getForeignKeys().forEach(foreignKey -> {
                    foreignKey.setPackagePath(tables.get(foreignKey.getTableName()));
                    foreignKey.setComment(comment.get(foreignKey.getTableName()));
                });
            }
        });

        autoCodeConfig.getTables().forEach(table -> {
            map.clear();
            map.put("autoCodeConfig", autoCodeConfig);
            map.put("table", table);
            map.put("basePackage", PackageUtil.mergePack(autoCodeConfig.getPackagePrefix()));
            map.put("expandPackage", PackageUtil.mergePack(table.getPackagePath()));
            map.put("package", PackageUtil.mergePack(autoCodeConfig.getPackagePrefix(),table.getPackagePath()));
            ftps.forEach(ftlName -> {
                if(!"application.java.ftl".equals(ftlName)&& !"mybatisPlusConfig.java.ftl".equals(ftlName)&& !"swaggerConfig.java.ftl".equals(ftlName)) {
                    createFile(ftlName, map, autoCodeConfig.getProjectPath(), ftlPath, table.getName());
                }
            });
        });

        createFile("application.java.ftl", map, autoCodeConfig.getProjectPath(), ftlPath, "");
        createFile("mybatisPlusConfig.java.ftl", map, autoCodeConfig.getProjectPath(), ftlPath, "");
        createFile("swaggerConfig.java.ftl", map, autoCodeConfig.getProjectPath(), ftlPath, "");

    }

    /**
     * 创建文件
     *
     * @param templateName
     * @param dataMap
     * @param savePath
     * @param fileName
     */
    private static void createFile(String templateName, Map<String, Object> dataMap, String savePath, String ftlPath, String fileName) {
        try {
            //创建配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件
            configuration.setClassForTemplateLoading(CodeUtil.class, File.separator + ftlPath + File.separator);

            //获取模板
            Template template = configuration.getTemplate(templateName);

            //默认保存路径 包+扩展包+文件名
            String saveFilePath = savePath+PackageUtil.packToFilePath(PackageUtil.mergePack("java",dataMap.get("basePackage").toString(),dataMap.get("expandPackage").toString(), getPack(templateName)));

            //当出现mapper.xml 统一保存到某个目录
            if (templateName.startsWith("mapper.xml")) {
                //保存到 xml+扩展包
                saveFilePath = savePath+PackageUtil.packToFilePath(PackageUtil.mergePack("resources.mapper",getPack(templateName),dataMap.get("expandPackage").toString()));
            }else if(templateName.startsWith("application.java")) {
                //保存到 跟目录
                saveFilePath = savePath+PackageUtil.packToFilePath(PackageUtil.mergePack("java",dataMap.get("basePackage").toString(),dataMap.get("expandPackage").toString()));
            }else if(templateName.startsWith("mybatisPlusConfig.java") || templateName.startsWith("swaggerConfig.java")) {
                //保存到 第一级目录
                saveFilePath = savePath+PackageUtil.packToFilePath(PackageUtil.mergePack("java",dataMap.get("basePackage").toString(),dataMap.get("expandPackage").toString(),"config"));
            }else if(templateName.startsWith("entity.java")){
                templateName=".java.ftl";
            }
                //输出文件
            File outFile = new File(saveFilePath + File.separator + StringUtil.dbToClassName(fileName)+getSuffix(templateName));

            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            //生成文件
            template.process(dataMap, out);

            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 获取模板类型
     *
     * @param fileName
     * @return
     */
    private static String getType(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * 获取模板文件类型及后缀
     *
     * @param fileName
     * @return
     */
    private static String getSuffix(String fileName) {
        return StringUtil.toUpperCaseFirstOne(fileName.substring(0, fileName.lastIndexOf(".")));
    }

    /**
     * 不同类型保存到不同目录下
     *
     * @param templateName
     * @return
     */
    private static String getPack(String templateName) {
        String type = getType(templateName);
        if (templateName.startsWith("mapper.xml")) {
            return "";
        } else if (templateName.startsWith("serviceImpl.java")) {
            return "service.impl";
        }
        return type;
    }

    /**
     * 获取 resources 下某个文件夹下的所以模板文件名
     *
     * @param ftlPath
     * @return
     */
    private static List<String> getFtlList(String ftlPath) {
        List<String> list = new ArrayList<>();
        String filePath = CodeUtil.class.getResource("/").getPath() + ftlPath + File.separator;
        File file = new File(filePath);
        for (File f : file.listFiles()) {
            list.add(f.getName());
        }
        return list;
    }

}
