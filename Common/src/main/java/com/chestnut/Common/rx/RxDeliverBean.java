package com.chestnut.Common.rx;

import java.util.Objects;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/1 23:02
 *     desc  :  rx中，传递的辅助对象
 *              当，rx中，需要传递的东西多于两个，
 *              可以使用此辅助类，去做，去继续传递
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class RxDeliverBean {

    public Objects objects1 = null;
    public Objects objects2 = null;
    public Objects objects3 = null;
    public Objects objects4 = null;
    public Objects objects5 = null;

    public RxDeliverBean(Objects objects1) {
        this.objects1 = objects1;
    }

    public RxDeliverBean(Objects objects1, Objects objects2) {
        this.objects1 = objects1;
        this.objects2 = objects2;
    }

    public RxDeliverBean(Objects objects1, Objects objects2, Objects objects3) {
        this.objects1 = objects1;
        this.objects2 = objects2;
        this.objects3 = objects3;
    }

    public RxDeliverBean(Objects objects1, Objects objects2, Objects objects3, Objects objects4) {
        this.objects1 = objects1;
        this.objects2 = objects2;
        this.objects3 = objects3;
        this.objects4 = objects4;
    }

    public RxDeliverBean(Objects objects1, Objects objects2, Objects objects3, Objects objects4, Objects objects5) {
        this.objects1 = objects1;
        this.objects2 = objects2;
        this.objects3 = objects3;
        this.objects4 = objects4;
        this.objects5 = objects5;
    }
}
