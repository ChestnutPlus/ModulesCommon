package com.chestnut.Common.Helper;

import android.media.MediaPlayer;

import com.chestnut.Common.utils.ExceptionCatchUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/25 15:48
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class MediaPlayerHelper {

    /*  变量/常量
    * */
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private MediaPlayer mediaPlayer = null;
    private CallBack callBack = null;
    private final String TAG = "MediaPlayerHelper";
    private String url = null;

    /*  方法
    * */
    public MediaPlayerHelper() {
        mediaPlayer = new MediaPlayer();
        MediaPlayer.OnPreparedListener onPreparedListener = mp -> {
            if (callBack!=null)
                callBack.onStart();
            singleThreadExecutor.execute(() -> mediaPlayer.start());
        };
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
            if (callBack!=null)
                callBack.onCompleted();
            mediaPlayer.reset();
        };
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        MediaPlayer.OnErrorListener onErrorListener = (mediaPlayer1, i, i1) -> {
            if (callBack != null)
                callBack.onError();
            return false;
        };
        mediaPlayer.setOnErrorListener(onErrorListener);
    }

    public MediaPlayerHelper setUrl(String url) {
        this.url = url;
        return this;
    }

    public MediaPlayerHelper play() {
        play(null);
        return this;
    }

    public MediaPlayerHelper play(CallBack callBack) {
        this.callBack = callBack;
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            ExceptionCatchUtils.getInstance().catchException(e,TAG,false);
        }
        return this;
    }

    public MediaPlayerHelper rePlay() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if (callBack!=null)
                callBack.onRestart();
        }
        return this;
    }

    public MediaPlayerHelper stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            if (callBack!=null)
                callBack.onStop();
        }
        mediaPlayer.reset();
        return this;
    }

    public MediaPlayerHelper pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (callBack!=null)
                callBack.onPause();
        }
        return this;
    }

    public void close() {
        stop();
        singleThreadExecutor.shutdown();
        singleThreadExecutor = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /*  回调接口
    * */
    public interface CallBack {
        void onStart();     //开始播放的时候回调
        void onRestart();   //重新开始播放时候回调
        void onCompleted();  //播放完成时候回调
        void onStop();      //播放为完成时，强制结束
        void onPause();     //未播放完成时，暂停回调
        void onError();     //出错时候回调
    }
}
