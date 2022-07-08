package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.CartOrderDetailVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author:juyi
 * @Date:2022/7/8 19:26
 */
@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    SkuFeignClient skuFeignClient;

    @Autowired
    WareFeignClient wareFeignClient;

    @Override
    public OrderConfirmVo getOrderConfirmData() {

        OrderConfirmVo confirmVo = new OrderConfirmVo();
        //1.获取用户收货地址列表
        List<UserAddress> data = userFeignClient.getUserAddress().getData();
        confirmVo.setUserAddressList(data);

        //2.获取购物车中选中的需要结算的商品
        List<CartInfo> checkedItem = cartFeignClient.getCheckedCartItem().getData();
        //转换数据类型 CartInfo --> CartOrderDetailVo
        List<CartOrderDetailVo> detailVos = checkedItem.stream()
                .map(cartInfo -> {
                    CartOrderDetailVo detailVo = new CartOrderDetailVo();

                    //设置ImgUrl
                    detailVo.setImgUrl(cartInfo.getImgUrl());

                    //设置SkuName
                    detailVo.setSkuName(cartInfo.getSkuName());

                    //查询最新的价格
                    Result<BigDecimal> price = skuFeignClient.getSkuPrice(cartInfo.getSkuId());
                    detailVo.setOrderPrice(price.getData());

                    //设置SkuNum
                    detailVo.setSkuNum(cartInfo.getSkuNum());

                    //设置Stock
                    String stock = wareFeignClient.haStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
                    detailVo.setStock(stock);

                    return detailVo;

                }).collect(Collectors.toList());

        confirmVo.setDetailArrayList(detailVos);

        //3.总数量
        Integer totalNum = checkedItem.stream()
                .map(cartInfo -> cartInfo.getSkuNum())
                .reduce((o1, o2) -> o1 + o2)
                .get();
        confirmVo.setTotalNum(totalNum);

        //4.总金额 每个商品实时价格*数量的加和
        BigDecimal bigDecimal = detailVos.stream()
                .map(cartOrderDetailVo -> cartOrderDetailVo.getOrderPrice().multiply(new BigDecimal(cartOrderDetailVo.getSkuNum())))
                .reduce((o1, o2) -> o1.add(o2))
                .get();
        confirmVo.setTotalAmount(bigDecimal);

        //5.防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        confirmVo.setTradeNo(token);

        return confirmVo;

    }
}
