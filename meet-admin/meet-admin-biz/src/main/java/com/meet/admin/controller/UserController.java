package com.meet.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meet.admin.service.RoleService;
import com.meet.admin.service.UserService;
import com.meet.auth.entity.SecurityUser;
import com.meet.dto.LoginUserDTO;
import com.meet.entity.User;
import com.meet.pub.entity.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/user")
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("meetUserDetailsService")
    private UserDetailsService userDetailsService;

    @ApiOperation(value = "按用户名查询登录信息")
    @GetMapping("login/{username}")
    public LoginUserDTO loginUser(@PathVariable String username) {
        SecurityUser securityUser;
        try {
            securityUser = (SecurityUser) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }
        com.meet.auth.entity.User currentUser = securityUser.getCurrentUserInfo();
        LoginUserDTO loginUser = new LoginUserDTO();
        loginUser.setUsername(currentUser.getUsername());
        loginUser.setPassword(currentUser.getPassword());
        loginUser.setNickName(currentUser.getNickName());
        loginUser.setSalt(currentUser.getSalt());
        loginUser.setToken(currentUser.getToken());
        loginUser.setPermissionValueList(securityUser.getPermissionValueList());
        return loginUser;
    }

    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
             User userQueryVo) {
        Page<User> pageParam = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username",userQueryVo.getUsername());
        }

        IPage<User> pageModel = userService.page(pageParam, wrapper);
        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public R save(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        userService.save(user);
        return R.ok();
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public R doAssign(@RequestParam String userId,@RequestParam String[] roleId) {
        roleService.saveUserRoleRealtionShip(userId,roleId);
        return R.ok();
    }
}

