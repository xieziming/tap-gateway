/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.controller;

import com.xieziming.tap.gateway.service.AuthService;
import com.xieziming.tap.gateway.service.GatewayService;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private AuthService authService;

    @RequestMapping("/**")
    public ResponseEntity<?> getResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!isAuthorized(req)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        CloseableHttpResponse httpResponse = (CloseableHttpResponse) gatewayService.getResponse(req, resp);
        IOUtils.closeQuietly(req.getInputStream());
        byte[] response = IOUtils.toByteArray(httpResponse.getEntity().getContent());

        httpResponse.close();
        return new ResponseEntity<Object>(response, HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
    }

    private boolean isAuthorized(HttpServletRequest req){
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
