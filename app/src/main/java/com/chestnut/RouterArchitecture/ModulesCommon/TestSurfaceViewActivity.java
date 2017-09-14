package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.chestnut.RouterArchitecture.ModulesCommon.surfaceAnimation.SurfaceViewAnimation;

public class TestSurfaceViewActivity extends Activity {

    private SurfaceViewAnimation surfaceViewAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface_view);

        SurfaceView mSurfaceView=  (SurfaceView) findViewById(R.id.surface_view);
        surfaceViewAnimation= new SurfaceViewAnimation.Builder(mSurfaceView,"honey_happy")
                        .setRepeatMode(SurfaceViewAnimation.MODE_INFINITE)
                        .build();
        surfaceViewAnimation.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (surfaceViewAnimation!=null)
            surfaceViewAnimation.stop();
        surfaceViewAnimation = null;
    }
}
