/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.model;

import lombok.Data;

/**
 * Created by Suny on 7/6/16.
 */
@Data
public class LoginResult {
    private boolean loggedIn;
    private String loginFailureReason;
    private String token;
    private User user;

    public static LoginResult userNotLoggedIn(){
        LoginResult loginResult = new LoginResult();
        loginResult.setLoggedIn(false);
        loginResult.setLoginFailureReason("user is not loggedIn.");
        return loginResult;
    }

    public static LoginResult incorrectToken(){
        LoginResult loginResult = new LoginResult();
        loginResult.setLoggedIn(false);
        loginResult.setLoginFailureReason("user token is incorrect.");
        return loginResult;
    }
}
