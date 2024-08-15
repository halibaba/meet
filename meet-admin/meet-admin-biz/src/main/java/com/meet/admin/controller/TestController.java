package com.meet.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meet.admin.feign.PaymentFeignService;
import com.meet.feign.MtUserInfoClient;
import com.meet.pub.entity.R;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @program: meet-boot
 * @ClassName TestController
 * @description:
 * @author: MT
 * @create: 2023-03-14 09:58
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("get1/{i}")
    public R queryPage(@PathVariable("i") Integer i){
        System.out.println("完成任务"+i);
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：" + i);
        paymentFeignService.testFeign();
        return R.ok();
    }

    @PostMapping("getTest")
    public R getTest(@RequestPart("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        return R.ok();
    }

    @GetMapping("getTest001")
    public R getTest001(){
        redisTemplate.opsForValue().get("key1");
        redisTemplate.opsForValue().set("key1", "1", 10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().get("key1");
        return R.ok();
    }

    @GetMapping("getTest002")
    public R getTest002(){
        System.out.println(56789);;

        return R.ok();
    }
}