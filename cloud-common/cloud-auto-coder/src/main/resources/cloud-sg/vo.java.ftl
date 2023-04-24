package ${javaPath}.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#list column as col>
    <#if col.type=='Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
        <#break>
    </#if>
</#list>

import lombok.Data;

<#if (foreignKeys?? && (foreignKeys?size > 0)) || (mergeTables?? && (mergeTables?size > 0)) >
import java.util.List;
</#if>
<#list column as col>
    <#if col.type=='Date'>
import java.util.Date;
        <#break>
    </#if>
</#list>
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
import ${foreignKey.packagePath}.vo.${foreignKey.joinTableNameClass}Vo;
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
import ${mergeTable.packagePath}.vo.${mergeTable.rightTableClass}Vo;
    </#list>
</#if>

/**
 * @author liulei
 * ${comment}
 */
@Data
@ApiModel(value = "${comment}响应对象", description = "${comment}响应对象")
public class ${nameClass}Vo {
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted" && col.nameClass != "version">
	/**
     * ${col.comment}
     */
    @ApiModelProperty(value = "${comment}${col.comment}")
    <#if col.type=='Long'>
    @JsonSerialize(using = ToStringSerializer.class)
    </#if>
    private ${col.type} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>

	/**
     * ${foreignKey.comment}
     */
    @ApiModelProperty(value = "${comment}${foreignKey.comment}")
    <#if foreignKey.uni>
    private ${foreignKey.joinTableNameClass}Vo ${foreignKey.joinTableNameClass? uncap_first}Vo;
        <#else>
    private List<${foreignKey.joinTableNameClass}Vo> ${foreignKey.joinTableNameClass? uncap_first}VoList;
    </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>

    /**
    * ${mergeTable.comment}
    */
    @ApiModelProperty(value = "${comment}${mergeTable.comment}")
    private List<${mergeTable.rightTableClass}Vo> ${mergeTable.rightTableClass? uncap_first}VOList;
    </#list>
</#if>
}
