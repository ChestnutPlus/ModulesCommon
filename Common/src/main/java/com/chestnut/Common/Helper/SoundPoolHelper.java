package com.chestnut.Common.Helper;

import android.media.AudioManager;
import android.media.SoundPool;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/6/22 17:24
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SoundPoolHelper {

    private SoundPool soundPool;
    private int NOW_STEAM = AudioManager.STREAM_MUSIC;

    public SoundPoolHelper setStream(int NOW_STEAM) {
        this.NOW_STEAM = NOW_STEAM;
        return this;
    }

    public SoundPoolHelper(int maxStream) {
        soundPool = new SoundPool(maxStream,NOW_STEAM,0);
    }

    public void release() {
        if (soundPool!=null)
            soundPool.release();
    }
}
