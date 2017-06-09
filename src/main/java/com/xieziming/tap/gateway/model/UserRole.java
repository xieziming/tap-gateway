/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Suny on 8/13/16.
 */
@Data
@Entity
@Table(name="user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length=20, unique = true)
    private String name;

    @Column(length=100, nullable = false)
    private String remark;
}
