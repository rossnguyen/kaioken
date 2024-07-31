package agv.kaak.vn.kaioken.service

import agv.kaak.vn.kaioken.activity.ForceLogoutActivity
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import io.socket.emitter.Emitter
import org.json.JSONObject


class FloattingViewService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val onListenerForceLoginResponse: OnListenerResponse = object : OnListenerResponse {
            override fun callBack(result: String?) {
                //do logout
                //forceLoginView.visibility=View.VISIBLE
                val jsonResponse = JSONObject(result)
                val deviceName = jsonResponse.getString("device_name")
                Log.d("****ForceLogout", deviceName)
                val intent = Intent(this@FloattingViewService, ForceLogoutActivity::class.java)
                intent.putExtra(ForceLogoutActivity.DEVICE_NAME, deviceName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        //listen socket for force login
        Constraint.BASE_SOCKET?.on("forceLogout") { args ->
            val async = RunOnUIServiceThread(onListenerForceLoginResponse)
            async.execute(args[0].toString())
        }
        return START_NOT_STICKY
    }

    class RunOnUIServiceThread(var onListenerResponse: OnListenerResponse) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            return params[0]!!
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            onListenerResponse.callBack(result)
        }
    }

    interface OnListenerResponse {
        fun callBack(result: String?)
    }
}
