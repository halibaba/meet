package com.meet.auth.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meet.pub.entity.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: meet-boot
 * @ClassName ResponseUtil
 * @description:
 * @author: MT
 * @create: 2024-08-06 22:20
 **/
public class ResponseUtil {

    public static void out(HttpServletResponse response, R r){
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}