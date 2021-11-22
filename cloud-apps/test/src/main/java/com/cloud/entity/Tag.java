package com.cloud.entity;

import lombok.Data;

/**
 * @author liulei
 * 标签
 */
@Data
public class Tag {

	/**
     * id
     */
    private Long id;

	/**
     * 标签名称
     */
    private String name;

	/**
     * 描述
     */
    private String describe;
}
