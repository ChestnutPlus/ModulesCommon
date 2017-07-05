package com.chestnut.Common.utils;

import android.media.MediaMetadataRetriever;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/5 18:34
 *     desc  :  获取媒体信息Info
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class MediaInfoUtils {

    public static void get() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String str = getExternalStorageDirectory() + "music/hetangyuese.mp3";
        try {
            mmr.setDataSource(str);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE); // api level 10, 即从GB2.3.3开始有此功能
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE); // 从api level 14才有，即从ICS4.0才有此功能
            String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            mmr.release();
        }
    }
}
