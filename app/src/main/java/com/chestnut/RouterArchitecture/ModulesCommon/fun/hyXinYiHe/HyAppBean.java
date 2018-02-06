package com.chestnut.RouterArchitecture.ModulesCommon.fun.hyXinYiHe;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 15:22
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class HyAppBean {

    public SFCAppInfo sfcAppInfo;
    public String tagName;
    public int tagId;
    public int appType;

    public static HyAppBean change(AppConfigBean.DataBean dataBean, Context context) throws PackageManager.NameNotFoundException {
        HyAppBean hyAppBean = new HyAppBean();
        SFCAppInfo sfcAppInfo = new SFCAppInfo();
        sfcAppInfo.setAppLable(dataBean.app_name);
        sfcAppInfo.setPkgName(dataBean.app_package);
        sfcAppInfo.setClsName(dataBean.app_class);
        sfcAppInfo.setAppIcon(context.getPackageManager().getApplicationIcon(dataBean.app_package));
        hyAppBean.sfcAppInfo = sfcAppInfo;
        hyAppBean.tagName = dataBean.app_tag_name;
        hyAppBean.tagId = dataBean.app_tag_id;
        hyAppBean.appType = dataBean.app_type;
        return hyAppBean;
    }
}
