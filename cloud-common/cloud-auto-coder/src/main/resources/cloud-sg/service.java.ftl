package ${javaPath}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tianque.doraemon.mybatis.support.PageParam;
import ${javaPath}.param.${nameClass}Param;
import ${javaPath}.query.${nameClass}Query;
import ${javaPath}.vo.${nameClass}Vo;

import java.util.List;

/**
 * @author liulei
 */
public interface ${nameClass}Service {

    /**
     * 保存对象
     *
     * @param ${nameClass? uncap_first}Param  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(${nameClass}Param ${nameClass? uncap_first}Param);

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
    List<${nameClass}Vo> findByIds(List<Long> ids);


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
     * @param pageParam    分页条件
     * @return 分页数据
     */
    IPage<${nameClass}Vo> queryPage(${nameClass}Query ${nameClass? uncap_first}Query, PageParam pageParam);
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