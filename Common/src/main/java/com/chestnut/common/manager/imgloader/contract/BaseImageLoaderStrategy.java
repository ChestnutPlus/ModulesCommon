package com.chestnut.common.manager.imgloader.contract;

import android.content.Context;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 9:48
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface BaseImageLoaderStrategy<T extends BaseImgConfig> {
    /**
     * 加载图片
     * @param context 上下文
     * @param config 配置
     * @param T 配置着
     */
    void load(Context context, T config);

    /**
     * 暂停/回复所有的任务
     */
    void pauseAll(Context context);
    void resumeAll(Context context);

    /**
     * 清除缓存
     */
    void cleanDiskCache(Context context);
    void clearMemoryCache(Context context);

    /**
     * 获取所有的缓存
     */
    long getAllCacheSizeBytes(Context context);
}
