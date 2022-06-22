package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author:juyi
 * @Date:2022/6/20 19:49
 */
@EnableTransactionManagement  //开启基于注解的自动事务管理
@Import(Swagger2Config.class)
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@SpringCloudApplication
public class ProductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class,args);
    }
}
