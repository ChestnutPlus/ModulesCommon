package com.chestnut.common.helper.manager;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.StringRes;

import com.chestnut.common.utils.AppUtils;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.SPUtils;

import java.io.File;

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
public class XUtilsManager {

    private static String CACHE_PATH = null;
    private static Context applicationContext;

    public static void init(Context context,String SP_NAME) {
        applicationContext = context.getApplicationContext();
        //初始化缓存地址
        if (context.getExternalCacheDir()!=null) {
            CACHE_PATH = context.getExternalCacheDir().getAbsolutePath();
        }
        else if (Environment.getExternalStorageDirectory()!=null){
            CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ AppUtils.getAppPackageName(context);
            File file = new File(CACHE_PATH);
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new RuntimeException("XUtilsManager CACHE_PATH create fail...");
                }
            }
        }
        else {
            CACHE_PATH = context.getCacheDir().getAbsolutePath();
        }
        LogUtils.init();
        LogUtils.i(true,"XUtilsManager","cache_path:"+CACHE_PATH);
        SPUtils.getInstance().init(context, SP_NAME);
    }

    public static void init(Context context) {
        init(context,AppUtils.getAppPackageName(context));
    }

    public static String getCachePath() {
        return CACHE_PATH;
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static String getStringRes(@StringRes int stringRes) {
        return getApplicationContext().getString(stringRes);
    }
}
