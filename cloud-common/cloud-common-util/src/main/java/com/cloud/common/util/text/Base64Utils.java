package com.cloud.common.util.text;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

    /**
     *
     * @param str 字符串
     * @return  返回编码后的结果
     */
    public static String encode(String str) {
        return encode(str.getBytes());
    }

    /**
     *
     * @param bytes 字节
     * @return  返回编码后的结果
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * 解码
     * @param str 字符串
     * @return 返回编码前的结果
     */
    public static String decode(String str) {
        return decode(str.getBytes());
    }

    /**
     * 解码
     * @param bytes 字节
     * @return 返回编码前的结果
     */
    public static String decode(byte[] bytes) {
        return new String(Base64.decodeBase64(bytes));
    }

}
