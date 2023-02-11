package com.cloud.auth.service.impl;

import com.cloud.auth.dto.OauthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        OauthUser oauthUser = new OauthUser();
        oauthUser.setUsername("");
        oauthUser.setPassword("");
        return null;
    }
}
