package com.cloud.common.kafka.util;

/**
 * @author liulei
 */
public class UUID {
	public static String getUUID32() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
