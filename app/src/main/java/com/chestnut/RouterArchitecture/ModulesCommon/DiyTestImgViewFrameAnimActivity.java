package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.chestnut.RouterArchitecture.ModulesCommon.imgFrameAnimation.HyAnimation;
import com.chestnut.RouterArchitecture.ModulesCommon.imgFrameAnimation.LoopAnimationDrawable;

public class DiyTestImgViewFrameAnimActivity extends Activity {

    private LoopAnimationDrawable presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_test_img_view_frame_anim);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        presenter = new LoopAnimationDrawable(imageView,new HyAnimation(R.drawable.spark_list, 0));
        presenter.playAnimate();
    }
}
