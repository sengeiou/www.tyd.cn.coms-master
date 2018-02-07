package com.touedian.com.facetyd.utilsx;

import com.google.gson.Gson;

/**
 *
 * Author:Administrator
 * Date:2018/newhomepageone/5
 * 佛祖保佑       永无BUG     永不修改
 */

public class GsonUtil {

    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义
     */
    private GsonUtil() {
        throw new AssertionError();
    }

    private static class GsonBuilder {
        public static Gson gson = new Gson();
    }

    private static Gson getGsonInstance() {
        return GsonBuilder.gson;
        //return null;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return getGsonInstance().fromJson(json, classOfT);
        //return null;

    }

    public static String toJson(Object src) {
        return getGsonInstance().toJson(src);
        //return null;

    }
}