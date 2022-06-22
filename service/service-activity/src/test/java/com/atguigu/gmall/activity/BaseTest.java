package com.atguigu.gmall.activity;

import com.atguigu.gmall.activity.service.ActivityInfoService;
import com.atguigu.gmall.model.activity.ActivityInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author:juyi
 * @Date:2022/6/22 19:08
 */
@SpringBootTest
public class BaseTest {

    @Autowired
    ActivityInfoService activityInfoService;

    @Test
    public void baseTest(){
        List<ActivityInfo> list = activityInfoService.list();
        System.out.println("list = " + list);
    }
}
