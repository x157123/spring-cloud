package com.cloud.entity;

import lombok.Data;

/**
 * @author liulei
 * 照片
 */
@Data
public class Img {

	/**
     * Id
     */
    private Long id;

	/**
     * 用户ID
     */
    private Long userId;

	/**
     * 图片路径
     */
    private String url;
}
