package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chestnut.RouterArchitecture.ModulesCommon.surfaceAnimation.FrameAnimation;

public class TestSurfaceViewActivity2 extends AppCompatActivity {

    private FrameAnimation mFrameAnimation;
    private String TAG = "TestSurface";
    private int[] srcId = {
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0001,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0002,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0003,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0004,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0005,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0006,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0007,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0008,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0009,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0010,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0011,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0012,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0013,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0014,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0015,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0016,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0017,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0018,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0019,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0020,
            R.mipmap.p03_h_ch_nhold_to_htalk_to_htalk0021,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface_view2);
        mFrameAnimation = (FrameAnimation) findViewById(R.id.frame_animation);
        //设置资源文件
        mFrameAnimation.setBitmapResoursID(srcId);
        //设置监听事件
        mFrameAnimation.setOnFrameFinisedListener(new FrameAnimation.OnFrameFinishedListener() {
            @Override
            public void onStop() {
                Log.e(TAG, "stop");

            }

            @Override
            public void onStart() {
                Log.e(TAG, "start");
                Log.e(TAG, Runtime.getRuntime().totalMemory() / 1024 + "k");
            }
        });

        //设置单张图片展示时长
        mFrameAnimation.setGapTime(20);
        mFrameAnimation.start();
    }
}
