package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.AppUtils;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.XFontUtils;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.subjects.PublishSubject;


public class MainActivity extends RxAppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    private XToast toast;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    TextView txtLog;
    SeekBar seekBar1;
    SeekBar seekBar2;
    List<String> logs;
    private PublishSubject<Integer> publishSubject;

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
            "1_"+"PayTest",
            "2_"+"蓝牙",
            "3_"+"",
            "4_"+"",
            "5_"+"",
            "6_"+"",
            "7_"+"",
            "8_"+"DiyTestImgViewFrameAnimActivity",
            "9_"+"DiyTestSurfaceViewActivity",
            "10_"+"TestSurfaceViewActivity",
            "11_"+"TestActivity",
            "12_"+"TimeLineActivity",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XFontUtils.getInstance().activitySetFont(this,"fonts/Test.TTF");
        logs = new ArrayList<>();

        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        img4 = (ImageView) findViewById(R.id.img_4);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar_1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar_2);
        toast = new XToast(this, Toast.LENGTH_LONG);
        toast.setTextTypeface(XFontUtils.getInstance().get("fonts/caonima.ttf"));
        toast.setTextSize(20);

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toast.setText("2:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        publishSubject = PublishSubject.create();
        publishSubject.throttleFirst(5, TimeUnit.SECONDS)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(integer -> {
                    LogUtils.i(OpenLog,TAG,String.valueOf(System.currentTimeMillis()));
                });
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
                startActivity(new Intent(this,PayActivity.class));
                break;
            case R.id.btn_2:
                startActivity(new Intent(this,RecordPlayActivity.class));
                break;
            case R.id.btn_3:
                break;
            case R.id.btn_4:
                break;
            case R.id.btn_5:
                break;
            case R.id.btn_6:
                break;
            case R.id.btn_7:
                break;
            case R.id.btn_8:
                startActivity(new Intent(this,DiyTestImgViewFrameAnimActivity.class));
                break;
            case R.id.btn_9:
                startActivity(new Intent(this,DiyTestSurfaceViewActivity.class));
                break;
            case R.id.btn_10:
                startActivity(new Intent(this,TestSurfaceViewActivity.class));
                break;
            case R.id.btn_11:
                startActivity(new Intent(this,TestActivity.class));
                break;
            case R.id.btn_12:
                startActivity(new Intent(this,TimeLineActivity.class));
                break;
        }
    };

    @Override
    public void onBackPressed() {
        AppUtils.pressTwiceExitApp(this,false,2000, new AppUtils.ExitAppCallBack() {
            @Override
            public void firstAsk() {
                LogUtils.i(OpenLog,TAG,"firstAsk");
                toast.setText("再按一次就退出啦~").show();
            }

            @Override
            public void beginExit() {
                LogUtils.i(OpenLog,TAG,"beginExit");
            }
        });
    }
}
