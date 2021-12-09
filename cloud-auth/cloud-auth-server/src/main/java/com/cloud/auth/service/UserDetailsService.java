package com.cloud.auth.service;

import java.util.Map;

/**
 * @author liulei
 */
public interface UserDetailsService {

    /**
     * 获取用户信息
     * @param userName
     * @param password
     * @return
     */
    Map<String,Object> getUserDetail(String userName, String password);

}
