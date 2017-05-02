package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chestnut.Common.Helper.AudioRecordHelper;
import com.chestnut.Common.Helper.MediaPlayerHelper;
import com.chestnut.Common.Helper.MusicHelper;
import com.chestnut.Common.utils.LogUtils;


public class MainActivity extends AppCompatActivity {

    private AudioRecordHelper audioRecordHelper = null;
    private MusicHelper musicHelper = null;
    private MediaPlayerHelper mediaPlayerHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioRecordHelper = new AudioRecordHelper();
        mediaPlayerHelper = new MediaPlayerHelper();
        musicHelper = new MusicHelper();
        audioRecordHelper.init(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/Test.wav");
        audioRecordHelper.setCallBack(new AudioRecordHelper.CallBack() {
            @Override
            public void onRecordStart(String file) {
                LogUtils.w("onRecordStart",file);
            }

            @Override
            public void onRecordDBChange(double dbValue) {
                LogUtils.w("onRecordDBChange",dbValue+"");
            }
            

            @Override
            public void onRecordFail(String file, String msg) {
                LogUtils.w("onRecordFail",file+","+msg);
            }

            @Override
            public void onRecordEnd(String file) {
                LogUtils.w("onRecordEnd",file);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecordHelper.startRecord();
//                AudioUtil.getInstance().startRecord();
//                AudioUtil.getInstance().recordData();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioRecordHelper.stopRecord();
//                AudioUtil.getInstance().stopRecord();
//                AudioUtil.getInstance().convertWaveFile();
            }
        });
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                musicHelper.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/Test.wav").play();
                startActivity(new Intent(MainActivity.this,ButtonsActivity.class));
                mediaPlayerHelper.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/FenShou.mp3")
                        .play(new MediaPlayerHelper.CallBack() {
                            @Override
                            public void onStart() {
                                LogUtils.w("onStart");
                            }

                            @Override
                            public void onRestart() {
                                LogUtils.w("onRestart");
                            }

                            @Override
                            public void onCompleted() {
                                LogUtils.w("onCompleted");
                            }

                            @Override
                            public void onStop() {
                                LogUtils.w("onStop");
                            }

                            @Override
                            public void onPause() {
                                LogUtils.w("onPause");
                            }

                            @Override
                            public void onError() {
                                LogUtils.w("onError");
                            }
                        });

//                musicHelper.setUrl(Environment.getExternalStorageDirectory().getAbsolutePath()+"/yinfu/yinfu.wav").play();
            }
        });
    }
}
