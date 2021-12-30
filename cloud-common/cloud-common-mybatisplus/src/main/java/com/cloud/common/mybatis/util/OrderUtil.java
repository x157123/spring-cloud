package com.cloud.common.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.common.mybatis.page.PageParam;
import com.sun.istack.internal.Nullable;

/**
 * @author liulei
 */
public class OrderUtil {

    private static final String ASC = "asc";

    /**
     * 获取分页条件
     *
     * @param pageParam
     * @param <T>
     * @return
     */
    public static <T> Page<T> getPage(PageParam pageParam) {
        pageParam = pageParam == null ? new PageParam() : pageParam;
        Page<T> page = new Page((long) pageParam.getPage(), (long) pageParam.getRows());
        if (hasText(pageParam.getSidx())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(pageParam.getSidx());
            if (ASC.equalsIgnoreCase(pageParam.getSord())) {
                orderItem.setAsc(Boolean.TRUE);
            } else {
                orderItem.setAsc(Boolean.FALSE);
            }
            page.addOrder(orderItem);
        }
        return page;
    }


    private static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
