package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.PackageUtil;
import com.cloud.auto.code.util.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReadMysqlTable {

    static boolean pc = true;
    static boolean zzkj = true;

    public static void main(String[] args) throws SQLException {

        Config config;
        boolean star = true;

        // 表前缀
        List<String> prefix = Arrays.asList("md_", "app_", "sg_et_", "wgh_", "sg_", "sync_", "zz_");
        //模版路径
        String ftlPath;

        List<String> ftlList = new ArrayList<>();
        List<String> ftlMergeList = new ArrayList<>();
        List<String> pom = new ArrayList<>();
        List<String> configs = new ArrayList<>();
        List<String> webList = new ArrayList<>();


        if (!star) {
            return;
        }
        if (zzkj) {
            config = new Config("mediation", "jdbc:mysql://localhost:3306/code_db", "root", "123456", "com.zc.conflict.dispute", "D:\\work\\service\\mediation\\universe-platform\\", "E:\\code\\web\\cloud-angular-web\\src\\app\\module\\", "liulei", "2023-01-09");
            ftlPath = "cloud-zzkj";
            config.setJavaFilePath("D:\\test\\java\\");
            config.setWebFilePath("D:\\test\\web\\");
            webList.addAll(Arrays.asList("list.vue.ftl", "edit.vue.ftl", "detail.vue.ftl", "api.js.ftl"));
            ftlList.addAll(Arrays.asList("entity.java.ftl", "dto.java.ftl", "query.java.ftl", "vo.java.ftl", "param.java.ftl"));

            ftlList.addAll(Arrays.asList("mapper.xml.ftl", "mapper.java.ftl", "service.java.ftl", "serviceImpl.java.ftl", "controller.java.ftl"));

            ftlMergeList.addAll(Arrays.asList("entityMerge.java.ftl", "mapperMerge.java.ftl", "serviceMerge.java.ftl", "serviceMergeImpl.java.ftl"));
        } else {
            config = new Config("tests", "jdbc:mysql://localhost:3306/cloud_test", "root", "123456", "com.cloud.test", "E:\\IdeaProjects\\spring-cloud\\cloud-apps\\", "E:\\code\\web\\cloud-angular-web\\src\\app\\module\\", "liulei", "2023-12-14");

            ftlPath = "cloud-new";
            ftlList.addAll(Arrays.asList("entity.java.ftl", "query.java.ftl", "vo.java.ftl", "param.java.ftl"));
            ftlList.addAll(Arrays.asList("mapper.xml.ftl", "mapper.java.ftl", "service.java.ftl", "serviceImpl.java.ftl", "controller.java.ftl"));

            configs.addAll(Arrays.asList("application.java.ftl", "application.yml.ftl", "mybatisPlusConfig.java.ftl"));

            webList.addAll(Arrays.asList("component.css.ftl", "component.html.ftl", "component.ts.ftl", "edit.component.css.ftl", "edit.component.html.ftl", "edit.component.ts.ftl", "module.ts.ftl", "routing.ts.ftl"));

            pom.addAll(Arrays.asList("pom.xml.ftl"));

            ftlMergeList.addAll(Arrays.asList("entityMerge.java.ftl", "mapperMerge.java.ftl", "serviceMerge.java.ftl", "serviceMergeImpl.java.ftl"));

            if (pc) {
                config.setUrl("jdbc:mysql://localhost:3306/cloud_test");
                config.setPackagePath("com.cloud.test");
                config.setProjectName("tests");
                config.setJavaFilePath("E:\\code\\java\\service\\spring-cloud\\cloud-apps\\");
                config.setWebFilePath("E:\\web\\");
                ftlPath = "cloud-new";
            }
        }

        try (Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())) {

            //  获取表结果集
            List<MysqlTable> tables = getTables(conn, config.getPackagePath(), prefix);

            //  设置表属性
            setColumn(conn, tables);

            //  设置关联表
//            setMergeTable(conn, tables, prefix);
            List<Keys> keys = getMergeMsg(conn, tables);

            setMerge(tables, keys, prefix);

            //过滤掉中间表
            List<MysqlTable> writerTables = tables.stream().filter(table -> !table.getName().matches("(.*)_merge$")).toList();

            List<MysqlTable> mergeTables = tables.stream().filter(table -> table.getName().matches("(.*)_merge$")).toList();

            System.out.println("xxxxxxxxxxxxx");

            for (MysqlTable mysqlTable : writerTables) {
                mysqlTable.setConfig(config);
            }

            writer(writerTables, ftlList, ftlPath, config.getJavaFilePath() + config.getProjectName() + "\\src\\main\\");

            writer(mergeTables, ftlMergeList, ftlPath, config.getJavaFilePath() + config.getProjectName() + "\\src\\main\\");

            writerConfig(configs, config, ftlPath, config.getJavaFilePath() + config.getProjectName() + "\\src\\main\\");

            writer(writerTables, pom, ftlPath, config.getJavaFilePath() + config.getProjectName());

            if (zzkj) {
                //地方限制web页面
                List<String> webTable = Arrays.asList("md_appeal");
                writerTables = tables.stream().filter(table -> webTable.contains(table.getName())).toList();
            }
            // 生成前端页面
            writer(writerTables, webList, ftlPath, config.getWebFilePath());

//            createPgSql(tables);

//            createRouting(tables);
        }
    }


    private static void setMerge(List<MysqlTable> tables, List<Keys> keys, List<String> prefix) {
        Map<String, MysqlTable> tableMap = tables.stream().collect(Collectors.toMap(MysqlTable::getName, table -> table));
        Map<String, Boolean> tableUniMap = getTableColumnUni(tables);
        for (Keys jo : keys) {
//* 关联表 a->b  当前表为 a  列表记录为 b 关联信息                             List<MysqlJoinKey> mysqlJoinKeys;
//* 关联表 a->b 当前表为b  显示被关联表 a信息                                  List<MysqlForeignKey> foreignKeys;
//* 经过关联中间表关联的表  a <- b ->c 当前表为 a  列表记录b表   指向a 与 c表信息  List<MysqlMergeTable> mergeTables;
//* 关联表详细信息 中间表使用   a <- b ->c 当前表为 b  信息为 b 指向a 与 c表信息   MysqlMergeTable mergeTable;
            MysqlTable a = tableMap.get(jo.getFkTableName());
            MysqlTable b = tableMap.get(jo.getPkTableName());
            if (jo.getFkTableName().matches("(.*)_merge$")) {
                if (a.getMergeTable() == null) {
                    a.setMergeTable(new MysqlMergeTable());
                }
                a.getMergeTable().setPackagePath(a.getJavaPath());
                a.getMergeTable().setMergeTable(jo.getFkTableName());
                a.getMergeTable().setTableNameClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(jo.getFkTableName(), prefix)));
                a.getMergeTable().setComment(a.getComment());
                //判断表字段关联的表是否是主表
                if (isPrimaryTable(a, jo.getFkColumnName())) {
                    //当为主表时
                    a.getMergeTable().setMaintain(b.getName());
                    a.getMergeTable().setLeftTable(b.getName());
                    a.getMergeTable().setLeftTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(b.getName(), prefix)));
                    a.getMergeTable().setLeftTableColumn(jo.getPkColumnName());
                    a.getMergeTable().setLeftTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(jo.getPkColumnName())));
                    a.getMergeTable().setLeftMergeTableColumn(jo.getFkColumnName());
                    a.getMergeTable().setLeftMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(jo.getFkColumnName())));
                    b.getMergeTables().add(a.getMergeTable());
                } else {
                    a.getMergeTable().setRightTable(b.getName());
                    a.getMergeTable().setRightTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(b.getName(), prefix)));
                    a.getMergeTable().setRightTableColumn(jo.getPkColumnName());
                    a.getMergeTable().setRightTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(jo.getPkColumnName())));
                    a.getMergeTable().setRightMergeTableColumn(jo.getFkColumnName());
                    a.getMergeTable().setRightMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(jo.getFkColumnName())));
                }
            } else {
                a.getMysqlJoinKeys().add(new MysqlJoinKey(jo.getPkTableName(), jo.getPkColumnName(), b.getComment(), prefix));
                b.getForeignKeys().add(new MysqlForeignKey(jo.getPkTableName(), jo.getPkColumnName(), jo.getFkTableName(), jo.getFkColumnName(), prefix, a.getJavaPath(), a.getComment(), getUni(tableUniMap, jo.getFkTableName(), jo.getFkColumnName())));
            }
        }
    }

    private static boolean isPrimaryTable(MysqlTable table, String columnName) {
        for (MysqlColumn column : table.getColumn()) {
            if (column.getName().equals(columnName)) {
                return column.getPrimaryTable() == null ? Boolean.FALSE : column.getPrimaryTable();
            }
        }
        return Boolean.TRUE;
    }

    private static List<Keys> getMergeMsg(Connection conn, List<MysqlTable> tables) {
        List<Keys> data = new ArrayList<>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            for (MysqlTable table : tables) {
                ResultSet foreignKeys = metaData.getImportedKeys(conn.getCatalog(), null, table.getName()); // 获取外键
                //遍历外键
                while (foreignKeys.next()) {
                    String fkTableName = foreignKeys.getString("FKTABLE_NAME"); // 外键所在表
                    String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME"); // 外键列名
                    String pkTableName = foreignKeys.getString("PKTABLE_NAME"); // 关联表
                    String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME"); // 关联表列名
                    System.out.println(fkTableName + "." + fkColumnName + "---->" + pkTableName + "." + pkColumnName);
                    data.add(new Keys(fkTableName, fkColumnName, pkTableName, pkColumnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void createRouting(List<MysqlTable> tables) {
        tables.forEach(table -> {
            String filePath = PackageUtil.packToFilePath(PackageUtil.mergePack(table.getExpandPackage(), StringUtil.toLowerCaseFirstOne(table.getNameClass())));

            System.out.println(
                    "      {\n" +
                            "        path: '" + StringUtil.toLowerCaseFirstOne(table.getNameClass()) + "',\n" +
                            "        loadChildren: () => import('./module/" + filePath + StringUtil.toLowerCaseFirstOne(table.getNameClass()) + ".module').then(m => m." + table.getNameClass() + "Module)\n" +
                            "      },");
        });
    }

    private static void createMenus(List<MysqlTable> tables) {

    }

    private static void createPgSql(List<MysqlTable> tables) {
        tables.forEach(table -> {
            System.out.println("create table " + table.getName() + " (");
            int i = table.getColumn().size();
            for (MysqlColumn column : table.getColumn()) {
                i--;
                System.out.println("    " + column.getName() + "                   " + column.getPgSqlType() + "            " + column.getPgRequired() + (i > 0 || !table.getName().matches("(.*)_merge$") ? "," : ""));
//                System.out.println("            " + column.getName() + "                   " + column.getPgSqlType() + "            " + column.getPgRequired() + ",");
            }
            if (!table.getName().matches("(.*)_merge$")) {
                System.out.println("    create_user            varchar(32)            NOT NULL,");
                System.out.println("    create_date            timestamp(6)            NOT NULL,");
                System.out.println("    update_user            varchar(32)            NULL,");
                System.out.println("    update_date            timestamp(6)            NULL,");
                System.out.println("    is_deleted            int2            NOT NULL DEFAULT 0,");
                System.out.println("    CONSTRAINT " + table.getName() + "_pk PRIMARY KEY (id)");
            }
            System.out.println(");");

            System.out.println("comment on table " + table.getName() + " is '" + table.getComment() + "';");
            table.getColumn().forEach(column -> {
                System.out.println("comment on column " + table.getName() + "." + column.getName() + " is '" + column.getComment() + "';");
            });
            if (!table.getName().matches("(.*)_merge$")) {
                System.out.println("comment on column " + table.getName() + ".create_user is '创建人';");
                System.out.println("comment on column " + table.getName() + ".create_date is '创建时间';");
                System.out.println("comment on column " + table.getName() + ".update_user is '跟新用户';");
                System.out.println("comment on column " + table.getName() + ".update_date is '更新时间';");
                System.out.println("comment on column " + table.getName() + ".is_deleted is '是否删除';");
            }
        });
    }


    private static Map<String, Boolean> getTableColumnUni(List<MysqlTable> tables) {
        //字段关联
        Map<String, Boolean> tableUniMap = new HashMap<>();
        String tmp = "-----";
        for (MysqlTable table : tables) {
            for (MysqlColumn mysqlColumn : table.getColumn()) {
                String key = table.getName() + tmp + mysqlColumn.getName();
                tableUniMap.put(key, mysqlColumn.getUni());
            }
        }
        return tableUniMap;
    }

    private static boolean getUni(Map<String, Boolean> tableUniMap, String table, String column) {
        String tmp = "-----";
        String key = table + tmp + column;
        Boolean bool = tableUniMap.get(key);
        if (bool != null) {
            return bool;
        }
        return Boolean.FALSE;
    }

    private static Template getTemplate(String ftlPath, String ftlName) {
        try {
            //创建配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件路径
            configuration.setClassForTemplateLoading(ReadMysqlTable.class, File.separator + ftlPath + File.separator);

            //获取模板
            Template template = configuration.getTemplate(ftlName);

            return template;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writer(List<MysqlTable> tables, List<String> ftlList, String ftlPath, String filePath) {
        for (MysqlTable mysqlTable : tables) {
            for (String ftlName : ftlList) {
                Template template = getTemplate(ftlPath, ftlName);
                if (template == null) {
                    continue;
                }
                String savePath = getSavePath(mysqlTable, filePath, ftlName);
                writerData(template, mysqlTable, savePath);
            }
        }
    }

    private static void writerConfig(List<String> ftlList, Config config, String ftlPath, String filePath) {
        MysqlTable mysqlTable = new MysqlTable(config);
        for (String ftlName : ftlList) {
            Template template = getTemplate(ftlPath, ftlName);
            if (template == null) {
                continue;
            }
            String savePath = getSavePath(mysqlTable, filePath, ftlName);
            writerData(template, mysqlTable, savePath);
        }
    }

    private static void writerData(Template template, MysqlTable mysqlTable, String savePath) {
        try {
            //输出文件
            File outFile = new File(savePath);
            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            //生成文件
            template.process(mysqlTable, out);
            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSavePath(MysqlTable mysqlTable, String savePath, String ftlPath) {
        String saveFilePath = null;
        switch (ftlPath) {
            case "mapper.xml.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("resources.mapper", mysqlTable.getExpandPackage())) + mysqlTable.getNameClass() + "Mapper.xml";
                if (zzkj) {
                    saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "mapper")) + mysqlTable.getNameClass() + "Mapper.xml";
                }
                break;
            case "mapper.java.ftl":
            case "mapperMerge.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "mapper")) + mysqlTable.getNameClass() + "Mapper.java";
                break;
            case "entity.java.ftl":
            case "entityMerge.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "entity")) + mysqlTable.getNameClass() + ".java";
                break;
            case "dto.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "dto")) + mysqlTable.getNameClass() + "DTO.java";
                break;
            case "query.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "query")) + mysqlTable.getNameClass() + "Query.java";
                break;
            case "vo.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "vo")) + mysqlTable.getNameClass() + "Vo.java";
                break;
            case "param.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "param")) + mysqlTable.getNameClass() + "Param.java";
                break;
            case "service.java.ftl":
            case "serviceMerge.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "service")) + mysqlTable.getNameClass() + "Service.java";
                break;
            case "serviceImpl.java.ftl":
            case "serviceMergeImpl.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "service", "impl")) + mysqlTable.getNameClass() + "ServiceImpl.java";
                break;
            case "controller.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "controller")) + mysqlTable.getNameClass() + "Controller.java";
                break;
            case "application.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getConfig().getPackagePath())) + "Application.java";
                break;
            case "application.yml.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("resources")) + "application.yml";
                break;
            case "pom.xml.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.filePathSeparator + "pom.xml";
                break;
            case "mybatisPlusConfig.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getConfig().getPackagePath(), "config")) + "MybatisPlusConfig.java";
                break;
            case "component.css.ftl":
            case "component.html.ftl":
            case "component.ts.ftl":
            case "module.ts.ftl":
            case "routing.ts.ftl":
                //保存到 web
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack(mysqlTable.getExpandPackage(), StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()))) + StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()) + PackageUtil.packSeparator + ftlPath.replace(".ftl", "");
                break;
            case "edit.component.css.ftl":
            case "edit.component.html.ftl":
            case "edit.component.ts.ftl":
                //保存到 web
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack(mysqlTable.getExpandPackage(), StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()))) + StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()) + StringUtil.toUpperCaseFirstOne(ftlPath.replace(".ftl", ""));
                break;
            case "edit.vue.ftl":
            case "index.vue.ftl":
            case "detail.vue.ftl":
            case "list.vue.ftl":
                //保存到 web
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("components", mysqlTable.getExpandPackage(), StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()))) + StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()) + StringUtil.toUpperCaseFirstOne(ftlPath.replace(".ftl", ""));
                break;
            case "api.js.ftl":
                //保存到 web
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("api", "modular", mysqlTable.getExpandPackage(), StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()))) + StringUtil.toLowerCaseFirstOne(mysqlTable.getNameClass()) + StringUtil.toUpperCaseFirstOne(ftlPath.replace(".ftl", ""));
                break;
        }
        System.out.println(saveFilePath);
        return saveFilePath;
    }


    /**
     * 设工作表属性
     *
     * @param conn
     * @param tables
     */
    public static void setColumn(Connection conn, List<MysqlTable> tables) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();
            for (MysqlTable table : tables) {
                List<MysqlColumn> columns = new ArrayList<>();
                try (ResultSet rs = metaData.getColumns(catalog, null, table.getName(), "%")) {
                    ResultSet keys = conn.getMetaData().getPrimaryKeys(catalog, null, table.getName());
                    List<String> keyList = new ArrayList<>();
                    while (keys.next()) {
                        keyList.add(keys.getString("COLUMN_NAME"));
                    }
                    while (rs.next()) {
                        String columnName = rs.getString("COLUMN_NAME");
                        String columnType = getTypeName(rs.getInt("DATA_TYPE"));
                        Boolean required = !"YES".equals(rs.getString("IS_NULLABLE")) ? Boolean.TRUE : Boolean.FALSE;
                        int columnSize = rs.getInt("COLUMN_SIZE");
                        String columnComment = rs.getString("REMARKS");
                        boolean uni = !keyList.isEmpty() && keyList.contains(columnName);
                        columns.add(new MysqlColumn(columnName, columnType, required, uni, columnSize, columnComment));
                    }
                }
                table.setColumn(columns);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类型名称
     *
     * @param dataType
     * @return
     */
    private static String getTypeName(int dataType) {
        switch (dataType) {
            case 4:
            case 5:
                return "Integer";
            case 8:
                return "Double";
            case -7:
            case -6:
            case -5:
                return "Long";
            case 1:
            case -1:
            case 12:
                return "String";
            case 91:
            case 92:
            case 93:
                return "java.util.Date";
            default:
                System.out.println("未知类型：" + dataType);
                return "Object";
        }
    }

    /**
     * 获取表列表
     *
     * @param conn
     * @return
     */
    public static List<MysqlTable> getTables(Connection conn, String packagePath, List<String> prefix) {
        List<MysqlTable> tables = new ArrayList<>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();
            //metaData 获取列表
            try (ResultSet rs = metaData.getTables(catalog, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableComment = rs.getString("REMARKS");
                    tables.add(new MysqlTable(tableName, tableComment, packagePath, prefix));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * 设置中间表
     *
     * @param conn
     * @param tables
     */
    private static void setMergeTable(Connection conn, List<MysqlTable> tables, List<String> prefix) {
        // tables list 转 Map<String,MysqlTable>
        Map<String, MysqlTable> tableMap = tables.stream().collect(Collectors.toMap(MysqlTable::getName, table -> table));
        //字段关联
        Map<String, Boolean> tableUniMap = getTableColumnUni(tables);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();

            List<MysqlMergeTable> mergeTables = new ArrayList<>();

            // 遍历所有表
            for (MysqlTable table : tables) {
                ResultSet foreignKeys = metaData.getImportedKeys(catalog, null, table.getName()); // 获取外键
                MysqlMergeTable mysqlMergeTable = null;
                //遍历外键
                while (foreignKeys.next()) {
                    String fkTableName = foreignKeys.getString("FKTABLE_NAME"); // 外键所在表
                    String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME"); // 外键列名
                    String pkTableName = foreignKeys.getString("PKTABLE_NAME"); // 关联表
                    String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME"); // 关联表列名
                    //判断是否是中间表
                    if (table.getName().matches("(.*)_merge$")) {
                        if (mysqlMergeTable == null) {
                            mysqlMergeTable = new MysqlMergeTable();
                        }
                        if (mysqlMergeTable.getMergeTable() == null) {
                            mysqlMergeTable.setMergeTable(table.getName());
                            mysqlMergeTable.setTableNameClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(table.getName(), prefix)));
                            mysqlMergeTable.setLeftTable(pkTableName);
                            mysqlMergeTable.setLeftTableColumn(pkColumnName);
                            mysqlMergeTable.setLeftMergeTableColumn(fkColumnName);
                        } else if (mysqlMergeTable.getRightTable() == null) {
                            mysqlMergeTable.setRightTable(pkTableName);
                            mysqlMergeTable.setRightTableColumn(pkColumnName);
                            mysqlMergeTable.setRightMergeTableColumn(fkColumnName);
                        }
                    } else {
                        MysqlTable fkTable = tableMap.get(fkTableName);
                        MysqlTable pkTable = tableMap.get(pkTableName);
                        MysqlForeignKey mysqlForeignKey = new MysqlForeignKey(pkTableName, pkColumnName, fkTableName, fkColumnName, prefix, fkTable.getJavaPath(), fkTable.getComment(), getUni(tableUniMap, fkTableName, fkColumnName));
                        pkTable.getForeignKeys().add(mysqlForeignKey);

                        MysqlJoinKey mysqlJoinKey = new MysqlJoinKey(mysqlForeignKey.getJoinTableName(), mysqlForeignKey.getJoinColumnName(), mysqlForeignKey.getComment(), prefix);
                        fkTable.getMysqlJoinKeys().add(mysqlJoinKey);
                        List<MysqlJoinKey> collect = fkTable.getMysqlJoinKeys().stream().collect(
                                Collectors.collectingAndThen(
                                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getTableName() + ";" + o.getColumnName()))), ArrayList::new)
                        );
                        fkTable.setMysqlJoinKeys(collect);
                    }
                }
                if (mysqlMergeTable != null) {
                    String oneKey = "";
                    for (MysqlColumn mysqlColumn : table.getColumn()) {
                        if (mysqlColumn != null && !mysqlColumn.getName().equals("id")) {
                            oneKey = mysqlColumn.getName();
                            break;
                        }
                    }
                    if (mysqlMergeTable.getLeftMergeTableColumn().equals(oneKey)) {
                        mysqlMergeTable.setMaintain(mysqlMergeTable.getLeftTable());
                    } else if (oneKey.equals(mysqlMergeTable.getRightMergeTableColumn())) {
                        mysqlMergeTable.setMaintain(mysqlMergeTable.getRightTable());
                    }
                    if (mysqlMergeTable.getRightMergeTableColumn() == null) {
                        System.out.println("我是null");
                    }
                    MysqlTable mysqlTable = tableMap.get(mysqlMergeTable.getRightTable());
                    mysqlMergeTable.setPackagePath(mysqlTable.getJavaPath());
                    mysqlMergeTable.setComment(mysqlTable.getComment());
                    mergeTables.add(mysqlMergeTable);
                    mysqlMergeTable.setRightTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getRightTableColumn())));
                    mysqlMergeTable.setRightTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getRightTable(), prefix)));
                    MysqlMergeTable newMysqlMergeTable = new MysqlMergeTable();
                    newMysqlMergeTable.setMaintain(mysqlMergeTable.getMaintain());
                    newMysqlMergeTable.setLeftMergeTableColumn(mysqlMergeTable.getRightMergeTableColumn());
                    newMysqlMergeTable.setLeftTableColumn(mysqlMergeTable.getRightTableColumn());
                    newMysqlMergeTable.setLeftTable(mysqlMergeTable.getRightTable());
                    newMysqlMergeTable.setMergeTable(mysqlMergeTable.getMergeTable());
                    newMysqlMergeTable.setRightMergeTableColumn(mysqlMergeTable.getLeftMergeTableColumn());
                    newMysqlMergeTable.setRightTable(mysqlMergeTable.getLeftTable());
                    newMysqlMergeTable.setRightTableColumn(mysqlMergeTable.getLeftTableColumn());
                    newMysqlMergeTable.setTableNameClass(mysqlMergeTable.getTableNameClass());
                    newMysqlMergeTable.setRightTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(newMysqlMergeTable.getRightTableColumn())));
                    newMysqlMergeTable.setRightTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(newMysqlMergeTable.getRightTable(), prefix)));
                    mysqlTable = tableMap.get(newMysqlMergeTable.getRightTable());
                    newMysqlMergeTable.setPackagePath(mysqlTable.getJavaPath());
                    newMysqlMergeTable.setComment(mysqlTable.getComment());

                    newMysqlMergeTable.setLeftMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getLeftMergeTableColumn())));
                    newMysqlMergeTable.setRightMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getRightMergeTableColumn())));
                    newMysqlMergeTable.setLeftTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getLeftTable(), prefix)));

                    mysqlMergeTable.setLeftMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getLeftMergeTableColumn())));
                    mysqlMergeTable.setRightMergeTableColumnClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getRightMergeTableColumn())));
                    mysqlMergeTable.setLeftTableClass(StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(mysqlMergeTable.getLeftTable(), prefix)));

                    mergeTables.add(newMysqlMergeTable);
                }
                foreignKeys.close();
            }

            if (!mergeTables.isEmpty()) {
                Map<String, List<MysqlMergeTable>> mergeTableMap = mergeTables.stream().collect(Collectors.groupingBy(MysqlMergeTable::getLeftTable));
                Map<String, MysqlMergeTable> mergeTableMaps = mergeTables.stream().collect(Collectors.toMap(MysqlMergeTable::getMergeTable, table -> table, (k1, k2) -> k1));
                for (MysqlTable mysqlTable : tables) {
                    List<MysqlMergeTable> tmpList = mergeTableMap.get(mysqlTable.getName());
                    MysqlMergeTable mysqlMergeTable = mergeTableMaps.get(mysqlTable.getName());
                    mysqlTable.setMergeTable(mysqlMergeTable);
                    mysqlTable.setMergeTables(tmpList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}