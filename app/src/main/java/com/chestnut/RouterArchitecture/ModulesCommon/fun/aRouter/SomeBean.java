package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.Serializable;

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

@Route(path = "/service/json-SomeBean")
public class SomeBean implements Serializable {

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
}
