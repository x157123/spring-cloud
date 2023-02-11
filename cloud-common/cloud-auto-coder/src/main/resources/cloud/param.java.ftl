package ${package}.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
<#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete">
        <#if col.type=='varchar'>
import org.hibernate.validator.constraints.Length;
            <#break>
        </#if>
    </#if>
</#list>
<#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete">
        <#if col.requiredType == 'true'>
            <#if col.type != 'varchar'>
import jakarta.validation.constraints.NotNull;
                <#break>
            </#if>
        </#if>
    </#if>
</#list>
<#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete">
        <#if col.requiredType == 'true'>
            <#if col.type == 'varchar'>
import javax.validation.constraints.NotBlank;
                <#break>
            </#if>
        </#if>
    </#if>
</#list>
<#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete">
    <#if col.javaType=='Date'>

import java.util.Date;
        <#break>
    </#if>
    </#if>
</#list>

/**
 * @author liulei
 * ${table.comment}
 */
@Data
@Schema(name = "${table.comment}响应对象", description = "${table.comment}响应对象")
public class ${table.className}Param {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete">

	/**
     * ${col.comment}
     */
<#if col.nameClass != "Id">
    <#if col.requiredType == 'true'>
        <#if col.type == 'varchar'>
    @NotBlank(message = "${table.comment}${col.comment}[${table.className}Vo.${col.nameClass? uncap_first}]不能为null")
        </#if>
        <#if col.type != 'varchar'>
    @NotNull(message = "${table.comment}${col.comment}[${table.className}Vo.${col.nameClass? uncap_first}]不能为null")
        </#if>
    </#if>
    <#if col.type == 'varchar'>
    @Length(max = ${col.length?c}, message = "${table.comment}${col.comment}[${table.className}Vo.${col.nameClass? uncap_first}]长度不能超过${col.length?c}个字符")
    </#if>
</#if>
    @Schema(description = "${col.comment}"<#if col.requiredType == 'true' && col.nameClass != "Id">, required = true</#if><#if col.type == 'NUMBER' || col.type == 'int' || col.type == 'bigint'></#if>)
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
}
