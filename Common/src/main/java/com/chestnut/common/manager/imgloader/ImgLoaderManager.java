package com.chestnut.common.manager.imgloader;

import android.content.Context;

import com.chestnut.common.manager.imgloader.contract.BaseImageLoaderStrategy;
import com.chestnut.common.manager.imgloader.contract.BaseImgConfig;
import com.chestnut.common.manager.imgloader.contract.BaseImgLoaderManager;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 10:23
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ImgLoaderManager implements BaseImgLoaderManager<ImgLoaderManager> {

    /*单例*/
    private static volatile ImgLoaderManager defaultInstance;
    public static ImgLoaderManager getInstance() {
        ImgLoaderManager xFontUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (ImgLoaderManager.class) {
                xFontUtils = defaultInstance;
                if (defaultInstance == null) {
                    xFontUtils = new ImgLoaderManager();
                    defaultInstance = xFontUtils;
                }
            }
        }
        return xFontUtils;
    }
    private ImgLoaderManager(){}

    private BaseImageLoaderStrategy strategy;

    @Override
    public void init(BaseImageLoaderStrategy strategy) {
        setStrategy(strategy);
    }

    @Override
    public void init() {
        setStrategy(new ImageLoaderStrategy());
    }

    @Override
    public ImgLoaderManager setStrategy(BaseImageLoaderStrategy mStrategy) {
        this.strategy = mStrategy;
        return this;
    }

    @Override
    public <Config extends BaseImgConfig> ImgLoaderManager load(Context context, Config config) {
        strategy.load(context, config);
        return this;
    }

    @Override
    public ImgLoaderManager pauseAll(Context context) {
        strategy.pauseAll(context);
        return this;
    }

    @Override
    public ImgLoaderManager resumeAll(Context context) {
        strategy.resumeAll(context);
        return this;
    }

    @Override
    public ImgLoaderManager cleanDiskCache(Context context) {
        strategy.cleanDiskCache(context);
        return this;
    }

    @Override
    public ImgLoaderManager clearMemoryCache(Context context) {
        strategy.clearMemoryCache(context);
        return this;
    }

    @Override
    public long getAllCacheSizeBytes(Context context) {
        return strategy.getAllCacheSizeBytes(context);
    }
}
