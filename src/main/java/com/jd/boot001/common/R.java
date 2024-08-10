package com.jd.boot001.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class R extends HashMap<String, Object> implements Serializable {
    /**
     * 响应码，200：成功，其它：失败
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    public static R ok() {
        R map = new R();
        map.put(Const.CODE, 200);
        map.put(Const.MSG, "操作成功");
        return map;
    }

    public static R ok(String msg) {
        R map = new R();
        map.put(Const.CODE, 200);
        map.put(Const.MSG, msg);
        return map;
    }

    public static R success() {
        R map = new R();
        map.put(Const.CODE, 200);
        map.put(Const.MSG, "操作成功");
        return map;
    }

    public static R success(Object data) {
        R map = new R();
        map.put(Const.CODE, 200);
        map.put(Const.MSG, "操作成功");
        map.put(Const.DATA, data);
        return map;
    }

    public static R error() {
        R map = new R();
        map.put(Const.CODE, "1");
        map.put(Const.MSG, "操作失败");
        return map;
    }

    public static R error(String msg) {
        R map = new R();
        map.put(Const.CODE, "1");
        map.put(Const.MSG, "操作失败，" + msg);
        return map;
    }



    public R code(String code) {
        this.put(Const.CODE, code);
        return this;
    }

    public R msg(String msg) {
        this.put(Const.MSG, msg);
        return this;
    }

    public R data(Object obj) {
        this.put(Const.DATA, obj);
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}
