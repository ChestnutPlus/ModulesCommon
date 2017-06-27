package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chestnut.Common.Helper.SoundPoolHelper;
import com.chestnut.Common.utils.LightUtils;
import com.chestnut.Common.utils.LogUtils;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    private SoundPoolHelper soundPoolHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPoolHelper = new SoundPoolHelper();

        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPoolHelper.loadDefault(MainActivity.this).playDefault();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e(OpenLog,TAG,"subLight100:"+LightUtils.subLight100(MainActivity.this));
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e(OpenLog,TAG,"App100:"+LightUtils.getAppLight100(MainActivity.this));
            }
        });
    }
}
