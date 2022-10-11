package com.cloud.auto.code.util;

import com.cloud.auto.code.vo.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 * mysql版本生成
 */
public class StartMysql extends DbComponent {

    /**
     * 默认数据库连接地址
     */
    private static final String defUrl = "jdbc:mysql://%s:%s/%s?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" + "&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%%2B8&nullCatalogMeansCurrent=true&useOldAliasMetadataBehavior=true";

    /**
     * 默认驱动地址
     */
    private static final String defDriver = "com.mysql.cj.jdbc.Driver";

    /**
     * 查询表
     */
    private String queryTable = "SELECT TABLE_NAME as name, TABLE_COMMENT as comment " + "FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s' order by CREATE_TIME asc";

    /**
     * 查询表字段
     */
    private String queryTableColumn = "select COLUMN_NAME as name,IS_NULLABLE as required,DATA_TYPE as type," + "ifnull( CHARACTER_MAXIMUM_LENGTH ,NUMERIC_PRECISION) as length,COLUMN_KEY as constr," + "NUMERIC_SCALE as accuracy,COLUMN_COMMENT as comment " + "FROM information_schema.COLUMNS WHERE table_schema = '%s' and table_name = '%s' order by ORDINAL_POSITION asc";

    /**
     * 外键关联字段 查询库所有表外键 取消指点表  and REFERENCED_TABLE_NAME='%s'
     */
    private String queryTableForeignKey = "select table_name,column_name,REFERENCED_TABLE_NAME as referenced_table_name," + "REFERENCED_COLUMN_NAME as referenced_column_name " + "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE constraint_schema = '%s' and REFERENCED_TABLE_NAME is not null";


    public static void main(String[] args) {
        //数据库
        String schema = "test";
        //使用生成模版
        String ftlPath;
        AutoCodeConfig autoCodeConfig = new AutoCodeConfig();

//        ftlPath = "cloud";
//        autoCodeConfig.setPackagePrefix("com.cloud");
//        autoCodeConfig.setProjectPath("F:/code/cloud/spring-cloud/cloud-apps/test/src/main/");

        ftlPath = "cloud-springfox-sg";
        autoCodeConfig.setPackagePrefix("com.tianque.scgrid.service.issue.party");
        autoCodeConfig.setProjectPath("D:/tianque/project/service/tq-scgrid-flowengine/tq-scgrid-eventcenter-service/src/main/");
        autoCodeConfig.setWebPath("C:/Users/liulei/Desktop");
        //设置服务名称
        autoCodeConfig.setServeName("tq-scgrid-issue-service");
        //是否生成启动配置文件
        autoCodeConfig.setStartApp(Boolean.FALSE);
        //去除表前缀
        List<String> prefix = Arrays.asList("app_", "wgh_", "sg_");
        //设置署名
        autoCodeConfig.setAuthor("lei.liu");
        //设置创建时间
        autoCodeConfig.setCreateTime(new Date());
        //设置保存mapper包路径
        autoCodeConfig.setMapperPath("party");

        StartMysql startMysql = new StartMysql();
        startMysql.initConnection(defDriver, defUrl, "127.0.0.1", "3306", schema, "root", "123456");
        //获取所有表
        List<Table> tables = startMysql.getTable(schema);
        //获取所有表外键
        List<ForeignKey> foreignKeys = startMysql.getForeignKey(schema, null);
        //根据表外键分组
        Map<String, List<ForeignKey>> foreignKeyMap = foreignKeys.stream().collect(Collectors.groupingBy(ForeignKey::getReferencedTableName));
        Map<String, List<ForeignKey>> foreignKeyTableMap = foreignKeys.stream().collect(Collectors.groupingBy(ForeignKey::getTableName));
        Map<String, List<Table>> tableMap = tables.stream().collect(Collectors.groupingBy(Table::getName));
        Map<String, List<Key>> keyMsp = new HashMap<>(tables.size());
        //获取表的列
        tables.forEach(table -> {
            table.setColumn(startMysql.getColumn(schema, table.getName()));
            table.setForeignKeys(foreignKeyMap.get(table.getName()));
            table.setPrefix(prefix);
        });
        List<Table> list = new ArrayList<>();
        List<Table> merge = new ArrayList<>();
        List<String> mergeTableNameTmp = new ArrayList<>();
        tables.forEach(table -> {
            //剔除中间表
            if (table.getName().matches("(.*)_merge$")) {
                mergeTableNameTmp.add(table.getName());
                merge.add(table);
            } else {
                list.add(table);
            }
        });
        startMysql.close();
        for (ForeignKey foreignKey : foreignKeys) {
            if (mergeTableNameTmp.contains(foreignKey.getTableName())) {
                //排除中间表
                continue;
            }
            foreignKey.setPrefix(prefix);
            List<Key> keys = keyMsp.get(foreignKey.getTableName());
            if (keys == null) {
                keys = new ArrayList<>();
            }
            Key key = new Key();
            key.setColumnName(foreignKey.getColumnName());
            key.setTableName(foreignKey.getTableName());
            List<Table> tablesTmp = tableMap.get(foreignKey.getReferencedTableName());
            if (tablesTmp != null && tablesTmp.size() > 0) {
                key.setTableComment(tablesTmp.get(0).getComment());
            }
            keys.add(key);
            keyMsp.put(foreignKey.getTableName(), keys);
        }
        for (Table table : list) {
            table.setKeys(keyMsp.get(table.getName()));
            if (table.getForeignKeys() != null && table.getForeignKeys().size() > 0) {
                Iterator<ForeignKey> iterator = table.getForeignKeys().iterator();
                List<MergeTable> mergeTables = new ArrayList<>();
                table.setMergeTables(mergeTables);
                while (iterator.hasNext()) {
                    ForeignKey foreignKey = iterator.next();
                    if (mergeTableNameTmp.contains(foreignKey.getTableName())) {
                        List<ForeignKey> foreignKeyList = foreignKeyTableMap.get(foreignKey.getTableName());
                        if (foreignKeyList.size() == 2) {
                            ForeignKey tmp = foreignKeyList.get(0);
                            if (tmp.getReferencedTableName().equals(foreignKey.getReferencedTableName())) {
                                tmp = foreignKeyList.get(1);
                            }
                            MergeTable mergeTable = new MergeTable();
                            mergeTable.setPrefix(prefix);
                            mergeTable.setMergeTable(foreignKey.getTableName());
                            mergeTable.setLeftMergeTableColumn(foreignKey.getColumnName());
                            mergeTable.setLeftTable(foreignKey.getReferencedTableName());
                            mergeTable.setLeftTableColumn(foreignKey.getReferencedColumnName());
                            mergeTable.setRightMergeTableColumn(tmp.getColumnName());
                            mergeTable.setRightTable(tmp.getReferencedTableName());
                            mergeTable.setRightTableColumn(tmp.getReferencedColumnName());

                            List<Table> ts = tableMap.get(tmp.getReferencedTableName());
                            if (ts != null && ts.size() >= 0) {
                                mergeTable.setComment(ts.get(0).getComment());
                                mergeTable.setPackagePath(ts.get(0).getPackagePath());
                            }
                            mergeTables.add(mergeTable);
                        }
                        iterator.remove();
                    }
                    for (Column column : table.getColumn()) {
                        if (foreignKey.getTableName().equals(table.getName()) && foreignKey.getColumnName().equals(column.getName())) {
                            foreignKey.setUni(column.isUni());
                        }
                    }
                }
            }
        }
        autoCodeConfig.setTables(list);
        CodeUtil.writer(autoCodeConfig, ftlPath);
        createPgSql(autoCodeConfig);
    }


    private static void createPgSql(AutoCodeConfig autoCodeConfig) {
        autoCodeConfig.getTables().forEach(table -> {
            System.out.println("create table " + table.getName() + " (");
            int i = table.getColumn().size();
            for (Column column : table.getColumn()) {
                i--;
                System.out.println("            " + column.getName() + "                   " + column.getPgSqlType() + "            " + column.getPgRequired() + (i > 0 ? "," : ""));
            }
            System.out.println(");");

            System.out.println("comment on table " + table.getName() + " is '" + table.getComment() + "';");
            table.getColumn().forEach(column -> {
                System.out.println("comment on column " + table.getName() + "." + column.getName() + " is '" + column.getComment() + "';");
            });

        });
    }


    private List<Table> getTable(String schema) {
        List<Table> list = this.readJdbcData(String.format(queryTable, schema), Table::new);
        return list;
    }

    public List<Column> getColumn(String schema, String table) {
        List<Column> list = this.readJdbcData(String.format(queryTableColumn, schema, table), Column::new);
        return list;
    }

    public List<ForeignKey> getForeignKey(String schema, String table) {
        List<ForeignKey> list = this.readJdbcData(String.format(queryTableForeignKey, schema), ForeignKey::new);
        return list;
    }
}
