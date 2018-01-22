package com.chestnut.RouterArchitecture.ModulesCommon.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/21 13:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class AppListBean {

    /**
     * pageSize : 8
     * pageIndex : 0
     * toatl : 26
     * data : [{"icon":"http://download.honeybot.cn:8080/Images/YolkParkHuiYu.png","Downloaded":31302,"ApplicationId":2,"version":"2.3","url":"http://download.honeybot.cn:8080/packages/com.Handict.YolkParkHuiYu.apk","package":"com.Handict.YolkParkHuiYu","enabled":"Y","version_code":1,"oldpackage":null,"Introduction":"YolkPark","Size":92893184,"uid":2,"name":"小哈蛋生园","MD5":"2682AA5561ADD84CC823B0A368554C17"},{"icon":"http://download.honeybot.cn:8080/Images/com.qiyi.video.child.png?version=7.9.0","Downloaded":31153,"ApplicationId":5,"version":"7.9.0","url":"http://download.honeybot.cn:8080/packages/com.qiyi.video.child.apk","package":"com.qiyi.video.child","enabled":"Y","version_code":800070900,"oldpackage":null,"Introduction":"CartoonVideo","Size":24112070,"uid":5,"name":"爱奇艺动画屋","MD5":"8B9614BFA70CCBBB30E0D374A9B9B078"},{"icon":"http://download.honeybot.cn:8080/Images/com.hy.storyMachine.png?version=1.1.0","Downloaded":31120,"ApplicationId":20,"version":"1.1.2","url":"http://download.honeybot.cn:8080/packages/com.hy.storyMachine.apk","package":"com.hy.storyMachine","enabled":"Y","version_code":2,"oldpackage":null,"Introduction":"StoryMachine","Size":9570406,"uid":20,"name":"小哈故事机","MD5":"1955F0E66D11AA1BB1DBB8013F058459"},{"icon":"http://download.honeybot.cn:8080/Images/yolkworld.png","Downloaded":31116,"ApplicationId":1,"version":"1.1","url":"http://download.honeybot.cn:8080/packages/com.Handict.YolkWolrdHuiYu.apk","package":"com.Handict.YolkWolrdHuiYu","enabled":"Y","version_code":1,"oldpackage":null,"Introduction":"YolkWorld","Size":140705792,"uid":1,"name":"小哈蛋生世界","MD5":"6AF027F29D4A4845190B1E2C9DA8B33A"},{"icon":"http://download.honeybot.cn:8080/Images/com.miboo.drawland.png","Downloaded":31109,"ApplicationId":4,"version":"1.0","url":"http://download.honeybot.cn:8080/packages/com.miboo.drawland.apk","package":"com.miboo.drawland","enabled":"Y","version_code":1,"oldpackage":null,"Introduction":"HonneyBotDrawing","Size":37265408,"uid":4,"name":"小哈学绘画","MD5":"106D0749F10790D4757D433FBCE763D9"},{"icon":"http://download.honeybot.cn:8080/Images/com.sinyee.babybus.food.png","Downloaded":31109,"ApplicationId":9,"version":"9.0.15.00","url":"http://download.honeybot.cn:8080/packages/com.sinyee.babybus.food.apk","package":"com.sinyee.babybus.food","enabled":"Y","version_code":901500,"oldpackage":null,"Introduction":"BabyBusFoods","Size":32714752,"uid":9,"name":"中华美食","MD5":"A917C6D42F948DBD7781928DB08442BC"},{"icon":"http://download.honeybot.cn:8080/Images/com.sinyee.babybus.shopping.png","Downloaded":31064,"ApplicationId":11,"version":"9.0.15.00","url":"http://download.honeybot.cn:8080/packages/com.sinyee.babybus.shopping.apk","package":"com.sinyee.babybus.shopping","enabled":"Y","version_code":901500,"oldpackage":null,"Introduction":"BabyBusShopping","Size":43462656,"uid":11,"name":"宝宝超市","MD5":"FDDD1BF13DB634F689C02C441BF58651"},{"icon":"http://download.honeybot.cn:8080/Images/com.huiyu.common.soundpicturebook.png","Downloaded":31063,"ApplicationId":18,"version":"1.1","url":"http://download.honeybot.cn:8080/packages/com.huiyu.common.soundpicturebook.apk","package":"com.huiyu.common.soundpicturebook","enabled":"Y","version_code":1,"oldpackage":null,"Introduction":"SoundPictureBook","Size":38963438,"uid":18,"name":"小哈读绘本","MD5":"777C84226EEE75E4D6C620BF59BDB6BE"}]
     */
    public int pageSize;
    public int pageIndex;
    public int toatl;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * icon : http://download.honeybot.cn:8080/Images/YolkParkHuiYu.png
         * Downloaded : 31302
         * ApplicationId : 2
         * version : 2.3
         * url : http://download.honeybot.cn:8080/packages/com.Handict.YolkParkHuiYu.apk
         * package : com.Handict.YolkParkHuiYu
         * enabled : Y
         * version_code : 1
         * oldpackage : null
         * Introduction : YolkPark
         * Size : 92893184
         * uid : 2
         * name : 小哈蛋生园
         * MD5 : 2682AA5561ADD84CC823B0A368554C17
         */
        public String icon;
        public int Downloaded;
        public int ApplicationId;
        public String version;
        public String url;
        @SerializedName("package")
        public String packageX;
        public String enabled;
        public int version_code;
        public Object oldpackage;
        public String Introduction;
        public int Size;
        public int uid;
        public String name;
        public String MD5;

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", MD5='" + MD5 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AppListBean{" +
                "pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", toatl=" + toatl +
                ", data=" + data +
                '}';
    }
}
