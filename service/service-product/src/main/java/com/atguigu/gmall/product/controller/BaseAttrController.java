package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/21 16:13
 */
@Slf4j
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {
    @Autowired
    BaseAttrInfoService baseAttrInfoService;
    @Autowired
    BaseAttrValueService baseAttrValueService;
    /**
     * 查询分类下的所有属性名和值
     *
     * @param category1Id category1 一级分类ID
     * @param category2Id category2 二级分类ID
     * @param category3Id category3 三级分类ID
     * @return {@link Result}
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getAttrInfoList(@PathVariable("category1Id") Long category1Id,
                                  @PathVariable("category2Id") Long category2Id,
                                  @PathVariable("category3Id") Long category3Id){
       List<BaseAttrInfo> infos = baseAttrInfoService.getAttrInfoAndValue(category1Id,category2Id,category3Id);
        return Result.ok(infos);
    }
    /**
     * 保存平台属性/修改二合一
     *
     * @param baseAttrInfo 封装前端返回的数据
     * @return {@link Result}
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        if (baseAttrInfo.getId()!=null){
            //有就修改
            baseAttrInfoService.updateAttrInfo(baseAttrInfo);
        }else{
            //没有就添加
            baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        }
        return Result.ok();
    }
    /**
     * 根据平台属性ID获取平台属性对象数据
     *
     * @param attrId attr id
     * @return {@link Result}
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueListById(@PathVariable Long attrId){
        List<BaseAttrValue> attrValues = baseAttrValueService.getAttrValueListById(attrId);
        return Result.ok(attrValues);
    }

}
