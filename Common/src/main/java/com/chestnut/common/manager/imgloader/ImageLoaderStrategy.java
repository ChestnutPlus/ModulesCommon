package com.chestnut.common.manager.imgloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.chestnut.common.manager.imgloader.contract.BaseImageLoaderStrategy;
import com.chestnut.common.manager.imgloader.contract.BaseImgConfig;
import com.chestnut.common.manager.imgloader.contract.ImgLoaderListener;
import com.chestnut.common.manager.imgloader.listener.ProgressInterceptor;
import com.chestnut.common.utils.FileUtils;
import com.chestnut.common.utils.StringUtils;

import java.io.File;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 9:52
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ImageLoaderStrategy implements BaseImageLoaderStrategy<ImgLoaderConfig> {

    @Override
    public void load(Context context, ImgLoaderConfig config) {
        //声明宿主
        RequestManager requestManager = Glide.with(context);
        DrawableTypeRequest drawableTypeRequest = null;
        String downloadFlag = null;
        //加载图片
        if (!StringUtils.isEmpty(config.url)) {
            drawableTypeRequest = requestManager.load(config.url);
            downloadFlag = config.url;
        }
        else if (config.uri!=null) {
            drawableTypeRequest = requestManager.load(config.uri);
            downloadFlag = config.uri.toString();
        }
        else if (config.urlRes>0) {
            drawableTypeRequest = requestManager.load(config.urlRes);
            downloadFlag = String.valueOf(config.urlRes);
        }
        //加载其他配置
        if (drawableTypeRequest!=null) {
            //cache setting
            switch (config.cacheStrategy) {
                case BaseImgConfig.CACHE_ALL:
                    break;
                case BaseImgConfig.CACHE_NO_MEMORY:
                    drawableTypeRequest.skipMemoryCache(true);
                    break;
                case BaseImgConfig.CACHE_NO_MEMORY_DISK:
                    drawableTypeRequest.skipMemoryCache(true);
                    drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
            }

            //占位图
            if (config.placeholder!=null)
                drawableTypeRequest.placeholder(config.placeholder);
            else if (config.placeholderRes>0)
                drawableTypeRequest.placeholder(config.placeholderRes);

            //错误加载图
            if (config.err!=null)
                drawableTypeRequest.error(config.err);
            else if (config.errRes>0)
                drawableTypeRequest.error(config.errRes);

            //缩略图
            if (config.thumb!=null)
                drawableTypeRequest.thumbnail(Glide.with(context).load(config.thumb));
            else if (config.thumbRes>0)
                drawableTypeRequest.thumbnail(Glide.with(context).load(config.thumbRes));
            else if (config.thumbSizeMultiplier>0)
                drawableTypeRequest.thumbnail(config.thumbSizeMultiplier);

            //变换
            if (config.circleTransform)
                drawableTypeRequest.transform(new GlideUtils.CircleTransform(context));
            if (config.roundTransformDp>0)
                drawableTypeRequest.transform(new GlideUtils.RoundTransform(context, config.roundTransformDp));

            //大小
            if (config.width==BaseImgConfig.SIZE_ORIGINAL && config.length==BaseImgConfig.SIZE_ORIGINAL)
                drawableTypeRequest.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            else if (config.width>0 && config.length>0)
                drawableTypeRequest.override(config.width, config.length);

            //listener, 加载到目标上
            if (config.imgLoaderListener!=null) {
                final String tag = downloadFlag;
                ProgressInterceptor.addListener(tag, config.imgLoaderListener);
                drawableTypeRequest.into(new DrawableImageViewTarget(config.imageView) {
                    @Override
                    public void onResourceReady(Drawable resource, GlideAnimation<? super Drawable> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        ImgLoaderListener imgLoaderListener = ProgressInterceptor.getListener(tag);
                        if (imgLoaderListener!=null) {
                            imgLoaderListener.onReady(resource);
                            ProgressInterceptor.removeListener(tag);
                        }
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ImgLoaderListener imgLoaderListener = ProgressInterceptor.getListener(tag);
                        if (imgLoaderListener!=null) {
                            imgLoaderListener.onErr();
                            ProgressInterceptor.removeListener(tag);
                        }
                    }
                });
            }
            else
                drawableTypeRequest.into(config.imageView);
        }
    }

    @Override
    public void pauseAll(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resumeAll(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void cleanDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public long getAllCacheSizeBytes(Context context) {
        try {
            File internalCache = new File(context.getCacheDir() + File.separator + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
            File externalCache = new File(context.getExternalCacheDir() + File.separator + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
            if (externalCache.exists()) {
                return FileUtils.getPathSizeByte(internalCache) + FileUtils.getPathSizeByte(externalCache);
            }
            else {
                return FileUtils.getPathSizeByte(internalCache);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
