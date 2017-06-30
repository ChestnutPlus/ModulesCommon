package com.chestnut.Common.utils;

import com.chestnut.Common.Interface.web.Http;
import com.chestnut.Common.Interface.web.HttpCallBack;

import java.util.Map;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/6/30 10:56
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class XHttpUtils implements Http<HttpCallBack,Map<String,?>,String>{
    @Override
    public Observable<String> RxGet(String url, Map<String, ?> stringMap) {
        return null;
    }

    @Override
    public Observable<String> RxPost(String url, Map<String, ?> stringMap) {
        return null;
    }

    @Override
    public Observable<String> RxPostFile(String url, Map<String, ?> map, String fileName, String fileType, byte[] fileBytes) {
        return null;
    }

    @Override
    public void Get(String url, Map<String, ?> stringMap, HttpCallBack httpCallBack) {

    }

    @Override
    public void Post(String url, Map<String, ?> stringMap, HttpCallBack httpCallBack) {

    }

    @Override
    public void PostFile(String url, Map<String, ?> map, String fileName, byte[] fileBytes, String fileType, HttpCallBack httpCallBack) {

    }
}
