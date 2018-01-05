// InstallCallBack.aidl
package com.hy.Install;

// Declare any non-default types here with import statements

interface InstallCallBack {
    void startInstall(String packageName, String appFile);
    void endInstall(String packageName, String appFile);
    void failInstall(int errCode, String msg, String packageName, String appFile);
}
