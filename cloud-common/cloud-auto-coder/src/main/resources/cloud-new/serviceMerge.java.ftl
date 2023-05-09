package ${javaPath}.service;

import java.util.List;

/**
 * @author liulei
 */
public interface ${nameClass}Service {

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
    Boolean save(<#list column as col><#if col_index = 0>Long ${col.nameClass? uncap_first}</#if><#if col_index = 1>, List<Long> ${col.nameClass? uncap_first}s</#if></#list>);
}