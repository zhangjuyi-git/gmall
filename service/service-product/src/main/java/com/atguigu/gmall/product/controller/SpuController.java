package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/22 14:09
 */
@RestController
@RequestMapping("/admin/product")
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;
    @Autowired
    BaseSaleAttrService baseSaleAttrService;
    @Autowired
    SpuImageService spuImageService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    /**
     * 获取spu分页列表
     *
     * @param page        页面
     * @param limit       限制
     * @param category3Id category3 id
     * @return {@link Result}
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuInfo(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit,
                             @RequestParam("category3Id") Long category3Id){
        Page<SpuInfo> spuInfoPage = new Page<>(page, limit);
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);
        IPage<SpuInfo> page1 = spuInfoService.page(spuInfoPage);
        return Result.ok(page1);
    }

    /**
     * 获取销售属性
     *
     * @return {@link Result}
     */
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList(){
        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);
    }

    /**
     * 添加spu
     *
     * @return {@link Result}
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 根据spuId获取图片列表
     *
     * @param spuId spu id
     * @return {@link Result}
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId){
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }

    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId")Long spuId){
       List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrService.spuSaleAttrList(spuId);
       return Result.ok(spuSaleAttrs);
    }

}
