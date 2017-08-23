package com.chestnut.Common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 *          http://blog.csdn.net/lovefish2/article/details/46129527
 *          http://www.cnblogs.com/brainy/archive/2012/05/30/2526538.html
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

    /**
     * 初始化
     *  主要是保留Application引用
     *  和初始化Map
     * @param context 上下文
     * @return  this
     */
    public XFontUtils init(Context context) {
        xSoftRefMap = new HashMap<>();
        applicationContext = context.getApplicationContext();
        return this;
    }

    /**
     * 同步加载字体文件
     * @param fontPathInAssets  字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     * @return  this
     */
    public XFontUtils load(String fontPathInAssets) {
        if (fontPathInAssets!=null && fontPathInAssets.length()!=0 && !xSoftRefMap.containsKey(fontPathInAssets)) {
            XSoftRef<Typeface> xSoftRef = new XSoftRef<>(_load(fontPathInAssets));
            xSoftRef.setNullCallBack(xSoftRef.new NullCallBack() {
                @Override
                public Typeface createNewObject() {
                    LogUtils.e(true,"XFontUtils","The GC kill the font : " + fontPathInAssets + ", now reLoad !");
                    return _load(fontPathInAssets);
                }
            });
            xSoftRefMap.put(fontPathInAssets,xSoftRef);
        }
        return this;
    }

    private Typeface _load(String fontPathInAssets) {
        Typeface typeface = null;
        try {
            long timeMsStart = System.currentTimeMillis();
            typeface = Typeface.createFromAsset(applicationContext.getAssets(), fontPathInAssets);
            LogUtils.e(true,"XFontUtils","load Font (" + fontPathInAssets + ") Ms : " + (System.currentTimeMillis()-timeMsStart));
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e);
        }
        return typeface;
    }

    /**
     * 获取字体Typeface
     * @param fontPathInAssets  字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     * @return  this
     */
    public Typeface get(String fontPathInAssets) {
        if (fontPathInAssets!=null && fontPathInAssets.length()!=0) {
            if (!xSoftRefMap.containsKey(fontPathInAssets))
                load(fontPathInAssets);
            return xSoftRefMap.get(fontPathInAssets).get();
        }
        else {
            LogUtils.e(true,"XFontUtils","the font path is null !");
            return null;
        }
    }

    /**
     * 为Activity设置字体
     * @param activity   activity
     * @param fontPathInAssets    字体地址
     */
    public XFontUtils activitySetFont(Activity activity, String fontPathInAssets) {
        if (activity==null || fontPathInAssets==null || fontPathInAssets.length()==0) return this;
        ViewGroup mContainer = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
        Typeface m = get(fontPathInAssets);
        _activitySetFont(mContainer,m);
        return this;
    }

    private void _activitySetFont(ViewGroup mContainer, Typeface mFont) {
        if (mContainer == null || mFont == null) return;
        for (int i = 0; i < mContainer.getChildCount(); ++i) {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView) {
                ((TextView) mChild).setTypeface(mFont);
            } else if (mChild instanceof ViewGroup) {
                _activitySetFont((ViewGroup) mChild, mFont);
            }
        }
    }
}
