package com.cloud.common.core.utils;

import java.util.Random;

/**
 * @author liulei
 */
public class DataVersionUtils {

    private static Random random = new Random();

    /**
     * 获取数据版本编号
     * @return
     */
    public static Integer next() {
        int num = random.nextInt(9999999);
        return (num < 1000000 ? num + 1000000 : num);
    }

}
