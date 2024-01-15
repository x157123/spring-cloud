package ${javaPath}.service;

import java.util.*;
import ${javaPath}.param.${mergeTable.rightTableClass? cap_first}Param;
import ${javaPath}.vo.${mergeTable.rightTableClass? cap_first}Vo;

/**
 * @author liulei
 */
public interface ${nameClass}Service {

    /**
     * 保存对象
     *
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}   ${mergeTable.leftMergeTableColumnClass? uncap_first}
     * @param ${mergeTable.rightTableClass? uncap_first}Params ${mergeTable.rightTableClass? uncap_first}Params
     * @return 返回保存成功状态
     */
    Boolean save(Long ${mergeTable.leftMergeTableColumnClass? uncap_first}, List<${mergeTable.rightTableClass? cap_first}Param> ${mergeTable.rightTableClass? uncap_first}Params);

    /**
     * 通过Id获取数据
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}s ${mergeTable.leftMergeTableColumnClass? uncap_first}s
     * @return 返回列表
     */
    Map<Long, List<${mergeTable.rightTableClass? cap_first}Vo>> findBy${mergeTable.leftMergeTableColumnClass? cap_first}s(Collection<Long> ${mergeTable.leftMergeTableColumnClass? uncap_first}s);
}