package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:juyi
 * @Date:2022/6/22 16:33
 */
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * 添加sku
     *
     * @return {@link Result}
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }


    /**
     * 获取sku分页列表
     *
     * @param page  页面
     * @param limit 限制
     * @return {@link Result}
     */
    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable("page") Long page,
                       @PathVariable("limit") Long limit){
        Page<SkuInfo> skuInfoPage = new Page<>(page,limit);
        Page<SkuInfo> page1 = skuInfoService.page(skuInfoPage);
        return Result.ok(page1);
    }


    /**
     * 上架
     *
     * @param skuId sku_id
     * @return {@link Result}
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")Long skuId){
        skuInfoService.onSale(skuId);
        return Result.ok();
    }

    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")Long skuId){
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }

}
