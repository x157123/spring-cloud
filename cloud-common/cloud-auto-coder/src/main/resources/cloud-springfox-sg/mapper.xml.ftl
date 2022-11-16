<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.mapper.${table.className}Mapper">

	<resultMap id="${table.className? uncap_first}Result" type="${package}.entity.${table.className}">
		<#list table.column as col>
		<result column="${col.name}" property="${col.nameClass? uncap_first}"/>
		</#list>
		<result column="create_user" property="createUser"/>
		<result column="create_date" property="createDate"/>
		<result column="update_user" property="updateUser"/>
		<result column="update_date" property="updateDate"/>
		<result column="is_deleted" property="isDeleted"/>
	</resultMap>


    <select id="queryPage" resultMap="${table.className? uncap_first}Result"
            parameterType="${package}.query.${table.className}Query">
        select * from ${table.name}
		where is_deleted =  0
		<#if table.column?? && (table.column?size > 0) >
		<#list table.column as col>
		<#if col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
			&& col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
		<#if col.type == 'varchar'>
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
		<#if table.mergeTables?? && (table.mergeTables?size > 0) >
		<#list table.mergeTables as mergeTable>
		<if test="param.${mergeTable.tableNameClass? uncap_first}Ids!=null and param.${mergeTable.tableNameClass? uncap_first}Ids.size()>0">
			and ${mergeTable.leftTableColumn} in (select ${mergeTable.leftMergeTableColumn} from ${mergeTable.mergeTable} where ${mergeTable.rightMergeTableColumn} in
			<foreach collection="param.${mergeTable.tableNameClass? uncap_first}Ids" item="item" open="(" close=")" separator="," >
				${r"#"}{item}
			</foreach>
		</if>
		</#list>
		</#if>
    </select>
</mapper>
