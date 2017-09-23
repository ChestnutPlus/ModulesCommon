package com.chestnut.RouterArchitecture.ModulesCommon.imgFrameAnimation;

import android.widget.ImageView;

/**
 * Created by cheng on 2017/2/9.
 */

public class OnceAnimationDrawable extends HyAnimationDrawable {
    private boolean isPlaying;
    private Runnable onCompleteRunnable;
    private boolean stoped;

    public OnceAnimationDrawable() {
        isPlaying = false;
        stoped = false;
    }

    public OnceAnimationDrawable(ImageView mImageView, HyAnimation mResourceId) {
        super(mImageView, mResourceId);
        isPlaying = false;
        stoped = false;
        onComplete = new Runnable() {
            @Override
            public void run() {
                isPlaying = false;
                if(onCompleteRunnable != null) {
                    onCompleteRunnable.run();
                }
            }
        };
    }

    public void start() {
        stoped = false;
        super.setStop(false);
    }

    @Override
    public void stop() {
        isPlaying = false;
        stoped = true;
        super.stop();
    }

    @Override
    public void playAnimate() {
        if(isPlaying) {
            return;
        }
        if(stoped) {
            return;
        }
        isPlaying = true;
        super.playAnimate();
    }

    @Override
    public void setOnComplete(final Runnable onComplete) {
        onCompleteRunnable = onComplete;
    }
}
