package com.chestnut.RouterArchitecture.ModulesCommon.fun.imgFrameAnimation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FrameAnimationDrawable {

    private DrawableCallback drawableCallback;

    public interface DrawableCallback {
        void onDrawableLoaded(int framesSize);
        void onStart(int framesSize);
        void onFrameChange(int framesSize, int index);
        void onEnd(int framesSize);
    }

    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
    }

    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(List<MyFrame> myFrames);
    }

    public void animateRawManuallyFromXML(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete, DrawableCallback drawableCallback) {
        animateRawManuallyFromXML(resourceId, imageView, onStart, onComplete);
        this.drawableCallback = drawableCallback;
    }

    public FrameAnimationDrawable animateRawManuallyFromXML(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete) {
        loadRaw(resourceId, imageView.getContext(),
                myFrames -> {
                    if (onStart != null) {
                        onStart.run();
                    }
                    animateRawManually(myFrames, imageView, onComplete);
                });
        return this;
    }

    //
    private void loadRaw(final int resourceId, final Context context,
                                final OnDrawableLoadedListener onDrawableLoadedListener) {
        loadFromXml(resourceId, context, onDrawableLoadedListener);
    }

    //
    private void loadFromXml(final int resourceId,
                                    final Context context,
                                    final OnDrawableLoadedListener onDrawableLoadedListener) {
        new Thread(() -> {
            final ArrayList<MyFrame> myFrames = new ArrayList<>();

            XmlResourceParser parser = context.getResources().getXml(
                    resourceId);

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
                            myFrames.add(myFrame);
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {

                    } else if (eventType == XmlPullParser.TEXT) {

                    }

                    eventType = parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            // Run on UI Thread
            new Handler(context.getMainLooper()).post(() -> {
                if (onDrawableLoadedListener != null) {
                    onDrawableLoadedListener.onDrawableLoaded(myFrames);
                    if (drawableCallback!=null)
                        drawableCallback.onDrawableLoaded(myFrames.size());
                }
            });
        }).run();
    }

    //
    private void animateRawManually(List<MyFrame> myFrames, ImageView imageView, Runnable onComplete) {
        animateRawManually(myFrames, imageView, onComplete, 0);
        if (drawableCallback!=null)
            drawableCallback.onStart(myFrames.size());
    }

    //
    private void animateRawManually(final List<MyFrame> myFrames, final ImageView imageView, final Runnable onComplete, final int frameNumber) {
        final MyFrame thisFrame = myFrames.get(frameNumber);

        if (frameNumber == 0) {
            thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), BitmapFactory.decodeByteArray(
                    thisFrame.bytes, 0, thisFrame.bytes.length));
        } else {
            MyFrame previousFrame = myFrames.get(frameNumber - 1);
            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;
            previousFrame.isReady = false;
        }

        imageView.setImageDrawable(thisFrame.drawable);
        new Handler().postDelayed(() -> {
            // Make sure ImageView hasn't been changed to a different Image
            // in this time
            if (imageView.getDrawable() == thisFrame.drawable) {
                if (frameNumber + 1 < myFrames.size()) {
                    MyFrame nextFrame = myFrames.get(frameNumber + 1);

                    if (nextFrame.isReady) {
                        // Animate next frame
                        animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
                        if (drawableCallback!=null) {
                            drawableCallback.onFrameChange(myFrames.size(), frameNumber);
                            if (myFrames.size()==frameNumber+2) {
                                drawableCallback.onEnd(myFrames.size());
                            }
                        }
                    } else {
                        nextFrame.isReady = true;
                    }
                } else {
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        }, thisFrame.duration);

        // Load next frame
        if (frameNumber + 1 < myFrames.size()) {
            new Thread(() -> {
                MyFrame nextFrame = myFrames.get(frameNumber + 1);
                nextFrame.drawable = new BitmapDrawable(imageView
                        .getContext().getResources(),
                        BitmapFactory.decodeByteArray(nextFrame.bytes, 0,
                                nextFrame.bytes.length));
                if (nextFrame.isReady) {
                    // Animate next frame
                    animateRawManually(myFrames, imageView, onComplete,
                            frameNumber + 1);
                } else {
                    nextFrame.isReady = true;
                }

            }).run();
        }
    }

    //带时间的方法
    public void animateManuallyFromRawResource(
            int animationDrawableResourceId, ImageView imageView,
            Runnable onStart, Runnable onComplete, int duration) throws IOException,
            XmlPullParserException {
        AnimationDrawable animationDrawable = new AnimationDrawable();

        XmlResourceParser parser = imageView.getContext().getResources()
                .getXml(animationDrawableResourceId);

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {

                if (parser.getName().equals("item")) {
                    Drawable drawable = null;

                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (parser.getAttributeName(i).equals("drawable")) {
                            int resId = Integer.parseInt(parser
                                    .getAttributeValue(i).substring(1));
                            byte[] bytes = IOUtils.toByteArray(imageView
                                    .getContext().getResources()
                                    .openRawResource(resId));//IOUtils.readBytes
                            drawable = new BitmapDrawable(imageView
                                    .getContext().getResources(),
                                    BitmapFactory.decodeByteArray(bytes, 0,
                                            bytes.length));
                        } else if (parser.getAttributeName(i)
                                .equals("duration")) {
                            duration = parser.getAttributeIntValue(i, 66);
                        }
                    }

                    animationDrawable.addFrame(drawable, duration);
                }

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {

            }

            eventType = parser.next();
        }

        if (onStart != null) {
            onStart.run();
        }
        animateDrawableManually(animationDrawable, imageView, onComplete, 0);
    }

    private void animateDrawableManually(
            final AnimationDrawable animationDrawable,
            final ImageView imageView, final Runnable onComplete,
            final int frameNumber) {
        final Drawable frame = animationDrawable.getFrame(frameNumber);
        imageView.setImageDrawable(frame);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make sure ImageView hasn't been changed to a different Image
                // in this time
                if (imageView.getDrawable() == frame) {
                    if (frameNumber + 1 < animationDrawable.getNumberOfFrames()) {
                        // Animate next frame
                        animateDrawableManually(animationDrawable, imageView,
                                onComplete, frameNumber + 1);
                    } else {
                        // Animation complete
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                }
            }
        }, animationDrawable.getDuration(frameNumber));
    }
}
