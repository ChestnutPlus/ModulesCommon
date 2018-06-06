package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/6 18:06
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class SomeBean implements Parcelable {

    public String a = "fsldkjfl";
    public boolean b = false;
    public int c = -199;

    public SomeBean() {
    }

    public SomeBean(String a, boolean b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return "SomeBean{" +
                "a='" + a + '\'' +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.a);
        dest.writeByte(this.b ? (byte) 1 : (byte) 0);
        dest.writeInt(this.c);
    }

    protected SomeBean(Parcel in) {
        this.a = in.readString();
        this.b = in.readByte() != 0;
        this.c = in.readInt();
    }

    public static final Creator<SomeBean> CREATOR = new Creator<SomeBean>() {
        @Override
        public SomeBean createFromParcel(Parcel source) {
            return new SomeBean(source);
        }

        @Override
        public SomeBean[] newArray(int size) {
            return new SomeBean[size];
        }
    };
}
