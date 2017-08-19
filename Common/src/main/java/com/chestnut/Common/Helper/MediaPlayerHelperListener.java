package com.chestnut.Common.Helper;

import android.media.MediaPlayer;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/7 17:05
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class MediaPlayerHelperListener {
    public void onStart(MediaPlayer mediaPlayer, int allSecond){}           //开始播放的时候回调
    public void onStop(MediaPlayer mediaPlayer){}                           //播放为完成时，强制结束
    public void onReStart(MediaPlayer mediaPlayer){}                        //暂停后，开始播放
    public void onCompleted(MediaPlayer mediaPlayer){}                      //播放完成时候回调
    public void onPause(MediaPlayer mediaPlayer){}                          //未播放完成时，暂停回调
    public void onError(MediaPlayer mediaPlayer){}                          //出错时候回调
    public void onProgressChange(MediaPlayer mediaPlayer, int nowSecond){}  //回调当前的进度
}
