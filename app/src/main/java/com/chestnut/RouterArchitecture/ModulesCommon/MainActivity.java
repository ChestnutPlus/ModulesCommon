package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.Common.Helper.MediaPlayerHelper;
import com.chestnut.Common.Helper.bean.RxMediaPlayerBean;
import com.chestnut.Common.ui.Toastc;
import com.chestnut.Common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    MediaPlayerHelper mediaPlayerHelper;
    private Toastc toast;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    TextView txtLog;
    SeekBar seekBar1;
    SeekBar seekBar2;
    List<String> logs;

    int btnIds[] = {
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8,
            R.id.btn_9,
            R.id.btn_10,
            R.id.btn_11,
            R.id.btn_12,
    };

    String toastAndBtnName[] = {
            "1_"+"加载本地mp3",
            "2_"+"pause",
            "3_"+"stop",
            "4_"+"play",
            "5_"+"play_callback",
            "6_"+"release",
            "7_"+"加载本地raw",
            "8_"+"rxPlay",
            "9_"+"加载在线mp3",
            "10_"+"",
            "11_"+"",
            "12_"+"",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logs = new ArrayList<>();

        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        img4 = (ImageView) findViewById(R.id.img_4);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar_1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar_2);

        toast = new Toastc(this, Toast.LENGTH_LONG);
        mediaPlayerHelper = new MediaPlayerHelper().init(this);

        TextView textView = null;
        txtLog = (TextView) findViewById(R.id.txt_log);
        for (int i = 0; i < btnIds.length; i++) {
            textView = (TextView) findViewById(btnIds[i]);
            textView.setText(toastAndBtnName[i]);
            textView.setOnClickListener(onClickListener);
            textView.setTag(i);
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toast.setText("1:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toast.setText("1:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toast.setText("2:"+seekBar.getProgress()).show();
                mediaPlayerHelper.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toast.setText("2:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerHelper.release();
    }

    private void viewLog(String TAG,String msg) {
        if (logs.size()>=6)
            logs.remove(0);
        logs.add(TAG+"\t\t"+msg);
        StringBuilder result = new StringBuilder();
        for (String s :
                logs) {
            result.append(s).append("\n");
        }
        txtLog.setText(result.toString());
    }

    private View.OnClickListener onClickListener = view -> {
        toast.setText(toastAndBtnName[(int) view.getTag()]).show();
        LogUtils.i(OpenLog,TAG,"btn-info:"+toastAndBtnName[(int) view.getTag()]);
        viewLog(TAG,toastAndBtnName[(int) view.getTag()]);
        switch (view.getId()) {
            case R.id.btn_1:
                mediaPlayerHelper.setUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/FenShou.mp3");
                break;
            case R.id.btn_2:
                mediaPlayerHelper.pause();
                break;
            case R.id.btn_3:
                mediaPlayerHelper.stop();
                break;
            case R.id.btn_4:
                mediaPlayerHelper.play();
                break;
            case R.id.btn_5:
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
                break;
            case R.id.btn_6:
                mediaPlayerHelper.release();
                break;
            case R.id.btn_7:
                mediaPlayerHelper.setUrl(R.raw.xiayao);
                break;
            case R.id.btn_8:
                mediaPlayerHelper.rxPlay()
                        .subscribe(rxMediaPlayerBean -> {
                            switch (rxMediaPlayerBean.status) {
                                case RxMediaPlayerBean.ON_START:
                                    LogUtils.e(OpenLog,TAG,"ON_START");
                                    break;
                                case RxMediaPlayerBean.ON_COMPLETED:
                                    LogUtils.e(OpenLog,TAG,"ON_COMPLETED");
                                    break;
                                case RxMediaPlayerBean.ON_ERROR:
                                    LogUtils.e(OpenLog,TAG,"ON_ERROR");
                                    break;
                                case RxMediaPlayerBean.ON_PAUSE:
                                    LogUtils.e(OpenLog,TAG,"ON_PAUSE");
                                    break;
                                case RxMediaPlayerBean.ON_RESTART:
                                    LogUtils.e(OpenLog,TAG,"ON_RESTART");
                                    break;
                                case RxMediaPlayerBean.ON_STOP:
                                    LogUtils.e(OpenLog,TAG,"ON_STOP");
                                    break;
                            }
                        });
                break;
            case R.id.btn_9:
                mediaPlayerHelper.setUrl("http://mp3.flash127.com/music/6010.qcc?.mp3");
                break;
            case R.id.btn_10:
                break;
        }
    };
}
