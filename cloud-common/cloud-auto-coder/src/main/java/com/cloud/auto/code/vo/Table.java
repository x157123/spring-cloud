package com.cloud.auto.code.vo;

import com.cloud.auto.code.util.StringUtil;
import com.cloud.common.core.utils.BeanUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 * SELECT TABLE_NAME as name, TABLE_COMMENT as comment FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'testuser' order by CREATE_TIME asc;
 */
@Data
public class Table {

    /**
     * 名称
     */
    private String name;

    /**
     * 类中属性
     */
    private List<Column> column;

    /**
     * 关联表
     */
    private List<ForeignKey> foreignKeys;

    /**
     * 经过关联中间表关联的表
     */
    private List<MergeTable> mergeTables;

    /**
     * 表关联字段
     */
    private List<Key> keys;

    /**
     * 功能说明
     * 中文备注->包指定目录    如：   人员信息->persion
     */
    private String comment;

    /**
     * 特殊包路径  获取 comment下的 persion
     */
    private String packagePath;

    /**
     * 前缀
     */
    private List<String> prefix;

    /**
     * 获取ClassName
     *
     * @return
     */
    public String getClassName() {
        String className = name;
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

    public List<Key> getKeys() {
        if (keys != null && keys.size() > 0) {
            Map<String, Key> map = new HashMap<>();
            for (Key key : keys) {
                map.put(key.getColumnName(), key);
            }
            List<Key> list = new ArrayList<>();
            map.forEach((k, v) -> {
                list.add(v);
            });
            keys = list;
        }
        return keys;
    }

    /**
     * 获取表备注
     *
     * @return
     */
    public String getComment() {
        comment = BeanUtil.replaceBlank(comment);
        if (comment != null && comment.lastIndexOf("->") > 0) {
            return comment.substring(0, comment.lastIndexOf("->"));
        } else if (comment == null) {
            return "";
        }
        return comment;
    }

    /**
     * 截取表备注-> 符号后配置的包路径
     *
     * @return
     */
    public String getPackagePath() {
        comment = BeanUtil.replaceBlank(comment);
        if (comment != null && comment.lastIndexOf("->") > 0) {
            return "." + comment.substring(comment.lastIndexOf("->") + 2);
        }
        return "";
    }

}
