package ${package}.entity;

import lombok.Data;
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
import java.util.List;
</#if>
<#list table.column as col>
    <#if col.javaType=='Date'>
import java.util.Date;
        <#break>
    </#if>
</#list>

/**
 * @author liulei
 * ${table.comment}
 */
@Data
public class ${table.className} {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>

	/**
     * ${col.comment}
     */
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#list>
</#if>
}
