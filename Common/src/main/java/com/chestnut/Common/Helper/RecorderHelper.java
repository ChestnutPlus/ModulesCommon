package com.chestnut.Common.Helper;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.util.Log;

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
 * </pre>
 */
public class RecorderHelper {

    private MediaRecorder recorder = null;
    private boolean isRecording = false;
    private String fileName = null;
    private CountDownTimer countDownTimer = null;

    /**
     * 释放资源
     */
    public void close() {
        if (recorder!=null) {
            recorder.release();
            recorder = null;
        }
        callBack = null;
    }

    /**
     *      录音
     */
    public void startRecord() {
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
            new Thread(() -> {
                try {
                    recorder.prepare();
                    recorder.start();
                    isRecording = true;
                    if (callBack!=null) {
                        callBack.onRecordStart(fileName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
                    if (callBack!=null) {
                        callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage());
                    }
                }
            }).start();
        }
    }

    /**
     *      结束录音
     */
    public void stopRecord() {
        if (recorder!=null && isRecording) {
            isRecording = false;
            recorder.stop();
            if (callBack!=null) {
                callBack.onRecordEnd(fileName);
            }
            recorder.reset();
            recorder = null;
            if (countDownTimer!=null)
                countDownTimer.cancel();
        }
    }

    /**
     * 初始化
     * @param fileName 文件名称
     * @return 返回 this 指针
     */
    public RecorderHelper init(String fileName) {
        this.fileName = fileName;
        try {
            if (recorder==null) {
                //  1.  实例化
                recorder = new MediaRecorder();
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
            }
        } catch (Exception e) {
            Log.e("RecorderHelper", e.getMessage()==null?"null":e.getMessage());
            if (callBack!=null) {
                callBack.onRecordFail(fileName,e.getMessage()==null?"null":e.getMessage());
            }
        }
        return this;
    }

    /**
     * 设置监听器
     * @param callBack  监听器
     */
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    private CallBack callBack = null;
    public interface CallBack {
        void onRecordStart(String file);
        void onRecordDBChange(double dbValue);
        void onRecordFail(String file, String msg);
        void onRecordEnd(String file);
    }

    /**
     * 查询当前分贝
     */
    private void updateMicStatus() {
        if (recorder != null) {
            double ratio = (double)recorder.getMaxAmplitude() / 1;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            if (callBack!=null) {
                callBack.onRecordDBChange(db);
            }
        }
    }
}
