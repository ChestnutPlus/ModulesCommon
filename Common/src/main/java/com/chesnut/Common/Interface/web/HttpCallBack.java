package com.chesnut.Common.Interface.web;

/**
 * Created by Chestnut on 2016/11/29.
 */

public interface HttpCallBack {
    void onSuccess(String result);
    void onFailure(String msg);
}
