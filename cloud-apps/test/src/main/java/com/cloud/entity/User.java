package com.cloud.entity;

import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 用户信息
 */
@Data
public class User {

	/**
     * 用户Id
     */
    private Long id;

	/**
     * 用户名
     */
    private String userName;

	/**
     * 密码
     */
    private String password;
}
