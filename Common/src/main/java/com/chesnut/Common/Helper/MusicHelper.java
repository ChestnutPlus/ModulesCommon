package com.chesnut.Common.Helper;

import android.media.MediaPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 22:51
 *     desc  :  封装了MediaPlayer
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class MusicHelper {

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    public MusicHelper() {
        mediaPlayer = new MediaPlayer();
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = mp -> {
        singleThreadExecutor.execute(() -> mediaPlayer.start());
        isPlaying = true;
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
        isPlaying = false;
        stop();
    };

    public MusicHelper setUrl(String url) {
        try {
            stop();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {

        }
        return this;
    }
    public MusicHelper play() {
        try {
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        } catch (Exception e) {

        }
        return this;
    }
    public MusicHelper stop() {
        if (isPlaying) {
            mediaPlayer.stop();
        }
        isPlaying=false;
        mediaPlayer.reset();
        return this;
    }

    public void close() {
        stop();
        singleThreadExecutor.shutdown();
        singleThreadExecutor = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
