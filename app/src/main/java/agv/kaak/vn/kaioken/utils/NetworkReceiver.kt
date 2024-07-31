package agv.kaak.vn.kaioken.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.util.Log


class NetworkReceiver(val callBack:OnChangeNetwork) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            callBack.onNetworkConnected()
        } else {
            callBack.onNetworkDisconnected()
        }
    }

    interface OnChangeNetwork{
        fun onNetworkConnected()
        fun onNetworkDisconnected()
    }
}