package com.chestnut.common.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chestnut.common.interfaceX.tools.XFontInterface;
import com.chestnut.common.utils.ExceptionCatchUtils;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.StringUtils;

import java.lang.ref.SoftReference;
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
 *              2.  继承TextView，在新的类里面去读取设置并设置 Typeface ，其实是1的变种。
 *              3.  在Activity的onCreate中，去遍历View，找出继承于：TextView 的 View，并对其设置 Typeface
 *              4.  Android 8.0 的新特性
 *           这里，封装1，3.
 *           方法一，请看XTextView.
 *     thanks To:
 *          http://blog.csdn.net/lovefish2/article/details/46129527
 *          http://www.cnblogs.com/brainy/archive/2012/05/30/2526538.html
 *     dependent on:
 *     update log:
 *              2018年2月5日00:17:40
 *                  1. 区分：load和get,load是加载，get是获取。
 *                  2. 添加，对单个View进行的设置
 *                  3. 抽离方法，做成接口。
 *                  4. 改变类名，以后的单例都做成Tools结尾。
 *
 * </pre>
 */
public class XFontTools implements XFontInterface<XFontTools>{

    private Map<String,SoftReference<Typeface>> xSoftRefMap;
    private Map<String,Typeface> typefaceMap;
    private Context applicationContext;

    /*单例*/
    private static volatile XFontTools defaultInstance;
    public static XFontTools getInstance() {
        XFontTools xFontTools = defaultInstance;
        if (defaultInstance == null) {
            synchronized (XFontTools.class) {
                xFontTools = defaultInstance;
                if (defaultInstance == null) {
                    xFontTools = new XFontTools();
                    defaultInstance = xFontTools;
                }
            }
        }
        return xFontTools;
    }

    @Override
    public XFontTools init(Context context) {
        xSoftRefMap = new HashMap<>();
        typefaceMap = new HashMap<>();
        applicationContext = context.getApplicationContext();
        return this;
    }

    @Override
    public XFontTools loadWithStrongRef(String fontPathInAssets) {
        if (StringUtils.isEmpty(fontPathInAssets))
            throw new RuntimeException("XFontTools: the font path is null !");
        else {
            if (!typefaceMap.containsKey(fontPathInAssets))
                typefaceMap.put(fontPathInAssets,_load(fontPathInAssets));
        }
        return this;
    }

    @Override
    public XFontTools loadWithSoftRef(String fontPathInAssets) {
        if (StringUtils.isEmpty(fontPathInAssets))
            throw new RuntimeException("XFontTools: the font path is null !");
        else {
            if (!xSoftRefMap.containsKey(fontPathInAssets)) {
                SoftReference<Typeface> xSoftRef = new SoftReference<>(_load(fontPathInAssets));
                xSoftRefMap.put(fontPathInAssets, xSoftRef);
            }
            else {
                SoftReference<Typeface> typefaceSoftReference = xSoftRefMap.get(fontPathInAssets);
                if (typefaceSoftReference==null || typefaceSoftReference.get()==null) {
                    xSoftRefMap.remove(fontPathInAssets);
                    SoftReference<Typeface> xSoftRef = new SoftReference<>(_load(fontPathInAssets));
                    xSoftRefMap.put(fontPathInAssets, xSoftRef);
                }
            }
        }
        return this;
    }

    /**
     * 加载字体
     * @param fontPathInAssets 字体路径
     * @return this
     */
    private Typeface _load(String fontPathInAssets) {
        Typeface typeface = null;
        try {
            long timeMsStart = System.currentTimeMillis();
            typeface = Typeface.createFromAsset(applicationContext.getAssets(), fontPathInAssets);
            LogUtils.e(true,"XFontTools","load Font (" + fontPathInAssets + ") Ms : " + (System.currentTimeMillis()-timeMsStart));
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e);
        }
        return typeface;
    }

    @Override
    public Typeface get(String fontPathInAssets) {
        Typeface typeface;
        if (StringUtils.isEmpty(fontPathInAssets))
            throw new RuntimeException("XFontTools: the font path is null !");
        else {
            if (typefaceMap.containsKey(fontPathInAssets))
                typeface = typefaceMap.get(fontPathInAssets);
            else {
                if (!xSoftRefMap.containsKey(fontPathInAssets)) {
                    typeface = _load(fontPathInAssets);
                    SoftReference<Typeface> xSoftRef = new SoftReference<>(typeface);
                    xSoftRefMap.put(fontPathInAssets, xSoftRef);
                } else {
                    SoftReference<Typeface> typefaceSoftReference = xSoftRefMap.get(fontPathInAssets);
                    if (typefaceSoftReference == null || typefaceSoftReference.get() == null) {
                        xSoftRefMap.remove(fontPathInAssets);
                        typeface = _load(fontPathInAssets);
                        SoftReference<Typeface> xSoftRef = new SoftReference<>(typeface);
                        xSoftRefMap.put(fontPathInAssets, xSoftRef);
                    } else {
                        typeface = typefaceSoftReference.get();
                    }
                }
            }
        }
        return typeface;
    }

    @Override
    public void setActivityFont(Activity activity, String fontPathInAssets) {
        if (activity==null || fontPathInAssets==null || fontPathInAssets.length()==0) return;
        ViewGroup mContainer = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
        Typeface m = get(fontPathInAssets);
        _setViewGroupFont(mContainer,m);
    }

    @Override
    public <V extends View> void setViewFont(V view, String fontPathInAssets) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(get(fontPathInAssets));
        } else if (view instanceof ViewGroup) {
            _setViewGroupFont((ViewGroup) view, get(fontPathInAssets));
        }
    }

    private void _setViewGroupFont(ViewGroup mContainer, Typeface mFont) {
        if (mContainer == null || mFont == null) return;
        for (int i = 0; i < mContainer.getChildCount(); ++i) {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView) {
                ((TextView) mChild).setTypeface(mFont);
            } else if (mChild instanceof ViewGroup) {
                _setViewGroupFont((ViewGroup) mChild, mFont);
            }
        }
    }
}
