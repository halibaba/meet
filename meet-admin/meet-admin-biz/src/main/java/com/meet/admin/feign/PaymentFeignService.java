package com.meet.admin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "meet-message-biz")
public interface PaymentFeignService {

    @GetMapping(value = "/message/testFeign")
    public void testFeign();

}
