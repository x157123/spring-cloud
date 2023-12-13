package ${javaPath}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ${javaPath}.entity.${nameClass};
import ${javaPath}.vo.${mergeTable.leftTableClass? cap_first}Vo;
import ${javaPath}.mapper.${nameClass}Mapper;
import ${javaPath}.service.${nameClass}Service;
import ${javaPath}.service.${mergeTable.leftTableClass? cap_first}Service;
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

    private final ${mergeTable.leftTableClass? cap_first}Service ${mergeTable.leftTableClass? uncap_first}Service;


    /**
     * 使用构造方法注入
     *
     * @param ${nameClass? uncap_first}Mapper ${comment}Mapper服务
     * @param ${mergeTable.leftTableClass? uncap_first}Service ${mergeTable.leftTableClass? uncap_first}Service
     */
    public ${nameClass}ServiceImpl(${nameClass}Mapper ${nameClass? uncap_first}Mapper, ${mergeTable.leftTableClass? cap_first}Service ${mergeTable.leftTableClass? uncap_first}Service){
        this.${nameClass? uncap_first}Mapper = ${nameClass? uncap_first}Mapper;
        this.${mergeTable.leftTableClass? uncap_first}Service = ${mergeTable.leftTableClass? uncap_first}Service;
    }


    /**
     * 保存对象
     *
     * @param ${mergeTable.rightMergeTableColumnClass? uncap_first}
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}s
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(Long ${mergeTable.rightMergeTableColumnClass? uncap_first}, List<Long> ${mergeTable.leftMergeTableColumnClass? uncap_first}s) {
        if (${mergeTable.rightMergeTableColumnClass? uncap_first} == null || CollectionUtils.isEmpty(${mergeTable.leftMergeTableColumnClass? uncap_first}s)) {
            return Boolean.FALSE;
        }
        //删除原有保存数据
        this.removeById(${mergeTable.rightMergeTableColumnClass? uncap_first});
        for (Long ${mergeTable.leftMergeTableColumnClass? uncap_first} : ${mergeTable.leftMergeTableColumnClass? uncap_first}s) {
            ${nameClass? uncap_first}Mapper.insert(new ${nameClass}(${mergeTable.rightMergeTableColumnClass? uncap_first}, ${mergeTable.leftMergeTableColumnClass? uncap_first}));
        }
        return Boolean.TRUE;
    }


    /**
     * 通过Id获取数据
     *
     * @param ${mergeTable.rightMergeTableColumnClass? uncap_first}s
     * @return 返回${mergeTable.leftTableClass? uncap_first}List列表
     */
    @Override
    public Map<Long, List<${mergeTable.leftTableClass? cap_first}Vo>> findBy${mergeTable.rightMergeTableColumnClass? cap_first}s(Collection<Long> ${mergeTable.rightMergeTableColumnClass? uncap_first}s) {
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${nameClass}::get${mergeTable.rightMergeTableColumnClass? cap_first}, ${mergeTable.rightMergeTableColumnClass? uncap_first}s);
        List<${nameClass}> list = ${nameClass? uncap_first}Mapper.selectList(queryWrapper);
        Set<Long> ${mergeTable.leftMergeTableColumnClass? uncap_first}s = list.stream().map(${nameClass}::get${mergeTable.leftMergeTableColumnClass? cap_first}).collect(Collectors.toSet());
        List<${mergeTable.leftTableClass? cap_first}Vo> ${mergeTable.leftTableClass? uncap_first}List = ${mergeTable.leftTableClass? uncap_first}Service.findByIds(${mergeTable.leftMergeTableColumnClass? uncap_first}s);
        Map<Long, List<${mergeTable.leftTableClass? cap_first}Vo>> data = new HashMap<>();
        Map<Long, ${mergeTable.leftTableClass? cap_first}Vo> ${nameClass? uncap_first}VoMap = ${mergeTable.leftTableClass? uncap_first}List.stream().collect(Collectors.toMap(${mergeTable.leftTableClass? cap_first}Vo::getId,${nameClass? uncap_first}->${nameClass? uncap_first},(${nameClass? uncap_first}1,${nameClass? uncap_first}2)->${nameClass? uncap_first}1));
        Map<Long, List<${nameClass}>> ${nameClass? uncap_first}Map = list.stream().collect(Collectors.groupingBy(${nameClass? cap_first}::get${mergeTable.rightMergeTableColumnClass? cap_first}));
        ${nameClass? uncap_first}Map.forEach((k,v)->{
            List<${mergeTable.leftTableClass? cap_first}Vo> ${mergeTable.leftTableClass? uncap_first}Vos = new ArrayList<>();
            for(${nameClass? cap_first} ${nameClass? uncap_first}: v) {
                ${mergeTable.leftTableClass? cap_first}Vo ${nameClass? uncap_first}Vo = ${nameClass? uncap_first}VoMap.get(${nameClass? uncap_first}.get${mergeTable.leftMergeTableColumnClass? cap_first}());
                if(${nameClass? uncap_first}Vo!=null) {
                    ${mergeTable.leftTableClass? uncap_first}Vos.add(${nameClass? uncap_first}Vo);
                }
            }
            data.put(k, ${mergeTable.leftTableClass? uncap_first}Vos);
        });
        return data;
    }

    /**
     * 传入Id 并删除
     *
     * @param ${mergeTable.rightMergeTableColumnClass? uncap_first}
     * @return 删除情况状态
     */
    private Boolean removeById(Long ${mergeTable.rightMergeTableColumnClass? uncap_first}) {
        if (${mergeTable.rightMergeTableColumnClass? uncap_first} == null) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<${nameClass}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(${nameClass}::get${mergeTable.rightMergeTableColumnClass? cap_first}, ${mergeTable.rightMergeTableColumnClass? uncap_first});
        ${nameClass? uncap_first}Mapper.delete(queryWrapper);
        return Boolean.TRUE;
    }
}