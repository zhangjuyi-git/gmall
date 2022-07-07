package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:juyi
 * @Date:2022/7/5 20:25
 */
@FeignClient("service-cart")
@RequestMapping("/rpc/inner/cart")
public interface CartFeignClient {


    /**
     * 添加商品到购物车
     *
     * @param skuId sku id
     * @param num   数量
     * @return {@link Result}<{@link AddSuccessVo}>
     */
    @GetMapping("/add/{skuId}")
    Result<AddSuccessVo> addSkuToCart(@PathVariable("skuId")Long skuId,
                                      @RequestParam("num")Integer num);


    /**
     * 删除选中的购物车项
     *
     * @return {@link Result}
     */
    @GetMapping("/delete/checked")
    Result deleteChecked();

}
