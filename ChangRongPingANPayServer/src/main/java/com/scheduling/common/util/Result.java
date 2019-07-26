package com.scheduling.common.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private int code = 1;
    private String msg = "Success !!";
    private String msg_cn = "操作成功！";
    private Object data = "null";

    public Result() {
    }

    public Result(int code, String msg, String msg_cn, Object data) {
        this.code = code;
        this.msg = msg;
        this.msg_cn = msg_cn;
        this.data = data;
    }

    public Result success(String msg_cn) {
        this.msg_cn = msg_cn;
        return this;
    }

    public Result success(String loginfo,String msg_cn) {
        if (null!=loginfo && !"".equals(loginfo)){
        }
        this.msg_cn = msg_cn;
        return this;
    }


    public Result failed(String msg_cn) {
        this.code = 0;
        this.msg = "Error !!";
        this.msg_cn = msg_cn;
        if (null!=msg_cn && !"".equals(msg_cn)){
        }
        return this;
    }

    public Result failed(String msg, String msg_cn) {
        this.code = 0;
        this.msg = msg;
        this.msg_cn = msg_cn;
        return this;
    }

    public Result error(Exception e) {
        this.code = 0;
        this.msg = "Error !!";
        if (e != null) {
            this.msg_cn = "系统错误！错误信息：" + e.getMessage();
        }
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg_cn() {
        return msg_cn;
    }

    public Result setMsg_cn(String msg_cn) {
        this.msg_cn = msg_cn;
        return this;
    }

    public Object getData() {
        return data;
    }

    public <T> T getDataParseEntity(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this.data), clazz);
    }

    public <T> List<T> getDataParseList(Class<T> clazz) {
        List data = (List<T>) this.data;
        List<T> result = new ArrayList<>();
        data.forEach(t -> result.add(JSON.parseObject(JSON.toJSONString(t), clazz)));
        return result;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
