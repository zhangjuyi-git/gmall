package com.atguigu.gmall.order.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.service.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:juyi
 * @Date:2022/7/8 18:12
 */
@RequestMapping("/rpc/inner/order")
@RestController
public class OrderRpcController {


    @Autowired
    OrderBizService orderBizService;

    @GetMapping("/confirm/data")
    public Result<OrderConfirmVo> getOrderConfirmData(){

        OrderConfirmVo orderConfirmVo = orderBizService.getOrderConfirmData();
        return Result.ok(orderConfirmVo);
    }

}
