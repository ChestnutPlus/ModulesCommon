package com.chestnut.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.chestnut.common.R;
import com.chestnut.common.utils.ExceptionCatchUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/6/25 22:42
 *     desc  :  继承TextView，扩张其原来的功能
 *     thanks To:   http://blog.csdn.net/zte5262453/article/details/42652927
 *     dependent on:
 *     update log:
 *          1.  2017年6月25日22:50:16  19（4.4）~ 22（5.1）
 *              自定义设置，需要引入：xmlns:app="http://schemas.android.com/apk/res-auto"
 *                  app:drawableWidth="30dp"
 *                  app:drawableHeight="30dp"
 *              1）增加：设置Drawable的宽，高
 *              2）增加：设置字体：字体文件需要放置放在：<Module_Name> / main / assets / fonts / HeiTi.ttf
 *              `   通过：设置：app:fontPath="fonts/Test.TTF"
 *          2.  2017年6月27日00:12:09
 *              测试通过：4.4，5.1，SVG，直接使用即可。
 *          3.  2017年7月11日16:13:30
 *              1）修复bug:因过度加载字体资源而导致内存OOM，在内部修改成，使用软引用对字体对象进行缓存
 *                  其用法仍不变。
 *          4.  2017年8月17日10:17:33
 *              1）增加静态加载字体的方法
 * </pre>
 */

public class XTextView extends AppCompatTextView {

    private static Map<String,SoftReference<Typeface>> mapSoftReferenceTypeFaces;

    public XTextView(Context context) {
        this(context, null);
    }
    public XTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public XTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (mapSoftReferenceTypeFaces==null)
            mapSoftReferenceTypeFaces = new HashMap<>();
        //设置Drawable 的宽高
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XTextView_Drawable);
        int drawableWidth = typedArray.getDimensionPixelSize(R.styleable.XTextView_Drawable_drawableWidth, -1);
        int drawableHeight = typedArray.getDimensionPixelSize(R.styleable.XTextView_Drawable_drawableHeight, -1);
        Drawable[] drawables = getCompoundDrawables();
        Drawable textDrawable = null;
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                textDrawable = drawable;
            }
        }
        if (textDrawable != null && drawableWidth != -1 && drawableHeight != -1) {
            textDrawable.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        //设置给TextView
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        //加载字体
        String fontPath = typedArray.getString(R.styleable.XTextView_Drawable_fontPath);
        try {
            if (fontPath!=null && fontPath.length()!=0) {
                Typeface typeFace;
                if (mapSoftReferenceTypeFaces.containsKey(fontPath)) {
                    SoftReference<Typeface> softReference = mapSoftReferenceTypeFaces.get(fontPath);
                    if (softReference.get()==null) {
                        typeFace = Typeface.createFromAsset(context.getAssets(), fontPath);
                        mapSoftReferenceTypeFaces.remove(fontPath);
                        mapSoftReferenceTypeFaces.put(fontPath, new SoftReference<>(typeFace));
                    }
                    else {
                        typeFace = softReference.get();
                    }
                }
                else {
                    typeFace = Typeface.createFromAsset(context.getAssets(), fontPath);
                    mapSoftReferenceTypeFaces.put(fontPath, new SoftReference<>(typeFace));
                }
                setTypeface(typeFace);
            }
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e,"XTextView");
        }
        //回收ta
        typedArray.recycle();
    }

    /**
     * 加载字体资源
     * @param context   上下文
     * @param fontPath  fontPath
     */
    public static void loadFont(Context context, String fontPath) {
        if (mapSoftReferenceTypeFaces==null)
            mapSoftReferenceTypeFaces = new HashMap<>();
        try {
            if (fontPath!=null && fontPath.length()!=0) {
                Typeface typeFace;
                if (mapSoftReferenceTypeFaces.containsKey(fontPath)) {
                    SoftReference<Typeface> softReference = mapSoftReferenceTypeFaces.get(fontPath);
                    if (softReference.get()==null) {
                        typeFace = Typeface.createFromAsset(context.getAssets(), fontPath);
                        mapSoftReferenceTypeFaces.remove(fontPath);
                        mapSoftReferenceTypeFaces.put(fontPath, new SoftReference<>(typeFace));
                    }
                    else {
                        typeFace = softReference.get();
                    }
                }
                else {
                    typeFace = Typeface.createFromAsset(context.getAssets(), fontPath);
                    mapSoftReferenceTypeFaces.put(fontPath, new SoftReference<>(typeFace));
                }
            }
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e,"XTextView");
        }
    }
}
