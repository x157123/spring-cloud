package ${javaPath}.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tianque.scgrid.service.componet.domain.BaseDomainExtend;
import lombok.Data;
<#if foreignKeys?? && (foreignKeys?size > 0) >
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
@TableName("${name}")
public class ${nameClass} extends BaseDomainExtend {
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
</#if>
}
