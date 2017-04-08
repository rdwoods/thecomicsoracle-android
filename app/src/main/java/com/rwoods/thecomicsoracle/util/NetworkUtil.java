package com.rwoods.thecomicsoracle.util;

/**
 * Created by rwoods on 1/29/2016.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient;
import com.rwoods.thecomicsoracle.encryption.AesEncrypt;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkUtil {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;

    private static boolean online = true;
    List<Cookie> cookies = new ArrayList<>();
    private AesEncrypt aesEncrypt;

    private static NetworkUtil instance = null;
    protected NetworkUtil() {
        // Exists only to defeat instantiation.
    }

    public static NetworkUtil getInstance() {
        if(instance == null) {
            instance = new NetworkUtil();
        }
        return instance;
    }


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getInstance().getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public boolean isOnline(Context context) {
        switch (getConnectivityStatus(context)){
            case NetworkUtil.TYPE_WIFI:
                return true;

            case NetworkUtil.TYPE_MOBILE:
                return true;

            case NetworkUtil.TYPE_NOT_CONNECTED:
                return false;

            default:
                return false;
        }
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public List<Cookie> getCookies(){
        HashMap<HttpUrl, List<Cookie>> cookieMap = ComicsOracleRetrofitApiRestClient.getAccessibleCookieStore();

        for (HttpUrl key : cookieMap.keySet()) {
            for (Cookie cookie : cookieMap.get(key)){
                cookies.add(cookie);
            }
        }

        return cookies;
    }


    public String getSpecificCookie(String cookieName){
        HashMap<HttpUrl, List<Cookie>> cookieMap = ComicsOracleRetrofitApiRestClient.getAccessibleCookieStore();

        String cookieValue = null;
        for (HttpUrl key : cookieMap.keySet()) {
            for (Cookie cookie : cookieMap.get(key)){
                if (cookie.name().contains(cookieName)){
                    cookieValue = cookie.value();
                }
            }
        }

        return cookieValue;
    }

    public AesEncrypt getAesEncrypt() {
        return aesEncrypt;
    }

    public void setAesEncrypt(AesEncrypt aesEncrypt) {
        this.aesEncrypt = aesEncrypt;
    }
}