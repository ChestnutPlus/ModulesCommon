package com.chestnut.Common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.chestnut.Common.R;
import com.chestnut.Common.utils.ExceptionCatchUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/8 19:33
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class XEditText extends AppCompatEditText{
    public XEditText(Context context) {
        this(context, null);
    }
    public XEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public XEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
                Typeface typeFace = Typeface.createFromAsset(context.getAssets(), fontPath);
                setTypeface(typeFace);
            }
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e,"XEditText");
        }
        //回收ta
        typedArray.recycle();
    }
}
