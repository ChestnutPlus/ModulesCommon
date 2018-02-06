package com.chestnut.RouterArchitecture.ModulesCommon.fun.diySurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.utils.LogUtils;

public class DiyTestSurfaceViewActivity extends Activity {

    private boolean OpenLog = true;
    private String TAG = "DiyTestSurfaceViewActivity";
    private FrameAnimationSurfaceView frameAnimationSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_test_surface_view);
        LogUtils.i(OpenLog,TAG,"activity_blue_tooth_play_record-Thread:"+android.os.Process.myTid());
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        frameAnimationSurfaceView = new FrameAnimationSurfaceView.Builder(surfaceView)
                .setMode(FrameAnimationSurfaceView.MODE_INFINITE)
                .build();
        frameAnimationSurfaceView.setCallback(new FrameAnimationSurfaceView.Callback() {
            @Override
            public void onStart() {
                LogUtils.i(OpenLog,TAG,"onStart-Thread:"+android.os.Process.myTid());
            }

            @Override
            public void onEnd() {
                LogUtils.i(OpenLog,TAG,"onEnd-Thread:"+android.os.Process.myTid());
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        frameAnimationSurfaceView.release();
    }
}




















