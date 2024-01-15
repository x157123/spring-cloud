package ${javaPath}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${javaPath}.entity.${nameClass};
import ${javaPath}.param.${nameClass}Param;
<#if mergeTable?? >
import ${javaPath}.param.${mergeTable.rightTableClass? cap_first}Param;
</#if>
import ${javaPath}.query.${nameClass}Query;
import ${javaPath}.vo.${nameClass}Vo;

import java.util.Collection;
import java.util.List;

/**
 * @author liulei
 */
public interface ${nameClass}Service extends IService<${nameClass}> {

    /**
     * 保存对象
     *
     * @param ${nameClass? uncap_first}Param  前端传入对象
     * @return  返回保存成功id
     */
    Long save(${nameClass}Param ${nameClass? uncap_first}Param);

<#if mergeTable?? >
    /**
     * 保存对象
     *
     * @param ${mergeTable.rightTableClass? uncap_first}Params ${mergeTable.rightTableClass? uncap_first}Params
     * @return  返回保存成功id
     */
    List<Long> save(List<${mergeTable.rightTableClass}Param> ${mergeTable.rightTableClass? uncap_first}Params);
</#if>

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    ${nameClass}Vo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<${nameClass}Vo> findByIds(Collection<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return  返回list结果
     */
    List<${nameClass}Vo> findByList(${nameClass}Query ${nameClass? uncap_first}Query);

    /**
     * 传入多个Id 并删除
     *
     * @param ids  id集合
     * @return  删除情况状态
     */
    Boolean removeByIds(List<Long> ids);

    /**
     * 数据分页查询
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return 分页数据
     */
    IPage<${nameClass}Vo> queryPage(${nameClass}Query ${nameClass? uncap_first}Query);
    <#if mysqlJoinKeys?? && (mysqlJoinKeys?size > 0) >
	<#list mysqlJoinKeys as key>
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param ${key.columnNameClass? uncap_first}s  查询结果集
     * @return 返回结果
     */
    List<${nameClass}Vo> findBy${key.columnNameClass? cap_first}(List<Long> ${key.columnNameClass? uncap_first}s);
	</#list>
    </#if>
}