package com.cloud.auto.code.util;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 获取数据表
 * @author liulei
 */
@FunctionalInterface
public interface DbReadDataCallBack<T> {

    /**
     * 回调
     * @param t
     * @param d
     * @throws SQLException
     */
    void callBack(T t, ResultSet d) throws SQLException;
}
