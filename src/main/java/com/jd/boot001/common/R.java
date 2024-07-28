package com.jd.boot001.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class R extends HashMap<String, Object> implements Serializable {
    /**
     * 响应码
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

    public static R success() {
        R map = new R();
        map.put(Const.CODE, "0");
        map.put(Const.MSG, "操作成功");
        return map;
    }

    public static R success(Object data) {
        R map = new R();
        map.put(Const.CODE, "0");
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
}
