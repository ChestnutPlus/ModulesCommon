package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.chestnut.Common.Helper.MediaPlayerHelper;
import com.chestnut.Common.ui.Toastc;
import com.chestnut.Common.utils.LogUtils;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    MediaPlayerHelper mediaPlayerHelper;
    private Toastc toast;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        img4 = (ImageView) findViewById(R.id.img_4);

        toast = new Toastc(this, Toast.LENGTH_LONG);
        mediaPlayerHelper = new MediaPlayerHelper();

        findViewById(R.id.btn_1).setOnClickListener(view -> {
            toast.setText("btn_1").show();
            mediaPlayerHelper.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/FenShou.mp3");
        });
        findViewById(R.id.btn_2).setOnClickListener(view -> {
            toast.setText("btn_2").show();
            mediaPlayerHelper.pause();
        });
        findViewById(R.id.btn_3).setOnClickListener(view -> {
            toast.setText("btn_3").show();
            mediaPlayerHelper.stop();
        });
        findViewById(R.id.btn_4).setOnClickListener(view -> {
            toast.setText("btn_4").show();
            mediaPlayerHelper.play();
        });
        findViewById(R.id.btn_5).setOnClickListener(view -> {
            toast.setText("btn_5").show();
            mediaPlayerHelper.play(new MediaPlayerHelper.CallBack() {
                @Override
                public void onStart() {
                    LogUtils.i(OpenLog,TAG,"onStart");
                }

                @Override
                public void onReStart() {
                    LogUtils.i(OpenLog,TAG,"onReStart");
                }

                @Override
                public void onCompleted() {
                    LogUtils.i(OpenLog,TAG,"onCompleted");
                }

                @Override
                public void onStop() {
                    LogUtils.i(OpenLog,TAG,"onStop");
                }

                @Override
                public void onPause() {
                    LogUtils.i(OpenLog,TAG,"onPause");
                }

                @Override
                public void onError() {
                    LogUtils.i(OpenLog,TAG,"onError");
                }
            });
        });
        findViewById(R.id.btn_6).setOnClickListener(view -> {
            toast.setText("btn_6").show();
            mediaPlayerHelper.release();
        });
        findViewById(R.id.btn_7).setOnClickListener(view -> {
            toast.setText("btn_7").show();
        });
        findViewById(R.id.btn_8).setOnClickListener(view -> {
            toast.setText("btn_8").show();
        });
        findViewById(R.id.btn_9).setOnClickListener(view -> {
            toast.setText("btn_9").show();
        });
        findViewById(R.id.btn_10).setOnClickListener(view -> {
            toast.setText("btn_10").show();
        });
        findViewById(R.id.btn_11).setOnClickListener(view -> {
            toast.setText("btn_11").show();
        });
        findViewById(R.id.btn_12).setOnClickListener(view -> {
            toast.setText("btn_12").show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
