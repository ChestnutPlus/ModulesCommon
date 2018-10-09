package com.chestnut.common.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

import com.chestnut.common.utils.ExceptionCatchUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

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
 *          2.  2018年1月31日10:27:44
 *              1）去掉主线程的回调，回调都在子线程
 *              2）对MediaPlayer的所有操作都置于子线程中做
 *              3）监听器置于本类，作为静态虚类
 * </pre>
 */

public class MediaPlayerHelper {

    /* 常量*/
    private String TAG = "MediaPlayerHelper";
    private boolean OpenLog = true;

    /* 变量*/
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private MediaPlayer mediaPlayer = null;
    private MediaPlayerHelperListener callBack = null;
    private String url = null;
    private boolean isStop = true;
    private boolean isPause = true;
    private int TYPE = 0;   //内部定义：0：本地path/网络，1：raw
    private Context context;
    private Disposable subscription;  //用于刷新播放进度

    /*方法*/
    /**
     * 对外的初始化
     * @param context 上下文
     * @return  this
     */
    public MediaPlayerHelper init(Context context) {
        this.context = context.getApplicationContext();
        switch (TYPE) {
            case 0:
                singleThreadExecutor.execute(()-> {
                    mediaPlayer = new MediaPlayer();
                    initAsync();
                });
                break;
            case 1:
                singleThreadExecutor.execute(()-> {
                    mediaPlayer = MediaPlayer.create(context, Integer.parseInt(url));
                    initAsync();
                });
                break;
            case 2:
                break;
        }
        return this;
    }

    /**
     * 内部方法初始化
     *  异步。
     */
    private void initAsync() {
        if (mediaPlayer!=null) {
            MediaPlayer.OnPreparedListener onPreparedListener = mp -> {
                isStop = false;
                isPause = false;
                singleThreadExecutor.execute(() -> {
                    singleThreadExecutor.execute(()-> {
                        if (mediaPlayer!=null) {
                            mediaPlayer.start();
                            startTimer();
                        }
                    });
                });
                if (callBack != null && mediaPlayer!=null)
                    callBack.onStart(mediaPlayer,mediaPlayer.getDuration()/1000);
            };
            MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
                stopTimer();
                if (callBack != null && mediaPlayer!=null)
                    callBack.onComplete(mediaPlayer);
                isStop = true;
                isPause = true;
                singleThreadExecutor.execute(()->{
                    if (mediaPlayer!=null)
                        mediaPlayer.reset();
                });
            };
            MediaPlayer.OnErrorListener onErrorListener = (mediaPlayer1, i, i1) -> {
                stopTimer();
                if (callBack != null && mediaPlayer!=null)
                    callBack.onError(mediaPlayer);
                isStop = true;
                isPause = true;
                url = null;
                return false;
            };
            MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = (mp, percent) -> {
                if (callBack!=null && mediaPlayer!=null) {
                    callBack.onBufferUpdate(mp, percent);
                }
            };
            mediaPlayer.setOnErrorListener(onErrorListener);
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
    }

    /**
     * 设置播放地址：
     *  可以是 R.raw.res资源
     *  也可以是文件
     *  也可以是网络链接
     * @param url url
     * @return this
     */
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
     * 设置回调
     *  注意，这里是在子线程中回调
     *  若操作UI，需手动切换主线程
     * @param callBack callback
     */
    public void setCallBack(MediaPlayerHelperListener callBack) {
        this.callBack = callBack;
    }

    /**
     * 播放
     * @return  this
     */
    public MediaPlayerHelper play() {
        if (url==null || mediaPlayer==null)
            return this;
        if (isStop) {
            switch (TYPE) {
                case 0:
                    singleThreadExecutor.execute(()->{
                        if (mediaPlayer!=null) {
                            try {
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepareAsync();
                            } catch (Exception e) {
                                ExceptionCatchUtils.catchE(e,"MediaPlayerHelper");
                            }
                        }
                    });
                    break;
                case 1:
                    init(context);
                    break;
                case 2:
                    break;
            }
        } else {
            if (isPause) {
                singleThreadExecutor.execute(() -> {
                    mediaPlayer.start();
                    startTimer();
                });
                if (this.callBack != null && mediaPlayer!=null)
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
                    if (callBack!=null && mediaPlayer!=null)
                        callBack.onProgressChange(mediaPlayer,mediaPlayer.getCurrentPosition()/1000);
                });
    }

    private void stopTimer() {
        if (subscription!=null && !subscription.isDisposed())
            subscription.dispose();
        subscription = null;
    }

    /**
     * 停止播放
     * @return this
     */
    public MediaPlayerHelper stop() {
        stopTimer();
        if (url==null || mediaPlayer==null)
            return this;
        if (callBack != null)
            callBack.onStop(mediaPlayer);
        isStop = true;
        singleThreadExecutor.execute(()->{
            if (mediaPlayer!=null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });
        return this;
    }

    /**
     * 暂停
     * @return this
     */
    public MediaPlayerHelper pause() {
        stopTimer();
        if (url==null || mediaPlayer==null)
            return this;
        singleThreadExecutor.execute(()->{
            if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                if (callBack!=null)
                    callBack.onPause(mediaPlayer);
                isPause = true;
            }
        });
        return this;
    }

    /**
     * 快进到某个端点
     * @param seconds 秒
     * @return this
     */
    public MediaPlayerHelper seekTo(int seconds) {
        if (url==null || mediaPlayer==null)
            return this;
        singleThreadExecutor.execute(()->{
            if (mediaPlayer!=null) {
                int to = seconds * 1000;
                to = to<=0 ? 0 : to;
                to = to>=mediaPlayer.getDuration() ? mediaPlayer.getDuration() : to;
                mediaPlayer.seekTo(to);
            }
        });
        return this;
    }

    /**
     * 是否正在播放
     * @return 是否
     */
    public boolean isPlaying() {
        return isPause;
    }

    /**
     * 释放
     */
    public void release() {
        if (mediaPlayer==null)
            return;
        stop();
        stopTimer();
        singleThreadExecutor.execute(()->{
            mediaPlayer.release();
            mediaPlayer = null;
            singleThreadExecutor.shutdown();
            singleThreadExecutor = null;
            isPause = true;
            isStop = true;
        });
    }

    /*static*/

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
                    subscriber.onComplete();
                    mediaPlayer1.release();
                });
                mediaPlayer.setOnErrorListener((mediaPlayer12, i, i1) -> {
                    subscriber.onNext(0);
                    subscriber.onComplete();
                    mediaPlayer12.release();
                    return false;
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                ExceptionCatchUtils.catchE(e,"MediaPlayerHelper");
                subscriber.onNext(0);
                subscriber.onComplete();
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
                subscriber.onComplete();
                mediaPlayer1.release();
            });
        });
    }

    /*接口，类*/
    public static abstract class MediaPlayerHelperListener {
        public void onStart(MediaPlayer mediaPlayer, int allSecond){}           //开始播放的时候回调
        public void onBufferUpdate(MediaPlayer mediaPlayer, int bufferUpdatePercent){}      //当播放为网络资源时候，会回调这个方法
        public void onStop(MediaPlayer mediaPlayer){}                           //播放为完成时，强制结束
        public void onReStart(MediaPlayer mediaPlayer){}                        //暂停后，开始播放
        public void onComplete(MediaPlayer mediaPlayer){}                      //播放完成时候回调
        public void onPause(MediaPlayer mediaPlayer){}                          //未播放完成时，暂停回调
        public void onError(MediaPlayer mediaPlayer){}                          //出错时候回调
        public void onProgressChange(MediaPlayer mediaPlayer, int nowSecond){}  //回调当前的进度
    }
}
