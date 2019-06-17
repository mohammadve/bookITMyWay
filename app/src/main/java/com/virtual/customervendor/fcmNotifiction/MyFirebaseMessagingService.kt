package com.virtual.customervendor.fcmNotifiction


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.DashBoardActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.NotificationClass
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.AppLog
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        AppLog.e(TAG, "From: ${remoteMessage?.from}")
        if (remoteMessage != null) {
            AppLog.e(TAG, "Message data payload: " + remoteMessage.getData())
            val params = remoteMessage.data
            val JsonObject = JSONObject(params)

            AppLog.e("Json Object", "" + JsonObject.toString())
            try {
                val notificationClass = Gson().fromJson(JsonObject.toString(), NotificationClass::class.java)
                if (SharedPreferenceManager.getBoolean(AppKeys.IS_NOTIFICTION_SHOW)) {

                    if (!SharedPreferenceManager.getAuthToken().equals("")) {
                        sendNotification(notificationClass)
                    }
                }


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onNewToken(token: String?) {
        AppLog.e(TAG, "Refreshed token: $token")
        SharedPreferenceManager.setFCMToken(token)
    }


    private fun sendNotification(notificationClass: NotificationClass) {
        AppLog.e("pendingIntent", "pendingIntent= " + notificationClass.toString())
        var pendingIntent: PendingIntent? = null
        var notificationBuilder: NotificationCompat.Builder

        val intent = Intent(this, DashBoardActivity::class.java)
        var bundle = Bundle()
        bundle.putSerializable(AppConstants.NOTIFICATION_DATA, notificationClass)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)


        val channelId = AppConstants.default_notification_channel_id
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)



        if (notificationClass.type.equals("newoffer")) {
            val futureTarget = Glide.with(this).asBitmap().load(notificationClass.image).submit()
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.splash_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.splash_logo))
                    .setContentTitle(resources.getString(R.string.get) + " " + notificationClass.discount + " % " + resources.getString(R.string.off))
                    .setContentText(notificationClass.startdate + " to " + notificationClass.endate)
                    .setAutoCancel(true)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(futureTarget.get()))
                    .setContentIntent(pendingIntent)
        } else {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.splash_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.splash_logo))
                    .setContentTitle(notificationClass.title)
                    .setContentText(notificationClass.message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
        }

        if (!SharedPreferenceManager.getBoolean(AppKeys.IS_NOTIFICTION_SOUND)) {
            notificationBuilder.priority = IMPORTANCE_LOW
            notificationBuilder.setSound(null)
        } else {
            notificationBuilder.setSound(defaultSoundUri)

        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (!SharedPreferenceManager.getBoolean(AppKeys.IS_NOTIFICTION_SOUND)) {
                val channel = NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(channel)
            } else {
                val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}