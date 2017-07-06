package com.chestnut.Common.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import rx.Observable;

import static android.R.attr.duration;

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

    public static Observable<MediaInfo> rxGet(String path) {
        return Observable.create(subscriber -> {
            MediaInfo mediaInfo = get(path);
            subscriber.onNext(mediaInfo);
            subscriber.onCompleted();
        });
    }

    public static MediaInfo get(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);                      //标题
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);                      //唱片
            String albumArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);          //唱片艺人
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);                    //歌手
            String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);                    //作者
            String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);                  //比特率
            String frameRate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                frameRate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);         //帧率
            }
            String cdTrackNumber = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);    //CD音轨
            String compilation = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION);          //编辑
            String composer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);                //作曲家
            String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);                        //日期
            String discNumber = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);           //盘数量
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);                //播放时长单位为毫秒
            String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);                      //种类
            String hasAudio = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);               //有音轨
            String hasVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);               //有视频轨
            String location = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);                //位置
            String mimeType = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);                //Mine类型
            String videoWidth = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);           //视频宽度
            String videoHeight = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);         //视频高度
            return new MediaInfo(title,album,albumArtist,artist,author,bitrate,frameRate,cdTrackNumber,compilation,composer
                    ,date,discNumber,duration,genre,hasAudio,hasVideo,location,mimeType,videoWidth,videoHeight);
        } catch (IllegalArgumentException | IllegalStateException e) {
            ExceptionCatchUtils.catchE(e,"MediaInfoUtils");
            return null;
        } finally {
            mmr.release();
        }
    }

    public static Bitmap getVideoThumb(String url,long timeS) {
        return ImageUtils.getBitmapFromUrl(url,timeS);
    }

    public static Observable<Bitmap> rxGetVideoThumb(String url,long timeS) {
        return Observable.create(subscriber -> {
            Bitmap bitmap = getVideoThumb(url, timeS);
            subscriber.onNext(bitmap);
            subscriber.onCompleted();
        });
    }

    public static class MediaInfo {
        public String title;
        public String album;
        public String albumArtist;
        public String artist;
        public String author;
        public String bitrate;
        public String frameRate;
        public String cdTrackNumber;
        public String compilation;
        public String composer;
        public String date;
        public String discNumber;
        public int durationMs;
        public String genre;
        public String hasAudio;
        public String hasVideo;
        public String location;
        public String mimeType;
        public int videoWidth;
        public int videoHeight;

        public MediaInfo(String title, String album, String albumArtist, String artist,
                         String author, String bitrate, String frameRate, String cdTrackNumber,
                         String compilation, String composer, String date, String discNumber,
                         String duration, String genre, String hasAudio, String hasVideo,
                         String location, String mimeType, String videoWidth, String videoHeight) {
            this.title = title;
            this.album = album;
            this.albumArtist = albumArtist;
            this.artist = artist;
            this.author = author;
            this.bitrate = bitrate;
            this.frameRate = frameRate;
            this.cdTrackNumber = cdTrackNumber;
            this.compilation = compilation;
            this.composer = composer;
            this.date = date;
            this.discNumber = discNumber;
            this.durationMs = Integer.parseInt(duration);
            this.genre = genre;
            this.hasAudio = hasAudio;
            this.hasVideo = hasVideo;
            this.location = location;
            this.mimeType = mimeType;
            this.videoWidth = Integer.parseInt(videoWidth);
            this.videoHeight = Integer.parseInt(videoHeight);
        }

        @Override
        public String toString() {
            return "MediaInfo{" +
                    "title='" + title + '\'' +
                    ", album='" + album + '\'' +
                    ", albumArtist='" + albumArtist + '\'' +
                    ", artist='" + artist + '\'' +
                    ", author='" + author + '\'' +
                    ", bitrate='" + bitrate + '\'' +
                    ", frameRate='" + frameRate + '\'' +
                    ", cdTrackNumber='" + cdTrackNumber + '\'' +
                    ", compilation='" + compilation + '\'' +
                    ", composer='" + composer + '\'' +
                    ", date='" + date + '\'' +
                    ", discNumber='" + discNumber + '\'' +
                    ", durationMs='" + duration + '\'' +
                    ", genre='" + genre + '\'' +
                    ", hasAudio='" + hasAudio + '\'' +
                    ", hasVideo='" + hasVideo + '\'' +
                    ", location='" + location + '\'' +
                    ", mimeType='" + mimeType + '\'' +
                    ", videoWidth='" + videoWidth + '\'' +
                    ", videoHeight='" + videoHeight + '\'' +
                    '}';
        }
    }
}
