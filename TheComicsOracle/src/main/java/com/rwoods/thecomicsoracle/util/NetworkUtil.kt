package com.rwoods.thecomicsoracle.util

/**
 * Created by rwoods on 1/29/2016.
 */

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Cookie
import java.util.*

class NetworkUtil protected constructor()// Exists only to defeat instantiation.
{
    internal var cookies: MutableList<Cookie> = ArrayList()
    var aesEncrypt: AesEncrypt? = null

    fun isOnline(context: Context): Boolean {
        when (getConnectivityStatus(context)) {
            TYPE_WIFI -> return true

            TYPE_MOBILE -> return true

            TYPE_NOT_CONNECTED -> return false

            else -> return false
        }
    }

    private var  online: Boolean = false

    fun setOnline(online: Boolean) {
        this.online = online
    }


    companion object {

        val TYPE_WIFI = 1
        val TYPE_MOBILE = 2
        val TYPE_NOT_CONNECTED = 0

        private var online = true

        fun getConnectivityStatus(context: Context): Int {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    return TYPE_WIFI
                }

                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    return TYPE_MOBILE
                }
            }
            return TYPE_NOT_CONNECTED
        }

        fun getConnectivityStatusString(context: Context): String? {
            val conn = getConnectivityStatus(context)
            var status: String? = null
            if (conn == TYPE_WIFI) {
                status = "Wifi enabled"
            } else if (conn == TYPE_MOBILE) {
                status = "Mobile data enabled"
            } else if (conn == TYPE_NOT_CONNECTED) {
                status = "Not connected to Internet"
            }
            return status
        }
    }
}