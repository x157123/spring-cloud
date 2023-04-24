package org.cloud.flowable.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ProcessNode {

    @Schema(name = "节点ID")
    private String id;

    @Schema(name = "节点名称")
    private String name;

    @Schema(name = "节点类型")
    private String type;

    @Schema(name = "下一节点")
    private ProcessNode next;

    @Schema(name = "分支")
    private List<ExclusiveBranch> exclusive;

    @Schema(name = "委托人")
    private Assignee assignee;

    @Schema(name = "表单权限")
    private List<FormPerm> formPerms;

}