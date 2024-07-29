package com.meet.threadLocal;

import org.springframework.stereotype.Service;

/**
 * @program: meet-boot
 * @ClassName ThreadLocalServiceImpl
 * @description:
 * @author: MT
 * @create: 2023-04-03 09:24
 **/
@Service
public class ThreadLocalServiceImpl implements ThreadLocalService{

    @Override
    public String test(Integer userId, Integer CompanyId, String name) {
        businessA(userId, CompanyId);
        businessB(userId, CompanyId);
        return "hello";
    }

    private void businessA(Integer userId, Integer CompanyId){
        System.out.println(userId + "执行了业务A");
    }

    private void businessB(Integer userId, Integer CompanyId){
        System.out.println(CompanyId + "执行了业务B");
    }
}