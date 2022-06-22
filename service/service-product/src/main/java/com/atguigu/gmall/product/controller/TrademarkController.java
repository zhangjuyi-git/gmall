package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/21 19:13
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class TrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * 获取品牌分页列表
     *
     * @param page  页面
     * @param limit 限制
     * @return {@link Result}
     */
    @GetMapping("/{page}/{limit}")
    public Result getBaseTrademark(@PathVariable("page") Long page,
                                   @PathVariable("limit") Long limit){
        Page<BaseTrademark> page1 = new Page<>(page,limit);
        Page<BaseTrademark> page2 = baseTrademarkService.page(page1);
        return Result.ok(page2);
    }


    /**
     * 保存品牌
     *
     * @param baseTrademark 基础商标
     * @return {@link Result}
     */
    @PostMapping("/save")
    public Result saveTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
     return Result.ok();
    }

    /**
     * 修改品牌
     *
     * @param baseTrademark 基础商标
     * @return {@link Result}
     */
    @PutMapping("/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 根据ID删除品牌
     *
     * @param id id
     * @return {@link Result}
     */
    @DeleteMapping("/remove/{id}")
    public Result deleteBaseTrademark(@PathVariable("id") Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    @GetMapping("/get/{id}")
    public Result getBaseTrademarkById(@PathVariable("id")Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);

    }

    /**
     * 获取品牌属性
     *
     * @return {@link Result}
     */
    @GetMapping("/getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }
}
