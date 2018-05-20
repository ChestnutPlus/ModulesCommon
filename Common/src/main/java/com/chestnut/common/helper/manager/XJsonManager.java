package com.chestnut.common.helper.manager;

import android.support.annotation.NonNull;

import com.chestnut.common.contract.json.JsonInterface;
import com.chestnut.common.utils.ExceptionCatchUtils;
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

public class XJsonManager implements JsonInterface<Gson> {

    /*单例*/
    private static volatile XJsonManager defaultInstance;
    public static XJsonManager getInstance() {
        XJsonManager jsonUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (XJsonManager.class) {
                jsonUtils = defaultInstance;
                if (defaultInstance == null) {
                    jsonUtils = new XJsonManager();
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
            ExceptionCatchUtils.catchE(e,"XJsonManager");
            return null;
        }
    }

    @Override
    public <Bean> List<Bean> getListBean(@NonNull String json, @NonNull Class<Bean> beanClass) {
        try {
            //List<BannerBean> bannerBeanList = new Gson().fromJson(s,new TypeToken<List<BannerBean>>(){}.getType());
            return gson.fromJson(json, new TypeToken<List<Bean>>(){}.getType());
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e,"XJsonManager");
            return null;
        }
    }

    @Override
    public <Bean> String toJson(@NonNull Bean bean) {
        return gson.toJson(bean);
    }

    @Override
    public Gson getFactory() {
        return gson;
    }
}
