package com.atguigu.gmall.front.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author:juyi
 * @Date:2022/7/8 18:56
 */
@Controller
public class OrderController {


    @Autowired
    OrderFeignClient orderFeignClient;

    /**
     * 跳转到订单确认页面
     *
     * @return {@link String}
     */
    @GetMapping("/trade.html")
    public String orderTrade(Model model){

        Result<OrderConfirmVo> orderConfirmData = orderFeignClient.getOrderConfirmData();
        OrderConfirmVo data = orderConfirmData.getData();
        //1.所有选中需要结算的商品
        model.addAttribute("detailArrayList",data.getDetailArrayList());

        //2.商品数量
        model.addAttribute("totalNum",data.getTotalNum());

        //3.商品总价
        model.addAttribute("totalAmount",data.getTotalAmount());

        //4.用户地址
        model.addAttribute("userAddressList",data.getUserAddressList());

        //5.tradeNo: 流水号(追踪号) : 防重令牌
        model.addAttribute("tradeNo",data.getTradeNo());

        return "order/trade";
    }

}
