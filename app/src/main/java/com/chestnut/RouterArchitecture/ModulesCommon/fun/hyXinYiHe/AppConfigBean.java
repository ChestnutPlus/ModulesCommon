package com.chestnut.RouterArchitecture.ModulesCommon.fun.hyXinYiHe;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 14:59
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class AppConfigBean {

    public List<DataBean> data;

    public static class DataBean {
        /**
         * app_tag_id : 2
         * app_tag_name : 自然科学
         * app_package : com.Handict.YolkWolrdHuiYu
         * app_name : 小哈蛋生世界
         * app_class : com.unity3d.player.UnityPlayerNativeActivity
         * app_type : 0
         */

        public int app_tag_id;
        public String app_tag_name;
        public String app_package;
        public String app_name;
        public String app_class;
        public int app_type;
    }
}
