package com.meet.threadLocal;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: meet-boot
 * @ClassName ThreadLocalService
 * @description:
 * @author: MT
 * @create: 2023-04-03 09:24
 **/
public interface ThreadLocalService {

    public String test(Integer userId, Integer CompanyId, String name);
}