package ${javaPath}.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
<#if foreignKeys?? && (foreignKeys?size > 0) >
    import java.util.List;
</#if>
<#assign indexNum = 0>
<#list column as col>
    <#if col.type=='Date'>
        import java.util.Date;
        <#break>
    </#if>
</#list>

import java.io.Serializable;

/**
 * @author liulei
 * ${comment}
 */
@Data
@TableName("${name}")
public class ${nameClass} implements Serializable {
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "id">

    /**
     * ${col.comment}
     */
    private ${col.type} ${col.nameClass? uncap_first};
        </#if>
    </#list>

    public ${nameClass}(<#if column?? && (column?size > 0) ><#list column as col><#if indexNum = 1>, </#if>${col.type} ${col.nameClass? uncap_first}<#assign indexNum = 1></#list></#if>) {
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "id">
        this.${col.nameClass? uncap_first} = ${col.nameClass? uncap_first};
        </#if>
    </#list>
    }
</#if>
}