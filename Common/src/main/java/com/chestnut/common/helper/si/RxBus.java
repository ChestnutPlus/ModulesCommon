package com.chestnut.common.helper.si;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 17:45
 *     desc  :  RxBus
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class RxBus {

    /*单例*/
    private static volatile RxBus defaultInstance;
    public static RxBus getInstance() {
        RxBus jsonUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                jsonUtils = defaultInstance;
                if (defaultInstance == null) {
                    jsonUtils = new RxBus();
                    defaultInstance = jsonUtils;
                }
            }
        }
        return jsonUtils;
    }
    private RxBus(){
        publishSubject = PublishSubject.create();
    }

    private PublishSubject<Object> publishSubject;

    public void post(Object o) {
        publishSubject.onNext(o);
    }

    public <T> Observable<T> listen(Class<T> eventType) {
        return publishSubject.ofType(eventType);
    }
}
