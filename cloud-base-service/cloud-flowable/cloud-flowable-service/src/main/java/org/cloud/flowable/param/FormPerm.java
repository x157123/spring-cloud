package org.cloud.flowable.param;

import io.swagger.v3.oas.annotations.media.Schema;

public class FormPerm {

    @Schema(name = "字段key")
    private String key;

    @Schema(name = "权限类型 编辑/只读/隐藏")
    private String perm;

    @Schema(name = "是否必须")
    private Boolean required;
}
