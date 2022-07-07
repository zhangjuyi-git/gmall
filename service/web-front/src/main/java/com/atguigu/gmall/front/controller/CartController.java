package com.atguigu.gmall.front.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:juyi
 * @Date:2022/7/5 18:41
 */
@Controller
public class CartController {

    @Autowired
    CartFeignClient cartFeignClient;


    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model) {
        Result<AddSuccessVo> result = cartFeignClient.addSkuToCart(skuId, skuNum);
        model.addAttribute("skuInfo", result.getData());
        model.addAttribute("skuNum", skuNum);
        return "cart/addCart";
    }

    /**
     * 购物车列表
     *
     * @return {@link String}
     */
    @GetMapping("/cart.html")
    public String cartList(){

        return "cart/index";
    }

    /**
     * 删除选中购物车项
     *
     * @return {@link String}
     */
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){
        cartFeignClient.deleteChecked();
        return "cart/index";
    }

}
