package com.atguigu.gmall.feign.ware;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:juyi
 * @Date:2022/7/8 20:56
 */
//value 必须配置
@FeignClient(url = "http://localhost:9001",value = "ware-manage")
public interface WareFeignClient {

    @GetMapping("/hasStock")
    public String haStock(@RequestParam("skuId")Long skuId,
                          @RequestParam("num")Integer num);

}
