package ${package}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@AllArgsConstructor
public class ${table.className}ServiceImpl implements ${table.className}Service {

    private ${table.className}Mapper ${table.className? uncap_first}Mapper;

<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
    <#list table.foreignKeys as foreignKey>
<#if foreignKey.tableNameClass != table.className>
    private ${foreignKey.tableNameClass}Service ${foreignKey.tableNameClass? uncap_first}Service;

</#if>
    </#list>
</#if>
    /**
     * 保存对象
     *
     * @param ${table.className? uncap_first}Param 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(${table.className}Param ${table.className? uncap_first}Param) {
        ${table.className} ${table.className? uncap_first} = BeanUtil.copyProperties(${table.className? uncap_first}Param, ${table.className}::new);
        if(${table.className? uncap_first}Param.getId()!=null){
            return this.updateById(${table.className? uncap_first});
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
        if(CollectionUtils.isEmpty(${table.className? uncap_first}Vos)){
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
        List<${table.className}> ${table.className? uncap_first}Entities = ${table.className? uncap_first}Mapper.selectList(queryWrapper);
        List<${table.className}Vo> list = BeanUtil.copyListProperties(${table.className? uncap_first}Entities, ${table.className}Vo::new);
		<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
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
        IPage<${table.className}Vo> page = iPage.convert(${table.className? uncap_first} -> BeanUtil.copyProperties(${table.className? uncap_first}, ${table.className}Vo::new));
		<#if table.foreignKeys?? && (table.foreignKeys?size > 0) >
		this.setParam(page.getRecords());
		</#if>
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param ${table.className? uncap_first} 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(${table.className} ${table.className? uncap_first}) {
        LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${table.className}::getId, ${table.className? uncap_first}.getId());
        ${table.className? uncap_first}Mapper.update(${table.className? uncap_first}, queryWrapper);
        return Boolean.TRUE;
    }

<#if table.keys?? && (table.keys?size > 0) >
	<#list table.keys as key>
	
	/**
     * 传入多个Id 查询数据
     * @param ${key.columnNameClass? uncap_first}s
     * @return
     */
    @Override
    public List<${table.className}Vo> findBy${key.columnNameClass}(List<Long> ${key.columnNameClass? uncap_first}s){
		LambdaQueryWrapper<${table.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${table.className}::get${key.columnNameClass}, ${key.columnNameClass? uncap_first}s);
        List<${table.className}> ${table.className? uncap_first}Entities = ${table.className? uncap_first}Mapper.selectList(queryWrapper);
        List<${table.className}Vo> list = BeanUtil.copyListProperties(${table.className? uncap_first}Entities, ${table.className}Vo::new);
        return list;
	}
	</#list>
</#if>
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
}
