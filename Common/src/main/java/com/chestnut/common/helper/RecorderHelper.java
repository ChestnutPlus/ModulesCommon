package com.chestnut.common.helper;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.chestnut.common.manager.UtilsManager;
import com.chestnut.common.utils.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


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
    private Handler mainHandler;

    //定义准备时间的概念，如果，太快调用stop方法，会触发回调：onRecordTooShort
    private boolean isReady = false;
    private int READY_TIME_MS = 700;
    private Disposable readyTimeSubscription;
    //结束录音后，延迟 STOP_TIME_MS 才真正结束
    private int stopDelayTimeMs = 100;

    //定义最大的录音时长，并在剩余N秒的时候，回调接口：onRecordTooLong
    private int THE_MAX_RECORD_TIME_SECOND = 60;    //最大录音时长
    private int THE_LEFT_TIME_NOTIFY_SECOND = 5;    //剩余多少秒的时候，回调函数
    private Disposable recordTimeSubscription;
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
     * 延后真正的结束时间
     * @param stopDelayTimeMs Ms
     */
    public void setStopDelayTimeMs(int stopDelayTimeMs) {
        stopDelayTimeMs = stopDelayTimeMs > 1000 ? 1000 : stopDelayTimeMs;
        this.stopDelayTimeMs = stopDelayTimeMs;
    }

    /**
     * 释放资源
     */
    public synchronized void close() {
        if (recorder!=null) {
            try {
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            recorder = null;
        }
        singleThreadExecutor.shutdown();
        if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
            readyTimeSubscription.dispose();
        }
        if (countDownTimer!=null)
            countDownTimer.cancel();
        if (recordTimeSubscription!=null && !recordTimeSubscription.isDisposed()) {
            recordTimeSubscription.dispose();
        }
        countDownTimer = null;
        readyTimeSubscription = null;
        recordTimeSubscription = null;
        callBack = null;
        mainHandler = null;
    }

    //开始录音
    public void startRecord() {
        LogUtils.i("RecorderHelper", "startRecord");
        synchronized(RecorderHelper.class) {
            isReady = false;
            if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
                readyTimeSubscription.dispose();
            }
            readyTimeSubscription = Observable.just(1)
                    .delay(READY_TIME_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        isReady = true;
                        _startRecord();
                    });
        }
    }

    /**
     *      录音
     */
    private void _startRecord() {
        LogUtils.i("RecorderHelper", "_startRecord");
        synchronized (RecorderHelper.class) {
            theRecordDuration = 0;
            try {
                if (recorder!=null) {
                    try {
                        recorder.reset();
                        recorder.release();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    recorder = null;
                }
                //  1.  实例化
                recorder = new MediaRecorder();
                //  2.  设置录音音频的来源，MIC 是指 Microphone audio source
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //  3.  设置录音音频的格式：
                //      格式有：3Gp,AMR,AAC
                recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                //  4.0 设置音频的 编码格式
                //      编码格式：对应于录音的格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                //  4.1  设置录音音频的存放地方
                recorder.setOutputFile(fileName);
                //  4.2  设置音频的 编码位率,?,比特率？ 采样率*声道*采样位数 = 比特率
//                recorder.setAudioEncodingBitRate(256000);
                //  4.3  设置音频的 采样率
//                recorder.setAudioSamplingRate(16000);
                //  4.4  设置声道，1：单声道，2：立体声道
//                recorder.setAudioChannels(2);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
                if (callBack!=null) {
                    mainHandler.post(()-> callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage()));
                }
                try {
                    recorder.reset();
                    recorder.release();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                recorder = null;
                if (countDownTimer!=null)
                    countDownTimer.cancel();
                if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
                    readyTimeSubscription.dispose();
                }
                if (recordTimeSubscription!=null && !recordTimeSubscription.isDisposed()) {
                    recordTimeSubscription.dispose();
                }
                readyTimeSubscription = null;
                recordTimeSubscription = null;
            }
            if (recorder!=null && !isRecording ) {
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
                recordTimeSubscription = Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(aLong -> {
                            theRecordDuration = aLong.intValue();
                            long a = THE_MAX_RECORD_TIME_SECOND - aLong;
                            if (a == 0) {
                                recordTimeSubscription.dispose();
                                stopRecord();
                            }
                            else if (a <= THE_LEFT_TIME_NOTIFY_SECOND) {
                                if (callBack!=null)
                                    mainHandler.post(()-> callBack.onRecordTooLong(fileName,THE_MAX_RECORD_TIME_SECOND,(int)a));
                            }
                        });
                singleThreadExecutor.execute(()->{
                    try {
                        recorder.prepare();
                        recorder.start();
                        isRecording = true;
                        if (callBack!=null) {
                            mainHandler.post(()-> callBack.onRecordStart(fileName));
                        }
                        if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
                            readyTimeSubscription.dispose();
                        }
                    } catch (Exception e) {
                        Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
                        if (callBack!=null) {
                            mainHandler.post(()-> callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage()));
                        }
                        try {
                            recorder.reset();
                            recorder.release();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        recorder = null;
                        if (countDownTimer!=null)
                            countDownTimer.cancel();
                        if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
                            readyTimeSubscription.dispose();
                        }
                        if (recordTimeSubscription!=null && !recordTimeSubscription.isDisposed()) {
                            recordTimeSubscription.dispose();
                        }
                        readyTimeSubscription = null;
                        recordTimeSubscription = null;
                    }
                });
            }
        }
    }

    /**
     *      结束录音
     */
    public void stopRecord() {
        LogUtils.i("RecorderHelper", "stopRecord");
        synchronized(RecorderHelper.class) {
            if (!isReady) {
                if (readyTimeSubscription!=null && !readyTimeSubscription.isDisposed()) {
                    readyTimeSubscription.dispose();
                }
                readyTimeSubscription = null;
                if (callBack!=null)
                    mainHandler.post(()-> callBack.onRecordTooShort(fileName,READY_TIME_MS));
                isRecording = false;
                return;
            }
            if (recordTimeSubscription!=null && !recordTimeSubscription.isDisposed()) {
                recordTimeSubscription.dispose();
            }
            recordTimeSubscription = null;
            if (isRecording) {
                isRecording = false;
                if (callBack!=null) {
                    mainHandler.post(()-> callBack.onRecordEnd(fileName,theRecordDuration));
                }
                if (countDownTimer!=null)
                    countDownTimer.cancel();
            }
            if (recorder!=null) {
                try {
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                } catch (Exception e) {
                    recorder = null;
                }
            }
            recorder = null;
        }
    }

    public RecorderHelper init() {
        synchronized (RecorderHelper.class) {
            init(outFile);
        }
        return this;
    }

    /**
     * 初始化
     * @param fileName 文件名称
     * @return 返回 this 指针
     */
    public RecorderHelper init(String fileName) {
        if (mainHandler==null)
            mainHandler = new Handler(Looper.getMainLooper());
        this.fileName = fileName;
        return this;
    }

    /**
     * 设置监听器
     * @param callBack  监听器
     */
    public void setCallBack(RecorderListener callBack) {
        this.callBack = callBack;
    }
    private RecorderListener callBack = null;

    /**
     * 查询当前分贝
     */
    private void updateMicStatus() {
        if (recorder != null) {
            if (callBack!=null) {
                mainHandler.post(()-> {
                    if (recorder!=null) {
                        try {
                            double ratio = (double) recorder.getMaxAmplitude() / 1;
                            double db = 0;// 分贝
                            if (ratio > 1)
                                db = 20 * Math.log10(ratio);
                            callBack.onRecordDBChange(db);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
