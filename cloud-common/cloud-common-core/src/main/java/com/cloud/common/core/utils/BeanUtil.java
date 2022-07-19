package com.cloud.common.core.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liulei
 */
public class BeanUtil {


    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");



    /**
     * 当任意一个为null时 停止拷贝
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            BeanUtils.copyProperties(source, target);
        }
    }


    /**
     * 当任意一个为null时 停止拷贝
     *
     * @param source
     * @param target
     */
    public static <T> T copyProperties(Object source, Supplier<T> target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            T t = target.get();
            BeanUtils.copyProperties(source, t);
            return t;
        }
        return null;
    }

    /**
     * 拷贝列表
     *
     * @param sources
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        if (sources == null || sources.size() <= 0 || target == null) {
            return new ArrayList<>();
        }
        return copyListProperties(sources, target, null);
    }

    /**
     * 拷贝列表 带回调
     *
     * @param sources
     * @param target
     * @param callBack
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BiConsumer<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        if (sources != null && target != null) {
            for (S source : sources) {
                T t = copyProperties(source, target);
                if (callBack != null) {
                    // 回调
                    callBack.accept(source, t);
                }
                if (t != null) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * 为对象属性设置值
     *
     * @param obj   对象
     * @param str   对象属性中文
     * @param value 设置到对象属性中的值
     */
    public static void setProperties(Object obj, String str, Object value) {
        try {
            Field f = obj.getClass().getDeclaredField(str);
            f.setAccessible(true);
            if (obj != null && value != null) {
                f.set(obj, value);
            }
        } catch (Exception e) {

        }

    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
