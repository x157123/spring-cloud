package ${javaPath}.query;

import com.zc.core.database.entity.Search;
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
@ApiModel(description = "${comment}")
public class ${nameClass}Query extends Search {
<#if column?? && (column?size > 0) >
    <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "version"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "createTime" && col.nameClass != "updateTime"
        && col.nameClass != "isDeleted" && col.nameClass != "id">

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
    private Long ${mergeTable.rightTableClass? uncap_first}Id;
</#list>
</#if>
}
