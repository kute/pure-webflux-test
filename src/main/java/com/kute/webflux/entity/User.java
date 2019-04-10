package com.kute.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * created by bailong001 on 2019/02/22 17:21
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "test")
public class User implements Serializable {

    @Id
    private String id;

    private Long ucId;

    /**
     * 若mongo不存在此索引会自动创建
     */
    @Indexed(unique = true)
    private String name;

    private int age;

    /**
     * spring data mongo 会内置一个 org.bson.BsonTimestamp -> java.time.Instant 的converter，
     * 可以替换为 Instant就不必注册新的convert，这里为了演示 新添加 convert
     */
    private Timestamp birthday;

    public static User randomUser(String id) {
        return new User(id, RandomUtils.nextLong(1L, 1000L),
                RandomStringUtils.random(11), RandomUtils.nextInt(10, 100),
                new Timestamp(System.currentTimeMillis()));
    }

}
