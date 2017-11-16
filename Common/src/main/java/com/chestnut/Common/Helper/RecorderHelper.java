package com.chestnut.Common.Helper;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.chestnut.Common.utils.ExceptionCatchUtils;
import com.chestnut.Common.utils.UtilsManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2017年3月3日15:40:04
 *     desc  : RecorderHelper，封装了：MediaRecorder
 *     thanks To:
 *     dependent on:
 *     updateLog:
 *          1.  栗子  2017年3月3日15:39:34   初始化
 *          2.  栗子  2017年8月6日20:02:54   优化API，增加人性化的回调接口
 * </pre>
 */
public class RecorderHelper {

    private MediaRecorder recorder = null;
    private boolean isRecording = false;
    private String fileName = null;
    private CountDownTimer countDownTimer = null;
    private String outFile = UtilsManager.getCachePath()+"/RecorderHelper-Temp.amr";
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private Handler handler;

    //定义准备时间的概念，如果，太快调用stop方法，会触发回调：onRecordTooShort
    private boolean isReady = false;
    private int READY_TIME_MS = 700;
    private Subscription readyTimeSubscription;

    //定义最大的录音时长，并在剩余N秒的时候，回调接口：onRecordTooLong
    private int THE_MAX_RECORD_TIME_SECOND = 60;    //最大录音时长
    private int THE_LEFT_TIME_NOTIFY_SECOND = 5;    //剩余多少秒的时候，回调函数
    private Subscription recordTimeSubscription;
    private int theRecordDuration = 0;

    /**
     * 设置准备时间
     * @param timeMs    准备时间，毫秒
     * @return this
     */
    public RecorderHelper setReadyTime(int timeMs) {
        this.READY_TIME_MS = timeMs;
        return this;
    }

    /**
     * 设置最大的录音时间和
     *  通知的倒计时时间
     * @param theMaxTimeSecond  theMaxTimeSecond
     * @param theNotifyLeftTime theNotifyLeftTime
     * @return  this
     */
    public RecorderHelper setMaxTimeAndNotifyLeftTime(int theMaxTimeSecond, int theNotifyLeftTime) {
        if (theMaxTimeSecond>=theNotifyLeftTime) {
            this.THE_MAX_RECORD_TIME_SECOND = theMaxTimeSecond;
            this.THE_LEFT_TIME_NOTIFY_SECOND = theNotifyLeftTime;
        }
        return this;
    }

    /**
     * 释放资源
     */
    public void close() {
        if (recorder!=null) {
            recorder.release();
            recorder = null;
        }
        singleThreadExecutor.shutdown();
        if (readyTimeSubscription!=null && !readyTimeSubscription.isUnsubscribed()) {
            readyTimeSubscription.unsubscribe();
        }
        if (countDownTimer!=null)
            countDownTimer.cancel();
        if (recordTimeSubscription!=null && !recordTimeSubscription.isUnsubscribed()) {
            recordTimeSubscription.unsubscribe();
        }
        countDownTimer = null;
        readyTimeSubscription = null;
        recordTimeSubscription = null;
        callBack = null;
        handler = null;
    }

    //开始录音
    public void startRecord() {
        isReady = false;
        readyTimeSubscription = Observable.just(1)
                .delay(READY_TIME_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    isReady = true;
                    _startRecord();
                });
    }

    /**
     *      录音
     */
    private void _startRecord() {
        theRecordDuration = 0;
        try {
            if (recorder==null) {
                //  1.  实例化
                recorder = new MediaRecorder();
            }
            //  2.  设置录音音频的来源，MIC 是指 Microphone audio source
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //  3.  设置录音音频的格式：
            //      格式有：3Gp,AMR,AAC
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //  4.0 设置音频的 编码格式
            //      编码格式：对应于录音的格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //  4.1 设置音频的 编码位率,?,比特率？ 采样率*声道*采样位数 = 比特率
//                recorder.setAudioEncodingBitRate(256000);
            //  4.2 设置音频的 采样率
//                recorder.setAudioSamplingRate(16000);
            //  4.3 设置声道，1：单声道，2：立体声道
//                recorder.setAudioChannels(2);
            //  5.  设置录音音频的存放地方
            recorder.setOutputFile(fileName);
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e,"RecorderHelper");
            Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
            if (callBack!=null) {
                handler.post(()-> callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage()));
            }
            recorder.reset();
            recorder = null;
            if (countDownTimer!=null)
                countDownTimer.cancel();
            if (readyTimeSubscription!=null && !readyTimeSubscription.isUnsubscribed()) {
                readyTimeSubscription.unsubscribe();
            }
            if (recordTimeSubscription!=null && !recordTimeSubscription.isUnsubscribed()) {
                recordTimeSubscription.unsubscribe();
            }
            readyTimeSubscription = null;
            recordTimeSubscription = null;
        }
        if ( recorder!=null && !isRecording ) {
            countDownTimer = new CountDownTimer(Integer.MAX_VALUE,300) {
                @Override
                public void onTick(long l) {
                    updateMicStatus();
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
            recordTimeSubscription = Observable.interval(1,TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        theRecordDuration = aLong.intValue();
                        long a = THE_MAX_RECORD_TIME_SECOND - aLong;
                        if (a == 0) {
                            recordTimeSubscription.unsubscribe();
                            stopRecord();
                        }
                        else if (a <= THE_LEFT_TIME_NOTIFY_SECOND) {
                            if (callBack!=null)
                                handler.post(()-> callBack.onRecordTooLong(fileName,THE_MAX_RECORD_TIME_SECOND,(int)a));
                        }
                    });
            singleThreadExecutor.execute(()->{
                try {
                    recorder.prepare();
                    recorder.start();
                    isRecording = true;
                    if (callBack!=null) {
                        handler.post(()-> callBack.onRecordStart(fileName));
                    }
                    if (readyTimeSubscription!=null && !readyTimeSubscription.isUnsubscribed()) {
                        readyTimeSubscription.unsubscribe();
                    }
                } catch (Exception e) {
                    ExceptionCatchUtils.catchE(e,"RecorderHelper");
                    Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
                    if (callBack!=null) {
                        handler.post(()-> callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage()));
                    }
                    recorder.reset();
                    if (countDownTimer!=null)
                        countDownTimer.cancel();
                    if (readyTimeSubscription!=null && !readyTimeSubscription.isUnsubscribed()) {
                        readyTimeSubscription.unsubscribe();
                    }
                    if (recordTimeSubscription!=null && !recordTimeSubscription.isUnsubscribed()) {
                        recordTimeSubscription.unsubscribe();
                    }
                    readyTimeSubscription = null;
                    recordTimeSubscription = null;
                }
            });
        }
    }

    /**
     *      结束录音
     */
    public void stopRecord() {
        if (!isReady) {
            if (readyTimeSubscription!=null && !readyTimeSubscription.isUnsubscribed()) {
                readyTimeSubscription.unsubscribe();
            }
            readyTimeSubscription = null;
            if (callBack!=null)
                handler.post(()-> callBack.onRecordTooShort(fileName,READY_TIME_MS));
            isRecording = false;
            return;
        }
        if (recordTimeSubscription!=null && !recordTimeSubscription.isUnsubscribed()) {
            recordTimeSubscription.unsubscribe();
        }
        recordTimeSubscription = null;
        if (recorder!=null && isRecording) {
            isRecording = false;
            recorder.stop();
            if (callBack!=null) {
                handler.post(()-> callBack.onRecordEnd(fileName,theRecordDuration));
            }
            if (countDownTimer!=null)
                countDownTimer.cancel();
        }
        if (recorder!=null)
            recorder.reset();
    }


    public RecorderHelper init() {
        init(outFile);
        return this;
    }

    /**
     * 初始化
     * @param fileName 文件名称
     * @return 返回 this 指针
     */
    public RecorderHelper init(String fileName) {
        if (handler==null)
            handler = new Handler(Looper.getMainLooper());
        this.fileName = fileName;
        return this;
    }

    /**
     * 设置监听器
     * @param callBack  监听器
     */
    public RecorderHelper setCallBack(RecorderListener callBack) {
        this.callBack = callBack;
        return this;
    }
    private RecorderListener callBack = null;

    /**
     * 查询当前分贝
     */
    private void updateMicStatus() {
        if (recorder != null) {
            if (callBack!=null) {
                handler.post(()-> {
                    double ratio = (double)recorder.getMaxAmplitude() / 1;
                    double db = 0;// 分贝
                    if (ratio > 1)
                        db = 20 * Math.log10(ratio);
                    callBack.onRecordDBChange(db);
                });
            }
        }
    }
}
