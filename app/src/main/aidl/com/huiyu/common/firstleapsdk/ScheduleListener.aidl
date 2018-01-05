// ScheduleListener.aidl
package com.huiyu.common.firstleapsdk;

// Declare any non-default types here with import statements

interface ScheduleListener {
    void onInvokResource(String route);     //调起资源，route-资源路径
    void onDownloadResource(String route);  //下载第二天资源，route-资源路径列表，以“;”隔开，会重复调用，请判断资源是否下载过避免重复下载
}
