package com.meet.threadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: meet-boot
 * @ClassName Test
 * @description:
 * @author: MT
 * @create: 2022-12-19 13:43
 **/
@RestController
public class Test {

    @Autowired
    private AsyncService asyncService;

    @RequestMapping("get1")
    public Object get1(){
        for (int i = 0; i < 10; i++) {
            asyncService.get2(i);
        }
        return null;
    }


}