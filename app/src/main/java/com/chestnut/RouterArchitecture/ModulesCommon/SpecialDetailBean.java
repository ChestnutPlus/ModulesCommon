package com.chestnut.RouterArchitecture.ModulesCommon;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/25 17:33
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class SpecialDetailBean {
    /**
     * Pic : http://img.idaddy.cn/b/6/l8nvh3pz.jpg
     * Url : http://cdn.open.idaddy.cn/apsmp3/b8fc/honeyhy000000001/201707250000/0/YS82L2w4bnZoM3B6LmF1ZGlv.mp3
     * Title : 洋娃娃和小熊跳舞
     * Special_id : 35
     * Duration : 74
     * Platform : 工程师爸爸
     * Chapter : -1
     * Type : 1
     * Uid : 1024
     */
    public String Pic;
    public String Url;
    public String Title;
    public int Special_id;
    public int Duration;
    public String Platform;
    public String Chapter;
    public int Type;
    public int Uid;

    @Override
    public String toString() {
        return "SpecialDetailBean{" +
                "Pic='" + Pic + '\'' +
                ", Url='" + Url + '\'' +
                ", Title='" + Title + '\'' +
                ", Special_id=" + Special_id +
                ", Duration=" + Duration +
                ", Platform='" + Platform + '\'' +
                ", Chapter='" + Chapter + '\'' +
                ", Type=" + Type +
                ", Uid=" + Uid +
                '}';
    }
}
