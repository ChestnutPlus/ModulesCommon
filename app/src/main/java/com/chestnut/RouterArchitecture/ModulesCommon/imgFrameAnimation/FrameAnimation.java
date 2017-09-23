package com.chestnut.RouterArchitecture.ModulesCommon.imgFrameAnimation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/24 17:34
 *     desc  :  帧动画，为了解决，图片过多的时候，出现了OOM
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class FrameAnimation {

    private Callback callback;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private ArrayList<MyFrame> myFrameList;
    private int resourceId;
    private ImageView imageView;

    private boolean isPause = true;
    private boolean init = false;
    private boolean isRelease = false;
    private int indexFrame = 0;

    /**
     * 设置监听器
     * @param callback  回调
     */
    public FrameAnimation setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public FrameAnimation setRes(@DrawableRes int resourceId, ImageView imageView) {
        this.resourceId = resourceId;
        this.imageView = imageView;
        return this;
    }

    public FrameAnimation start() {
        if (!isRelease && getPause()) {
            setPause(false);
            if (indexFrame == 0) {
                if (!init)
                    loadFromXml(resourceId, imageView);
                else
                    animateRawManually(imageView, indexFrame);
            } else {
                animateRawManually(imageView, indexFrame);
            }
        }
        return this;
    }

    public FrameAnimation release() {
        if (!isRelease) {
            isRelease = true;
            setPause(true);
            if (singleThreadExecutor != null) {
                singleThreadExecutor.shutdownNow();
                singleThreadExecutor = null;
            }
            if (myFrameList != null) {
                new Handler(imageView.getContext().getMainLooper()).postDelayed(
                        () -> imageView.setImageDrawable(getDrawable(myFrameList.get(0))),200);
            }
        }
        return this;
    }

    /**
     * 从XML中加载动画，必须是
     *  animation-list
     *
     * @param resourceId    资源id
     * @param imageView   imageView
     */
    private void loadFromXml(int resourceId, ImageView imageView) {
        this.imageView = imageView;
        Context context = imageView.getContext();
        singleThreadExecutor.execute(()->{
            myFrameList = new ArrayList<>();
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
                                if (parser.getAttributeName(i).equals(
                                        "drawable")) {
                                    int resId = Integer.parseInt(parser
                                            .getAttributeValue(i)
                                            .substring(1));
                                    bytes = IOUtils.toByteArray(context
                                            .getResources()
                                            .openRawResource(resId));
                                } else if (parser.getAttributeName(i)
                                        .equals("duration")) {
                                    duration = parser.getAttributeIntValue(
                                            i, 1000);
                                }
                            }

                            MyFrame myFrame = new MyFrame();
                            myFrame.bytes = bytes;
                            myFrame.duration = duration;
                            myFrameList.add(myFrame);
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {

                    } else if (eventType == XmlPullParser.TEXT) {

                    }
                    eventType = parser.next();
                    init = true;
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            // Run on UI Thread
            new Handler(context.getMainLooper()).post(() -> {
                if (callback != null)
                    callback.onFrameLoaded(myFrameList.size());
                //启动动画
                if (callback!=null)
                    callback.onFrameAnimStart(myFrameList.size());
                if (!getPause() && singleThreadExecutor!=null && !singleThreadExecutor.isShutdown())
                    singleThreadExecutor.execute(()->animateRawManually(imageView,indexFrame));
            });
        });
    }

    /**
     * 手动执行动画
     *  其实是一帧一帧地替换动画
     * @param imageView imageView
     * @param frameNumber 帧序号
     */
    private void animateRawManually(ImageView imageView, int frameNumber) {
        //从res中加载为bitmap
        final MyFrame thisFrame = myFrameList.get(frameNumber);
        if (frameNumber == 0) {
            thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), BitmapFactory.decodeByteArray(
                    thisFrame.bytes, 0, thisFrame.bytes.length));
        } else {
            MyFrame previousFrame = myFrameList.get(frameNumber - 1);
            if (previousFrame.drawable!=null)
                ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;
            previousFrame.isReady = false;
        }
        new Handler(imageView.getContext().getMainLooper()).post(() -> {
            imageView.setImageDrawable(getDrawable(thisFrame));
            if (callback!=null) {
                callback.onFrameAnimChange(myFrameList.size(), frameNumber);
            }
        });
        new Handler(imageView.getContext().getMainLooper()).postDelayed(() -> {
            // Make sure ImageView hasn't been changed to a different Image
            // in this time
            if (imageView.getDrawable() == thisFrame.drawable) {
                //非最后一帧
                if (frameNumber + 1 < myFrameList.size()) {
                    MyFrame nextFrame = myFrameList.get(frameNumber + 1);
                    if (nextFrame.isReady) {
                        // Animate next frame
                        indexFrame = frameNumber + 1;
                        animateRawManually(imageView, indexFrame);

                    } else {
                        nextFrame.isReady = true;
                    }
                }
                //最后一帧
                else {
                    if (callback != null) {
                        callback.onFrameAnimEnd(myFrameList.size());
                    }
                    indexFrame = 0;
                    isPause = true;
                }
            }
        }, thisFrame.duration);
        // Load next frame
        if (frameNumber + 1 < myFrameList.size() && !getPause() && singleThreadExecutor!=null && !singleThreadExecutor.isShutdown()) {
            singleThreadExecutor.execute(() -> {
                MyFrame nextFrame = myFrameList.get(frameNumber + 1);
                nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
                if (nextFrame.isReady) {
                    // Animate next frame
                    indexFrame = frameNumber + 1;
                    animateRawManually(imageView, indexFrame);
                } else {
                    nextFrame.isReady = true;
                }
            });
        }
    }

    private synchronized void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    private synchronized boolean getPause() {
        return isPause;
    }

    private Drawable getDrawable(MyFrame myFrame) {
        if (myFrame.drawable==null || ((BitmapDrawable) myFrame.drawable).getBitmap()!=null || ((BitmapDrawable) myFrame.drawable).getBitmap().isRecycled())
            myFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(myFrame.bytes, 0, myFrame.bytes.length));
        return myFrame.drawable;
    }

    /*类，接口*/
    //回调
    public abstract static class Callback {
        public void onFrameLoaded(int framesSize){}
        public void onFrameAnimStart(int framesSize){}
        public void onFrameAnimChange(int framesSize,int index){}
        public void onFrameAnimEnd(int framesSize){}
    }
    //每一帧对象
    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
    }
}
