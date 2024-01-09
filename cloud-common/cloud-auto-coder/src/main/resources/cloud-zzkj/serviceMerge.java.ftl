package ${javaPath}.service;


import java.util.*;
import ${javaPath}.vo.${mergeTable.leftTableClass? cap_first}Vo;

/**
 * @author liulei
 */
public interface ${nameClass}Service {

    /**
     * 保存对象
     *
     * @param ${mergeTable.rightMergeTableColumnClass? uncap_first}
     * @param ${mergeTable.leftMergeTableColumnClass? uncap_first}s
     * @return 返回保存成功状态
     */
    Boolean save(Long ${mergeTable.rightMergeTableColumnClass? uncap_first}, List<Long> ${mergeTable.leftMergeTableColumnClass? uncap_first}s);

    /**
     * 通过Id获取数据
     * @param ${mergeTable.rightMergeTableColumnClass? uncap_first}s
     * @return 返回列表
     */
    Map<Long, List<${mergeTable.leftTableClass? cap_first}Vo>> findBy${mergeTable.rightMergeTableColumnClass? cap_first}s(Collection<Long> ${mergeTable.rightMergeTableColumnClass? uncap_first}s);
}