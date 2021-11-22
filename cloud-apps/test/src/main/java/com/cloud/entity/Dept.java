package com.cloud.entity;

import lombok.Data;

/**
 * @author liulei
 * 部门
 */
@Data
public class Dept {

	/**
     * id
     */
    private Long id;

	/**
     * 部门名称
     */
    private String name;

	/**
     * 排序
     */
    private Integer seq;

	/**
     * 父节点
     */
    private Long nodeId;
}
