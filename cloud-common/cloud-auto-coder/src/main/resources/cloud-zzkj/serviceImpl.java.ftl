package ${javaPath}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zc.core.common.util.$;
import com.zc.core.database.util.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${javaPath}.entity.${nameClass};
<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
import ${mergeTable.packagePath}.service.${mergeTable.tableNameClass? cap_first}Service;
        </#if>
    </#list>
</#if>
import com.zc.core.common.exception.ServiceException;
import ${javaPath}.vo.${nameClass}Vo;
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
import ${mergeTable.rightTablePath}.vo.${mergeTable.rightTableClass? cap_first}Vo;
    </#if>
    </#list>
    </#if>
import ${javaPath}.mapper.${nameClass}Mapper;
import ${javaPath}.query.${nameClass}Query;
import ${javaPath}.service.${nameClass}Service;
import ${javaPath}.param.${nameClass}Param;
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
import ${foreignKey.packagePath}.vo.${foreignKey.joinTableNameClass}Vo;
import ${foreignKey.packagePath}.service.${foreignKey.joinTableNameClass}Service;
        </#if>
    </#list>
</#if>
<#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
import org.apache.commons.collections4.ListUtils;
</#if>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
<#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
import java.util.stream.Collectors;
</#if>

/**
 * @author liulei
 */
@Service
public class ${nameClass}ServiceImpl extends ServiceImpl<${nameClass}Mapper, ${nameClass}> implements ${nameClass}Service{

    private final ${nameClass}Mapper ${nameClass? uncap_first}Mapper;

<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
    private final ${mergeTable.tableNameClass? cap_first}Service ${mergeTable.tableNameClass? uncap_first}Service;

        </#if>
    </#list>
</#if>
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
<#if foreignKey.joinTableNameClass != nameClass>
    private final ${foreignKey.joinTableNameClass}Service ${foreignKey.joinTableNameClass? uncap_first}Service;

</#if>
    </#list>
</#if>
    /**
     * 使用构造方法注入
     *
     * @param ${nameClass? uncap_first}Mapper ${comment}Mapper服务
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.tableNameClass != nameClass>
     * @param ${foreignKey.tableNameClass? uncap_first}Service  ${foreignKey.comment}Mapper服务
        </#if>
    </#list>
</#if>
     */
    public ${nameClass}ServiceImpl(${nameClass}Mapper ${nameClass? uncap_first}Mapper<#if foreignKeys?? && (foreignKeys?size > 0) ><#list foreignKeys as foreignKey><#if foreignKey.joinTableNameClass != nameClass>, ${foreignKey.joinTableNameClass}Service ${foreignKey.joinTableNameClass? uncap_first}Service</#if></#list></#if><#if mergeTables?? && (mergeTables?size > 0) ><#list mergeTables as mergeTable><#if mergeTable.leftTable == mergeTable.maintain>, ${mergeTable.tableNameClass? cap_first}Service ${mergeTable.tableNameClass? uncap_first}Service</#if></#list></#if>){
        this.${nameClass? uncap_first}Mapper = ${nameClass? uncap_first}Mapper;
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
        this.${foreignKey.joinTableNameClass? uncap_first}Service = ${foreignKey.joinTableNameClass? uncap_first}Service;
        </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
        this.${mergeTable.tableNameClass? uncap_first}Service = ${mergeTable.tableNameClass? uncap_first}Service;
        </#if>
    </#list>
</#if>
    }

    /**
     * 保存对象
     *
     * @param ${nameClass? uncap_first}Param 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Long save(${nameClass}Param ${nameClass? uncap_first}Param) {
        ${nameClass} ${nameClass? uncap_first} = $.copy(${nameClass? uncap_first}Param, ${nameClass}.class);
        if (${nameClass? uncap_first} != null && ${nameClass? uncap_first}.getId() != null) {
            this.update(${nameClass? uncap_first});
        }else{
            ${nameClass? uncap_first}Mapper.insert(${nameClass? uncap_first});
        }
        assert ${nameClass? uncap_first} != null;
<#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
        // 保存 ${foreignKey.comment}
        ${foreignKey.joinTableNameClass? uncap_first}Service.save(${nameClass? uncap_first}.getId(), ${nameClass? uncap_first}Param.get${foreignKey.joinTableNameClass? cap_first}Params(), Boolean.FALSE);
        </#if>
    </#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
        // 保存 ${mergeTable.comment}
        ${mergeTable.tableNameClass? uncap_first}Service.save(${nameClass? uncap_first}.getId(), ${nameClass? uncap_first}Param.get${mergeTable.rightTableClass? cap_first}Params());
    </#if>
</#list>
</#if>
        return ${nameClass? uncap_first}.getId();
    }

<#if mergeTable?? >
    /**
     * 保存对象
     *
     * @param ${mergeTable.rightTableClass? uncap_first}Params ${mergeTable.rightTableClass? uncap_first}Params
     * @return  返回保存成功id
     */
    @Override
    @Transactional
    public List<Long> save(List<${mergeTable.rightTableClass}Param> ${mergeTable.rightTableClass? uncap_first}Params){
        if(CollectionUtils.isEmpty(${mergeTable.rightTableClass? uncap_first}Params)){
            return new ArrayList<>();
        }
        List<${mergeTable.rightTableClass}> ${mergeTable.rightTableClass? uncap_first}s = $.copy(${mergeTable.rightTableClass? uncap_first}Params,${mergeTable.rightTableClass}.class);
        List<Long> ids = new ArrayList<>();
        for(${mergeTable.rightTableClass} ${mergeTable.rightTableClass? uncap_first} : ${mergeTable.rightTableClass? uncap_first}s) {
            this.saveOrUpdate(${mergeTable.rightTableClass? uncap_first});
            ids.add(${mergeTable.rightTableClass? uncap_first}.getId());
        }
        return ids;
    }
    </#if>

    <#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
    <#list mysqlJoinKeys as key>

    /**
    * 保存对象
    *
    * @param ${key.columnNameClass? uncap_first} ${key.columnNameClass? uncap_first}
    * @param ${nameClass? uncap_first}Params ${nameClass? uncap_first}Params
    * @param del 是否删除关联数据
    * @return  返回保存成功
    */
    @Override
    @Transactional
    public Boolean save(Long ${key.columnNameClass? uncap_first}, List<${nameClass? cap_first}Param> ${nameClass? uncap_first}Params, boolean del){
        if(!CollectionUtils.isEmpty(${nameClass? uncap_first}Params)){
            List<${nameClass}> ${nameClass? uncap_first}s = $.copy(${nameClass? uncap_first}Params,${nameClass}.class);
            ${nameClass? uncap_first}s.forEach(t->t.set${key.columnNameClass? cap_first}(${key.columnNameClass? uncap_first}));
            if(del) {
            List<Long> ids = ${nameClass? uncap_first}s.stream().map(t -> t.getId()).collect(Collectors.toList());
                this.remove(new LambdaQueryWrapper<${nameClass}>().notIn(${nameClass}::getId, ids));
            }
            this.saveOrUpdateBatch(${nameClass? uncap_first}s);
        }
        return Boolean.TRUE;
    }
    </#list>
    </#if>

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ${nameClass}Vo findById(Long id) {
        List<${nameClass}Vo> ${nameClass? uncap_first}Vos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(${nameClass? uncap_first}Vos)) {
            return null;
        }
        return CollectionUtils.firstElement(${nameClass? uncap_first}Vos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<${nameClass}Vo> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${nameClass}::getId, ids);
        List<${nameClass}Vo> list = queryWrapper(queryWrapper);
		<#if foreignKeys?? && (foreignKeys?size > 0) >
        //封装关联数据
		this.setParam(list);
		</#if>
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return 返回list结果
     */
    @Override
    public List<${nameClass}Vo> findByList(${nameClass}Query ${nameClass? uncap_first}Query) {
        IPage<${nameClass}Vo> iPage = this.queryPage(${nameClass? uncap_first}Query);
		<#if foreignKeys?? && (foreignKeys?size > 0) >
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
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${nameClass}::getId, ids);
        ${nameClass? uncap_first}Mapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return 分页数据
     */
    @Override
    public IPage<${nameClass}Vo> queryPage(${nameClass}Query ${nameClass? uncap_first}Query) {
        IPage<${nameClass}> iPage = ${nameClass? uncap_first}Mapper.queryPage(PageUtil.getPage(${nameClass? uncap_first}Query), ${nameClass? uncap_first}Query);
<#if foreignKeys?? && (foreignKeys?size > 0) >
        IPage<${nameClass}Vo> page = iPage.convert(${nameClass? uncap_first} -> $.copy(${nameClass? uncap_first}, ${nameClass}Vo.class));
		this.setParam(page.getRecords());
        return page;
<#else >
        return iPage.convert(${nameClass? uncap_first} -> $.copy(${nameClass? uncap_first}, ${nameClass}Vo.class));
</#if>
    }
<#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
	<#list mysqlJoinKeys as key>
	
	/**
     * 传入多个Id 查询数据
     *
     * @param ${key.columnNameClass? uncap_first}s id集合
     * @return  返回查询结果
     */
    @Override
    public List<${nameClass}Vo> findBy${key.columnNameClass? cap_first}(List<Long> ${key.columnNameClass? uncap_first}s){
        if (${key.columnNameClass? uncap_first}s == null || ${key.columnNameClass? uncap_first}s.isEmpty()) {
            return new ArrayList<>();
        }
        List<${nameClass}Vo> dataList = new ArrayList<>();
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(${key.columnNameClass? uncap_first}s, 5000);
        for(List<Long> list : subLists) {
            queryWrapper.in(${nameClass}::get${key.columnNameClass? cap_first}, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }
	</#list>
</#if>

    /**
     * 通过Id 更新数据
     *
     * @param ${nameClass? uncap_first} 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(${nameClass} ${nameClass? uncap_first}) {
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(${nameClass}::getId, ${nameClass? uncap_first}.getId());
        int count = ${nameClass? uncap_first}Mapper.update(${nameClass? uncap_first}, queryWrapper);
        if (count <= 0) {
            throw new ServiceException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

<#if foreignKeys?? && (foreignKeys?size > 0) >
	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<${nameClass}Vo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(${nameClass}Vo::getId).collect(Collectors.toList());
	<#list foreignKeys as foreignKey>
            Map<Long, List<${foreignKey.joinTableNameClass}Vo>> ${foreignKey.joinTableNameClass? uncap_first}Map = <#if foreignKey.joinTableNameClass != nameClass>${foreignKey.joinTableNameClass? uncap_first}Service.</#if>findBy${foreignKey.joinColumnNameClass}(ids).stream().collect(Collectors.groupingBy(${foreignKey.joinTableNameClass}Vo::get${foreignKey.joinColumnNameClass}));
	</#list>
    <#if mergeTables?? && (mergeTables?size > 0) >
        <#list mergeTables as mergeTable>
            <#if mergeTable.leftTable == mergeTable.maintain>
            Map<Long, List<${mergeTable.rightTableClass? cap_first}Vo>> ${mergeTable.rightTableClass? uncap_first}Map = ${mergeTable.tableNameClass? uncap_first}Service.findBy${mergeTable.leftMergeTableColumnClass? cap_first}s(ids);
            </#if>
        </#list>
    </#if>
            for (${nameClass}Vo ${nameClass? uncap_first} : list) {
	<#list foreignKeys as foreignKey>
        <#if foreignKey.uni>
                List<${foreignKey.joinTableNameClass}Vo> ${foreignKey.joinTableNameClass? uncap_first}Vos = ${foreignKey.joinTableNameClass? uncap_first}Map.get(${nameClass? uncap_first}.getId());
                if (${foreignKey.joinTableNameClass? uncap_first}Vos != null && ${foreignKey.joinTableNameClass? uncap_first}Vos.size() > 0) {
                    ${nameClass? uncap_first}.set${foreignKey.joinTableNameClass}Vo(${foreignKey.joinTableNameClass? uncap_first}Vos.get(0));
                }
        <#else>
                ${nameClass? uncap_first}.set${foreignKey.joinTableNameClass}VoList(${foreignKey.joinTableNameClass? uncap_first}Map.get(${nameClass? uncap_first}.getId()));
        </#if>
    </#list>
        <#if mergeTables?? && (mergeTables?size > 0) >
        <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
                ${nameClass? uncap_first}.set${mergeTable.rightTableClass? cap_first}VoList(${mergeTable.rightTableClass? uncap_first}Map.get(${nameClass? uncap_first}.getId()));
        </#if>
            </#list>
            </#if>
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
    private List<${nameClass}Vo> queryWrapper(LambdaQueryWrapper<${nameClass}> queryWrapper){
        // 数据查询
        List<${nameClass}> ${nameClass? uncap_first}Entities = ${nameClass? uncap_first}Mapper.selectList(queryWrapper);
        // 数据转换
        return $.copy(${nameClass? uncap_first}Entities, ${nameClass}Vo.class);
    }
}