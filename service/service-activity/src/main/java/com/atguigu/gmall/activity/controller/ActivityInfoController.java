package com.atguigu.gmall.activity.controller;

import com.atguigu.gmall.activity.service.ActivityInfoService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.activity.ActivityInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/22 19:18
 */
@RestController
@RequestMapping("/admin/activity/activityInfo")
public class ActivityInfoController {

    @Autowired
    ActivityInfoService activityInfoService;

    /**
     * 获得活动信息页面
     *
     * @param page  页面
     * @param limit 限制
     * @return {@link Result}
     */
    @GetMapping("/{page}/{limit}")
    public Result getActivityInfoPage(@PathVariable("page") Long page,
                                      @PathVariable("limit") Long limit){
        Page<ActivityInfo> p = new Page<>(page, limit);
        Page<ActivityInfo> page1 = activityInfoService.page(p);
        return Result.ok(page1);
    }
    /**
     * 添加活动信息
     *
     * @param activityInfo 活动信息
     * @return {@link Result}
     */
    @PostMapping(("/save"))
    public Result save(@RequestBody ActivityInfo activityInfo){
        activityInfoService.save(activityInfo);
        return Result.ok();
    }
    /**
     * 通过id获取活动
     *
     * @param activityId 活动id
     * @return {@link Result}
     */
    @GetMapping("/get/{activityId}")
    public Result getActivityById(@PathVariable("activityId")Long activityId){
        ActivityInfo activityInfo = activityInfoService.getById(activityId);
        return Result.ok(activityInfo);
    }

    /**
     * 查询活动规则列表
     *
     * @param activityRuleId 活动规则id
     * @return {@link Result}
     */
    @GetMapping("/findActivityRuleList/{activityRuleId}")
    public Result findActivityRuleList(@PathVariable("activityRuleId")Long activityRuleId){
        ActivityInfo activityInfo = activityInfoService.getById(activityRuleId);
        return Result.ok(activityInfo);
    }

    /**
     * 删除活动id
     *
     * @param activityId 活动id
     * @return {@link Result}
     */
    @DeleteMapping("/remove/{activityId}")
    public Result removeActivityById(@PathVariable("activityId")Long activityId){
        activityInfoService.removeById(activityId);
        return Result.ok();
    }


    /**
     * 批量删除
     *
     * @param list 列表
     * @return {@link Result}
     */
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Integer> list){
        activityInfoService.removeByIds(list);
        return Result.ok();
    }

    @PutMapping(("/update"))
    public Result update(@RequestBody ActivityInfo activityInfo){
        activityInfoService.updateById(activityInfo);
        return Result.ok();
    }

}
