package ${javaPath}.entity;

import com.zc.core.database.entity.BaseEntity;
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
public class ${nameClass} extends BaseEntity {
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "version"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">

    /**
     * ${col.comment}
     */
    private ${col.type} ${col.nameClass? uncap_first};
        </#if>
    </#list>

    public ${nameClass}(){}

    public ${nameClass}(<#if column?? && (column?size > 0) ><#list column as col><#if col.nameClass != "createUser" && col.nameClass != "updateUser"
&& col.nameClass != "createDate" && col.nameClass != "updateDate"
&& col.nameClass != "isDelete" && col.nameClass != "isDeleted"
&& col.nameClass != "id" && col.nameClass != "version"
&& col.nameClass != "createBy" && col.nameClass != "updateBy"
&& col.nameClass != "createTime" && col.nameClass != "updateTime"><#if indexNum = 1>, </#if>${col.type} ${col.nameClass? uncap_first}<#assign indexNum = 1></#if></#list></#if>) {
    <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "id" && col.nameClass != "version"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">
        this.${col.nameClass? uncap_first} = ${col.nameClass? uncap_first};
        </#if>
    </#list>
    }
</#if>
}