package com.chestnut.RouterArchitecture.ModulesCommon.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/21 13:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public interface GetAppList {

    @GET("getAppList?pageIndex=0&pageSize=8")
    Call<AppListBean> get();

    @GET("getAppList")
    Call<AppListBean> get(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);
}
