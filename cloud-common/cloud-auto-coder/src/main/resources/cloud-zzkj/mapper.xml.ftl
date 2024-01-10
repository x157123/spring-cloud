<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${javaPath}.mapper.${nameClass}Mapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${javaPath}.entity.${nameClass}">
        <#if column?? && (column?size > 0) >
            <#list column as col>
        <result column="${col.name}" property="${col.nameClass? uncap_first}"/>
            </#list>
        </#if>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="BaseColumnList">
        <#if column?? && (column?size > 0) >
            <#list column as col>
        `${col.name}`<#if !col?is_last>,</#if>
            </#list>
        </#if>
    </sql>



    <select id="queryPage" resultType="${javaPath}.entity.${nameClass}"
            parameterType="${javaPath}.query.${nameClass}Query">
        select * from ${name}
        <where>
            <#if column?? && (column?size > 0) >
                <#list column as col>
                    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
                    && col.nameClass != "createDate" && col.nameClass != "updateDate"
                    && col.nameClass != "isDelete" && col.nameClass != "version"
                    && col.nameClass != "createBy" && col.nameClass != "updateBy"
                    && col.nameClass != "createTime" && col.nameClass != "updateTime"
                    && col.nameClass != "isDeleted" && col.nameClass != "id">
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
            <if test="param.${mergeTable.rightTableClass? uncap_first}Id!=null and param.${mergeTable.rightTableClass? uncap_first}Id>0">
                and ${mergeTable.leftTableColumn} in (select ${mergeTable.leftMergeTableColumn}
                    from ${mergeTable.mergeTable} where ${mergeTable.rightMergeTableColumn} = ${r"#"}{param.${mergeTable.rightTableClass ? uncap_first}Id}
                )
            </if>
                </#list>
            </#if>
        </where>
    </select>
</mapper>
