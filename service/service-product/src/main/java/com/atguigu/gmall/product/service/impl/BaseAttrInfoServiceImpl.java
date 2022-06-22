package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author zhangjuyi
 * @date 2022/06/21
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    @Override
    public List<BaseAttrInfo> getAttrInfoAndValue(Long category1Id, Long category2Id, Long category3Id) {

        List<BaseAttrInfo> infos =baseAttrInfoMapper.getAttrInfoAndValue(category1Id, category2Id,category3Id);
        return infos;
    }
    @Transactional
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //保存属性名
        baseAttrInfoMapper.insert(baseAttrInfo);
        //mybatis-plus自动回填自增id到原来的JavaBean中
        Long id = baseAttrInfo.getId();

        //保存属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue value : attrValueList) {
            value.setId(id);
        }
        //批量保存属性值(含上级分类ID)
        baseAttrValueService.saveBatch(attrValueList);
    }
    @Transactional
    @Override
    public void updateAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改属性名
        baseAttrInfoMapper.updateById(baseAttrInfo);
        //修改属性值
        List<Long> ids = new ArrayList<>();
        List<BaseAttrValue> valueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue value : valueList) {
            if (value.getId() == null) {
                //新增(没带ID)
                //回填ID
                value.setAttrId(baseAttrInfo.getId());
                baseAttrValueService.save(value);
            }
            if (value.getId() != null) {
                //修改(带ID,值有变化)
                baseAttrValueService.updateById(value);
                ids.add(value.getId());
            }
        }

            //删除(带ID,删除的值)
            if (ids.size()>0){
                QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
                wrapper.eq("attr_id",baseAttrInfo.getId());
                wrapper.notIn("id",ids);
                baseAttrValueService.remove(wrapper);
            }else{
                //全删
                QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
                wrapper.eq("attr_id",baseAttrInfo.getId());
                baseAttrValueService.remove(wrapper);
            }

        }

}




