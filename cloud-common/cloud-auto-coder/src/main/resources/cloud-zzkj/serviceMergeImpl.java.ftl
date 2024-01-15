package ${javaPath}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ${javaPath}.entity.${nameClass};
import ${javaPath}.param.${mergeTable.rightTableClass? cap_first}Param;
import ${javaPath}.vo.${mergeTable.rightTableClass? cap_first}Vo;
import ${javaPath}.mapper.${nameClass}Mapper;
import ${javaPath}.service.${nameClass}Service;
import ${javaPath}.service.${mergeTable.rightTableClass? cap_first}Service;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class ${nameClass}ServiceImpl implements ${nameClass}Service {

    private final ${nameClass}Mapper ${nameClass? uncap_first}Mapper;

    private final ${mergeTable.rightTableClass? cap_first}Service ${mergeTable.rightTableClass? uncap_first}Service;


    /**
     * 使用构造方法注入
     *
     * @param ${nameClass? uncap_first}Mapper ${comment}Mapper服务
     * @param ${mergeTable.rightTableClass? uncap_first}Service ${mergeTable.rightTableClass? uncap_first}Service
     */
    public ${nameClass}ServiceImpl(${nameClass}Mapper ${nameClass? uncap_first}Mapper, ${mergeTable.rightTableClass? cap_first}Service ${mergeTable.rightTableClass? uncap_first}Service){
        this.${nameClass? uncap_first}Mapper = ${nameClass? uncap_first}Mapper;
        this.${mergeTable.rightTableClass? uncap_first}Service = ${mergeTable.rightTableClass? uncap_first}Service;
    }



    /**
     * 保存对象
     *
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}   ${mergeTable.leftMergeTableColumnClass? uncap_first}
     * @param ${mergeTable.rightTableClass? uncap_first}Params ${mergeTable.rightTableClass? uncap_first}Params
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(Long ${mergeTable.leftMergeTableColumnClass? uncap_first}, List<${mergeTable.rightTableClass? cap_first}Param> ${mergeTable.rightTableClass? uncap_first}Params) {
        if (${mergeTable.leftMergeTableColumnClass? uncap_first} == null || CollectionUtils.isEmpty(${mergeTable.rightTableClass? uncap_first}Params)) {
            return null;
        }
        List<Long> ids = ${mergeTable.rightTableClass? uncap_first}Service.save(${mergeTable.rightTableClass? uncap_first}Params);
        //删除原有保存数据
        this.removeById(appealId);
        if (ids != null && !ids.isEmpty()) {
            ids.forEach(id -> {
                ${nameClass? uncap_first}Mapper.insert(new ${nameClass}(${mergeTable.leftMergeTableColumnClass? uncap_first}, id));
            });
        }
        return Boolean.TRUE;
    }


    /**
     * 通过Id获取数据
     *
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}s ${mergeTable.leftMergeTableColumnClass? uncap_first}s
     * @return 返回列表
     */
    @Override
    public Map<Long, List<${mergeTable.rightTableClass? cap_first}Vo>> findBy${mergeTable.leftMergeTableColumnClass? cap_first}s(Collection<Long> ${mergeTable.leftMergeTableColumnClass? uncap_first}s) {
        Map<Long, List<${mergeTable.rightTableClass? cap_first}Vo>> data = new HashMap<>();
        if (!${mergeTable.leftMergeTableColumnClass? uncap_first}s.isEmpty()) {
            LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(${nameClass}::get${mergeTable.leftMergeTableColumnClass? cap_first}, ${mergeTable.leftMergeTableColumnClass? uncap_first}s);
            //获取中间表数据
            List<${nameClass}> list = ${nameClass? uncap_first}Mapper.selectList(queryWrapper);
            //关联数据id
            Set<Long> ${mergeTable.rightMergeTableColumnClass? uncap_first}s = list.stream().map(${nameClass}::get${mergeTable.rightMergeTableColumnClass? cap_first}).collect(Collectors.toSet());
            //查询关联数据
            List<${mergeTable.rightTableClass? cap_first}Vo> ${mergeTable.rightTableClass? uncap_first}List = ${mergeTable.rightTableClass? uncap_first}Service.findByIds(${mergeTable.rightMergeTableColumnClass? uncap_first}s);
            Map<Long, ${mergeTable.rightTableClass? cap_first}Vo> ${nameClass? uncap_first}VoMap = ${mergeTable.rightTableClass? uncap_first}List.stream().collect(Collectors.toMap(${mergeTable.rightTableClass? cap_first}Vo::getId, ${nameClass? uncap_first} -> ${nameClass? uncap_first}, (${nameClass? uncap_first}1, ${nameClass? uncap_first}2) -> ${nameClass? uncap_first}1));
            Map<Long, List<${nameClass}>> ${nameClass? uncap_first}Map = list.stream().collect(Collectors.groupingBy(${nameClass? cap_first}::get${mergeTable.leftMergeTableColumnClass? cap_first}));
            ${nameClass? uncap_first}Map.forEach((k, v) -> {
                List<${mergeTable.rightTableClass? cap_first}Vo> ${mergeTable.rightTableClass? uncap_first}Vos = new ArrayList<>();
                for (${nameClass? cap_first} ${nameClass? uncap_first} : v) {
                    ${mergeTable.rightTableClass? cap_first}Vo ${mergeTable.rightTableClass? uncap_first}Vo = ${nameClass? uncap_first}VoMap.get(${nameClass? uncap_first}.get${mergeTable.rightMergeTableColumnClass? cap_first}());
                    if (${mergeTable.rightTableClass? uncap_first}Vo != null) {
                        ${mergeTable.rightTableClass? uncap_first}Vos.add(${mergeTable.rightTableClass? uncap_first}Vo);
                    }
                }
                data.put(k, ${mergeTable.rightTableClass? uncap_first}Vos);
            });
        }
        return data;
    }

    /**
     * 传入Id 并删除
     *
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first} ${mergeTable.leftMergeTableColumnClass? uncap_first}
     */
    private void removeById(Long ${mergeTable.leftMergeTableColumnClass? uncap_first}) {
        if (${mergeTable.leftMergeTableColumnClass? uncap_first} != null) {
            LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(${nameClass}::get${mergeTable.leftMergeTableColumnClass? cap_first}, ${mergeTable.leftMergeTableColumnClass? uncap_first});
            ${nameClass? uncap_first}Mapper.delete(queryWrapper);
        }
    }
}