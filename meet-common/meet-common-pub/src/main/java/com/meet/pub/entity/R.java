package com.meet.pub.entity;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Data
public class R<T> {

    private Boolean success;

    private Integer code;

    private String message;

    private T data;



    //把构造方法私有
    private R() {}

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    public static R ok(Object data) {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(20001);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R<Map<String, Object>> data(String key, Object value){
        Map<String, Object> map = new HashMap();
        map.put(key, value);
        R<Map<String, Object>> r = new R<>();
        r.setData(map);
        return r;
    }

    public R<T> data(T data){
        this.setData(data);
        return this;
    }


}
