package com.company.fastweb.core.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RedisTemplateSerializationTest extends BaseCacheIntegrationTest {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testSerializeComplexObject() {
        if (redisTemplate == null) {
            return;
        }
        String key = "ser:obj";
        User u = new User(1L, "Tom", LocalDateTime.now());
        redisTemplate.opsForValue().set(key, u);
        Object obj = redisTemplate.opsForValue().get(key);
        Assertions.assertTrue(obj instanceof User);
        Assertions.assertEquals(u, obj);
    }

    public static class User implements Serializable {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public User() {}
        public User(Long id, String name, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public Long getId() {return id;}
        public String getName() {return name;}
        public LocalDateTime getCreatedAt() {return createdAt;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(id, user.id) && Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}