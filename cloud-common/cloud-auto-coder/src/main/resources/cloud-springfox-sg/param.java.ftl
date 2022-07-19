package ${package}.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#list table.column as col>
    <#if col.type=='varchar'>
import org.hibernate.validator.constraints.Length;
        <#break>
    </#if>
</#list>
<#list table.column as col>
    <#if col.requiredType == 'true'>

import javax.validation.constraints.NotBlank;
        <#break>
    </#if>
</#list>
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
@ApiModel(value = "${table.comment}响应对象", description = "${table.comment}响应对象")
public class ${table.className}Param {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">

	/**
     * ${col.comment}
     */
<#if col.nameClass != "Id">
<#if col.requiredType == 'true'>
    @NotBlank(message = "${table.comment}${col.comment}[${table.className}Vo.${col.nameClass? uncap_first}]不能为null")
</#if>
<#if col.type == 'varchar'>
    @Length(max = ${col.length?c}, message = "${table.comment}${col.comment}[${table.className}Vo.${col.nameClass? uncap_first}]长度不能大于${col.length}")
</#if>
</#if>
    @ApiModelProperty(value = "${col.comment}"<#if col.requiredType == 'true' && col.nameClass != "Id">, required = true</#if><#if col.type == 'NUMBER' || col.type == 'int' || col.type == 'bigint'>, example = "1"</#if>)
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
}
