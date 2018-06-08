package com.chestnut.common.manager.imgloader.contract;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 9:46
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class BaseImgConfig {

    //cache 策略
    public static final int CACHE_ALL = 0;//默认，内存缓存、本地缓存，从内存缓存、本地缓存获取
    public static final int CACHE_NO_MEMORY = 1;//只需本地缓存
    public static final int CACHE_NO_MEMORY_DISK = 2;//直接从网络获取
    @IntDef({CACHE_ALL, CACHE_NO_MEMORY, CACHE_NO_MEMORY_DISK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {}
    @CacheStrategy
    public int cacheStrategy = CACHE_ALL;

    //图片的来源
    public String url = null;
    public int urlRes = -1;
    public Uri uri = null;
    //加载的目标
    public ImageView imageView;
    //占位图
    @DrawableRes
    public int placeholderRes = -1;
    public Drawable placeholder = null;
    //错误加载图
    @DrawableRes
    public int errRes = -1;
    public Drawable err = null;
    //缩略图
    @DrawableRes
    public int thumbRes = -1;
    public Drawable thumb = null;
    public float thumbSizeMultiplier = -1f;
    //圆角,圆形
    public int roundTransformDp = -1;
    public boolean circleTransform = false;
    //listener
    public ImgLoaderListener imgLoaderListener;
    //大小
    public int width = -1;
    public int length = -1;
    public static final int SIZE_ORIGINAL = -111;

    protected BaseImgConfig() {}
}
