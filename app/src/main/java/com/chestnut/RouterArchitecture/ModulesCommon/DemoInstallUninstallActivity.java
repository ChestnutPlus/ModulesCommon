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
import com.hy.Install.InstallCallBack;
import com.hy.Install.InstallManager;
import com.hy.Install.UninstallCallBack;

public class DemoInstallUninstallActivity extends AppCompatActivity {

    private boolean OpenLog = true;
    private String TAG = "DemoInstallUninstallActivity";

    private InstallManager installManager;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            installManager = InstallManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            installManager = null;
        }
    };

    private String[] packageNames = {
            "com.icoolme.android.weather",
            "com.qq.reader",
            "com.tencent.mm",
            "com.tencent.mobileqq",
            "com.tencent.qqmusic",
            "com.tencent.tmgp.wec",
            "com.wuba",
    };
    private String[] apkPaths = {
            "/sdcard/_0.apk",
            "/sdcard/_1.apk",
            "/sdcard/_2.apk",
            "/sdcard/_3.apk",
            "/sdcard/_4.apk",
            "/sdcard/_5.apk",
            "/sdcard/_6.apk",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_install_uninstall);
        Intent intent = new Intent();
        intent.setClassName("com.hy.HYInstallService","com.hy.HYInstallService.InstallService");
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.txt_install).setOnClickListener(view -> {
            if (installManager!=null) {
                for (int i = 0; i < packageNames.length; i++) {
                    try {
                        installManager.installApkQuietlyCallBack(packageNames[i], apkPaths[i], new InstallCallBack.Stub() {
                            @Override
                            public void startInstall(String packageName, String appFile) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"startInstall"+",packageName:"+packageName);
                            }

                            @Override
                            public void endInstall(String packageName, String appFile) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"endInstall"+",packageName:"+packageName);
                            }

                            @Override
                            public void failInstall(int errCode, String msg, String packageName, String appFile) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"failInstall"+",packageName:"+packageName);
                            }
                        });
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        LogUtils.i(OpenLog,TAG,"i:"+i+",install-RemoteException:"+e.getMessage());
                    }
                }
            }
        });
        findViewById(R.id.txt_uninstall).setOnClickListener(view -> {
            if (installManager!=null) {
                for (int i = 0; i < packageNames.length; i++) {
                    try {
                        installManager.uninstallApkQuietlyCallBack(packageNames[i],new UninstallCallBack.Stub() {
                            @Override
                            public void startUninstall(String packageName) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"startUninstall"+",packageName:"+packageName);
                            }

                            @Override
                            public void endUninstall(String packageName) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"endUninstall"+",packageName:"+packageName);
                            }

                            @Override
                            public void failUninstall(int errCode, String msg, String packageName) throws RemoteException {
                                LogUtils.i(OpenLog,TAG,"failUninstall"+",packageName:"+packageName);
                            }
                        });
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        LogUtils.i(OpenLog,TAG,"i:"+i+",uninstall-RemoteException:"+e.getMessage());
                    }
                }
            }
        });
    }
}
