package com.atguigu.gmall.activity.controller;

import com.atguigu.gmall.activity.service.CouponInfoService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.activity.CouponInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/22 19:18
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Autowired
    CouponInfoService couponInfoService;

    /**
     * 获得活动信息页面
     *
     * @param page  页面
     * @param limit 限制
     * @return {@link Result}
     */
    @GetMapping("/{page}/{limit}")
    public Result getCouponInfoPage(@PathVariable("page") Long page,
                                      @PathVariable("limit") Long limit){
        Page<CouponInfo> p = new Page<>(page, limit);
        Page<CouponInfo> page1 = couponInfoService.page(p);
        return Result.ok(page1);
    }

    @PostMapping(("/save"))
    public Result save(@RequestBody CouponInfo couponInfo){
        couponInfoService.save(couponInfo);
        return Result.ok();
    }

    @GetMapping("/get/{couponInfoId}")
    public Result getCouponById(@PathVariable("couponInfoId")Long couponInfoId){
        CouponInfo couponInfo = couponInfoService.getById(couponInfoId);
        return Result.ok(couponInfo);
    }

    /*//http://192.168.200.1/admin/activity/couponInfo/findCouponRuleList/7
    @GetMapping("/findCouponRuleList/{activityRuleId}")
    public Result findCouponRuleList(@PathVariable("activityRuleId")Long activityRuleId){
        ActivityInfo activityInfo = couponInfoService.getById(activityRuleId);
        return Result.ok(activityInfo);
    }*/

    @DeleteMapping("/remove/{couponInfoId}")
    public Result removeCouponById(@PathVariable("couponInfoId")Long couponInfoId){
        couponInfoService.removeById(couponInfoId);
        return Result.ok();
    }

    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Integer> list){
        couponInfoService.removeByIds(list);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param couponInfo 优惠券信息
     * @return {@link Result}
     */
    @PutMapping(("/update"))
    public Result update(@RequestBody CouponInfo couponInfo){
        couponInfoService.updateById(couponInfo);
        return Result.ok();
    }
}
