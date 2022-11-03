package ${package}.entity;

import com.cloud.common.core.entity.BaseEntity;
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
public class ${table.className} extends BaseEntity {
<#if table.column?? && (table.column?size > 0) >
    <#list table.column as col>
    <#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate"
        && col.nameClass != "IsDelete" && col.nameClass != "Version"
        && col.nameClass != "Id">

	/**
     * ${col.comment}
     */
    private ${col.javaType} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
}
