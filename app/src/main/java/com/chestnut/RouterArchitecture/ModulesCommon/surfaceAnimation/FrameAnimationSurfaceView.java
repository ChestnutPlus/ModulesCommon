package com.chestnut.RouterArchitecture.ModulesCommon.surfaceAnimation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.annotation.IntDef;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/9/23 11:58
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public final class FrameAnimationSurfaceView {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private ArrayList<MyFrame> frames;

    private boolean isLoaded = false;
    private boolean isStart = false;
    private boolean isPause = true;
    private boolean isEnd = true;
    private boolean isRelease = false;
    private LoadedCallback loadedCallback;
    private IsEndCallback isEndCallback;
    private IsStopCallback isStopCallback;

    public static final int MODE_ONCE = 1;
    public static final int MODE_INFINITE = 2;

    @IntDef({MODE_INFINITE, MODE_ONCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }
    @RepeatMode
    private int mode = MODE_INFINITE;

    private FrameAnimationSurfaceView() {}

    //Builder
    public static class Builder {

        private FrameAnimationSurfaceView frameAnimationSurfaceView;

        public Builder(SurfaceView surfaceView) {
            frameAnimationSurfaceView = new FrameAnimationSurfaceView();
            frameAnimationSurfaceView.surfaceView = surfaceView;
            frameAnimationSurfaceView.surfaceHolder = surfaceView.getHolder();
            frameAnimationSurfaceView.surfaceView.setZOrderOnTop(true);
            frameAnimationSurfaceView.surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
            frameAnimationSurfaceView.frames = new ArrayList<>();
            frameAnimationSurfaceView.surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }

        public Builder setMode(@RepeatMode int mode) {
            frameAnimationSurfaceView.mode = mode;
            return this;
        }

        public FrameAnimationSurfaceView build() {
            return frameAnimationSurfaceView;
        }
    }

    /**
     * 开启动画
     */
    public void start() {
        if (!isRelease) {
            if (isLoaded)
                _start();
            else
                loadedCallback = new LoadedCallback() {
                    @Override
                    public void onLoadStart() {

                    }

                    @Override
                    public void onLoadEnd() {
                        _start();
                    }
                };
        }
    }
    private void _start() {
        if (!isStart) {
            isEnd = false;
            isStart = true;
            isPause = false;
            fixedThreadPool.execute(() -> {
                while (!isEnd) {
                    for (int i = 0; i < frames.size(); i++) {
                        changeToFrame(i,surfaceHolder,frames);
                        try {
                            Thread.sleep(frames.get(i).duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (isPause) {
                            if (isEnd)
                                break;
                        }
                        if (isEnd)
                            break;
                    }
                    if (mode == MODE_ONCE) {
                        isStart = false;
                        isPause = true;
                        isEnd = true;
                        changeToFrame(-1,surfaceHolder,frames);
                        break;
                    }
                }
                if (isEndCallback != null)
                    isEndCallback.onEnd();
            });
        } else
            isPause = false;
    }

    public void pause() {
        if (isStart && !isRelease)
            isPause = true;
    }

    public void stop() {
        if (!isRelease) {
            if (frames.size() > 0)
                isEndCallback = () -> {
                    fixedThreadPool.execute(() -> {
                        changeToFrame(-1,surfaceHolder,frames);
                        isEndCallback = null;
                        if (isStopCallback!=null)
                            isStopCallback.stop();
                    });
                };
            isEnd = true;
            isStart = false;
        }
    }

    public void release() {
        if (!isRelease) {
            isRelease = true;
            isEndCallback = () -> {
                changeToFrame(-1,surfaceHolder,frames);
                isEndCallback = null;
                fixedThreadPool.shutdownNow();
                surfaceView = null;
                surfaceHolder = null;
                frames.clear();
                frames = null;
            };
            isEnd = true;
        }
    }

    public FrameAnimationSurfaceView setMode(@RepeatMode int mode) {
        this.mode = mode;
        return this;
    }

    private void changeToFrame(int position, SurfaceHolder surfaceHolder, List<MyFrame> frames) {
        if (position==-1) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        else if (frames!=null && position<frames.size()-1 && surfaceHolder!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(frames.get(position).bytes, 0, frames.get(position).bytes.length);
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null)
                return;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(bitmap, 0, 0, null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 加载资源
     * @param resourceId    id
     * @param context   context
     */
    public FrameAnimationSurfaceView load(final int resourceId, final Context context) {
        isLoaded = false;
        if (isStart) {
            isStopCallback = () -> {
                _load(resourceId, context);
                isStopCallback = null;
            };
            stop();
        }
        else {
            _load(resourceId, context);
        }
        return this;
    }

    private void _load(final int resourceId, final Context context) {
        frames.clear();
        fixedThreadPool.execute(() -> {
            if (loadedCallback != null)
                loadedCallback.onLoadStart();
            XmlResourceParser parser = context.getResources().getXml(resourceId);
            try {
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {

                    } else if (eventType == XmlPullParser.START_TAG) {

                        if (parser.getName().equals("item")) {
                            byte[] bytes = null;
                            int duration = 1000;

                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                if (parser.getAttributeName(i).equals("drawable")) {
                                    int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                                    bytes = IOUtils.toByteArray(context.getResources().openRawResource(resId));
                                } else if (parser.getAttributeName(i).equals("duration")) {
                                    duration = parser.getAttributeIntValue(i, 1000);
                                }
                            }

                            MyFrame myFrame = new MyFrame();
                            myFrame.bytes = bytes;
                            myFrame.duration = duration;
                            frames.add(myFrame);
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {

                    } else if (eventType == XmlPullParser.TEXT) {

                    }
                    eventType = parser.next();
                }
                if (loadedCallback != null)
                    loadedCallback.onLoadEnd();
                loadedCallback = null;
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            isLoaded = true;
        });
    }

    /**
     * 每一帧对象
     */
    public static class MyFrame {
        byte[] bytes;
        int duration;
        Bitmap bitmap;
        boolean isReady = false;
    }

    /**
     * 加载资源的回调
     */
    private interface LoadedCallback {
        void onLoadStart();
        void onLoadEnd();
    }

    private interface IsEndCallback {
        void onEnd();
    }

    private interface IsStopCallback {
        void stop();
    }
}
