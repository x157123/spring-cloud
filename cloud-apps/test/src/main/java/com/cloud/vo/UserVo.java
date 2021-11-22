package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import com.cloud.vo.ImgVo;
import com.cloud.vo.UserDetailVo;
import com.cloud.vo.PermissionsVo;
import com.cloud.vo.TagVo;
import com.cloud.vo.DeptVo;

/**
 * @author liulei
 * 用户信息
 */
@Data
@ApiModel(value = "用户信息响应对象", description = "用户信息响应对象")
public class UserVo {

	/**
     * 用户Id
     */
    @ApiModelProperty(value = "用户信息用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 用户名
     */
    @ApiModelProperty(value = "用户信息用户名")
    private String userName;

	/**
     * 密码
     */
    @ApiModelProperty(value = "用户信息密码")
    private String password;

	/**
     * 照片
     */
    @ApiModelProperty(value = "用户信息照片")
    private List<ImgVo> imgVOList;

	/**
     * 用户详情
     */
    @ApiModelProperty(value = "用户信息用户详情")
    private List<UserDetailVo> userDetailVOList;

    /**
    * 权限
    */
    @ApiModelProperty(value = "用户信息权限")
    private List<PermissionsVo> permissionsVOList;

    /**
    * 标签
    */
    @ApiModelProperty(value = "用户信息标签")
    private List<TagVo> tagVOList;

    /**
    * 部门
    */
    @ApiModelProperty(value = "用户信息部门")
    private List<DeptVo> deptVOList;
}
