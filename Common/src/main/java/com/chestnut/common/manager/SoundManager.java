package com.chestnut.common.manager;

import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;
import android.util.SparseIntArray;

import com.chestnut.common.contract.common.CommonContract;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/5/16 18:00
 *     desc  :  封装了SoundPool管理
 *              1. 只允许播放raw下面的音频
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SoundManager {

    /*单例*/
    private static volatile SoundManager defaultInstance;
    public static SoundManager getInstance() {
        SoundManager xFontUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (SoundManager.class) {
                xFontUtils = defaultInstance;
                if (defaultInstance == null) {
                    xFontUtils = new SoundManager();
                    defaultInstance = xFontUtils;
                }
            }
        }
        return xFontUtils;
    }
    private SoundManager() {}

    private Context mContext;
    private int currentStream = AudioManager.STREAM_MUSIC;
    private SoundPool mSoundPool;
    private SparseIntArray mResourceMap = new SparseIntArray();//key-value, <res-id> - <loaded-id>

    public void init(Context context) {
        init(context,8,AudioManager.STREAM_MUSIC);
    }

    public void init(Context context, int streamsMax, int streamType) {
        this.mContext = context;
        this.currentStream = streamType;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(streamsMax);//传入音频数量，这里是指，同时播放音频数，最大不要超过125
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(streamType);//设置音频流的合适的属性
            builder.setAudioAttributes(attrBuilder.build());//加载一个AudioAttributes
            this.mSoundPool = builder.build();
        }
        else {
            mSoundPool = new SoundPool(streamsMax,streamType,1);
        }
    }

    public void play(@RawRes int id) {
        play(id, false, -1, 1.0f, currentStream, null);
    }

    public void play(@RawRes int id, CommonContract.Function onLoadCompleteListener) {
        play(id, false, -1, 1.0f, currentStream, onLoadCompleteListener);
    }

    public void play(@RawRes int id, boolean loop, float curVolume, float rate, int stream, CommonContract.Function onLoadCompleteListener) {
        if(mSoundPool != null && mResourceMap != null) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
            if (audioManager!=null) {
                if (curVolume < 0)
                    curVolume = (float) audioManager.getStreamVolume(stream) / audioManager.getStreamMaxVolume(stream);
            }
            //已经load过了
            if(mResourceMap.indexOfKey(id) >= 0) {
                if (onLoadCompleteListener!=null)
                    onLoadCompleteListener.onAction();
                mSoundPool.play(mResourceMap.get(id), curVolume, curVolume, 1, loop ? 1 : 0, rate);
            }
            //未load
            else {
                final float volumeTemp = curVolume;
                mSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                    if (status == 0) {
                        if (onLoadCompleteListener != null)
                            onLoadCompleteListener.onAction();
                        mSoundPool.play(mResourceMap.get(id), volumeTemp, volumeTemp, 1, loop ? 1 : 0, rate);
                    }
                });
                mResourceMap.put(id, mSoundPool.load(mContext, id, 1));
            }
        }
    }

    public void stop(@RawRes int id) {
        int temp = mResourceMap.get(id,-1);
        if (temp>0) {
            mSoundPool.stop(temp);
        }
    }

    public void unload(@RawRes int id) {
        int temp = mResourceMap.get(id,-1);
        if (temp>0) {
            mSoundPool.unload(temp);
        }
    }

    public void release() {
        try {
            for(int i=0; i<mResourceMap.size(); i++) {
                mSoundPool.unload(mResourceMap.valueAt(i));
            }
            mResourceMap.clear();
            if (mSoundPool!=null)
                mSoundPool.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
