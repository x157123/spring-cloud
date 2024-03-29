package ${javaPath}.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "isDeleted" && col.enums?? && (col.enums?size > 0)>
import ${javaPath}.enums.${col.nameClass? cap_first}Enum;
        </#if>
    </#list>
</#if>
import ${javaPath}.param.${nameClass}Param;
import ${javaPath}.query.${nameClass}Query;
import ${javaPath}.service.${nameClass}Service;
import ${javaPath}.vo.${nameClass}Vo;
import com.zc.core.auth.annotation.PreAuth;
import com.zc.core.common.api.Result;
import com.zc.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
<#if column?? && (column?size > 0) >
    <#list column as col>
        <#if col.nameClass != "isDeleted" && col.enums?? && (col.enums?size > 0)>
import java.util.Map;
        <#break>
        </#if>
    </#list>
</#if>

/**
 * @author liulei
 */
@RestController
@RequestMapping("/${nameClass? uncap_first}")
@Api(value = "${comment}", tags = "${comment}")
public class ${nameClass}Controller {

    private final ${nameClass}Service ${nameClass? uncap_first}Service;

    /**
     * 使用构造方法注入
     *
     * @param ${nameClass? uncap_first}Service    注册${comment}
     */
    public ${nameClass}Controller(${nameClass}Service ${nameClass? uncap_first}Service){
        this.${nameClass? uncap_first}Service= ${nameClass? uncap_first}Service;
    }

    /**
     * 保存对象
     *
     * @param ${nameClass? uncap_first}Param  ${comment}参数
     * @return  返回是否成功
     */
    @PreAuth
    @Log(value = "${comment}保存数据", exception = "${comment}保存数据异常")
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "${comment}")
    public Result<Long> save(@RequestBody @Valid ${nameClass}Param ${nameClass? uncap_first}Param) {
        return Result.data(${nameClass? uncap_first}Service.save(${nameClass? uncap_first}Param));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @PreAuth
    @Log(value = "${comment}Id查询数据", exception = "${comment}Id查询数据异常")
    @GetMapping(value = "/findById/{id}")
    @ApiOperation(value = "通过Id查询数据", notes = "${comment}")
    public Result<${nameClass}Vo> findById(@PathVariable Long id) {
        return Result.data(${nameClass? uncap_first}Service.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PreAuth
    @Log(value = "${comment}多Id查询数据", exception = "${comment}多Id查询数据异常")
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "${comment}")
    public Result<List<${nameClass}Vo>> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return Result.data(${nameClass? uncap_first}Service.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return  返回List结果集
     */
    @PreAuth
    @Log(value = "${comment}查询数据", exception = "${comment}查询数据异常")
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "${comment}")
    public Result<List<${nameClass}Vo>> findByList(${nameClass}Query ${nameClass? uncap_first}Query) {
        return Result.data(${nameClass? uncap_first}Service.findByList(${nameClass? uncap_first}Query));
    }

    /**
     * 传入Id 并删除
     *
     * @param id 删除Id
     * @return 返回是否成功
     */
    @PreAuth
    @Log(value = "${comment}删除数据", exception = "${comment}删除数据异常")
    @ApiOperation(value = "传入Id并删除", notes = "${comment}")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "id", required = true, value = "数据id"),
    })
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> removeById(@PathVariable Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return Result.data(${nameClass? uncap_first}Service.removeByIds(ids));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PreAuth
    @Log(value = "${comment}删除数据", exception = "${comment}删除数据异常")
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "${comment}")
    public Result<Boolean> removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return Result.data(${nameClass? uncap_first}Service.removeByIds(ids));
    }
<#if column?? && (column?size > 0) >
<#list column as col>
<#if col.nameClass != "isDeleted" && col.enums?? && (col.enums?size > 0)>

    /**
    * 数据来源类型
    * @return
    */
    @Log(value = "${col.webComment}枚举类型", exception = "${col.webComment}枚举类型")
    @GetMapping(value = "/get${col.nameClass? cap_first}Enum")
    @ApiOperation(value = "获取${col.webComment}枚举类型", notes = "${comment}")
    public Result<List<Map<String, Object>>> get${col.nameClass? cap_first}Enum(){
        List<Map<String, Object>> ${col.nameClass}Enums = ${col.nameClass? cap_first}Enum.getList();
        return Result.data(${col.nameClass}Enums);
    }
</#if>
</#list>
</#if>

    /**
     * 数据分页查询
     *
     * @param ${nameClass? uncap_first}Query  分页查询条件
     * @return  返回分页结果
     */
    @PreAuth
    @Log(value = "${comment}分页查询数据", exception = "${comment}分页查询数据异常")
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "${comment}")
    public Result<IPage<${nameClass}Vo>> queryPage(${nameClass}Query ${nameClass? uncap_first}Query) {
        return Result.data(${nameClass? uncap_first}Service.queryPage(${nameClass? uncap_first}Query));
    }
}