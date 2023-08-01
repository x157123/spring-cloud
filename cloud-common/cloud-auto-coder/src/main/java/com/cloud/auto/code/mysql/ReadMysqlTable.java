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

    public static void main(String[] args) throws SQLException {
        boolean star = false;
        boolean sg = false;
        String url = "jdbc:mysql://localhost:3306/cloud_sync";
        String username = "root";
        String password = "123456";
        String packagePath = "com.cloud.sync";
        List<String> prefix = Arrays.asList("app_", "sg_et_", "wgh_", "sg_", "sync_", "zz_");
        //模版路径
        String ftlPath = "cloud-new";
        //服务名
        String projectName = "cloud-sync";

        List<String> ftlList = new ArrayList<>();
        List<String> ftlMergeList = new ArrayList<>();
        List<String> pom = new ArrayList<>();

        ftlList.addAll(Arrays.asList("entity.java.ftl", "query.java.ftl", "vo.java.ftl", "param.java.ftl"));
        ftlList.addAll(Arrays.asList("mapper.xml.ftl", "mapper.java.ftl", "service.java.ftl", "serviceImpl.java.ftl", "controller.java.ftl"));


//        ftlList.addAll(Arrays.asList("application.java.ftl", "application.yml.ftl", "mybatisPlusConfig.java.ftl"));

//        pom.addAll(Arrays.asList("pom.xml.ftl"));

        ftlMergeList.addAll(Arrays.asList("entityMerge.java.ftl", "mapperMerge.java.ftl", "serviceMerge.java.ftl", "serviceMergeImpl.java.ftl"));

        String filePath = "D:\\me\\project\\springCloud\\service\\spring-cloud\\cloud-base-service\\" + projectName + "\\";

        if (!star) {
            return;
        }
        if (sg) {
            url = "jdbc:mysql://localhost:3306/test_sg";
            ftlPath = "cloud-sg";
            projectName = "tq-project-dl-issue-service";
            filePath = "D:\\tianque\\project\\service\\tq-project-danlings\\" + projectName + "\\";
            packagePath = "com.tianque.scgrid.service";
        }

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            //  获取表结果集
            List<MysqlTable> tables = getTables(conn, packagePath, prefix);

            //  设置表属性
            setColumn(conn, tables);

            //  设置关联表
            setMergeTable(conn, tables, prefix);

            //过滤掉中间表
            List<MysqlTable> writerTables = tables.stream().filter(table -> !table.getName().matches("(.*)_merge$")).toList();

            List<MysqlTable> mergeTables = tables.stream().filter(table -> table.getName().matches("(.*)_merge$")).toList();

            writer(writerTables, ftlList, ftlPath, filePath + "src\\main\\");

            writer(mergeTables, ftlMergeList, ftlPath, filePath + "src\\main\\");

            writer(writerTables, pom, ftlPath, filePath);

            createPgSql(tables);
        }
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
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath())) + "Application.java";
                break;
            case "application.yml.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("resources")) + "application.yml";
                break;
            case "pom.xml.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + "pom.xml";
                break;
            case "mybatisPlusConfig.java.ftl":
                //保存到 xml+扩展包
                saveFilePath = savePath + PackageUtil.packToFilePath(PackageUtil.mergePack("java", mysqlTable.getJavaPath(), "config")) + "MybatisPlusConfig.java";
                break;

        }
        return saveFilePath;
    }


    /**
     * 设工作表属性
     *
     * @param conn
     * @param tables
     */
    private static void setColumn(Connection conn, List<MysqlTable> tables) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 当前数据库名
            String catalog = conn.getCatalog();
            for (MysqlTable table : tables) {
                List<MysqlColumn> columns = new ArrayList<>();
                try (ResultSet rs = metaData.getColumns(catalog, null, table.getName(), "%")) {
                    while (rs.next()) {
                        String columnName = rs.getString("COLUMN_NAME");
                        String columnType = getTypeName(rs.getInt("DATA_TYPE"));
                        Boolean required = !"YES".equals(rs.getString("IS_NULLABLE")) ? Boolean.TRUE : Boolean.FALSE;
                        int columnSize = rs.getInt("COLUMN_SIZE");
                        String columnComment = rs.getString("REMARKS");
                        ResultSet index = conn.createStatement().executeQuery("SHOW COLUMNS FROM " + catalog + "." + table.getName() + " WHERE Field='" + columnName + "'");
                        Boolean uni = false;
                        if (index.next()) {
                            String key = index.getString("Key");
                            if ("UNI".equals(key)) {
                                // columnName字段为唯一索引
                                uni = true;
                            }
                        }
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
    private static List<MysqlTable> getTables(Connection conn, String packagePath, List<String> prefix) {
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
        Map<String, Boolean> tableUniMap = new HashMap<>();

        String tmp = "-----";
        for (MysqlTable table : tables) {
            for (MysqlColumn mysqlColumn : table.getColumn()) {
                String key = table.getName() + tmp + mysqlColumn.getName();
                tableUniMap.put(key, mysqlColumn.getUni());
            }
        }
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
                    mergeTables.add(newMysqlMergeTable);
                }
                foreignKeys.close();
            }

            if (mergeTables != null && mergeTables.size() > 0) {
                Map<String, List<MysqlMergeTable>> mergeTableMap = mergeTables.stream().collect(Collectors.groupingBy(MysqlMergeTable::getLeftTable));
                for (MysqlTable mysqlTable : tables) {
                    List<MysqlMergeTable> tmpList = mergeTableMap.get(mysqlTable.getName());
                    mysqlTable.setMergeTables(tmpList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}