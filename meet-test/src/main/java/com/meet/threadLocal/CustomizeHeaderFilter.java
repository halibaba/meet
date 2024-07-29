package com.meet.threadLocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: meet-boot
 * @ClassName CustomizeHeaderFilter
 * @description: 自定义过滤器,填充上下文
 * @author: MT
 * @create: 2023-04-03 10:10
 **/
@Slf4j
@Component
public class CustomizeHeaderFilter extends OncePerRequestFilter {

    private static final String OXY_USER_ID = "oxy-user-id";
    private static final String OXY_COMPANY_ID = "oxy-company-id";

    //请求接口时优先执行过滤器，填充上下文
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //
            String userId = request.getHeader(OXY_USER_ID);
            String companyId = request.getHeader(OXY_COMPANY_ID);

            //填充上下文
            OxyContent oxyContent = ThreadLocalUtil.getContext();
            oxyContent.setCompanyId(Integer.parseInt(companyId));
            oxyContent.setUserId(Integer.parseInt(userId));

        }catch (Exception e){
            log.error("填充上下文异常:" + e);
        }

        filterChain.doFilter(request, response);
    }
}