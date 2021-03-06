/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.controller;

import com.xieziming.tap.gateway.service.AuthService;
import com.xieziming.tap.gateway.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Suny on 7/5/16.
 */
@Controller
@Slf4j
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private AuthService authService;

    @Value("${tap-authentication-enabled}")
    private boolean authenticationEnabled;

    @RequestMapping("/**")
    public ResponseEntity<?> getResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!isAuthenticated(req)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        CloseableHttpResponse httpResponse = (CloseableHttpResponse) gatewayService.getResponse(req, resp);
        IOUtils.closeQuietly(req.getInputStream());
        byte[] response = IOUtils.toByteArray(httpResponse.getEntity().getContent());

        httpResponse.close();
        return new ResponseEntity<Object>(response, HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
    }

    private boolean isAuthenticated(HttpServletRequest req){
        if(!authenticationEnabled) return true;
        Enumeration<String> headerNameEnum = req.getHeaderNames();
        String userName = null, token = null;
        while(headerNameEnum.hasMoreElements()){
            String headerName = headerNameEnum.nextElement();
            //logger.info("Header "+headerName+", value:  "+req.getHeader(headerName));
            if(headerName.matches("tap-user")){
                userName = req.getHeader(headerName);
            }

            if(headerName.matches("tap-token")){
                token = req.getHeader(headerName);
            }
        }

        if(userName != null && token != null && authService.loggedIn(userName, token).isLoggedIn())  return true;

        return false;
    }
}
