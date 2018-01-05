package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import com.chestnut.common.utils.LogUtils;
import com.huiyu.common.firstleapsdk.FirstleapListener;
import com.huiyu.common.firstleapsdk.WakeupListener;

public class OemHWLActivity extends AppCompatActivity {

    private boolean OpenLog = true;
    private String TAG = "OemHWLActivity";
    private WakeupListener wakeupListener = new WakeupListener.Stub() {
        @Override
        public void onWakeupStart() throws RemoteException {
            LogUtils.i(OpenLog,TAG,"onWakeupStart");
        }

        @Override
        public void onWakeup() throws RemoteException {
            LogUtils.i(OpenLog,TAG,"onWakeup");
        }

        @Override
        public void onWakeupStop() throws RemoteException {
            LogUtils.i(OpenLog,TAG,"onWakeupStop");
        }
    };

    private FirstleapListener service;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = FirstleapListener.Stub.asInterface(iBinder);
            try {
                service.setWakeupListener(wakeupListener);
            } catch (RemoteException e) {
                LogUtils.i(OpenLog,TAG,"RemoteException-1:"+e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oem_hwl);
        Intent intent = new Intent();
        intent.setAction("com.huiyu.honeybot.FirstleapService");
        intent.setPackage("com.huiyu.common.firstleapsdk");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.txt_wakeup_start).setOnClickListener(view -> {
            try {
                LogUtils.i(OpenLog,TAG,"txt_wakeup_start-Click");
                service.startWakeup();
            } catch (RemoteException e) {
                LogUtils.i(OpenLog,TAG,"txt_wakeup_start-RemoteException:"+e.getMessage());
                e.printStackTrace();
            }
        });
        findViewById(R.id.txt_wakeup_stop).setOnClickListener(view -> {
            try {
                LogUtils.i(OpenLog,TAG,"txt_wakeup_stop-Click");
                service.stopWakeup();
            } catch (RemoteException e) {
                LogUtils.i(OpenLog,TAG,"txt_wakeup_stop-RemoteException:"+e.getMessage());
                e.printStackTrace();
            }
        });
        findViewById(R.id.txt_ai_start).setOnClickListener(view -> {
            try {
                LogUtils.i(OpenLog,TAG,"txt_ai_start-Click");
                service.startVoiceInteration();
            } catch (RemoteException e) {
                LogUtils.i(OpenLog,TAG,"txt_ai_start-RemoteException:"+e.getMessage());
                e.printStackTrace();
            }
        });
        findViewById(R.id.txt_ai_stop).setOnClickListener(view -> {
            try {
                LogUtils.i(OpenLog,TAG,"txt_ai_stop-Click");
                service.stopVoiceInteration();
            } catch (RemoteException e) {
                LogUtils.i(OpenLog,TAG,"txt_ai_stop-RemoteException:"+e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
