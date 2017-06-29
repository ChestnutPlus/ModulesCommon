package com.chestnut.Common.utils;

import android.support.annotation.NonNull;

import com.chestnut.Common.Interface.json.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/6/29 22:49
 *     desc  :  对Json解析进行封装
 *     thanks To:
 *     dependent on:
 *     update log:
 *
 * </pre>
 */

public class JsonUtils implements Json{

    /*单例*/
    private static volatile JsonUtils defaultInstance;
    public static JsonUtils getInstance() {
        JsonUtils jsonUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (JsonUtils.class) {
                jsonUtils = defaultInstance;
                if (defaultInstance == null) {
                    jsonUtils = new JsonUtils();
                    defaultInstance = jsonUtils;
                }
            }
        }
        return jsonUtils;
    }
    private Gson gson = new Gson();

    @Override
    public <Bean> Bean getBean(@NonNull String json, @NonNull Class<Bean> beanClass) {
        try {
            return gson.fromJson(json,beanClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <Bean> List<Bean> getListBean(@NonNull String json, @NonNull Class<Bean> beanClass) {
        try {
            return gson.fromJson(json, new TypeToken<List<Bean>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
