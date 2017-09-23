package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.chestnut.Common.utils.LogUtils;
import com.chestnut.RouterArchitecture.ModulesCommon.surfaceAnimation.FrameAnimationSurfaceView;

public class DiyTestSurfaceViewActivity extends Activity {

    private boolean OpenLog = true;
    private String TAG = "DiyTestSurfaceViewActivity";
    private FrameAnimationSurfaceView frameAnimationSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_test_surface_view);
        LogUtils.i(OpenLog,TAG,"main-Thread:"+android.os.Process.myTid());
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        frameAnimationSurfaceView = new FrameAnimationSurfaceView.Builder(surfaceView)
                .setMode(FrameAnimationSurfaceView.MODE_INFINITE)
                .build();
        frameAnimationSurfaceView.load(R.drawable.spark_list,this);

        findViewById(R.id.btn_pause).setOnClickListener(v -> {
            frameAnimationSurfaceView.pause();
        });
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            frameAnimationSurfaceView.start();
        });
        findViewById(R.id.btn_stop).setOnClickListener(v -> {
            frameAnimationSurfaceView.stop();
        });
        findViewById(R.id.btn_release).setOnClickListener(v -> {
            frameAnimationSurfaceView.release();
        });
        findViewById(R.id.btn_change).setOnClickListener(v -> {
            frameAnimationSurfaceView
                    .setMode(FrameAnimationSurfaceView.MODE_ONCE)
                    .load(R.drawable.presenter_enter_list,this)
                    .start();
        });
    }
}




















