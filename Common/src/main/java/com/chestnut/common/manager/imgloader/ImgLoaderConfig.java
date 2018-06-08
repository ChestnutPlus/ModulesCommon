package com.chestnut.common.manager.imgloader;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.chestnut.common.manager.imgloader.contract.BaseImgConfig;
import com.chestnut.common.manager.imgloader.contract.ImgLoaderListener;


/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 9:49
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ImgLoaderConfig extends BaseImgConfig {

    protected ImgLoaderConfig(Builder builder) {
        this.cacheStrategy = builder.cacheStrategy;
        this.url = builder.url;
        this.urlRes = builder.urlRes;
        this.uri = builder.uri;
        this.imageView = builder.imageView;
        this.placeholderRes = builder.placeholderRes;
        this.placeholder = builder.placeholder;
        this.errRes = builder.errRes;
        this.err = builder.err;
        this.thumbRes = builder.thumbRes;
        this.thumb = builder.thumb;
        this.thumbSizeMultiplier = builder.thumbSizeMultiplier;
        this.roundTransformDp = builder.roundTransformDp;
        this.circleTransform = builder.circleTransform;
        this.imgLoaderListener = builder.imgLoaderListener;
        this.width = builder.width;
        this.length = builder.length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        //cache 策略
        protected int cacheStrategy = BaseImgConfig.CACHE_ALL;
        //图片的来源
        protected String url = null;
        protected int urlRes = -1;
        protected Uri uri = null;
        //加载的目标
        protected ImageView imageView;
        //占位图
        @DrawableRes
        protected int placeholderRes = -1;
        protected Drawable placeholder = null;
        //错误加载图
        @DrawableRes
        protected int errRes = -1;
        protected Drawable err = null;
        //缩略图
        @DrawableRes
        protected int thumbRes = -1;
        protected Drawable thumb = null;
        protected float thumbSizeMultiplier = -1f;
        //圆角,圆形
        protected int roundTransformDp = -1;
        protected boolean circleTransform = false;
        //listener
        protected ImgLoaderListener imgLoaderListener;
        //大小
        protected int width = -1;
        protected int length = -1;

        private Builder() {}

        public Builder from(String url) {
            this.url = url;
            return this;
        }

        public Builder from(@DrawableRes int urlRes) {
            this.urlRes = urlRes;
            return this;
        }

        public Builder from(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder to(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder placeholder(@DrawableRes int placeholderRes) {
            this.placeholderRes = placeholderRes;
            return this;
        }

        public Builder placeholder(Drawable placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder err(@DrawableRes int errRes) {
            this.errRes = errRes;
            return this;
        }

        public Builder err(Drawable err) {
            this.err = err;
            return this;
        }

        public Builder thumb(@DrawableRes int thumbRes) {
            this.thumbRes = thumbRes;
            return this;
        }

        public Builder thumb(Drawable thumb) {
            this.thumb = thumb;
            return this;
        }

        public Builder cacheStrategy(@BaseImgConfig.CacheStrategy int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder thumbSizeMultiplier(float thumbSizeMultiplier) {
            this.thumbSizeMultiplier = thumbSizeMultiplier;
            return this;
        }

        public Builder circleTransform(boolean circleTransform) {
            this.circleTransform = circleTransform;
            return this;
        }

        public Builder roundTransformDp(int roundTransformDp) {
            this.roundTransformDp = roundTransformDp;
            return this;
        }

        public Builder listen(ImgLoaderListener imgLoaderListener) {
            this.imgLoaderListener = imgLoaderListener;
            return this;
        }

        public Builder size(int length, int width) {
            this.length = length;
            this.width = width;
            return this;
        }

        public ImgLoaderConfig build() {
            return new ImgLoaderConfig(this);
        }
    }
}
