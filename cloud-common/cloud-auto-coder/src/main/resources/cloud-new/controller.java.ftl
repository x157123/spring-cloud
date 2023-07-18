package ${javaPath}.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import ${javaPath}.param.${nameClass}Param;
import ${javaPath}.query.${nameClass}Query;
import ${javaPath}.service.${nameClass}Service;
import ${javaPath}.vo.${nameClass}Vo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/${nameClass? uncap_first}")
@Tag(description = "${comment}", name = "${comment}")
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
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "${comment}")
    public ResultBody save(@RequestBody ${nameClass}Param ${nameClass? uncap_first}Param) {
        return ResultBody.success(${nameClass? uncap_first}Service.save(${nameClass? uncap_first}Param));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "${comment}")
    public ResultBody findById(Long id) {
        return ResultBody.success(${nameClass? uncap_first}Service.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "${comment}")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(${nameClass? uncap_first}Service.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param ${nameClass? uncap_first}Query 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "${comment}")
    public ResultBody findByList(${nameClass}Query ${nameClass? uncap_first}Query) {
        return ResultBody.success(${nameClass? uncap_first}Service.findByList(${nameClass? uncap_first}Query));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "${comment}")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(${nameClass? uncap_first}Service.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param ${nameClass? uncap_first}Query  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "${comment}")
    public ResultBody queryPage(@ParameterObject ${nameClass}Query ${nameClass? uncap_first}Query, @ParameterObject PageParam pageParam) {
        return ResultBody.success(${nameClass? uncap_first}Service.queryPage(${nameClass? uncap_first}Query, pageParam));
    }

}