package ${package}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
<#assign showVersion = 0>
<#list table.column as col>
    <#if col.nameClass != "Version">
        <#assign showVersion = 1>
import com.cloud.common.core.utils.DataVersionUtils;
        <#break>
    </#if>
</#list>
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import ${package}.entity.${table.className};
import ${package}.vo.${table.className}Vo;
import ${package}.mapper.${table.className}Mapper;
import ${package}.query.${table.className}Query;
import ${package}.service.${table.className}Service;
import ${package}.param.${table.className}Param;
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
        <#if foreignKey.tableNameClass != table.className>
import ${basePackage}${foreignKey.packagePath}.vo.${foreignKey.tableNameClass}Vo;
import ${basePackage}${foreignKey.packagePath}.service.${foreignKey.tableNameClass}Service;
        </#if>
    </#list>
</#if>
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

<#if table.keys?? && (table.keys?size > 0) >
import java.util.ArrayList;
</#if>
import java.util.Collections;
import java.util.List;
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
import java.util.Map;
import java.util.stream.Collectors;
</#if>

/**
 * @author liulei
 */
@Service
public class ${table.className}ServiceImpl implements ${table.className}Service {

    private final ${table.className}Mapper ${table.className? uncap_first}Mapper;

<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
<#if foreignKey.tableNameClass != table.className>
    private final ${foreignKey.tableNameClass}Service ${foreignKey.tableNameClass? uncap_first}Service;

</#if>
    </#list>
</#if>
    /**
     * 使用构造方法注入
     *
     * @param ${table.className? uncap_first}Mapper ${table.comment}Mapper服务
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
        <#if foreignKey.tableNameClass != table.className>
     * @param ${foreignKey.tableNameClass? uncap_first}Service  ${foreignKey.comment}Mapper服务
        </#if>
    </#list>
</#if>
     */
    public ${table.className}ServiceImpl(${table.className}Mapper ${table.className? uncap_first}Mapper<#if table.foreignKeys?? && (table.foreignKeys?size > 0) ><#list table.foreignKeys as foreignKey><#if foreignKey.tableNameClass != table.className>, ${foreignKey.tableNameClass}Service ${foreignKey.tableNameClass? uncap_first}Service</#if></#list></#if>){
        this.${table.className? uncap_first}Mapper = ${table.className? uncap_first}Mapper;
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
        <#if foreignKey.tableNameClass != table.className>
        this.${foreignKey.tableNameClass? uncap_first}Service = ${foreignKey.tableNameClass? uncap_first}Service;
        </#if>
    </#list>
</#if>
    }

    /**
     * 保存对象
     *
     * @param ${table.className? uncap_first}Param 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(${table.className}Param ${table.className? uncap_first}Param) {
        ValidationUtils.validate(${table.className? uncap_first}Param);
        ${table.className} ${table.className? uncap_first} = BeanUtil.copyProperties(${table.className? uncap_first}Param, ${table.className}::new);
        if (${table.className? uncap_first} != null && ${table.className? uncap_first}.getId() != null) {
            LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
<#if showVersion==0 >
            queryWrapper.eq(${table.className}::getId, ${table.className? uncap_first}.getId());
</#if>
<#if showVersion==1 >
            queryWrapper.eq(${table.className}::getId, ${table.className? uncap_first}.getId())
                    .eq(${table.className}::getVersion, ${table.className? uncap_first}.getVersion());
            ${table.className? uncap_first}.setVersion(DataVersionUtils.next());
</#if>
            return this.update(queryWrapper, ${table.className? uncap_first});
        }
        ${table.className? uncap_first}Mapper.insert(${table.className? uncap_first});
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ${table.className}Vo findById(Long id) {
        List<${table.className}Vo> ${table.className? uncap_first}Vos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(${table.className? uncap_first}Vos)) {
            return null;
        }
        return CollectionUtils.firstElement(${table.className? uncap_first}Vos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<${table.className}Vo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${table.className}::getId, ids);
        List<${table.className}Vo> list = queryWrapper(queryWrapper);
		<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
        //封装关联数据
		this.setParam(list);
		</#if>
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param ${table.className? uncap_first}Query 查询条件
     * @return 返回list结果
     */
    @Override
    public List<${table.className}Vo> findByList(${table.className}Query ${table.className? uncap_first}Query) {
        IPage<${table.className}Vo> iPage = this.queryPage(${table.className? uncap_first}Query, new PageParam());
		<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
        //封装关联数据
		this.setParam(iPage.getRecords());
		</#if>
        return iPage.getRecords();
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids id集合
     * @return 删除情况状态
     */
    @Override
    public Boolean removeByIds(List<Long> ids) {
        LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${table.className}::getId, ids);
        ${table.className? uncap_first}Mapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param ${table.className? uncap_first}Query 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<${table.className}Vo> queryPage(${table.className}Query ${table.className? uncap_first}Query, PageParam pageParam) {
        IPage<${table.className}> iPage = ${table.className? uncap_first}Mapper.queryPage(OrderUtil.getPage(pageParam), ${table.className? uncap_first}Query);
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
        IPage<${table.className}Vo> page = iPage.convert(${table.className? uncap_first} -> BeanUtil.copyProperties(${table.className? uncap_first}, ${table.className}Vo::new));
		this.setParam(page.getRecords());
        return page;
<#else >
        return iPage.convert(${table.className? uncap_first} -> BeanUtil.copyProperties(${table.className? uncap_first}, ${table.className}Vo::new));
</#if>
    }
<#if table.keys?? && (table.keys?size > 0) >
	<#list table.keys as key>
	
	/**
     * 传入多个Id 查询数据
     *
     * @param ${key.columnNameClass? uncap_first}s id集合
     * @return  返回查询结果
     */
    @Override
    public List<${table.className}Vo> findBy${key.columnNameClass}(List<Long> ${key.columnNameClass? uncap_first}s){
        if (${key.columnNameClass? uncap_first}s == null || ${key.columnNameClass? uncap_first}s.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${table.className}::get${key.columnNameClass}, ${key.columnNameClass? uncap_first}s);
        return queryWrapper(queryWrapper);
	}
	</#list>
</#if>

    /**
     * 通过Id 更新数据
     *
     * @param ${table.className? uncap_first} 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(LambdaQueryWrapper<${table.className}> queryWrapper, ${table.className} ${table.className? uncap_first}) {
    <#if showVersion==1 >
        ${table.className? uncap_first}.setVersion(DataVersionUtils.next());
    </#if>
        int count = ${table.className? uncap_first}Mapper.update(${table.className? uncap_first}, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }
<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<${table.className}Vo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(${table.className}Vo::getId).collect(Collectors.toList());
	<#list table.foreignKeys as foreignKey>
            Map<Long, List<${foreignKey.tableNameClass}Vo>> ${foreignKey.tableNameClass? uncap_first}Map = <#if foreignKey.tableNameClass != table.className>${foreignKey.tableNameClass? uncap_first}Service.</#if>findBy${foreignKey.columnNameClass}(ids).stream().collect(Collectors.groupingBy(${foreignKey.tableNameClass}Vo::get${foreignKey.columnNameClass}));
	</#list>
            for (${table.className}Vo ${table.className? uncap_first} : list) {
	<#list table.foreignKeys as foreignKey>
        <#if foreignKey.uni>
                List<${foreignKey.tableNameClass}Vo> ${foreignKey.tableNameClass? uncap_first}Vos = ${foreignKey.tableNameClass? uncap_first}Map.get(${table.className? uncap_first}.getId());
                if (${foreignKey.tableNameClass? uncap_first}Vos != null && ${foreignKey.tableNameClass? uncap_first}Vos.size() > 0) {
                    ${table.className? uncap_first}.set${foreignKey.tableNameClass}VO(${foreignKey.tableNameClass? uncap_first}Vos.get(0));
                }
        <#else>
                ${table.className? uncap_first}.set${foreignKey.tableNameClass}VOList(${foreignKey.tableNameClass? uncap_first}Map.get(${table.className? uncap_first}.getId()));
        </#if>
    </#list>
            }
        }
    }
</#if>

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<${table.className}Vo> queryWrapper(LambdaQueryWrapper<${table.className}> queryWrapper){
        // 数据查询
        List<${table.className}> ${table.className? uncap_first}Entities = ${table.className? uncap_first}Mapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(${table.className? uncap_first}Entities, ${table.className}Vo::new);
    }
}