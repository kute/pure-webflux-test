package com.kute.webflux.entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * created by bailong001 on 2019/02/22 17:21
 */
@Document
public class User implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private int age;

    public User() {
    }

    public User(String id) {
        this.id = id;
        this.name = RandomStringUtils.random(11);
        this.age = RandomUtils.nextInt(10, 100);
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
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
