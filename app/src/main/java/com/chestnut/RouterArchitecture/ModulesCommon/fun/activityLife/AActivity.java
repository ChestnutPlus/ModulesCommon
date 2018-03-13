package com.chestnut.RouterArchitecture.ModulesCommon.fun.activityLife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.utils.LogUtils;

public class AActivity extends AppCompatActivity {

    private String TAG = "AActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        LogUtils.i(TAG,"onCreate,"+this.hashCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i(TAG,"onStart,"+this.hashCode());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i(TAG,"onRestart,"+this.hashCode());
    }

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(this,BActivity.class));
        LogUtils.i(TAG,"onResume,"+this.hashCode());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(TAG,"onPause,"+this.hashCode());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(TAG,"onStop,"+this.hashCode());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG,"onDestroy,"+this.hashCode());
    }
}
