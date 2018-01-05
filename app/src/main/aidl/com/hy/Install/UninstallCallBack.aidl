// UninstallCallBack.aidl
package com.hy.Install;

// Declare any non-default types here with import statements

interface UninstallCallBack {
    void startUninstall(String packageName);
    void endUninstall(String packageName);
    void failUninstall(int errCode, String msg, String packageName);
}
