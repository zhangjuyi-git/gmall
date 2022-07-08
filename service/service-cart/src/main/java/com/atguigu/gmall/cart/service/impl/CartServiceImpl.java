package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.SkuFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.cart.AddSuccessVo;
import com.atguigu.gmall.model.vo.user.UserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author:juyi
 * @Date:2022/7/5 19:05
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SkuFeignClient skuFeignClient;
    
    @Override
    public AddSuccessVo addToCart(Long skuId, Integer num) {

        AddSuccessVo successVo = new AddSuccessVo();
        //1.决定适用哪个购物车
       String cartKey= determineCartKey();
       //从购物车中获取该商品  有就增加数量  没有就新增
        CartInfo item = getCartItem(cartKey,skuId);
        if (item==null){
            //没有就新增
            CartInfo info = getCartInfoFromRpc(skuId);
            //设置数量
            info.setSkuNum(num);
            //同步到redis
            saveItemToCart(info,cartKey);
            successVo.setSkuDefaultImg(info.getImgUrl());
            successVo.setSkuName(info.getSkuName());
            successVo.setId(info.getSkuId());
        }else{
            //修改数量
            item.setSkuNum(item.getSkuNum()+num);
            //同步到redis
            saveItemToCart(item,cartKey);
            successVo.setSkuDefaultImg(item.getImgUrl());
            successVo.setSkuName(item.getSkuName());
            successVo.setId(item.getSkuId());
        }

        //设置过期时间；
        setTempCartExpire();
        
        return successVo;
    }

    @Override
    public String determineCartKey() {

        //1.拿到用户信息
        UserAuth userAuth = AuthContextHolder.getUserAuth();
        String cartKey="";
        if (userAuth.getUserId()!=null){
            //用户登录了
           cartKey = RedisConst.CART_INFO_PREFIX+userAuth.getUserId();
        }else {
            //未登录 使用临时Id
            cartKey = RedisConst.CART_INFO_PREFIX+userAuth.getTempId();
        }
        return cartKey;
    }

    @Override
    public CartInfo getCartItem(String cartKey, Long skuId) {

        //1.获取一个hash对象
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        //2.获取cartkey中指定的skuId商品
        String json = ops.get(cartKey, skuId.toString());
        //3.逆转
        if (StringUtils.isEmpty(json)){
            return null;
        }else{
            CartInfo info = JSONs.toObj(json, CartInfo.class);
            return info;
        }
    }

    @Override
    public CartInfo getCartInfoFromRpc(Long skuId) {

        Result<CartInfo> info = skuFeignClient.getCartInfoBySkuId(skuId);
        CartInfo data = info.getData();
        return data;
    }

    @Override
    public void saveItemToCart(CartInfo item, String cartKey) {
        //1.拿到一个操作hash对象
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Long skuId = item.getSkuId();

        //判断购物车是否已满
        if (ops.size(cartKey) < RedisConst.CART_SIZE_LIMIT){
            //保存到redis
            ops.put(cartKey,skuId.toString(),JSONs.toStr(item));
        }else {
            throw new GmallException(ResultCodeEnum.OUT_OF_CART);
        }
    }

    @Override
    public List<CartInfo> getCartAllItem() {
        //1.是否需要合并 只要有userId 并且tempId对应购物车中有东西 就执行合并操作
        //拿到身份验证信息  判断是否有userId或tempId
        UserAuth auth = AuthContextHolder.getUserAuth();
        //判断
        if (auth.getUserId()!=null && !StringUtils.isEmpty(auth.getTempId())){
            //如果购物车中有商品就可以执行合并操作了
            Boolean hasKey = redisTemplate.hasKey(RedisConst.CART_INFO_PREFIX + auth.getTempId());
            if (hasKey) {
                //购物车中有商品执行合并操作
                //先拿到临时购物车中的商品
                List<CartInfo> cartInfos = getCartAllItem(RedisConst.CART_INFO_PREFIX + auth.getTempId());
                cartInfos.forEach(tempItem ->
                        //将临时购物车中的商品添加到用户购物车中
                        addToCart(tempItem.getSkuId(),tempItem.getSkuNum()));
                //删除临时购物车  **细节**
                redisTemplate.delete(RedisConst.CART_INFO_PREFIX+auth.getTempId());
            }
        }

        //2.查询完整的购物车信息
        String cartKey = determineCartKey();
        List<CartInfo> allItem = getCartAllItem(cartKey);

        //时事更新价格  由于使用异步操作  需要将老请求绑定到新的线程上来

        //获取老请求
        RequestAttributes oldRequest = RequestContextHolder.getRequestAttributes();

        //每一个商品查一下价格
        CompletableFuture.runAsync(() -> {
            log.info("提交一个时事改价的异步线程");
            allItem.forEach(item ->{
                //将老请求绑定到这个异步线程上
                RequestContextHolder.setRequestAttributes(oldRequest);
                //获取价格
                Result<BigDecimal> price = skuFeignClient.getSkuPrice(item.getSkuId());
                //ThreadLocal中的所有东西一定要有放   有删   这里使用了线程池   线程会被复用
                RequestContextHolder.resetRequestAttributes();
                if (!item.getSkuPrice().equals(price.getData())) {
                    log.info("正在后台实时更新 【{}】 购物车，【{}】商品的价格；原【{}】，现：【{}】",
                            cartKey,item.getSkuId(),item.getSkuPrice(),price.getData());
                    //价格发生改变
                    item.setSkuPrice(price.getData());
                    //同步到redis
                    saveItemToCart(item,cartKey);
                }
            });
        });

        return allItem;
    }

    @Override
    public List<CartInfo> getCartAllItem(String cartKey) {
        //获取redis操作hash对象
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        List<CartInfo> collect = ops.values(cartKey)
                //获取stream对象
                .stream()
                //将流对象转换为指定的javaBean对象
                .map(jsonStr -> JSONs.toObj(jsonStr, CartInfo.class))
                //指定排序规则  防止商品显示错乱  **细节**
                .sorted((pre, next) -> (int) (next.getCreateTime().getTime() - pre.getCreateTime().getTime()))
                //获取结果集 指定是list类型的结果集
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public void checkCart(Long skuId, Integer status) {
        String cartKey = determineCartKey();
        CartInfo item = getCartItem(cartKey, skuId);
        item.setIsChecked(status);

        //同步到redis
        saveItemToCart(item,cartKey);
    }

    @Override
    public void deleteCartItem(Long skuId) {

        String cartKey = determineCartKey();
        //Long(序列化)  ==  String(序列化)
        redisTemplate.opsForHash().delete(cartKey,skuId.toString());
    }

    @Override
    public void deleteChecked() {
        String cartKey = determineCartKey();
        //找到选中的商品  删除
        List<CartInfo> cartInfos = getAllCheckedItem(cartKey);
        Object[] ids = cartInfos.stream()
                .map(cartInfo -> cartInfo.getSkuId().toString())
                .toArray();
        //删除
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        ops.delete(cartKey,ids);
    }

    @Override
    public List<CartInfo> getAllCheckedItem(String cartKey) {

        HashOperations<String, String, String> ops = redisTemplate.opsForHash();

        List<CartInfo> infos = ops.values(cartKey)
                .stream()
                .map(jsonStr -> JSONs.toObj(jsonStr, CartInfo.class))
                .filter(info -> info.getIsChecked() == 1)
                .collect(Collectors.toList());
        return infos;
    }

    @Override
    public void setTempCartExpire() {

        UserAuth userAuth = AuthContextHolder.getUserAuth();
        //用户只操作临时购物车
        if (!StringUtils.isEmpty(userAuth.getTempId()) && userAuth.getUserId() ==null ){
            //用户带了临时token
            Boolean hasKey = redisTemplate.hasKey(RedisConst.CART_INFO_PREFIX + userAuth.getTempId());
            if (hasKey){
                //临时购物车设置一年有效时间
                redisTemplate.expire(RedisConst.CART_INFO_PREFIX+userAuth.getTempId(),365, TimeUnit.DAYS);
            }

        }

    }
}
