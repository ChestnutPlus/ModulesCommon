package com.chestnut.RouterArchitecture.ModulesCommon.fun.hyXinYiHe;

import android.graphics.drawable.Drawable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 15:53
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SFCAppInfo {
    private String appLabel;
    private String pkgName;
    private String clsName;
    private Drawable appIcon;

    public SFCAppInfo() {
    }

    public void setAppLable(String lable) {
        this.appLabel = lable;
    }

    public void setPkgName(String pkg) {
        this.pkgName = pkg;
    }

    public void setClsName(String cls) {
        this.clsName = cls;
    }

    public void setAppIcon(Drawable icon) {
        this.appIcon = icon;
    }

    public String getAppLable() {
        return this.appLabel;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public String getClsName() {
        return this.clsName;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public String toString() {
        return "app:" + this.appLabel + ", pkg:" + this.pkgName + ", cls:" + this.clsName + ", icon:" + this.appIcon;
    }
}
