package com.example.pawfectcareapp.ui.notificatioHelper;

import static android.os.Build.VERSION_CODES.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.pawfectcareapp.BuildConfig;
import com.example.pawfectcareapp.R;
import com.example.pawfectcareapp.Utils.SharedPref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        sendNotification(remoteMessage);
        Map<String,String> data=remoteMessage.getData();
        SharedPref utils=new SharedPref();
        if(remoteMessage.getNotification().getBody().contains("GATE-IN")) {
            utils.writePrefString(MyFirebaseMessagingService.this, BuildConfig.HAS_NOTIFICATION_ASSIGN_TASK, "true");
        }

    }

    private void sendNotification(RemoteMessage remoteMessage){
        Map<String,String> data=remoteMessage.getData();
        String title =data.get("title");
        String topic =data.get("topic");
        String content = data.get("content");

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="AscentDev";

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"PawfectCare Notification",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("AscentDev channel for app test FCM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(com.example.pawfectcareapp.R.drawable.bell_notif)
                .setTicker("Hearty365")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentInfo("info");

        notificationManager.notify(1,notificationBuilder.build());
    }
}
