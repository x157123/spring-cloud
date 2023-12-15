package com.cloud.common.core.utils;

/**
 * @author liulei
 */
public class StringUtils {


    public static boolean isBlank(String source) {
        if (source == null || source.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
