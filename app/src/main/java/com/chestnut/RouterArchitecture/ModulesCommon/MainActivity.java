package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.Common.Helper.XSoftRef;
import com.chestnut.Common.ui.Toastc;
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
            "1_"+"HelloKitty哈",
            "2_"+"熊猫哈",
            "3_"+"钢铁哈",
            "4_"+"",
            "5_"+"",
            "6_"+"",
            "7_"+"",
            "8_"+"",
            "9_"+"",
            "10_"+"",
            "11_"+"",
            "12_"+"",
    };

    XSoftRef<String> stringXWeakRef = new XSoftRef<>(new String("坑货"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XFontUtils.getInstance().activitySetFont(this,"fonts/caonima.ttf");
        logs = new ArrayList<>();
        stringXWeakRef.setNullCallBack(stringXWeakRef.new NullCallBack() {
            @Override
            public String createNewObject() {
                return "坑货";
            }
        });

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
        Intent intent = new Intent(this,LockActivity.class);
        switch (view.getId()) {
            case R.id.btn_1:
                intent.putExtra(LockActivity.ID,R.drawable._1);
                startActivity(intent);
                break;
            case R.id.btn_2:
                intent.putExtra(LockActivity.ID,R.drawable._2);
                startActivity(intent);
                break;
            case R.id.btn_3:
                intent.putExtra(LockActivity.ID,R.drawable._3);
                startActivity(intent);
                break;
            case R.id.btn_4:
                LogUtils.i(true,TAG,"temp:"+stringXWeakRef.get());
                break;
            case R.id.btn_5:
                break;
            case R.id.btn_6:
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Test.TTF");
                Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/caonima.ttf");
                break;
            case R.id.btn_7:
                break;
            case R.id.btn_8:
                break;
            case R.id.btn_9:
                break;
            case R.id.btn_10:
                break;
            case R.id.btn_11:
                startActivity(new Intent(this,TestActivity.class));
                break;
            case R.id.btn_12:
                startActivity(new Intent(this,TimeLineActivity.class));
                break;
        }
    };
}
