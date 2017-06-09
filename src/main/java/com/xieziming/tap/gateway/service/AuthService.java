/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.service;

import com.xieziming.tap.gateway.model.LoginResult;
import com.xieziming.tap.gateway.model.LogoutResult;

/**
 * Created by Suny on 7/6/16.
 */
public interface AuthService {
    LoginResult login(String userName, String password);
    LogoutResult logout(String userName, String token);
    LoginResult loggedIn(String userName, String token);
}
