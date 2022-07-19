package ${package}.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tianque.doraemon.mybatis.support.PageParam;
import ${package}.param.${table.className}Param;
import ${package}.query.${table.className}Query;
import ${package}.service.${table.className}Service;
import ${package}.vo.${table.className}Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/${table.className? uncap_first}")
@Api(value = "${table.comment}", tags = "${table.comment}")
public class ${table.className}Controller {

    private ${table.className}Service ${table.className? uncap_first}Service;

    /**
     * 使用构造方法注入
     *
     * @param ${table.className? uncap_first}Service
     */
    public ${table.className}Controller(${table.className}Service ${table.className? uncap_first}Service){
        this.${table.className? uncap_first}Service= ${table.className? uncap_first}Service;
    }

    /**
     * 保存对象
     *
     * @param ${table.className? uncap_first}Param
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "${table.comment}")
    public Boolean save(${table.className}Param ${table.className? uncap_first}Param) {
        return ${table.className? uncap_first}Service.save(${table.className? uncap_first}Param);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "${table.comment}")
    public ${table.className}Vo findById(Long id) {
        return ${table.className? uncap_first}Service.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "${table.comment}")
    public List<${table.className}Vo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ${table.className? uncap_first}Service.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param ${table.className? uncap_first}Query
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "${table.comment}")
    public List<${table.className}Vo> findByList(${table.className}Query ${table.className? uncap_first}Query) {
        return ${table.className? uncap_first}Service.findByList(${table.className? uncap_first}Query);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "${table.comment}")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ${table.className? uncap_first}Service.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param ${table.className? uncap_first}Query
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "${table.comment}")
    public IPage<${table.className}Vo> queryPage(${table.className}Query ${table.className? uncap_first}Query, PageParam pageParam) {
        return ${table.className? uncap_first}Service.queryPage(${table.className? uncap_first}Query, pageParam);
    }

}
