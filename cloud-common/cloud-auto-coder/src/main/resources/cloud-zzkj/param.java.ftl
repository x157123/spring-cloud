package ${javaPath}.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
import ${mergeTable.rightTablePath}.param.${mergeTable.rightTableClass}Param;
        </#if>
    </#list>
</#if>
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
import ${foreignKey.packagePath}.param.${foreignKey.joinTableNameClass}Param;
        </#if>
    </#list>
</#if>

<#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
        <#if col.required>
            <#if col.type == 'String'>
import jakarta.validation.constraints.NotBlank;
                <#break>
            </#if>
        </#if>
    </#if>
</#list>
<#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
        <#if col.required>
            <#if col.type != 'String'>
                <#break>
            </#if>
        </#if>
    </#if>
</#list>
import lombok.Data;
<#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
        <#if col.type=='String'>
import org.hibernate.validator.constraints.Length;
            <#break>
        </#if>
    </#if>
</#list>

<#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
    <#if col.type=='Date'>
import java.time.LocalDateTime;
        <#break>
    </#if>
    </#if>
</#list>
<#if (foreignKeys?? && (foreignKeys?size > 0)) || (mergeTables?? && (mergeTables?size > 0)) >
import java.util.List;
</#if>

/**
 * @author liulei
 * ${comment}
 */
@Data
@ApiModel(description = "${comment}响应对象")
public class ${nameClass}Param {
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">

	/**
     * ${col.comment}
     */
<#if col.nameClass != "id">
    <#if col.required>
<#--        <#if col.type == 'String'>-->
<#--    @NotBlank(message = "${comment}${col.comment}[${nameClass}Vo.${col.nameClass? uncap_first}]不能为null")-->
<#--        </#if>-->
<#--        <#if col.type != 'String'>-->
<#--    @NotNull(message = "${comment}${col.comment}[${nameClass}Vo.${col.nameClass? uncap_first}]不能为null")-->
<#--        </#if>-->
    </#if>
    <#if col.type == 'String'>
    @Length(max = ${col.length?c}, message = "${comment}${col.comment}[${nameClass}Vo.${col.nameClass? uncap_first}]长度不能超过${col.length?c}个字符")
    </#if>
</#if>
    @ApiModelProperty(value = "${col.comment}"<#if col.required && col.nameClass != "id">, requiredMode = Schema.RequiredMode.REQUIRED</#if><#if col.type == 'NUMBER' || col.type == 'int' || col.type == 'bigint'></#if>)
    private <#if col.type=='Date'>LocalDateTime</#if><#if col.type!='Date'>${col.type}</#if> ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>

    <#if mergeTable.leftTable == mergeTable.maintain>
    /**
    * ${mergeTable.comment}
    */
    @ApiModelProperty(value = "${comment}${mergeTable.comment}")
    private List<${mergeTable.rightTableClass}Param> ${mergeTable.rightTableClass? uncap_first}Params;
    </#if>
    </#list>
</#if>

<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
    /**
     * ${foreignKey.comment}
     */
    @ApiModelProperty(value = "${comment}${foreignKey.comment}")
    private List<${foreignKey.joinTableNameClass}Param> ${foreignKey.joinTableNameClass? uncap_first}Params;
        </#if>
    </#list>
</#if>
}