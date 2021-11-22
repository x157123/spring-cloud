package com.cloud.entity;

import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 菜单权限
 */
@Data
public class Menu {

	/**
     * id
     */
    private Long id;

	/**
     * 菜单名称
     */
    private String name;

	/**
     * 排序
     */
    private Integer seq;

	/**
     * 父类id
     */
    private Long nodeId;

	/**
     * 地址
     */
    private String url;

	/**
     * 外部跳转地址
     */
    private String externalUrl;
}
