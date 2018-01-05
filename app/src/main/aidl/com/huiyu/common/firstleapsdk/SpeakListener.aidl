// SpeakListener.aidl
package com.huiyu.common.firstleapsdk;

// Declare any non-default types here with import statements

interface SpeakListener {
    void onBeginSpeak();    //开始讲话时调用
    void onEndSpeak();      //讲话结束时调用
}
