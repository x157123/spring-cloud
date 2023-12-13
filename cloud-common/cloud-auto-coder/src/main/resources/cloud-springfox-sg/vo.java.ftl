package ${package}.vo;

<#list table.column as col>
    <#if col.javaType=='Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
        <#break>
    </#if>
</#list>

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

<#if (table.foreignKeys?? && (table.foreignKeys?size > 0)) || (table.mergeTables?? && (table.mergeTables?size > 0)) >
import java.util.List;
</#if>
<#list table.column as col>
    <#if col.javaType=='Date'>
import java.util.Date;
        <#break>
    </#if>
</#list>
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
import ${basePackage}${foreignKey.packagePath}.vo.${foreignKey.tableNameClass}Vo;
    </#list>
</#if>
<#if table.mergeTables?? && (table.mergeTables?size > 0) >
    <#list table.mergeTables as mergeTable>
import ${basePackage}${mergeTable.packagePath}.vo.${mergeTable.tableNameClass}Vo;
    </#list>
</#if>

/**
 * @author liulei
 * ${table.comment}
 */
@Data
@ApiModel(value = "${table.comment}响应对象", description = "${table.comment}响应对象")
public class ${table.className}Vo {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
    && col.nameClass != "IsDelete" && col.nameClass != "IsDeleted" && col.nameClass != "Version">

	/**
     * ${col.comment}
     */
    @ApiModelProperty(value = "${table.comment}${col.comment}")
    <#if col.javaType=='Long'>
    @JsonSerialize(using = ToStringSerializer.class)
    </#if>
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>

	/**
     * ${foreignKey.comment}
     */
    @ApiModelProperty(value = "${table.comment}${foreignKey.comment}")
    <#if foreignKey.uni>
    private ${foreignKey.tableNameClass}Vo ${foreignKey.tableNameClass? uncap_first}VO;
    <#else>
    private List<${foreignKey.tableNameClass}Vo> ${foreignKey.tableNameClass? uncap_first}VoList;
    </#if>
    </#list>
</#if>
<#if table.mergeTables?? && (table.mergeTables?size > 0) >
    <#list table.mergeTables as mergeTable>

    /**
    * ${mergeTable.comment}
    */
    @ApiModelProperty(value = "${table.comment}${mergeTable.comment}")
    private List<${mergeTable.tableNameClass}Vo> ${mergeTable.tableNameClass? uncap_first}VoList;
    </#list>
</#if>
}
