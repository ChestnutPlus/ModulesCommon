package com.chestnut.Common.Helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

import com.chestnut.Common.utils.ExceptionCatchUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/25 15:48
 *     desc  :  重新封装：MediaPlayer
 *              主要是面向：播放Music
 *     thanks To:
 *     dependent on:
 *     update log:
 *          1.  2017年7月26日09:27:47：增加监听回调，mediaPlayer
 * </pre>
 */

public class MediaPlayerHelper {

    /* 常量*/
    private String TAG = "MediaPlayerHelper";
    private boolean OpenLog = true;

    /* 变量*/
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private MediaPlayer mediaPlayer = null;
    private CallBack callBack = null;
    private String url = null;
    private boolean isStop = true;
    private boolean isPause = true;
    private int TYPE = 0;   //内部定义：0：本地path/网络，1：raw
    private Context context;
    private Subscription subscription;  //用于刷新播放进度

    /*方法*/
    public MediaPlayerHelper init(Context context) {
        this.context = context.getApplicationContext();
        switch (TYPE) {
            case 0:
                mediaPlayer = new MediaPlayer();
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(context, Integer.parseInt(url));
                break;
            case 2:
                break;
        }
        MediaPlayer.OnPreparedListener onPreparedListener = mp -> {
            isStop = false;
            isPause = false;
            singleThreadExecutor.execute(() -> {
                mediaPlayer.start();
                startTimer();
            });
            if (callBack != null)
                callBack.onStart(mediaPlayer,mediaPlayer.getDuration()/1000);
        };
        MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
            stopTimer();
            if (callBack != null)
                callBack.onCompleted(mediaPlayer);
            isStop = true;
            isPause = true;
            mediaPlayer.reset();
        };
        MediaPlayer.OnErrorListener onErrorListener = (mediaPlayer1, i, i1) -> {
            stopTimer();
            if (callBack != null)
                callBack.onError(mediaPlayer);
            isPause = true;
            isPause = true;
            url = null;
            return false;
        };
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return this;
    }

    public MediaPlayerHelper setUrl(String url) {
        if (mediaPlayer!=null && url!=null)
            stop();
        TYPE = 0;
        this.url = url;
        return this;
    }

    public MediaPlayerHelper setUrl(@RawRes int urlId) {
        if (mediaPlayer!=null && url!=null)
            stop();
        TYPE = 1;
        url = String.valueOf(urlId);
        return this;
    }

    /**
     * 播放
     * @param callBack  回调
     * @return  this
     */
    public MediaPlayerHelper play(CallBack callBack) {
        if (url==null || mediaPlayer==null)
            return this;
        this.callBack = callBack;
        if (isStop) {
            try {
                switch (TYPE) {
                    case 0:
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync();
                        break;
                    case 1:
                        init(context);
                        break;
                    case 2:
                        break;
                }
            } catch (Exception e) {
                ExceptionCatchUtils.catchE(e,"MediaPlayerHelper");
            }
        } else {
            if (isPause) {
                singleThreadExecutor.execute(() -> {
                    mediaPlayer.start();
                    startTimer();
                });
                if (this.callBack != null)
                    this.callBack.onReStart(mediaPlayer);
            } else
                pause();
        }
        return this;
    }

    /**
     * 开启定时器任务，去刷新
     * 播放的进度
     */
    private void startTimer() {
        subscription = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    if (callBack!=null)
                        callBack.onProgressChange(mediaPlayer,mediaPlayer.getCurrentPosition()/1000);
                });
    }

    private void stopTimer() {
        if (subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = null;
    }

    public MediaPlayerHelper play() {
        play(null);
        return this;
    }

    public MediaPlayerHelper stop() {
        stopTimer();
        if (url==null || mediaPlayer==null)
            return this;
        mediaPlayer.stop();
        if (callBack!=null)
            callBack.onStop(mediaPlayer);
        isStop = true;
        mediaPlayer.reset();
        return this;
    }

    public MediaPlayerHelper pause() {
        stopTimer();
        if (url==null || mediaPlayer==null)
            return this;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (callBack!=null)
                callBack.onPause(mediaPlayer);
            isPause = true;
        }
        return this;
    }

    public MediaPlayerHelper seekTo(int seconds) {
        if (url==null || mediaPlayer==null)
            return this;
        int to = seconds * 1000;
        to = to<=0 ? 0 : to;
        to = to>=mediaPlayer.getDuration() ? mediaPlayer.getDuration() : to;
        mediaPlayer.seekTo(to);
        return this;
    }

    public void release() {
        if (mediaPlayer==null)
            return;
        stop();
        stopTimer();
        singleThreadExecutor.shutdown();
        singleThreadExecutor = null;
        mediaPlayer.release();
        mediaPlayer = null;
        isPause = true;
        isStop = true;
    }

    /*接口，类*/
    public interface CallBack {
        void onStart(MediaPlayer mediaPlayer, int allSecond);     //开始播放的时候回调
        void onReStart(MediaPlayer mediaPlayer);    //暂停后，开始播放
        void onCompleted(MediaPlayer mediaPlayer);  //播放完成时候回调
        void onStop(MediaPlayer mediaPlayer);      //播放为完成时，强制结束
        void onPause(MediaPlayer mediaPlayer);     //未播放完成时，暂停回调
        void onError(MediaPlayer mediaPlayer);     //出错时候回调
        void onProgressChange(MediaPlayer mediaPlayer, int nowSecond);  //回调当前的进度
    }

    /**
     * 获得音频文件的时长
     * @return  时长，秒
     */
    public static Observable<Integer> getDuration(String mp3Path) {
        return Observable.create(subscriber -> {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mp3Path);
                mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
                    subscriber.onNext(mediaPlayer1.getDuration()/1000);
                    mediaPlayer1.release();
                });
                mediaPlayer.setOnErrorListener((mediaPlayer12, i, i1) -> {
                    subscriber.onNext(0);
                    mediaPlayer12.release();
                   return false;
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                ExceptionCatchUtils.catchE(e,"MediaPlayerHelper");
                subscriber.onNext(0);
            }
        });
    }

    /**
     * 获得音频文件的时长
     * @return  时长，秒
     */
    public static Observable<Integer> getDuration(Context context,@RawRes int rawRes) {
        return Observable.create(subscriber -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(context,rawRes);
            mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
                subscriber.onNext(mediaPlayer1.getDuration()/1000);
                mediaPlayer1.release();
            });
        });
    }
}
