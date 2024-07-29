package com.meet.aop;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @program: meet-boot
 * @ClassName SomeService
 * @description:
 * @author: MT
 * @create: 2022-12-09 10:02
 **/
@Service("myService")
@Data
public class SomeService {

    public String sayHello() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "ok";
    }

    @MyInner
    public void sayHello2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("userHello2");
    }

}