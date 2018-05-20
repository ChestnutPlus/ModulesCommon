package com.chestnut.common.helper;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/23 14:54
 *     desc  :  对弱引用的一个封装
 *     thanks To:
 *            http://www.cnblogs.com/alias-blog/p/5793108.html
 *     dependent on:
 *     update log:
 *          1.  完成了测试。
 * </pre>
 */
public class WeakRefHelper<DataType> {

    private WeakReference<DataType> dataTypeWeakReference;
    private NullCallBack nullCallBack;

    public WeakRefHelper(DataType dataType) {
        dataTypeWeakReference = new WeakReference<>(dataType);
    }

    public DataType get(NullCallBack nullCallBack) {
        if (dataTypeWeakReference.get()!=null)
            return dataTypeWeakReference.get();
        else {
            if (nullCallBack!=null) {
                DataType dataType = nullCallBack.createNewObject();
                dataTypeWeakReference = new WeakReference<>(dataType);
            }
            if (this.nullCallBack!=null) {
                DataType dataType = this.nullCallBack.createNewObject();
                dataTypeWeakReference = new WeakReference<>(dataType);
            }
            return dataTypeWeakReference.get();
        }
    }

    public void setNullCallBack(NullCallBack nullCallBack) {
        this.nullCallBack = nullCallBack;
    }

    public DataType get() {
        return get(null);
    }

    public abstract class NullCallBack {
        public DataType createNewObject(){
            return null;
        }
    }
}
