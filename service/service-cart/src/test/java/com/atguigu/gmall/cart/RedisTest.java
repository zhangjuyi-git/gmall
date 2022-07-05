package com.atguigu.gmall.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author:juyi
 * @Date:2022/7/5 18:15
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void testRedis(){
        redisTemplate.opsForValue().set("aaa","aaaaa1111");


        String aaa = redisTemplate.opsForValue().get("aaa");

        System.out.println("aaa = " + aaa);
    }

}
