package com.cloud.entity;

import lombok.Data;

/**
 * @author liulei
 * 用户详情
 */
@Data
public class UserDetail {

	/**
     * Id
     */
    private Long id;

	/**
     * 用户Id
     */
    private Long userId;

	/**
     * 用户名字
     */
    private String name;

	/**
     * 年龄
     */
    private Integer age;
}
