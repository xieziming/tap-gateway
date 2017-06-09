/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Suny on 5/31/17.
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String health(){
        return "{\"status\" : \"ok\"}";
    }
}
