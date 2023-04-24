package org.cloud.flowable.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class Assignee {
    @Schema(name = "委托人列表")
    private List<String> users;

    @Schema(name = "多人审批方式")
    private String multiMode;
}