package com.example.sadokmm.student;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


public class MessageNotifClass {

    private Context context ;
    private final String CHANNEL_NAME = "channel1";
    private final String CHANNEL_ID = "55";
    private NotificationManagerCompat notificationManager;

    private String titre;
    private String msg;
    private Bitmap img;


    public MessageNotifClass(Context context, String titre, String msg, Bitmap img) {
        this.context = context;
        this.titre = titre;
        this.msg = msg;
        this.img = img;
        notificationManager=NotificationManagerCompat.from(context);
    }

    public void createNotificationChannels(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("I'M THE FIEST CHANNEL");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);

        }
        createNotif();
    }




    public void createNotif() {


        //createNotificationChannels();

        Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.isgicon)
                .setColor(111111)
                .setBadgeIconType(R.drawable.ic_launcher_background)
                .setContentTitle(titre)
                .setContentText(msg)
                .setChannelId(CHANNEL_ID)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(0,notification);


    }



}
