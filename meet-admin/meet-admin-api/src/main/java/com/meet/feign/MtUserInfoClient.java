package com.meet.feign;

import com.meet.entity.MtUserInfo;
import com.meet.pub.entity.R;
import com.meet.vo.MtUserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import sun.security.util.SecurityConstants;

/**
 * @program: meet-boot
 * @ClassName MtUserInfoClient
 * @description:
 * @author: MT
 * @create: 2022-12-06 12:16
 **/
@FeignClient(value = "meet-admin-biz")
public interface MtUserInfoClient {

    /**
     * 通过
     * @param username 用户名
     * @return R
     */
    @GetMapping("/mt-user-info/query_one/{username}")
    MtUserInfoVo queryOne(@PathVariable("username") String username);

    /**
     * 通过
     * @return R
     */
    @GetMapping("/test/get1/{i}")
    R get1(@PathVariable("i") Integer i);
}