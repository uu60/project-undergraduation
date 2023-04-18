package com.octopus.graduationdesign.utils;

import com.octopus.graduationdesign.properties.OProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来封装http返回信息的类
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private R() {
        put("code", OProperties.HTTP_SUCCESS);
        put("msg", "success");
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(int code) {
        R r = new R();
        r.put("code", code);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
