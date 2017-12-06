package com.chestnut.common.helper;

import java.lang.ref.SoftReference;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/23 14:54
 *     desc  :  对软引用的一个封装
 *     thanks To:
 *            http://www.cnblogs.com/alias-blog/p/5793108.html
 *     dependent on:
 *     update log:
 *          1.  完成了测试。
 * </pre>
 */
public class XSoftRef<DataType> {

    private SoftReference<DataType> dataTypeSoftReference;
    private NullCallBack nullCallBack;

    public XSoftRef(DataType dataType) {
        dataTypeSoftReference = new SoftReference<>(dataType);
    }

    public DataType get(NullCallBack nullCallBack) {
        if (dataTypeSoftReference.get()!=null)
            return dataTypeSoftReference.get();
        else {
            if (nullCallBack!=null) {
                DataType dataType = nullCallBack.createNewObject();
                dataTypeSoftReference = new SoftReference<>(dataType);
            }
            if (this.nullCallBack!=null) {
                DataType dataType = this.nullCallBack.createNewObject();
                dataTypeSoftReference = new SoftReference<>(dataType);
            }
            return dataTypeSoftReference.get();
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
