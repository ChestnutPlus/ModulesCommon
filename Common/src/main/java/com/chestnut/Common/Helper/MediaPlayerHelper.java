package com.chestnut.Common.Helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

import com.chestnut.Common.Helper.bean.RxMediaPlayerBean;
import com.chestnut.Common.utils.ExceptionCatchUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;

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
            singleThreadExecutor.execute(() -> mediaPlayer.start());
            if (callBack != null)
                callBack.onStart();
        };
        MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
            if (callBack != null)
                callBack.onCompleted();
            isStop = true;
            isPause = true;
            mediaPlayer.reset();
        };
        MediaPlayer.OnErrorListener onErrorListener = (mediaPlayer1, i, i1) -> {
            if (callBack != null)
                callBack.onError();
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
                ExceptionCatchUtils.getInstance().catchException(e, TAG, false);
            }
        } else {
            if (isPause) {
                singleThreadExecutor.execute(() -> mediaPlayer.start());
                if (this.callBack != null)
                    this.callBack.onReStart();
            } else
                pause();
        }
        return this;
    }

    public MediaPlayerHelper play() {
        play(null);
        return this;
    }

    public Observable<RxMediaPlayerBean> rxPlay() {
        return Observable.create(subscriber -> {
            play(new CallBack() {
                @Override
                public void onStart() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_START));
                }

                @Override
                public void onReStart() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_RESTART));
                }

                @Override
                public void onCompleted() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_COMPLETED));
                }

                @Override
                public void onStop() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_STOP));
                }

                @Override
                public void onPause() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_PAUSE));
                }

                @Override
                public void onError() {
                    subscriber.onNext(new RxMediaPlayerBean(RxMediaPlayerBean.ON_ERROR));
                }
            });
        });
    }

    public MediaPlayerHelper stop() {
        if (url==null || mediaPlayer==null)
            return this;
        mediaPlayer.stop();
        if (callBack!=null)
            callBack.onStop();
        isStop = true;
        mediaPlayer.reset();
        return this;
    }

    public MediaPlayerHelper pause() {
        if (url==null || mediaPlayer==null)
            return this;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (callBack!=null)
                callBack.onPause();
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
        singleThreadExecutor.shutdown();
        singleThreadExecutor = null;
        mediaPlayer.release();
        mediaPlayer = null;
        isPause = true;
        isStop = true;
    }

    /*接口，类*/
    public interface CallBack {
        void onStart();     //开始播放的时候回调
        void onReStart();    //暂停后，开始播放
        void onCompleted();  //播放完成时候回调
        void onStop();      //播放为完成时，强制结束
        void onPause();     //未播放完成时，暂停回调
        void onError();     //出错时候回调
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
                e.printStackTrace();
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
