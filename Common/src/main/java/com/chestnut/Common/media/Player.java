package com.chestnut.Common.media;

import android.media.MediaPlayer;

import com.chestnut.Common.tips.CyclePoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月29日15:36:21
 *     desc  : 播放类，直接调用，
 *     thanks To:
 *     dependent on:
 *     updateLog：
 *          1.0.0   播放音乐（本地+在线）
 * </pre>
 */
public class Player {

    //播放对象。
    public static class MediaInfo {
        private String title;       //标题
        private String specialName; //专辑名称
        private int duration;       //时长
        private String url;         //播放地址

        public MediaInfo(String title, String specialName, int duration, String url) {
            this.title = title;
            this.specialName = specialName;
            this.duration = duration;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSpecialName() {
            return specialName;
        }

        public void setSpecialName(String specialName) {
            this.specialName = specialName;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "MediaInfo{" +
                    "title='" + title + '\'' +
                    ", specialName='" + specialName + '\'' +
                    ", duration=" + duration +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    private volatile static Player player;
    private MediaPlayer mediaPlayer = null;
    private CyclePoint point = null;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private List<MediaInfo> list = null;
    private Player(){}
    private boolean isInit = false;

    //播放状态
    private boolean isListEnd = true;
    private boolean isPlaying = false;
    private boolean isStop = true;
    private boolean isFirstPlay = false;

    private CyclePoint.PointCallBack pointCallBack = new CyclePoint.PointCallBack() {
        @Override
        public void pointNext(int point, int num) {

        }

        @Override
        public void pointLast(int point, int num) {

        }

        @Override
        public void changeSize(int point, int num) {

        }

        @Override
        public void onListEnd(int point, int num) {
            isListEnd = true;
        }
    };
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            isPlaying = false;
            if (playerCallBack!=null)
                playerCallBack.onItemEnd(point.get(),list.get(point.get()));
            if (isListEnd && playerCallBack!=null) {
                playerCallBack.onListEnd(point.num);
            }
        }
    };
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (playerCallBack!=null)
                        playerCallBack.onItemStart(point.get(),list.get(point.get()));
                    mediaPlayer.start();
                    isPlaying = true;
                }
            });
        }
    };
    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (playerCallBack!=null)
                playerCallBack.onItemError(point.get(),list.get(point.get()),"what:"+what+",extra:"+extra);
            return false;
        }
    };

    /**
     * 取的单例
     * @return  Player
     */
    public static Player getInstance() {
        if (player==null) {
            synchronized (Player.class) {
                if (player==null) {
                    player = new Player();
                }
            }
        }
        return player;
    }

    /**
     * 初始化
     */
    public void init() {
        if (mediaPlayer==null)
            mediaPlayer = new MediaPlayer();
        if (list==null)
            list = new ArrayList<>();
        isInit = true;
    }

    /**
     * 播放单曲，传入播放信息对象
     * @param MediaInfo 播放信息对象
     */
    public void Music(MediaInfo MediaInfo) {
        if (!isInit)
            init();
        isStop = false;
        isFirstPlay = true;
        isListEnd = false;
        isPlaying = false;
        list.clear();
        list.add(MediaInfo);
        point = new CyclePoint(0,1,false);
        point.setCallBack(pointCallBack);
        if (playerCallBack!=null)
            playerCallBack.onListStart(point.num);
    }

    /**
     * 播放列表，传入一个列表
     * @param mediaInfoList 列表
     */
    public void Music(List<MediaInfo> mediaInfoList) {
        if (!isInit)
            init();
        isStop = false;
        isFirstPlay = true;
        isListEnd = false;
        isPlaying = false;
        list.clear();
        list.addAll(mediaInfoList);
        point = new CyclePoint(0,mediaInfoList.size(),false);
        point.setCallBack(pointCallBack);
        if (playerCallBack!=null)
            playerCallBack.onListStart(point.num);
    }

    /**
     * 播放下一个。
     */
    public void next() {
        if (isStop)
            return;
        if (!isInit)
            return;
        if (isPlaying && playerCallBack!=null)
            playerCallBack.onItemEnd(point.get(),list.get(point.get()));
        if (isFirstPlay) {
            playNext(0);
            if (point.num==1)
                point.next();
        }
        else
            playNext(point.next());
    }

    /**
     * 播放上一个。
     */
    public void last() {
        if (isStop)
            return;
        if (!isInit)
            return;
        if (isPlaying && playerCallBack!=null)
            playerCallBack.onItemEnd(point.get(),list.get(point.get()));
        if (isListEnd)
            isListEnd = false;
        playNext(point.last());
    }

    /**
     * 播放,用于暂停后，播放的。
     */
    public void start() {
        if (isPlaying)
            mediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    /**
     * 快进到某个position,单位秒。
     * @param position 位置
     */
    public void seekTo(int position) {
        if (mediaPlayer.isPlaying()) {
            int max = mediaPlayer.getDuration() / 1000; //秒
            mediaPlayer.seekTo( max <= position ? max*1000-500 : position*1000 );
        }
    }

    /**
     * 得到当前的播放进度，当已经停止播放的时候，
     * 返回 -1.单位是秒。
     * @return 当前的播放进度（秒）
     */
    public int getPosition() {
        if (mediaPlayer.isPlaying())
            return mediaPlayer.getCurrentPosition()/1000;
        return -1;
    }

    /**
     * 停止播放，停止整个列表。
     */
    public void stop() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        if (playerCallBack!=null && isPlaying)
            playerCallBack.onItemEnd(point.get(),list.get(point.get()));
        isStop = true;
        isPlaying = false;
        isListEnd = true;
        if (isListEnd && playerCallBack!=null) {
            playerCallBack.onListEnd(point.num);
        }
    }

    /**
     * 播放 X point 的资源
     * @param index x
     */
    private void playNext(int index) {
        isFirstPlay = false;
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        try {
            mediaPlayer.setDataSource(list.get(index).getUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *      接口
     */
    public interface PlayerCallBack {
        void onListStart(int listNum);
        void onListEnd(int listNum);
        void onItemStart(int playIndex, MediaInfo MediaInfo);
        void onItemEnd(int playIndex, MediaInfo MediaInfo);
        void onItemError(int playIndex, MediaInfo MediaInfo, String errorMsg);
    }
    private PlayerCallBack playerCallBack = null;
    public void setPlayerCallBack(PlayerCallBack playerCallBack) {
        this.playerCallBack = playerCallBack;
    }
}
