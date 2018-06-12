package com.chestnut.common.manager.imgloader.contract;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/11 17:25
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface ImgDownloadListener {
    void onSuccess(String url, String filePath);
    void onFail(String url, String filePath);
}
