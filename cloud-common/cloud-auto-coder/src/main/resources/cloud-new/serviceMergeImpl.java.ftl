package ${javaPath}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ${javaPath}.entity.${nameClass};
import ${javaPath}.mapper.${nameClass}Mapper;
import ${javaPath}.service.${nameClass}Service;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

<#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
import java.util.ArrayList;
</#if>
import java.util.List;
<#if foreignKeys?? && (foreignKeys?size > 0) >
import java.util.Map;
import java.util.stream.Collectors;
</#if>

/**
 * @author liulei
 */
@Service
public class ${nameClass}ServiceImpl implements ${nameClass}Service {

    private final ${nameClass}Mapper ${nameClass? uncap_first}Mapper;

<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
<#if foreignKey.joinTableNameClass != nameClass>
    private final ${foreignKey.joinTableNameClass}Service ${foreignKey.joinTableNameClass? uncap_first}Service;

</#if>
    </#list>
</#if>
    /**
     * 使用构造方法注入
     *
     * @param ${nameClass? uncap_first}Mapper ${comment}Mapper服务
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.tableNameClass != nameClass>
     * @param ${foreignKey.tableNameClass? uncap_first}Service  ${foreignKey.comment}Mapper服务
        </#if>
    </#list>
</#if>
     */
    public ${nameClass}ServiceImpl(${nameClass}Mapper ${nameClass? uncap_first}Mapper<#if foreignKeys?? && (foreignKeys?size > 0) ><#list foreignKeys as foreignKey><#if foreignKey.joinTableNameClass != nameClass>, ${foreignKey.joinTableNameClass}Service ${foreignKey.joinTableNameClass? uncap_first}Service</#if></#list></#if>){
        this.${nameClass? uncap_first}Mapper = ${nameClass? uncap_first}Mapper;
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
        this.${foreignKey.joinTableNameClass? uncap_first}Service = ${foreignKey.joinTableNameClass? uncap_first}Service;
        </#if>
    </#list>
</#if>
    }

    /**
     * 保存对象
     *
<#list column as col>
    <#if col_index = 0>
     * @param ${col.nameClass? uncap_first}    ${col.comment}
    </#if>
    <#if col_index = 1>
     * @param ${col.nameClass? uncap_first}s    ${col.comment}
    </#if>
</#list>
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(<#list column as col><#if col_index = 0>Long ${col.nameClass? uncap_first}</#if><#if col_index = 1>, List<Long> ${col.nameClass? uncap_first}s</#if></#list>) {
        if (<#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list> == null || CollectionUtils.isEmpty(<#list column as col><#if col_index = 1>${col.nameClass? uncap_first}s</#if></#list>)) {
            return Boolean.FALSE;
        }
        //删除原有保存数据
        this.removeById(<#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list>);
        for (Long <#list column as col><#if col_index = 1>${col.nameClass? uncap_first}</#if></#list> : <#list column as col><#if col_index = 1>${col.nameClass? uncap_first}s</#if></#list>) {
            ${nameClass? uncap_first}Mapper.insert(new ${nameClass}(<#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list>, <#list column as col><#if col_index = 1>${col.nameClass? uncap_first}</#if></#list>));
        }
        return Boolean.TRUE;
    }

    /**
     * 传入Id 并删除
     *
     * @param <#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list> id
     * @return 删除情况状态
     */
    private Boolean removeById(<#list column as col><#if col_index = 0>Long ${col.nameClass? uncap_first}</#if></#list>) {
        if (<#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list> != null) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${nameClass}::get<#list column as col><#if col_index = 0>${col.nameClass? cap_first}</#if></#list>, <#list column as col><#if col_index = 0>${col.nameClass? uncap_first}</#if></#list>);
        ${nameClass? uncap_first}Mapper.delete(queryWrapper);
        return Boolean.TRUE;
    }
}