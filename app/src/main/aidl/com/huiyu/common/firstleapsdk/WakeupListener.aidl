// WakeupListener.aidl
package com.huiyu.common.firstleapsdk;

// Declare any non-default types here with import statements

interface WakeupListener {
    void onWakeupStart();       //唤醒启动的时候，回调
    void onWakeup();            //识别到唤醒词的时候，回调
    void onWakeupStop();        //唤醒停止的时候，回调
}
