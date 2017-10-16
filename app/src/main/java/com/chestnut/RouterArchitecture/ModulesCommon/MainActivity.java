package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.Common.ui.Toastc;
import com.chestnut.Common.utils.AppUtils;
import com.chestnut.Common.utils.LogUtils;
import com.chestnut.Common.utils.XFontUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends RxAppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
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
            "1_"+"PayTest",
            "2_"+"",
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
        toast = new Toastc(this, Toast.LENGTH_LONG);

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
        AppUtils.pressTwiceExitApp(this, false, "王尼玛，你敢再按一次试一试？", 2000, new AppUtils.ExitAppCallBack() {
            @Override
            public void firstAsk() {
                LogUtils.i(OpenLog,TAG,"firstAsk");
            }

            @Override
            public void beginExit() {
                LogUtils.i(OpenLog,TAG,"beginExit");
            }
        });
    }
}
