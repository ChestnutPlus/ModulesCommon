package com.chestnut.common.contract.web;


import io.reactivex.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月30日14:52:41
 *     desc  : 规范定义了Http请求
 *             用户封装第三方的请求库
 *             @param <HttpParam>   请求传入的参数
 *             @param <RxHttpResult>  Rx返回的结果，一般返回String。
 *     thanks To:
 *     dependent on:
 *     updateLog：
 * </pre>
 */
public interface HttpInterface<HttpParam,RxHttpResult> {
    Observable<RxHttpResult> RxGet(String url, HttpParam httpParam);
    Observable<RxHttpResult> RxPost(String url, HttpParam httpParam);
    Observable<String> RxPostFile(String url, HttpParam map,String fileType, String fileName, String mediaType, byte[] fileBytes);
}
