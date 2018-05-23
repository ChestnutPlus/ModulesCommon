package com.chestnut.common.helper;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/3/30 12:03
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface WifiHelperContract {

    /**
     * 定义几种WIfi密码的加密方式
     */
    int Wifi_Psw_Type_WEP = 0x11;
    int Wifi_Psw_Type_PSK = 0x12;
    int Wifi_Psw_Type_EAP = 0x13;
    int Wifi_Psw_Type_NO = 0x14;
    @IntDef({Wifi_Psw_Type_WEP,Wifi_Psw_Type_PSK,Wifi_Psw_Type_EAP,Wifi_Psw_Type_NO})
    @Retention(RetentionPolicy.SOURCE)
    @interface WifiPswType {}

    /**
     * 定义连接过程中,RX的连接状态值
     */
    int Wifi_Connected = -4;
    int Wifi_ConnectFail = -5;
    int Wifi_Err_Psw = -6;
    int Wifi_Another_Is_Connecting = -7;

    void init(Context context);
    WifiManager getManager();
    WifiInfo getConnectionInfo();
    boolean open();
    void scan();
    List<ScanResult> getScan();
    List<ScanResult> getScanOrderByLevel();

    /**
     * 是否配置过wifi，也就是是否
     *  保存过此wifi的密码
     * @param ssId wifi名称
     * @return 是否
     */
    boolean isSave(String ssId);
    WifiConfiguration getSaveConfiguration(String ssId);
    List<WifiConfiguration> getSaveConfigurationList();

    String getConnectingBSSID();
    String getConnectedBSSID();

    Observable<Integer> connectRx(ScanResult scanResult, String password);
    boolean removeConfiguration(String ssId);
    void disConnectCurrent();
    void release();

    /**
     * 获取某个WIFI的加密方式：
     *  方式请见前面的定义
     * @param scanResult wifi scanResult
     * @return 类型
     */
    int getPswType(ScanResult scanResult);

    /**
     * 检查当前的wifi是否有密码
     *  利用WifiConfiguration.KeyManager的管理机制，来判断当前wifi是否需要连接密码
     * @param wifiManager wifi管理者
     * @param currentWifiSSID 当前的wifi名称
     * @return 是否
     */
    static boolean checkIsCurrentWifiHasPassword(WifiManager wifiManager, String currentWifiSSID) {
        try {
            // 得到当前连接的wifi热点的信息
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            // 得到当前WifiConfiguration列表，此列表包含所有已经连过的wifi的热点信息，未连过的热点不包含在此表中
            List<WifiConfiguration> wifiConfiguration = wifiManager.getConfiguredNetworks();

            String currentSSID = wifiInfo.getSSID();
            if (currentSSID != null && currentSSID.length() > 2) {
                if (currentSSID.startsWith("\"") && currentSSID.endsWith("\"")) {
                    currentSSID = currentSSID.substring(1, currentSSID.length() - 1);
                }

                if (wifiConfiguration != null && wifiConfiguration.size() > 0) {
                    for (WifiConfiguration configuration : wifiConfiguration) {
                        if (configuration != null && configuration.status == WifiConfiguration.Status.CURRENT) {
                            String ssid = null;
                            if (!TextUtils.isEmpty(configuration.SSID)) {
                                ssid = configuration.SSID;
                                if (configuration.SSID.startsWith("\"") && configuration.SSID.endsWith("\"")) {
                                    ssid = configuration.SSID.substring(1, configuration.SSID.length() - 1);
                                }
                            }
                            if (TextUtils.isEmpty(currentSSID) || currentSSID.equalsIgnoreCase(ssid)) {
                                //KeyMgmt.NONE表示无需密码
                                return (!configuration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            //do nothing
        }
        //默认为需要连接密码
        return true;
    }
}
