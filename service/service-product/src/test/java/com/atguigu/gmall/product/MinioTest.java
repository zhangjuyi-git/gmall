package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

/**
 * @Author:juyi
 * @Date:2022/6/21 19:50
 */
@SpringBootTest
public class MinioTest {

    @Autowired
    MinioClient minioClient;
    @Test
    void  testUpload() throws Exception {
        FileInputStream stream = new FileInputStream("C:\\Users\\juyi\\Desktop\\imgs\\girl2.jpg");
        PutObjectOptions options = new PutObjectOptions(stream.available(),-1L);
        options.setContentType("image/jpeg");
        minioClient.putObject("gmall","girl2.jpg",stream,options);
        }
}
