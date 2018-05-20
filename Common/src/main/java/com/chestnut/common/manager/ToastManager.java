package com.chestnut.common.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;

import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.StringUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/5/16 18:00
 *     desc  :  封装了Toast，基于XToast。
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ToastManager {

    private XToast xToast;

    /*单例*/
    private static volatile ToastManager defaultInstance;
    public static ToastManager getInstance() {
        ToastManager xFontUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (ToastManager.class) {
                xFontUtils = defaultInstance;
                if (defaultInstance == null) {
                    xFontUtils = new ToastManager();
                    defaultInstance = xFontUtils;
                }
            }
        }
        return xFontUtils;
    }
    private ToastManager(){}
    private Context applicationContext;

    /**
     * 初始化
     * @param context 上下文，强列建议传入ApplicationContext
     * @param showTimeDuration toast的时长
     * @param tvSize 大小，传<=0不生效
     * @param typeface 字体
     */
    public void init(Context context, int showTimeDuration, int tvSize, Typeface typeface) {
        applicationContext = context.getApplicationContext();
        xToast = new XToast(applicationContext, showTimeDuration);
        if (typeface!=null)
            xToast.setTextTypeface(typeface);
        if (tvSize>0)
            xToast.setTextSize(tvSize);
    }

    /**
     * 释放资源
     *  释放之后，需要重新初始化
     */
    public void release() {
        applicationContext = null;
        xToast = null;
    }

    /**
     * 显示
     * @param str str
     * @param tag tag
     */
    public void toast(String str, String tag) {
        if (!StringUtils.isEmpty(str)) {
            if (!StringUtils.isEmpty(tag))
                xToast.setText(str+":("+tag+")").show();
            else
                xToast.setText(str).show();
        }
    }

    public void toast(@StringRes int strRes) {
        toast(applicationContext.getString(strRes),null);
    }

    public void toast(@StringRes int strRes, String tag) {
        toast(applicationContext.getString(strRes),tag);
    }

    public void toast(String str) {
        toast(str,null);
    }
}
