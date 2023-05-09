package ${javaPath}.query;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ${nameClass}Query {
<#if column?? && (column?size > 0) >
    <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "version">

	/**
     * ${col.comment}
     */
    @Schema(description = "${comment}${col.comment}")
    private ${col.type} ${col.nameClass? uncap_first};
    </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>

    /**
    * ${mergeTable.comment}
    */
    @Schema(description = "${comment}${mergeTable.comment}")
    private Long ${mergeTable.rightTableClass? uncap_first}Id;
</#list>
</#if>
}
