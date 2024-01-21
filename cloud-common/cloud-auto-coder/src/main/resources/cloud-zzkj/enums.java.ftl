package ${javaPath}.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 * ${enumComment}
 */
@Getter
public enum ${enumName}Enum {

<#list tmpEnums as col>
    ${col.name}("${col.value}", ${col.key}),
</#list>
    ;

    private final String label;

    private final Integer value;

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    ${enumName}Enum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public static List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for(${enumName}Enum ${enumName? uncap_first}Enum : values()){
            Map<String, Object> map = new HashMap<>();
            map.put("value",${enumName? uncap_first}Enum.getValue());
            map.put("label",${enumName? uncap_first}Enum.getLabel());
            list.add(map);
        }
        return list;
    }

    public static ${enumName}Enum contains(Integer value) {
        for (${enumName}Enum ${enumName? uncap_first}Enum : values()) {
            if (${enumName? uncap_first}Enum.value.equals(value)) {
                return ${enumName? uncap_first}Enum;
            }
        }
    <#list tmpEnums as col>
        <#if col_index == 0>
        return ${enumName}Enum.${col.name};
        </#if>
    </#list>
    }
}