package com.chestnut.RouterArchitecture.ModulesCommon.imgFrameAnimation;

/**
 * Created by cheng on 2017/2/14.
 */

public class HyAnimation {
    private int animationResourceId;
    private int soundResourceId;

    public HyAnimation(int animationResourceId, int soundResourceId) {
        this.animationResourceId = animationResourceId;
        this.soundResourceId = soundResourceId;
    }

    public int getAnimationResourceId() {
        return animationResourceId;
    }

    public void setAnimationResourceId(int animationResourceId) {
        this.animationResourceId = animationResourceId;
    }

    public int getSoundResourceId() {
        return soundResourceId;
    }

    public void setSoundResourceId(int soundResourceId) {
        this.soundResourceId = soundResourceId;
    }
}
