package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:juyi
 * @Date:2022/6/21 21:19
 */
public interface FileService {
    String upload(MultipartFile file) throws Exception;
}
