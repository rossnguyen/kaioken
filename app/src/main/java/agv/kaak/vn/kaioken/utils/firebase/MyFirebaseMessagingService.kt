package agv.kaak.vn.kaioken.utils.firebase

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.SplashActivity
import agv.kaak.vn.kaioken.entity.result.NotificationResult
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.lang.Exception
import android.content.pm.PackageManager
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        var numberMessage=0
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("****FCM", "From: " + p0!!.from)

        // Check if message contains a data payload.
        if (p0.data.size > 0) {
            Log.d("****FCM_data", "Message data payload: " + p0.data)

            handleWithData(p0)
        }

        // Check if message contains a notification payload.
        if (p0.notification != null) {
            Log.d("****FCM", "Message Notification Body: " + p0.notification!!.body)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun handleWithData(remoteMessage: RemoteMessage) {
        val notificationByFirebase: NotificationResult = Gson().fromJson<NotificationResult>(
                remoteMessage.data["body"],
                NotificationResult::class.java
        )

        Log.d("****FCM_Message", remoteMessage.data["body"])

        // Build Notification , setOngoing keeps the notification always in status bar
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationByFirebase.info?.pageInfo?.pageName)
                .setContentText(notificationByFirebase.content)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setNumber(++numberMessage)

        /*val pm = packageManager
        val launchIntent = pm.getLaunchIntentForPackage("agv.kaak.vn.kaioken")
        val contentIntent = PendingIntent.getActivity(this, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        */
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("FROM_NOTIFICATION",true)
        val contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(contentIntent)

        // Gets an instance of the NotificationManager service
        val mNotifyMgr = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANEL_ID = "KAIOKEN"
            val notificationChanel = NotificationChannel(CHANEL_ID, "Kaioken", NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANEL_ID)
            mNotifyMgr.createNotificationChannel(notificationChanel)
        }

        //first param is a id of notification
        //if want show multi notification: set id different
        mNotifyMgr.notify(1001, mBuilder.build())
        //mNotifyMgr.notify(Random().nextInt(1000), mBuilder.build())
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)
    }
}