// CollectionListener.aidl
package com.huiyu.common.firstleapsdk;

// Declare any non-default types here with import statements

interface CollectionListener {
    void onBeginCollect();  //开始采集语音时调用
    void onEndCollect();    //结束采集语音时调用
}
