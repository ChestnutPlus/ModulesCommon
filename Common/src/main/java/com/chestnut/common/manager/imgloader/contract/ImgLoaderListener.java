package com.chestnut.common.manager.imgloader.contract;

import android.graphics.drawable.Drawable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 15:24
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class ImgLoaderListener {
    public void onReady(Drawable drawable){}
    public void onErr(){}
    public void onProgress(int progress){}
}
