package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chestnut.Common.utils.ScreenUtils;

public class LockActivity extends AppCompatActivity {

    public static final String ID = "LockActivity-ID";
    private int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_lock);
        ImageView imageView = (ImageView) findViewById(R.id.img);
        if (getIntent().getExtras()!=null) {
            resId = getIntent().getIntExtra(ID,R.drawable._1);
            Glide.with(this)
                    .load(resId)
                    .into(imageView);
        }

        ScreenUtils.setListenerScreenLock(this, new ScreenUtils.Callback() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {

            }

            @Override
            public void onUserUnlockThePhone() {

            }

            @Override
            public void onHomeKeyShortPress() {

            }

            @Override
            public void onHomeKeyLongPress() {

            }
        });
    }
}
