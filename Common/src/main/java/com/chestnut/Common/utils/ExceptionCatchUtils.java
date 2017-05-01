package com.chestnut.Common.utils;

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

    private volatile static ExceptionCatchUtils mInstance;

    /**
     * 获取单例
     * @return 单例
     */
    public static ExceptionCatchUtils getInstance() {
        synchronized (ExceptionCatchUtils.class) {
            if (null == mInstance) {
                mInstance = new ExceptionCatchUtils();
            }
        }
        return mInstance;
    }

    /**
     * 捕获异常
     * @param e 异常体
     * @param TAG TAG
     * @param isLogToFile 是否需要写到日志文件中
     * @return this
     */
    public ExceptionCatchUtils catchException(Exception e, String TAG, boolean isLogToFile) {
        if (TAG==null)
            TAG = "ExceptionCatchUtils";
        if (e!=null) {
            e.printStackTrace();
            String msg = e.getMessage() == null ? "null" : e.getMessage();
            LogUtils.eD(msg,ExceptionCatchUtils.class);
            if (isLogToFile) {
                LogUtils.eToFile(TAG,msg);
            }
        }
        else {
            LogUtils.eD("Exception-Is-Null");
            if (isLogToFile) {
                LogUtils.eToFile(TAG,"Exception-Is-Null");
            }
        }
        return this;
    }
}
