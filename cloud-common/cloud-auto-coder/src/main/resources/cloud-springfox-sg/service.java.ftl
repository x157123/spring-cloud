package ${package}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tianque.doraemon.mybatis.support.PageParam;
import ${package}.param.${table.className}Param;
import ${package}.query.${table.className}Query;
import ${package}.vo.${table.className}Vo;

import java.util.List;

/**
 *  ${table.comment}
 *
 * @author liulei
 */
public interface ${table.className}Service {

    /**
     * 保存${table.comment}
     *
     * @param ${table.className? uncap_first}Param
     * @return
     */
    Boolean save(${table.className}Param ${table.className? uncap_first}Param);

    /**
     * 通过Id查询${table.comment}数据
     *
     * @param id
     * @return
     */
    ${table.className}Vo findById(Long id);

    /**
     * 传入多个Id 查询${table.comment}数据
     *
     * @param ids
     * @return
     */
    List<${table.className}Vo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询${table.comment}列表
     *
     * @param ${table.className? uncap_first}Query
     * @return
     */
    List<${table.className}Vo> findByList(${table.className}Query ${table.className? uncap_first}Query);

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    Boolean removeByIds(List<Long> ids);

    /**
     * 数据分页查询
     *
     * @param ${table.className? uncap_first}Query
     * @param pageParam
     * @return
     */
    IPage<${table.className}Vo> queryPage(${table.className}Query ${table.className? uncap_first}Query, PageParam pageParam);
    <#if table.keys?? && (table.keys?size > 0) >
	<#list table.keys as key>
	
    /**
     * 传入多个${key.tableComment}业务Id 查询数据
     *
     * @param ${key.columnNameClass? uncap_first}s
     * @return
     */
    List<${table.className}Vo> findBy${key.columnNameClass}(List<Long> ${key.columnNameClass? uncap_first}s);
	</#list>
    </#if>
}
