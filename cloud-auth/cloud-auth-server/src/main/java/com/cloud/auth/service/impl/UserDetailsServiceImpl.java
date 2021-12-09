package com.cloud.auth.service.impl;

import com.cloud.auth.service.UserDetailsService;

import java.util.Map;

/**
 * @author liulei
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 获取用户信息
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public Map<String, Object> getUserDetail(String userName, String password) {
        return null;
    }
}
