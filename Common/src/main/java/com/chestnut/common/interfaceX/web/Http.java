package com.chestnut.common.interfaceX.web;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月30日14:52:41
 *     desc  : 规范定义了Http请求
 *             用户封装第三方的请求库
 *     thanks To:
 *     dependent on:
 *     updateLog：
 * </pre>
 */

/**
 *  Http请求接口
 * @param <CallBack>    回调接口，用于普通的请求时候的回调
 * @param <HttpParam>   请求传入的参数
 * @param <RxHttpResult>  Rx返回的结果，一般返回String。
 */
public interface Http<CallBack,HttpParam,RxHttpResult> {
    Observable<RxHttpResult> RxGet(String url, HttpParam httpParam);
    Observable<RxHttpResult> RxPost(String url, HttpParam httpParam);
    Observable<String> RxPostFile(String url, HttpParam map, String fileName, String fileType, byte[] fileBytes);
    void Get(String url, HttpParam httpParam, CallBack callBack);
    void Post(String url, HttpParam httpParam, CallBack callBack);
    void PostFile(String url, HttpParam map, String fileName, byte[] fileBytes, String fileType, CallBack callBack);
}
