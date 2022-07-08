package com.atguigu.gmall.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车订单详细信息有关签证官
 *
 * @author zhangjuyi
 * @date 2022/07/08
 */
@Data
public class CartOrderDetailVo {
    //imgUrl、skuName、orderPrice、skuNum、stock
    private String imgUrl;
    private String skuName;
    private BigDecimal orderPrice;
    private Integer skuNum;
    private String stock; //1有货、无货0
}
