package me.kkwang.commonlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import me.kkwang.commonlib.base.AppContextUtil;

/**
 * Created by Administrator on 2016/3/5.
 */
public class NetUtils {


    private NetUtils() {

    }

    public static boolean isNetworkConnected() {
        if (AppContextUtil.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContextUtil.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected() {
        if (AppContextUtil.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContextUtil.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected() {
        if (AppContextUtil.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContextUtil.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType() {
        if (AppContextUtil.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContextUtil.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
