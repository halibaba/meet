package com.meet.api.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Primary
@Component
public class MeetMetaObjectHandler implements MetaObjectHandler {

    @Override
    //使用mp实现添加操作，这个方法执行
    public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("createDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("createBy", "mt", metaObject);
        this.setFieldValByName("lastUpdateBy", "mt", metaObject);
		this.setFieldValByName("lastUpdateDate", LocalDateTime.now(), metaObject);
    }

    @Override
    //使用mp实现修改操作，这个方法执行
    public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("lastUpdateDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("lastUpdateBy", "mt", metaObject);
    }
}
