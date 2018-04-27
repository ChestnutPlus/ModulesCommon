package com.chestnut.common.helper.def;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.chestnut.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/20 13:21
 *     desc  :  wifi 方法封装，继承于：WifiUtils
 *     thanks To:
 *     dependent on:
 *     update log:
 *              2018年3月30日10:36:47
 *                  1.  把连接封装成Rx
 *                  2.  去掉回调方法，以Rx形式展示结果
 *                  3.  强制允许最多只有一个在连接。
 * </pre>
 */

public class WifiHelper implements WifiHelperContract {

    private final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");
    private WifiManager wifiManager;
    private WifiManager.WifiLock wifiLock;
    private Context applicationContext;
    private String connectingBSSID = "";
    private String connectedBSSID = "";
    private ConnectWifiCallback connectWifiCallback;
    private boolean isReadyToCallback = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action!=null) {
                if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    NetworkInfo.DetailedState state = info.getDetailedState();
                    if (state.equals(NetworkInfo.DetailedState.CONNECTED)) {
                        WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                        if (wifiInfo!=null && wifiInfo.getBSSID()!=null) {
                            connectedBSSID = wifiInfo.getBSSID();
                            if (wifiInfo.getBSSID().equalsIgnoreCase(connectingBSSID) && connectWifiCallback != null) {
                                connectWifiCallback.onConnectSuccess();
                            }
                        }
                    }
                } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                    int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                    SupplicantState netNewState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                    if (connectWifiCallback!=null && netNewState == SupplicantState.ASSOCIATING) {
                        isReadyToCallback = true;
                    }
                    if (isReadyToCallback && netNewState == SupplicantState.DISCONNECTED && error == WifiManager.ERROR_AUTHENTICATING) {
                        if (connectWifiCallback!=null)
                            connectWifiCallback.onConnectPswFail();
                    }
                }
            }
        }
    };

    @Override
    public void init(Context context) {
        applicationContext = context.getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        applicationContext.registerReceiver(broadcastReceiver,filter);
        wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        open();
        wifiLock = this.wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "WifiHelper");
        wifiLock.acquire();
    }

    @Override
    public WifiManager getManager() {
        return wifiManager;
    }

    @Override
    public WifiInfo getConnectionInfo() {
        if(this.wifiManager != null) {
            return wifiManager.getConnectionInfo();
        } else {
            return null;
        }
    }

    @Override
    public boolean open() {
        boolean bRet = true;
        if (this.wifiManager != null && !wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    @Override
    public void scan() {
        if (wifiManager!=null)
            wifiManager.startScan();
    }

    @Override
    public List<ScanResult> getScan() {
        if(this.wifiManager != null) {
            return wifiManager.getScanResults();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ScanResult> getScanOrderByLevel() {
        List<ScanResult> result = getScan();
        WifiInfo mWifiInfo = getConnectionInfo();
        Collections.sort(result, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                if (mWifiInfo != null && lhs.BSSID.equalsIgnoreCase(mWifiInfo.getBSSID())) {
                    return -1;
                } else if (mWifiInfo != null && rhs.BSSID.equalsIgnoreCase(mWifiInfo.getBSSID())) {
                    return 1;
                } else if (rhs.level > lhs.level) {
                    return 1;
                } else if (rhs.level == lhs.level) {
                    return 0;
                } else {
                    return -1;
                }
            }

            @Override
            public boolean equals(Object object) {
                return false;
            }
        });
        for (int i = result.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(result.get(i).SSID)) {
                result.remove(i);
            } else {
                for (int j = 0; j < i; j++) {
                    if (result.get(i).SSID.equals(result.get(j).SSID)) {
                        result.remove(i);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Observable<Integer> connectRx(ScanResult scanResult, String password) {
        return Observable.create(e -> {
            isReadyToCallback = false;
            if (!StringUtils.isEmpty(connectingBSSID)) {
                e.onNext(WifiHelperContract.Wifi_Another_Is_Connecting);
                e.onComplete();
            }
            else {
                connectingBSSID = scanResult.BSSID;
                if(getPswType(scanResult) != Wifi_Psw_Type_NO && !isSave(scanResult.SSID) && TextUtils.isEmpty(password)) {
                    connectingBSSID = "";
                    e.onNext(WifiHelperContract.Wifi_Err_Psw);
                    e.onComplete();
                    connectWifiCallback = null;
                }
                else {
                    connectedBSSID = "";
                    connectWifiCallback = new ConnectWifiCallback() {
                        @Override
                        public void onConnectPswFail() {
                            connectingBSSID = "";
                            e.onNext(WifiHelperContract.Wifi_Err_Psw);
                            e.onComplete();
                            connectWifiCallback = null;
                        }

                        @Override
                        public void onConnectSuccess() {
                            connectingBSSID = "";
                            e.onNext(WifiHelperContract.Wifi_Connected);
                            e.onComplete();
                            connectWifiCallback = null;
                        }
                    };
                    boolean result = connect(scanResult,password);
                    if (!result) {
                        connectingBSSID = "";
                        e.onNext(WifiHelperContract.Wifi_ConnectFail);
                        e.onComplete();
                        connectWifiCallback = null;
                    }
                }
            }
        });
    }

    private boolean connect(ScanResult AP, String password) {
        WifiConfiguration wifiConfig = this.getSaveConfiguration(AP.SSID);
        if(wifiConfig == null) {
            int type = getPswType(AP);
            wifiConfig = createWifiInfo(AP.SSID, password, type);
        }
        return enableNetwork(wifiConfig);
    }

    private boolean enableNetwork(final WifiConfiguration wifiConfiguration) {
        boolean result = this.wifiManager.enableNetwork(this.wifiManager.addNetwork(wifiConfiguration), true);
        if(result) {
            this.wifiManager.saveConfiguration();
        }
        return result;
    }

    private WifiConfiguration createWifiInfo(String ssId, String password, @WifiPswType int type) {
        final WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + ssId + "\"";
        WifiConfiguration wifiConfig = this.getSaveConfiguration(ssId);
        if (wifiConfig != null) {
            wifiManager.removeNetwork(wifiConfig.networkId);
        }
        switch (type) {
            case Wifi_Psw_Type_NO: {
                wifiConfiguration.allowedKeyManagement.set(0);
                return wifiConfiguration;
            }
            case Wifi_Psw_Type_WEP: {
                wifiConfiguration.allowedKeyManagement.set(0);
                wifiConfiguration.allowedAuthAlgorithms.set(0);
                wifiConfiguration.allowedAuthAlgorithms.set(1);
                if (password.length() == 0) {
                    break;
                }
                final int length = password.length();
                if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {
                    wifiConfiguration.wepKeys[0] = password;
                    return wifiConfiguration;
                }
                wifiConfiguration.wepKeys[0] = '\"' + password + '\"';
                return wifiConfiguration;
            }
            case Wifi_Psw_Type_PSK: {
                wifiConfiguration.allowedKeyManagement.set(1);
                if (password.length() == 0) {
                    break;
                }
                if (password.matches("[0-9A-Fa-f]{64}")) {
                    wifiConfiguration.preSharedKey = password;
                    return wifiConfiguration;
                }
                wifiConfiguration.preSharedKey = '\"' + password + '\"';
                return wifiConfiguration;
            }
            case Wifi_Psw_Type_EAP: {
                wifiConfiguration.allowedKeyManagement.set(1);
                if (password.length() == 0) {
                    break;
                }
                if (password.matches("[0-9A-Fa-f]{64}")) {
                    wifiConfiguration.preSharedKey = password;
                    return wifiConfiguration;
                }
                wifiConfiguration.preSharedKey = '\"' + password + '\"';
                return wifiConfiguration;
            }
        }
        return wifiConfiguration;
    }

    @Override
    public boolean isSave(String ssId) {
        return getSaveConfiguration(ssId) != null;
    }

    @Override
    public WifiConfiguration getSaveConfiguration(String ssId) {
        if(wifiManager != null) {
            List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + ssId + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    @Override
    public List<WifiConfiguration> getSaveConfigurationList() {
        if(wifiManager != null) {
            return wifiManager.getConfiguredNetworks();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getConnectingBSSID() {
        return connectingBSSID;
    }

    @Override
    public String getConnectedBSSID() {
        return connectedBSSID;
    }

    @Override
    public boolean removeConfiguration(String ssId) {
        WifiConfiguration wifiConfiguration = getSaveConfiguration(ssId);
        if(wifiConfiguration != null) {
            boolean result = wifiManager.removeNetwork(wifiConfiguration.networkId);
            wifiManager.saveConfiguration();
            return result;
        }
        return true;
    }

    @Override
    public void disConnectCurrent() {
        if(wifiManager != null) {
            WifiInfo wifiInfo = getConnectionInfo();
            if(wifiInfo != null) {
                wifiManager.disableNetwork(wifiInfo.getNetworkId());
            }
            wifiManager.disconnect();
            connectedBSSID = "";
        }
    }

    @Override
    public @WifiHelperContract.WifiPswType int getPswType(final ScanResult scanResult) {
        if (scanResult.capabilities.contains("WEP")) {
            return WifiHelperContract.Wifi_Psw_Type_WEP;
        }
        else if (scanResult.capabilities.contains("PSK")) {
            return WifiHelperContract.Wifi_Psw_Type_PSK;
        }
        else if (scanResult.capabilities.contains("EAP")) {
            return WifiHelperContract.Wifi_Psw_Type_EAP;
        }
        else
            return WifiHelperContract.Wifi_Psw_Type_NO;
    }

    @Override
    public void release() {
        if(wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
        if(wifiManager != null) {
            wifiManager = null;
        }
        releaseBroadcastReceiver(broadcastReceiver,applicationContext);
    }

    private void releaseBroadcastReceiver(BroadcastReceiver broadcastReceiver, Context context) {
        if (context!=null && broadcastReceiver!=null) {
            try {
                context.unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*以下为辅助方法*/
    private String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }

    /**
     * Encloses the incoming string inside double quotes, if it isn't already quoted.
     * @param s the input string
     * @return OnScreenChangeListener quoted string, of the form "input".  If the input string is null, it returns null
     * as well.
     */
    private String convertToQuotedString(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        // If already quoted, return as-is
        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s;
        }
        return '\"' + s + '\"';
    }

    /**
     * @param value input to check
     * @param allowedLengths allowed lengths, if any
     * @return true if value is OnScreenChangeListener non-null, non-empty string of hex digits, and if allowed lengths are given, has
     *  an allowed length
     */
    private boolean isHexOfLength(CharSequence value, int... allowedLengths) {
        if (value == null || !HEX_DIGITS.matcher(value).matches()) {
            return false;
        }
        if (allowedLengths.length == 0) {
            return true;
        }
        for (int length : allowedLengths) {
            if (value.length() == length) {
                return true;
            }
        }
        return false;
    }

    /*类，接口*/
    private interface ConnectWifiCallback {
        void onConnectPswFail();
        void onConnectSuccess();
    }
}