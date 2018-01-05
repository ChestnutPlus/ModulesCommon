// InstallManager.aidl
package com.hy.Install;
import com.hy.Install.InstallCallBack;
import com.hy.Install.UninstallCallBack;

// Declare any non-default types here with import statements

interface InstallManager {
    boolean installApkQuietly(String packageName, String appFile);
    boolean uninstallApkQuietly(String packageName);
    void installApkQuietlyCallBack(String packageName, String appFile, InstallCallBack installCallBack);
    void uninstallApkQuietlyCallBack(String packageName, UninstallCallBack uninstallCallBack);
    boolean isInstalling(String packageName);
}
