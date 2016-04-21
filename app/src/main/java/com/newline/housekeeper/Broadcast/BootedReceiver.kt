package com.newline.housekeeper.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Administrator on 2016/4/19.
 */
class BootedReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        throw UnsupportedOperationException()
        //context.startService(intent:)
    }
}