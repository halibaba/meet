package com.meet.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: meet-boot
 * @ClassName MeetException
 * @description:
 * @author: MT
 * @create: 2024-08-06 21:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetException extends RuntimeException{

    private Integer code;
    private String msg;
}