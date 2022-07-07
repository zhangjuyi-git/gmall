package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/7/5 19:04
 */
public interface CartService {
    AddSuccessVo addToCart(Long skuId, Integer num);

    String determineCartKey();

    CartInfo getCartItem(String cartKey,Long skuId);

    CartInfo getCartInfoFromRpc(Long skuId);

    void saveItemToCart(CartInfo item,String cartKey);

    List<CartInfo> getCartAllItem();

    List<CartInfo> getCartAllItem(String cartKey);

    void checkCart(Long skuId, Integer status);

    void deleteCartItem(Long skuId);

    void deleteChecked();

    List<CartInfo> getAllCheckedItem(String cartKey);

    void setTempCartExpire();
}
