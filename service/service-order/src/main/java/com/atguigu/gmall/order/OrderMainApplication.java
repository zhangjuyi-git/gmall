package com.atguigu.gmall.order;

import com.atguigu.gmall.common.annotation.EnableFeignInterceptor;
import com.atguigu.gmall.common.annotation.EnableThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author:juyi
 * @Date:2022/7/8 16:41
 */
@EnableThreadPool
@SpringCloudApplication
@MapperScan(basePackages = "com.atguigu.gmall.order.mapper")
@EnableFeignInterceptor
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign.user",
        "com.atguigu.gmall.feign.cart",
        "com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.ware"
})
public class OrderMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class,args);
    }
}
