package com.chestnut.common.manager.imgloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.chestnut.common.manager.imgloader.contract.BaseImageLoaderStrategy;
import com.chestnut.common.manager.imgloader.contract.BaseImgConfig;
import com.chestnut.common.manager.imgloader.contract.ImgDownloadListener;
import com.chestnut.common.manager.imgloader.contract.ImgLoaderListener;
import com.chestnut.common.manager.imgloader.listener.ProgressInterceptor;
import com.chestnut.common.utils.FileUtils;
import com.chestnut.common.utils.StringUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
        else {
            drawableTypeRequest = requestManager.load("null");
            downloadFlag = config.url;
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
                drawableTypeRequest.transform(new CircleTransform(context));
            if (config.roundTransformDp>0)
                drawableTypeRequest.transform(new RoundTransform(context, config.roundTransformDp));

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

    @Override
    public void download(Context context, String downloadUrl, String saveFilePath, ImgDownloadListener downloadListener) {
        Context applicationContext = context.getApplicationContext();
        Observable.just(true)
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
                    //参数校验
                    if (applicationContext == null || StringUtils.isEmpty(downloadUrl) || StringUtils.isEmpty(saveFilePath)) {
                        if (downloadListener!=null)
                            downloadListener.onFail(downloadUrl, saveFilePath);
                    }
                    //下载
                    else {
                        FutureTarget<File> target = Glide.with(applicationContext)
                                .load(downloadUrl)
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        File file = target.get();
                        File dest = new File(saveFilePath);
                        if (FileUtils.copyFile(file, dest) && dest.exists() && dest.isFile()) {
                            if (downloadListener != null)
                                downloadListener.onSuccess(downloadUrl, saveFilePath);
                        }
                        else {
                            if (downloadListener!=null)
                                downloadListener.onFail(downloadUrl, saveFilePath);
                        }
                    }
                    return aBoolean;
                })
                .subscribe(aBoolean -> {}, throwable -> {
                    if (downloadListener!=null)
                        downloadListener.onFail(downloadUrl, saveFilePath);
                });
    }

    /**
     * 圆形图片，
     * 使用：
     *  Glide.with(this)
     *      .load(R.drawable.li_bao_en)
     *      .transform(new GlideUtils.CircleTransform(this))
     *      .into(circleView);
     */
    private class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }
        @Override public String getId() {
            return getClass().getName();
        }
    }

    /**
     * 圆角图片，
     * 使用：
     *  Glide.with(this)
     *      .load(R.drawable.li_bao_en)
     *      .transform(new GlideUtils.RoundTransform(this,10))
     *      .into(roundView);
     */
    private class RoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public RoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
