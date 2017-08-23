package com.chestnut.Common.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.chestnut.Common.Helper.XSoftRef;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/23 16:39
 *     desc  :  封装，自定义字体
 *          自定义字体，通常使用以下的方法：
 *              1.  直接对 TextView 设置 Typeface
 *              2.  集成TextView，在新的类里面去读取设置并设置 Typeface ，其实是1的变种。
 *              3.  在Activity的onCreate中，去遍历View，找出继承于：TextView 的 View，并对其设置 Typeface
 *              4.  Android 8.0
 *           这里，封装1，3.
 *           方法一，请看XTextView.
 *     thanks To:
 *     dependent on:
 *     update log:
 *
 * </pre>
 */
public class XFontUtils {

    private Map<String,XSoftRef<Typeface>> xSoftRefMap;
    private Context applicationContext;

    /*单例*/
    private static volatile XFontUtils defaultInstance;
    public static XFontUtils getInstance() {
        XFontUtils xFontUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (XFontUtils.class) {
                xFontUtils = defaultInstance;
                if (defaultInstance == null) {
                    xFontUtils = new XFontUtils();
                    defaultInstance = xFontUtils;
                }
            }
        }
        return xFontUtils;
    }

    public XFontUtils init(Context context) {
        xSoftRefMap = new HashMap<>();
        applicationContext = context.getApplicationContext();
        return this;
    }

    public XFontUtils load(String fontPathInAssets) {
        if (fontPathInAssets!=null && fontPathInAssets.length()!=0 && !xSoftRefMap.containsKey(fontPathInAssets)) {
            Typeface typeface = null;
            try {
                typeface = Typeface.createFromAsset(applicationContext.getAssets(), fontPathInAssets);
            } catch (Exception e) {
                ExceptionCatchUtils.catchE(e);
            }
            XSoftRef<Typeface> xSoftRef = new XSoftRef<>(typeface);
            xSoftRef.setNullCallBack(xSoftRef.new NullCallBack() {
                @Override
                public Typeface createNewObject() {
                    Typeface typeface = null;
                    try {
                        typeface = Typeface.createFromAsset(applicationContext.getAssets(), fontPathInAssets);
                    } catch (Exception e) {
                        ExceptionCatchUtils.catchE(e);
                    }
                    return typeface;
                }
            });
            xSoftRefMap.put(fontPathInAssets,xSoftRef);
        }
        return this;
    }
}
