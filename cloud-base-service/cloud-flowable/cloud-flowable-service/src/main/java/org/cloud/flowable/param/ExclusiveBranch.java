package org.cloud.flowable.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExclusiveBranch {
    
    @Schema(name = "id")
    private String id;

    @Schema(name = "分支条件")
    private String condition;

    @Schema(name = "分支内部流程")
    private ProcessNode process;
}