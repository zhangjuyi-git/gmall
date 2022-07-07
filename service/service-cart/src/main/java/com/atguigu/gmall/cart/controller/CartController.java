package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/7/6 18:16
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {


    @Autowired
    CartService cartService;

    /**
     * 查询购物车列表
     *
     * @return {@link Result}
     */
    @GetMapping("/cartList")
    public Result cartList(){

        //1.查询当前用户所有购物车项 items
       List<CartInfo> items = cartService.getCartAllItem();

        return Result.ok(items);
    }


    /**
     * 添加到购物车
     *
     * @param skuId skuId
     * @param num   数量
     * @return {@link Result}
     */
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result addToCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("num")Integer num){

        AddSuccessVo vo = cartService.addToCart(skuId, num);
        return Result.ok(vo);
    }


    /**
     * 修改购物车项选中状态
     *
     * @param skuId  sku id
     * @param status 状态
     * @return {@link Result}
     */
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result checkCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("status")Integer status){
        cartService.checkCart(skuId,status);
        return Result.ok();
    }


    /**
     * 删除购物车项
     *
     * @param skuId sku id
     * @return {@link Result}
     */
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCartItem(@PathVariable("skuId")Long skuId){

        cartService.deleteCartItem(skuId);

        return Result.ok();
    }



}
