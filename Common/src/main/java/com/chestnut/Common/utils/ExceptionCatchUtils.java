package com.chestnut.Common.utils;

import android.support.annotation.NonNull;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/25 16:30
 *     desc  :  异常捕抓工具类
 *     thanks To:
 *     dependent on:
 *          LogUtils
 *     update log:
 * </pre>
 */

public class ExceptionCatchUtils {

    /**
     * 捕获异常
     * @param e 异常体
     * @param TAG TAG
     * @param isLogToFile 是否需要写到日志文件中
     */
    public static void catchE(@NonNull Exception e, String TAG, boolean isLogToFile) {
        if (TAG==null)
            TAG = "ExceptionCatchUtils";
        e.printStackTrace();
        String msg = e.getMessage() == null ? "null" : e.getMessage();
        LogUtils.eD(msg,ExceptionCatchUtils.class);
        if (isLogToFile) {
            LogUtils.Config.LOG_TO_FILE = true;
            LogUtils.eToFile(TAG,msg);
        }
    }

    public static void catchE(Exception e) {
        catchE(e,"ExceptionCatchUtils",false);
    }

    public static void catchE(Exception e, String TAG) {
        catchE(e,TAG,false);
    }
}
