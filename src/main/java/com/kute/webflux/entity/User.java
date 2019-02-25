package com.kute.webflux.entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;

/**
 * created by bailong001 on 2019/02/22 17:21
 */
public class User implements Serializable {

    private Integer id;

    private String name;

    private int age;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
        this.name = RandomStringUtils.random(11);
        this.age = RandomUtils.nextInt(10, 100);
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }
}
