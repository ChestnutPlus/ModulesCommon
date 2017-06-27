package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chestnut.Common.Helper.SoundPoolHelper;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    private SoundPoolHelper soundPoolHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化，指定是最大3个Stream流，使用默认的Stream：TYPE_MUSIC
        soundPoolHelper = new SoundPoolHelper(3,SoundPoolHelper.TYPE_MUSIC)
                .setRingtoneType(SoundPoolHelper.RING_TYPE_MUSIC)
                //加载默认音频，因为上面指定了，所以其默认是：RING_TYPE_MUSIC
                //加载了Test_1,Test_2
                .loadDefault(MainActivity.this)
                .load(MainActivity.this,"happy1",R.raw.happy1)
                .load(MainActivity.this,"happy2",R.raw.happy2);


        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPoolHelper.playDefault();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPoolHelper.play("happy1",true);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPoolHelper.play("happy2",false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPoolHelper.release();
    }
}
