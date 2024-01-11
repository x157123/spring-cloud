package ${javaPath}.entity;

<#list column as col>
    <#if col.nameClass=='isDeleted'>
import com.baomidou.mybatisplus.annotation.TableLogic;
        <#break>
    </#if>
</#list>
import com.zc.core.database.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

<#list column as col>
    <#if col.type=='Date'>
import java.time.LocalDateTime;
        <#break>
    </#if>
</#list>

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
        && col.nameClass != "id" && col.nameClass != "version"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">

    /**
     * ${col.comment}
     */
<#if col.nameClass=='isDeleted'>
    @TableLogic
</#if>
    private <#if col.type=='Date'>LocalDateTime</#if><#if col.type!='Date'>${col.type}</#if> ${col.nameClass? uncap_first};
        </#if>
    </#list>
</#if>
}
