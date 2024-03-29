package ${package}.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import ${package}.param.${table.className}Param;
import ${package}.query.${table.className}Query;
import ${package}.service.${table.className}Service;
import ${package}.vo.${table.className}Vo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/${table.className? uncap_first}")
@Tag(description = "${table.comment}", name = "${table.comment}")
public class ${table.className}Controller {

    private final ${table.className}Service ${table.className? uncap_first}Service;

    /**
     * 使用构造方法注入
     *
     * @param ${table.className? uncap_first}Service    注册${table.comment}
     */
    public ${table.className}Controller(${table.className}Service ${table.className? uncap_first}Service){
        this.${table.className? uncap_first}Service= ${table.className? uncap_first}Service;
    }

    /**
     * 保存对象
     *
     * @param ${table.className? uncap_first}Param  ${table.comment}参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "${table.comment}")
    public ResultBody save(@RequestBody ${table.className}Param ${table.className? uncap_first}Param) {
        return ResultBody.success(${table.className? uncap_first}Service.save(${table.className? uncap_first}Param));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "${table.comment}")
    public ResultBody findById(Long id) {
        return ResultBody.success(${table.className? uncap_first}Service.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "${table.comment}")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(${table.className? uncap_first}Service.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param ${table.className? uncap_first}Query 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "${table.comment}")
    public ResultBody findByList(${table.className}Query ${table.className? uncap_first}Query) {
        return ResultBody.success(${table.className? uncap_first}Service.findByList(${table.className? uncap_first}Query));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "${table.comment}")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(${table.className? uncap_first}Service.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param ${table.className? uncap_first}Query  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "${table.comment}")
    public ResultBody queryPage(${table.className}Query ${table.className? uncap_first}Query, PageParam pageParam) {
        return ResultBody.success(${table.className? uncap_first}Service.queryPage(${table.className? uncap_first}Query, pageParam));
    }

}
