package com.rwoods.thecomicsoracle.data.receiver

/**
 * Created by rwoods on 1/29/2016.
 */

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

import com.rwoods.thecomicsoracle.util.NetworkUtil

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val status = NetworkUtil.getConnectivityStatusString(context)

        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
    }
}