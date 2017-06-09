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
public class LogoutResult {
    private boolean loggedOut;
    private String logoutFailureReason;

    public static LogoutResult userNotLoggedIn(){
        LogoutResult logoutResult = new LogoutResult();
        logoutResult.setLoggedOut(false);
        logoutResult.setLogoutFailureReason("user not logged in, so cannot logout.");
        return logoutResult;
    }

    public static LogoutResult userLoggedOut(){
        LogoutResult logoutResult = new LogoutResult();
        logoutResult.setLoggedOut(true);
        logoutResult.setLogoutFailureReason("user logged out.");
        return logoutResult;
    }
}
