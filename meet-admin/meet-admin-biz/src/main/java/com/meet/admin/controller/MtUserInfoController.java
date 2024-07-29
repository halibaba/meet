package com.meet.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meet.admin.service.IMtUserInfoService;
import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.pub.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

/**
 * <p>
 * 用户基本信息表 前端控制器
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
@RestController
@RequestMapping("/mt-user-info")
public class MtUserInfoController {

    @Autowired
    private IMtUserInfoService mtUserInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("query_page")
    public R queryPage(Page page, MtUserInfoDTO mtUserInfo){
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("++++++++++++++++++"+now);

        System.out.println(httpServletRequest.getRequestURI());
        return R.ok(mtUserInfoService.queryPage(page, mtUserInfo));
    }

    @GetMapping("page")
    public R queryPage(){
        return R.ok(mtUserInfoService.page(new Page<>(), new LambdaQueryWrapper<MtUserInfo>()));
    }

    @PostMapping("insert")
    public R insert(@RequestBody MtUserInfoDTO dto){
        return R.ok(mtUserInfoService.insert(dto));
    }

    @PostMapping("update")
    public R update(@RequestBody MtUserInfoDTO dto){
        return R.ok(mtUserInfoService.update(dto));
    }

    @GetMapping("query_one/{id}")
    public R queryOne(@PathVariable String id){
        return R.ok(mtUserInfoService.getById(id));
    }
}

