package com.chestnut.common.contract.common;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/5 13:55
 *     desc  :  通用接口
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface CommonContract {
    interface Result<T,V> {
        void onSuccess(T t);
        void onError(V v,int code);
    }
    interface Success<T> {
        void onSuccess(T t);
    }
    interface Error<V> {
        void onError(V v);
    }

    interface Function {
        void onAction();
    }
    interface FunctionR<R> {
        R onAction();
    }

    interface Function1<T> {
        void onAction(T t);
    }
    interface FunctionR1<R,T> {
        R onAction(T t);
    }

    interface Function2<T,T1> {
        void onAction(T t,T1 t1);
    }
    interface FunctionR2<R,T,T1> {
        R onAction(T t,T1 t1);
    }

    interface Function3<T,T1,T2> {
        void onAction(T t,T1 t1,T2 t2);
    }
    interface FunctionR3<R,T,T1,T2> {
        R onAction(T t,T1 t1,T2 t2);
    }

    interface Function4<T,T1,T2,T3> {
        void onAction(T t,T1 t1,T2 t2, T3 t3);
    }
    interface FunctionR4<R,T,T1,T2,T3> {
        R onAction(T t, T1 t1, T2 t2, T3 t3);
    }

    interface ViewLife {
        void onResume();
        void onPause();
    }
}
