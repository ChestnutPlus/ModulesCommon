package com.chesnut.Common.Interface.web;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月30日14:52:41
 *     desc  : 规范定义了Http请求
 *     thanks To:
 *     dependent on:
 *     updateLog：
 * </pre>
 */

/**
 *  Http请求接口
 * @param <CallBack>    回调接口
 * @param <HttpParam>   传入的参数
 * @param <HttpResult>  Rx返回的结果
 */
public interface Http<CallBack,HttpParam,HttpResult> {
    Observable<HttpResult> RxGet(String url, HttpParam httpParam);
    Observable<HttpResult> RxPost(String url, HttpParam httpParam);
    Observable<String> RxPostFile(String url, HttpParam map, String fileName, String fileType, byte[] fileBytes);
    void Get(String url, HttpParam httpParam, CallBack callBack);
    void Post(String url, HttpParam httpParam, CallBack callBack);
    void PostFile(String url, HttpParam map, String fileName, byte[] fileBytes, String fileType, CallBack callBack);
}
