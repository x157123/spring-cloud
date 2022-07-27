package ${package}.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianque.scgrid.service.componet.domain.BaseDomainExtend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("${table.name}")
public class ${table.className} extends BaseDomainExtend{
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
        <#if col.nameClass != "Id" && col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
	/**
     * ${col.comment}
     */
    @TableField(value = "${col.name}")
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
}
