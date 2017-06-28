package com.chestnut.Common.Helper.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/6/28 22:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class RxMediaPlayerBean {
    public static final int ON_START = 1;       //开始播放的时候回调
    public static final int ON_RESTART = 2;     //暂停后，开始播放
    public static final int ON_COMPLETED = 3;   //播放完成时候回调
    public static final int ON_STOP = 4;        //播放为完成时，强制结束
    public static final int ON_PAUSE = 5;       //未播放完成时，暂停回调
    public static final int ON_ERROR = 6;       //出错时候回调
    @IntDef({ON_START, ON_RESTART, ON_COMPLETED,ON_STOP,ON_PAUSE,ON_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATUS {}
    public int status;
    public RxMediaPlayerBean(@STATUS int status) {
        this.status = status;
    }
}
