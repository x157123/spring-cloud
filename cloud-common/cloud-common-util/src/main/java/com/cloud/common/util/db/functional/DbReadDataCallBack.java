package com.cloud.common.util.db.functional;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 获取数据表
 * @author liulei
 */
@FunctionalInterface
public interface DbReadDataCallBack<S, T> {

    /**
     * 回调
     * @param t
     * @param s
     * @param d
     * @throws SQLException
     */
    void callBack(S t, T s, ResultSet d) throws SQLException;
}
