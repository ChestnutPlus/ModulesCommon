package com.chestnut.RouterArchitecture.ModulesCommon.fun.helperCamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.helper.CameraHelper;

public class CameraHelperActivity extends AppCompatActivity {

    private CameraHelper cameraHelper = new CameraHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_helper);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        cameraHelper.init(surfaceView,90);
        cameraHelper.setAutoFocus(1000);

        surfaceView.setOnClickListener(view -> {
            cameraHelper.setAutoFocus();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraHelper.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraHelper.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraHelper.stopPreview();
        cameraHelper.release();
    }
}
