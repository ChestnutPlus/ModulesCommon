package com.chestnut.common.contract.db;

import android.content.Context;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月30日14:52:41
 *     desc  : 规范定义了DB的行为
 *              CallBack   :   Async回调的接口
 *                  Bean   :   继承数据库bean的类
 *     thanks To:
 *     dependent on:
 *     updateLog：
 * </pre>
 */
public interface DbInterface<CallBack,Bean> {

    boolean init(Context context);
    boolean closeDB();

    <DbBean extends Bean> boolean addSync(DbBean o);
    <DbBean extends Bean> boolean addAsync(DbBean o, CallBack r);
    <DbBean extends Bean> Observable<Boolean> addRx(Class<DbBean> DbBean, DbBean o);

    <DbBean extends Bean> boolean addOrUpdateSync(DbBean o);
    <DbBean extends Bean> boolean addOrUpdateAsync(DbBean o, CallBack r);
    <DbBean extends Bean> Observable<Boolean> addOrUpdateRx(Class<DbBean> DbBean, DbBean o);
}
