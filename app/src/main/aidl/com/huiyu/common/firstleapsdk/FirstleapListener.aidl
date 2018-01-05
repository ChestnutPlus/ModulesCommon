// FirstleapListener.aidl
package com.huiyu.common.firstleapsdk;

import com.huiyu.common.firstleapsdk.CollectionListener;
import com.huiyu.common.firstleapsdk.SpeakListener;
import com.huiyu.common.firstleapsdk.ScheduleListener;
import com.huiyu.common.firstleapsdk.WakeupListener;
// Declare any non-default types here with import statements

interface FirstleapListener {
        String getSerialno();                                   //获取序列号
        boolean isVoiceInterationStarted();                     //获取AI聊天是否启动
        void startVoiceInteration();                            //启动AI聊天
        void stopVoiceInteration();                             //关闭AI聊天
        void speak(String sentence);                            //语音合成
        void startWakeup();                                     //开启唤醒，一旦唤醒，则会一直不断地识别
        void stopWakeup();                                      //停止唤醒
        void setWakeupListener(WakeupListener listener);        //设置唤醒监听器
        void setCollectionListener(CollectionListener listener);//设置采集监听器
        void setSpeakListener(SpeakListener listener);          //设置讲话监听器
        void robotWheelRunFront();                              //控制小哈向前走
        void robotWheelRunBack();                               //控制小哈向后退
        void robotWheelRunLeft();                               //控制小哈向左转
        void robotWheelRunRight();                              //控制小哈向右转
        void robotWheelRunStop();                               //控制小哈停止
        void shutDownRobot();                                   //关机
        void lockRobot();                                       //锁屏
        boolean isAdbEnabled();                                 //获取adb开关情况
        void setAdbEnabled(boolean enable);                    //设置adb开关
        void setScheduleListener(ScheduleListener listener);    //设置提醒回调
}
