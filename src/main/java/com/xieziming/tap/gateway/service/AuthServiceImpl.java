/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.service;

import com.xieziming.tap.gateway.model.LoginResult;
import com.xieziming.tap.gateway.model.LogoutResult;
import com.xieziming.tap.gateway.model.User;
import com.xieziming.tap.gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by Suny on 7/6/16.
 */
@Component
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public EhCacheCacheManager ehCacheManager;

    public static final String CACHE_NAME = "auth_cache";

    @Override
    public LoginResult login(String userName, String password) {
        LoginResult loginResult = new LoginResult();
        List<User> userList = userRepository.findByUserNameAndPassword(userName, password);
        if(userList.size() == 1) {
            User user = userList.get(0);
            user.setPassword(null);
            loginResult.setLoggedIn(true);
            loginResult.setUser(user);
            loginResult.setToken(UUID.randomUUID().toString());
            ehCacheManager.getCache(CACHE_NAME).put(userName, loginResult);
            log.info("User {} auth successfully and cached with token {}", userName, loginResult.getToken());
        }else {
            loginResult.setLoggedIn(false);
            loginResult.setLoginFailureReason("username or password incorrect.");
            log.info("User {} auth failed!", userName);
        }

        return loginResult;
    }

    @Override
    public LoginResult loggedIn(String userName, String token) {
        return getCachedLoginResult(userName, token);
    }

    @Override
    public LogoutResult logout(String userName, String token) {
        LoginResult loginResult = getCachedLoginResult(userName, token);

        if(loginResult.isLoggedIn()){
            ehCacheManager.getCache(CACHE_NAME).evict(userName);
            return LogoutResult.userLoggedOut();
        }else {
            return LogoutResult.userNotLoggedIn();
        }
    }

    private LoginResult getCachedLoginResult(String userName, String token){
        LoginResult loginResult = ehCacheManager.getCache(CACHE_NAME).get(userName, LoginResult.class);
        if(loginResult == null) return LoginResult.userNotLoggedIn();

        if(token != null && token.equals(loginResult.getToken())){
            return loginResult;
        }

        return LoginResult.incorrectToken();
    }
}
