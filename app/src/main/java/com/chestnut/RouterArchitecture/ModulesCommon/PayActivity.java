package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity {

    private ImageView imgResult;
    private TextView txtTips;
    private Button btnPay;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        imgResult = (ImageView) findViewById(R.id.img_result);
        txtTips = (TextView) findViewById(R.id.txt_tip);
        btnPay = (Button) findViewById(R.id.btn_pay);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("com.hy.honeybot.payResult")) {
                    int result = intent.getIntExtra("result",-1);
                    switch (result) {
                        case -1:
                            txtTips.setText("网络异常，请稍后重试");
                            imgResult.setImageResource(R.drawable.xdialog_error);
                            btnPay.setVisibility(View.VISIBLE);
                            break;
                        case 0:
                            txtTips.setText("您未购买此课程");
                            imgResult.setImageResource(R.drawable.xdialog_warning);
                            btnPay.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            txtTips.setText("您已购买此课程");
                            imgResult.setImageResource(R.drawable.xdialog_success);
                            btnPay.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
                else if (intent.getAction().equalsIgnoreCase("com.hy.honeybot.payDialog.dismiss")) {
                    getThePayStatus();
                }
            }
        };
        IntentFilter screenRecordeFilter = new IntentFilter();
        screenRecordeFilter.addAction("com.hy.honeybot.payResult");
        screenRecordeFilter.addAction("com.hy.honeybot.payDialog.dismiss");
        registerReceiver(broadcastReceiver,screenRecordeFilter);

        btnPay.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("com.hy.honeybot.askForPay");
            this.sendBroadcast(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getThePayStatus();
    }

    private void getThePayStatus() {
        Intent intent1 = new Intent();
        intent1.setAction("com.hy.honeybot.askForPayResult");
        this.sendBroadcast(intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadcastReceiver);
    }
}
