package com.chestnut.RouterArchitecture.ModulesCommon.fun.imgFrameAnimation;

import android.widget.ImageView;

/**
 * Created by cheng on 2017/2/9.
 */

public class LoopAnimationDrawable extends HyAnimationDrawable {
    private HyAnimation defultResourceId;
    private boolean isPlaying;
    private Runnable onCompleteRunnable;
    private boolean needLoop;

    public LoopAnimationDrawable() {
    }

    public LoopAnimationDrawable(ImageView mImageView, final HyAnimation mResourceId) {
        super(mImageView, mResourceId);
        defultResourceId = mResourceId;
        isPlaying = false;
        needLoop = true;
        onComplete = new Runnable() {
            @Override
            public void run() {
                isPlaying = false;
                if(onCompleteRunnable != null && LoopAnimationDrawable.this.animation != defultResourceId) {
                    onCompleteRunnable.run();
                }
                if(needLoop) {
                    LoopAnimationDrawable.this.animation = defultResourceId;
                    LoopAnimationDrawable.super.setNeedReplaySound(false);
                    LoopAnimationDrawable.super.playAnimate();
                }
            }
        };
    }

    @Override
    public void playAnimate() {
        if(isPlaying) {
            return;
        } else {
            if(!animation.equals(defultResourceId)) {
                isPlaying = true;
            }
            super.playAnimate();
        }
    }

    @Override
    public void stop() {
        isPlaying = false;
        animation = defultResourceId;
        super.stop();
    }

    @Override
    public void setOnComplete(final Runnable onComplete) {
        onCompleteRunnable = onComplete;
    }

    public HyAnimation getDefultResourceId() {
        return defultResourceId;
    }

    public void setDefultResourceId(HyAnimation defultResourceId) {
        this.defultResourceId = defultResourceId;
    }

    public boolean isNeedLoop() {
        return needLoop;
    }

    public void setNeedLoop(boolean needLoop) {
        this.needLoop = needLoop;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void playImmediately(HyAnimation value) {
        isPlaying = false;
        animation = value;
        setNeedReplaySound(true);
        playAnimate();
    }
}
