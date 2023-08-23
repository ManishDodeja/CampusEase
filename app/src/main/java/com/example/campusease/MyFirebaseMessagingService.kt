package com.example.campusease

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId="notification_channel"
const val channelName="com.example.campusease"
class MyFirebaseMessagingService : FirebaseMessagingService() {


    //generate the notification
    //attach the notification with custom layout
    //show the notification

    fun generateNotification(title:String, message:String){
        val intent=Intent(this@MyFirebaseMessagingService, First_Screen::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //channel id and channel nameToast.makeText(applicationContext,"start", Toast.LENGTH_SHORT).show()

        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo2)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(1)

        builder=builder.setContent(getRemoteView(title, message))


        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        else{
            Log.d("version is lesser ", "oreo")
        }

        notificationManager.notify(0, builder.build())
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification!=null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews? {
        val remoteView= RemoteViews("com.example.campusease",R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title,title)
        remoteView.setTextViewText(R.id.notification_message,message)
        remoteView.setImageViewResource(R.id.notification_logo,R.drawable.logo2)
        return remoteView
    }

}