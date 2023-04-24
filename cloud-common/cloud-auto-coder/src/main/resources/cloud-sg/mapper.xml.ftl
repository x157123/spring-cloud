<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${javaPath}.mapper.${nameClass}Mapper">

    <select id="queryPage" resultType="${javaPath}.entity.${nameClass}"
            parameterType="${javaPath}.query.${nameClass}Query">
        select * from ${name}
        <where>
            <#if column?? && (column?size > 0) >
                <#list column as col>
                    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
                    && col.nameClass != "vreateDate" && col.nameClass != "UpdateDate"
                    && col.nameClass != "isDelete" && col.nameClass != "version">
                        <#if col.type == 'String'>
            <if test="param.${col.nameClass ? uncap_first} != null and param.${col.nameClass ? uncap_first} != ''">
                and ${col.name} like concat('%',${r"#"}{param.${col.nameClass ? uncap_first}},'%')
            </if>
                        <#else>
            <if test="param.${col.nameClass ? uncap_first} != null">
                and ${col.name} = ${r"#"}{param.${col.nameClass ? uncap_first}}
            </if>
                        </#if>
                    </#if>
                </#list>
            </#if>
            <#if mergeTables?? && (mergeTables?size > 0) >
                <#list mergeTables as mergeTable>
            <if test="param.${mergeTable.rightTableClass? uncap_first}Ids!=null and param.${mergeTable.rightTableClass? uncap_first}Ids.size()>0">
                and ${mergeTable.leftTableColumn} in (select ${mergeTable.leftMergeTableColumn}
                    from ${mergeTable.mergeTable} where ${mergeTable.rightMergeTableColumn} in
                    <foreach collection="param.${mergeTable.rightTableClass? uncap_first}Ids" item="item" open="(" close=")" separator=",">
                        ${r"#"}{item}
                    </foreach>
                )
            </if>
                </#list>
            </#if>
        </where>
    </select>
</mapper>
