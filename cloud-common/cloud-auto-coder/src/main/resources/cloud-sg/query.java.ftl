package ${javaPath}.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#if mergeTables?? && (mergeTables?size > 0) >
import java.util.List;
</#if>
<#list column as col>
    <#if col.type=='Date'>

import java.util.Date;
        <#break>
    </#if>
</#list>

/**
 * @author liulei
 * ${comment}
 */
@Data
@ApiModel(value = "${comment}查询对象", description = "${comment}查询对象")
public class ${nameClass}Query {
<#if column?? && (column?size > 0) >
    <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "version">

	/**
     * ${col.comment}
     */
    @ApiModelProperty(value = "${comment}${col.comment}")
    private ${col.type} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>

    /**
    * ${mergeTable.comment}
    */
    @ApiModelProperty(value = "${comment}${mergeTable.comment}")
    private List<Long> ${mergeTable.rightTableClass? uncap_first}Ids;
</#list>
</#if>
}
