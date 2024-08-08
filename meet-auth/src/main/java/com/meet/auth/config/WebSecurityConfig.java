package com.meet.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @program: meet-boot
 * @ClassName WebSecurityConfig
 * @description:
 * @author: MT
 * @create: 2022-12-05 16:44
 **/
//@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 这里的userDetailsService要和UserDetailsService类中的userDetailsService对应
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;


    /**
     * 通过配置类设置用户名密码-不推荐
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("1234");
//        auth.inMemoryAuthentication().withUser("1234").password(password).roles("admin");
//    }

    /**
     * 通过自定义实现类重写该方法实现权限校验
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * configure(WebSecurity)用于影响全局安全性(配置资源，设置调试模式，通过实现自定义防火墙定义拒绝请求)的配置设置。
     * 一般用于配置全局的某些通用事物，例如静态资源等
     * @param web
     * @throws Exception
     */
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/ignore2");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * 配置接口拦截
     * configure(HttpSecurity)允许基于选择匹配在资源级配置基于网络的安全性，也就是对角色所能访问的接口做出限制
     * @param httpSecurity 请求属性
     * @throws Exception
     */
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        //退出
////        httpSecurity.logout().logoutUrl("/logout").logoutSuccessUrl("/test/index").permitAll();
//        //报错403时跳转到自定义页面
////        httpSecurity.exceptionHandling().accessDeniedPage("/unauth.html");
//        httpSecurity.authorizeRequests()
//                // 允许get请求，而无需认证，不配置HttpMethod默认允许所有请求类型
////                .antMatchers(HttpMethod.GET, "/oauth/update2").permitAll()
//                //指定角色为admin才能访问，这里和方法注解配置效果一样，但是会覆盖注解,这里对应new GrantedAuthority()返回的ROLE_sale
////                .antMatchers("/**").hasRole("sale")
//                //指定权限为admin才能访问，这里和方法注解配置效果一样，但是会覆盖注解,这里需要和List<GrantedAuthority> auths = new ArrayList<>();
//                //        auths.add(new SimpleGrantedAuthority("admin"))对应
////                .antMatchers("/**").hasAuthority("admin")
//                //免验证的路径
//                .antMatchers("/oauth/index").permitAll()
//                // 所有请求都需要验证
//                .anyRequest().authenticated()
//                .and()
//                //.httpBasic() Basic认证，和表单认证只能选一个
//                // 使用表单认证页面
//                .formLogin()
//                //页面登陆设置
//                .loginPage("/login.html")
//                //配置登录入口，默认为security自带的页面/login
//                .loginProcessingUrl("/user/login")
//                //登陆成功之后，跳转路径
//                .defaultSuccessUrl("/oauth/index").permitAll()
//                .and();
//                // post请求要关闭csrf验证,不然访问报错；实际开发中开启，需要前端配合传递其他参数
////                .csrf().disable();
//        httpSecurity.formLogin()
//                .failureUrl("/userLogin?err");
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.logout().logoutUrl("/logout")
                        .logoutSuccessUrl("/oauth/index");
        httpSecurity.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/success.html").permitAll()
                .and().authorizeRequests()
//                .antMatchers("/oauth/index").permitAll()
//                .antMatchers("/oauth/users").hasAuthority("ROLE_user")
//                .antMatchers("/oauth/users").hasRole("user")
                .anyRequest().authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)  //设置有效时长 单位秒
                .userDetailsService(userDetailsService)
                .and().exceptionHandling().accessDeniedPage("/errDir/403.html")
                .and().csrf().disable();
    }

}