/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.controller;

import com.xieziming.tap.gateway.model.LoginResult;
import com.xieziming.tap.gateway.model.LogoutResult;
import com.xieziming.tap.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Suny on 5/31/17.
 */
@RestController
@RequestMapping("/auth")
public class AuthServiceController {
    @Autowired
    AuthService authService;

    @RequestMapping(value = "/login", method = {RequestMethod.PUT, RequestMethod.POST})
    public LoginResult login(@RequestParam String userName, @RequestParam String password){
        return authService.login(userName, password);
    }

    @RequestMapping(value = "/loggedIn", method = {RequestMethod.PUT, RequestMethod.POST})
    public LoginResult loggedIn(@RequestParam String userName, @RequestParam String token){
        return authService.loggedIn(userName, token);
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.PUT, RequestMethod.POST})
    public LogoutResult logout(@RequestParam String userName, @RequestParam String token){
        return authService.logout(userName, token);
    }
}
