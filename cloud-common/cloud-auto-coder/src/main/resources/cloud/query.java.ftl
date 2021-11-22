package ${package}.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#if table.mergeTables?? && (table.mergeTables?size > 0) >
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
public class ${table.className}Query {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">

	/**
     * ${col.comment}
     */
    @ApiModelProperty(value = "${table.comment}${col.comment}")
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if table.mergeTables?? && (table.mergeTables?size > 0) >
<#list table.mergeTables as mergeTable>

    /**
    * ${mergeTable.comment}
    */
    @ApiModelProperty(value = "${table.comment}${mergeTable.comment}")
    private List<Long> ${mergeTable.tableNameClass? uncap_first}Ids;
</#list>
</#if>
}
