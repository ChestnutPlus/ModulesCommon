package com.chestnut.RouterArchitecture.ModulesCommon.fun.imgFrameAnimation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cheng on 2017/2/8.
 */

public class HyAnimationDrawable {
    public static class HyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
    }

    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(List<HyFrame> myFrames);
    }

    protected ImageView mImageView;
    protected HyAnimation animation;
    protected Runnable onComplete = null;
    private Boolean isStop;
    private int playingId;
    private boolean needReplaySound;
    private Thread loadThread;
    private UUID uid;
    private String tag;

    public HyAnimationDrawable() {
        uid = UUID.randomUUID();
    }

    public HyAnimationDrawable(ImageView mImageView, HyAnimation animation) {
        this.mHandler = new Handler(mImageView.getContext().getMainLooper());
        this.mImageView = mImageView;
        this.animation = animation;
        this.isStop = false;
        this.needReplaySound = false;
        this.playingId = 0;
        uid = UUID.randomUUID();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public HyAnimation getResource() {
        return animation;
    }

    public void setResourceId(HyAnimation mResourceId) {
        this.animation = mResourceId;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public void playAnimate() {
        if(this.mImageView != null && this.mImageView.getVisibility() == View.VISIBLE) {
//            Log.d("HyAnimationDrawable", "playAnimate: " + getTag());
            animateRawManuallyFromXML(this.animation.getAnimationResourceId(), this.mImageView, null, this.onComplete);
        }
    }

    public void stop() {
            isStop = true;
    }

    public void unstop() {
        isStop = false;
    }

    public void setNeedReplaySound(boolean needReplaySound) {
        this.needReplaySound = needReplaySound;
    }

    private void animateRawManuallyFromXML(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete) {
        if(resourceId > 0) {
            loadRaw(resourceId, imageView.getContext(),
                    new OnDrawableLoadedListener() {
                        @Override
                        public void onDrawableLoaded(List<HyFrame> myFrames) {
                            if (onStart != null) {
                                onStart.run();
                            }
                            if (playingId > 0 && needReplaySound) {
                                needReplaySound = false;
                                //SoundMenager.getInstance().stop(playingId);
                                playingId = 0;
                            }
                            if (animation.getSoundResourceId() > 0) {
                                //HyAnimationDrawable.this.playingId = SoundMenager.getInstance().play(animation.getSoundResourceId());
//                            Log.d("test", uid.toString() + "play sound:" + String.valueOf(animation.getSoundResourceId()));
                            }
                            animateRawManually(myFrames, imageView, onComplete);
                        }
                    });
        }
    }

    // 2
    private void loadRaw(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        loadFromXml(resourceId, context, onDrawableLoadedListener);
    }

    // 3
    private void loadFromXml(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        if(loadThread != null) {
            loadThread.interrupt();
        }
        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HyFrame> myFrames = new ArrayList<HyFrame>();
//                Log.d("test", "load animation:" + String.valueOf(resourceId));
                XmlResourceParser parser = context.getResources().getXml(resourceId);

                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (HyAnimationDrawable.this.isStop != null && isStop) {
//                            Log.d("test", "animation stop!");
                            HyAnimationDrawable.this.isStop = false;
                            myFrames.clear();
                            return;
                        }

                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("item")) {
                                byte[] bytes = null;
                                int duration = 10;

                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals("drawable")) {
                                        int resId = Integer.parseInt(parser
                                                .getAttributeValue(i)
                                                .substring(1));
                                        bytes = IOUtils.toByteArray(context
                                                .getResources()
                                                .openRawResource(resId));
                                    } else if (parser.getAttributeName(i).equals("duration")) {
                                        duration = parser.getAttributeIntValue(i, 10);
                                    }
                                }

                                HyFrame myFrame = new HyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                myFrames.add(myFrame);
                            }

                        } else if (eventType == XmlPullParser.END_TAG) {

                        } else if (eventType == XmlPullParser.TEXT) {

                        }

                        eventType = parser.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }

                if(Thread.interrupted()) {
                    return;
                }

                if(mHandler != null) {
                    mHandler.removeCallbacks(loadedRunnable);
                    loadedRunnable.setFrames(myFrames);
                    loadedRunnable.setOnDrawableLoadedListener(onDrawableLoadedListener);
                    mHandler.post(loadedRunnable);
                }
                // Run on UI Thread
//                new Handler(context.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (onDrawableLoadedListener != null) {
//                            onDrawableLoadedListener.onDrawableLoaded(myFrames);
//                        }
//                    }
//                });
            }
        });
        loadThread.run();
    }

    // 4
    private void animateRawManually(List<HyFrame> myFrames, ImageView imageView, Runnable onComplete) {
        animateRawManually(myFrames, imageView, onComplete, 0);
    }

    // 5
    private void animateRawManually(final List<HyFrame> myFrames, final ImageView imageView, final Runnable onComplete, final int frameNumber) {
        if(myFrames != null && myFrames.size() > 0) {
            if (isStop != null && isStop) {
                isStop = false;
//                HyFrame previousFrame;
//                HyFrame thisFrame;
//                if(frameNumber == 0) {
//                    thisFrame = myFrames.get(frameNumber);
//                    thisFrame.drawable = new BitmapDrawable(imageView.getContext()
//                            .getResources(), BitmapFactory.decodeByteArray(
//                            thisFrame.bytes, 0, thisFrame.bytes.length));
//                    imageView.setImageDrawable(thisFrame.drawable);
//                } else if(frameNumber == 1){
//                    previousFrame = myFrames.get(frameNumber);
//                    if(previousFrame != null) {
//                        if(previousFrame.drawable != null) {
//                            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
//                            previousFrame.drawable = null;
//                        }
//                        previousFrame.isReady = false;
//                    }
//
//                    thisFrame = myFrames.get(frameNumber - 1);
//                    imageView.setImageDrawable(thisFrame.drawable);
//                } else {
//                    previousFrame = myFrames.get(frameNumber - 1);
//                    if(previousFrame != null) {
//                        if(previousFrame.drawable != null) {
//                            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
//                            previousFrame.drawable = null;
//                        }
//                        previousFrame.isReady = false;
//                    }
//
//                    previousFrame = myFrames.get(frameNumber);
//                    if(previousFrame != null) {
//                        if(previousFrame.drawable != null) {
//                            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
//                            previousFrame.drawable = null;
//                        }
//                        previousFrame.isReady = false;
//                    }
//
//                    thisFrame = myFrames.get(0);
//                    thisFrame.drawable = new BitmapDrawable(imageView.getContext()
//                            .getResources(), BitmapFactory.decodeByteArray(
//                            thisFrame.bytes, 0, thisFrame.bytes.length));
//                    imageView.setImageDrawable(thisFrame.drawable);
//                }


                if(frameNumber == myFrames.size() - 1) {
                    HyFrame previousFrame = myFrames.get(frameNumber - 1);
                    HyFrame thisFrame = myFrames.get(frameNumber);

                    if(previousFrame != null) {
                        if(previousFrame.drawable != null) {
                            Bitmap bitmap = ((BitmapDrawable) previousFrame.drawable).getBitmap();
                            if(bitmap != null) {
                                bitmap.recycle();
                            }
                            previousFrame.drawable = null;
                        }
                        previousFrame.isReady = false;
                    }
                    imageView.setImageDrawable(thisFrame.drawable);
                } else {
                    HyFrame previousFrame;
                    HyFrame thisFrame = myFrames.get(myFrames.size() - 1);
                    if(frameNumber != 0) {
                        previousFrame = myFrames.get(frameNumber - 1);
                        if(previousFrame != null) {
                            if(previousFrame.drawable != null) {
                                Bitmap bitmap = ((BitmapDrawable) previousFrame.drawable).getBitmap();
                                if(bitmap != null) {
                                    bitmap.recycle();
                                }
                                previousFrame.drawable = null;
                            }
                            previousFrame.isReady = false;
                        }
                    }

                    previousFrame = myFrames.get(frameNumber);
                    if(previousFrame != null) {
                        if (previousFrame.drawable != null) {
                            Bitmap bitmap = ((BitmapDrawable) previousFrame.drawable).getBitmap();
                            if(bitmap != null) {
                                bitmap.recycle();
                            }
                            previousFrame.drawable = null;
                        }
                        previousFrame.isReady = false;
                    }

                    thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                            .getResources(), BitmapFactory.decodeByteArray(
                            thisFrame.bytes, 0, thisFrame.bytes.length));
                    imageView.setImageDrawable(thisFrame.drawable);
                }
                myFrames.clear();
                return;
            }
            final HyFrame thisFrame = myFrames.get(frameNumber);

            if (frameNumber == 0) {
                thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                        .getResources(), BitmapFactory.decodeByteArray(
                        thisFrame.bytes, 0, thisFrame.bytes.length));
            } else {
                HyFrame previousFrame = myFrames.get(frameNumber - 1);
                if(previousFrame != null) {
                    if (previousFrame.drawable != null) {
                        Bitmap bitmap = ((BitmapDrawable) previousFrame.drawable).getBitmap();
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }
                    previousFrame.drawable = null;
                    previousFrame.isReady = false;
                }
            }

            imageView.setImageDrawable(thisFrame.drawable);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Make sure ImageView hasn't been changed to OnScreenChangeListener different Image
                    // in this time
                    if (imageView.getDrawable() == thisFrame.drawable) {
                        if (frameNumber + 1 < myFrames.size()) {
                            HyFrame nextFrame = myFrames.get(frameNumber + 1);

                            if (nextFrame.isReady) {
                                // Animate next frame
                                animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
                            } else {
                                nextFrame.isReady = true;
                            }
                        } else {
                            if (onComplete != null) {
                                onComplete.run();
                            }
                            myFrames.clear();
                        }
                    } else {
                        myFrames.clear();
                    }
                }
            }, thisFrame.duration);

            // Load next frame
            if (frameNumber + 1 < myFrames.size()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HyFrame nextFrame = myFrames.get(frameNumber + 1);
                        nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
                        if (nextFrame.isReady) {
                            // Animate next frame
                            animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
                        } else {
                            nextFrame.isReady = true;
                        }
                    }
                }).run();
            }
        }
    }

    private Handler mHandler;
    private loadCompleteRunnable loadedRunnable = new loadCompleteRunnable();

    private class loadCompleteRunnable implements Runnable {
        private ArrayList<HyFrame> mFrames;
        private OnDrawableLoadedListener onDrawableLoadedListener;
        @Override
        public void run() {
            if (onDrawableLoadedListener != null && mFrames != null) {
                onDrawableLoadedListener.onDrawableLoaded(mFrames);
            }
        }

        public void setFrames(ArrayList<HyFrame> mFrames) {
            this.mFrames = mFrames;
        }

        public void setOnDrawableLoadedListener(OnDrawableLoadedListener onDrawableLoadedListener) {
            this.onDrawableLoadedListener = onDrawableLoadedListener;
        }
    }
}
