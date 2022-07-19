package com.cloud.common.util.db.functional;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 获取数据表
 * @author liulei
 */
@FunctionalInterface
public interface DbReadDataCallBackFun<T> {

    /**
     * 回调
     * @param t
     * @param d
     * @throws SQLException
     */
    void callBack(T t, ResultSet d) throws SQLException;
}
