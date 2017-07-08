package com.chestnut.Common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/6 10:13
 *     desc  :  工具类的管理者，请在Application中初始化
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class UtilsManager {

    private static String CACHE_PATH = null;
    private static String TAG = "UtilsManager";

    public static void init(Context c,String SP_NAME) {
        Observable.just(c.getApplicationContext())
                .observeOn(Schedulers.newThread())
                .subscribe(context -> {
                    //初始化缓存地址
                    if (context.getExternalCacheDir()!=null) {
                        CACHE_PATH = context.getExternalCacheDir().getAbsolutePath();
                    }
                    else if (Environment.getExternalStorageDirectory()!=null){
                        CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+AppUtils.getAppPackageName(context);
                        File file = new File(CACHE_PATH);
                        if (!file.exists()) {
                            if (!file.mkdir()) {
                                throw new RuntimeException("UtilsManager CACHE_PATH create fail...");
                            }
                        }
                    }
                    else {
                        CACHE_PATH = context.getCacheDir().getAbsolutePath();
                    }
                    LogUtils.i(true,"cache_path:"+CACHE_PATH);
                    //初始化LogUtils
                    LogUtils.init(context.getApplicationContext());
                    //初始化SPUtils
                    SPUtils.getInstance().init(context, SP_NAME);
                },throwable -> LogUtils.e(true,"init-error:"+throwable.getMessage()));
    }

    public static void init(Context context) {
        init(context,AppUtils.getAppPackageName(context));
    }

    public static String getCachePath() {
        LogUtils.i(true,"cache_path:"+CACHE_PATH);
        return CACHE_PATH;
    }
}
