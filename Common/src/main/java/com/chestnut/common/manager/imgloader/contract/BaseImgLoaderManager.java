package com.chestnut.common.manager.imgloader.contract;

import android.content.Context;

import com.chestnut.common.manager.imgloader.ImgLoaderManager;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 12:58
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface BaseImgLoaderManager<T> {
    /**
     * 初始化，指定策略
     * @param strategy 策略
     */
    void init(BaseImageLoaderStrategy strategy);
    void init();

    /**
     * 设置策略
     * @param mStrategy 策略
     * @return T
     */
    T setStrategy(BaseImageLoaderStrategy mStrategy);

    /**
     * 加载图片
     * @param context 上下文
     * @param config 配置
     * @return T
     */
    <Config extends BaseImgConfig> ImgLoaderManager load(Context context, Config config);

    /**
     * 暂停/回复所有的任务
     * @return T
     */
    T pauseAll(Context context);
    T resumeAll(Context context);

    /**
     * 清除缓存
     * @return T
     */
    T cleanDiskCache(Context context);
    T clearMemoryCache(Context context);

    /**
     * 获取所有的缓存
     * @return T
     */
    long getAllCacheSizeBytes(Context context);
}
