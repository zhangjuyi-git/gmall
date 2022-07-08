package com.atguigu.gmall.cart.rpc;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/7/5 18:57
 */
@RestController
@RequestMapping("/rpc/inner/cart")
public class CartRpcController {


    @Autowired
    CartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param skuId sku id
     * @param num   数量
     * @return {@link Result}<{@link AddSuccessVo}>
     */
    @GetMapping("/add/{skuId}")
    public Result<AddSuccessVo> addSkuToCart(@PathVariable("skuId")Long skuId,
                                             @RequestParam("num")Integer num){

        AddSuccessVo vo = cartService.addToCart(skuId,num);
        return Result.ok(vo);
    }

    /**
     * 删除选中的购物车项
     *
     * @return {@link Result}
     */
    @GetMapping("/delete/checked")
    public Result deleteChecked(){
        cartService.deleteChecked();
        return Result.ok();
    }

    /**
     * 得到检查购物车条目
     *
     * @return {@link Result}<{@link List}<{@link CartInfo}>>
     */
    @GetMapping("/checked/items")
    public Result<List<CartInfo>> getCheckedCartItem(){

        String cartKey = cartService.determineCartKey();
        List<CartInfo> item = cartService.getAllCheckedItem(cartKey);
        return Result.ok(item);
    }

}
