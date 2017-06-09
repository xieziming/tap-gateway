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
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length=100, unique = true)
    private String userName;

    @Column(length=100, nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private UserRole userRole;
}
