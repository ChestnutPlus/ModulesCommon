package com.chestnut.common.manager;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 17:45
 *     desc  :  RxBusManager
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class RxBusManager {

    /*单例*/
    private static volatile RxBusManager defaultInstance;
    public static RxBusManager getInstance() {
        RxBusManager jsonUtils = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBusManager.class) {
                jsonUtils = defaultInstance;
                if (defaultInstance == null) {
                    jsonUtils = new RxBusManager();
                    defaultInstance = jsonUtils;
                }
            }
        }
        return jsonUtils;
    }
    private RxBusManager(){
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
