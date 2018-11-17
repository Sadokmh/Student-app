package com.example.sadokmm.student;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Activities.ProfileActivity;
import com.example.sadokmm.student.Activities.firstActivity;


public class MessageActuNotification {

    private static final String NOTIFICATION_TAG = "NewMessage";
    private static final String CHANNEL_ID = "25";
    private static final String CHANNEL_NAME = "CHANNEL_25";


    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";







    public static void notify(final Context context,
                              final String exampleString, final int number  , Bitmap pic) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.


        //String imgurl = img;

        /*AQuery aq = new AQuery(context);
        aq.ajax(imgurl,Bitmap.class,0,new AjaxCallback<Bitmap>(){
            @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {
                super.callback(url, object, status);
                picture = object ;

            }
        });*/



        final String ticker = "Nouvelle notification";
        final String title = "ISG Sousse";
        final String text =   exampleString ;

        //final Notification builderO;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);


            Notification builderO = new Notification.Builder(context,CHANNEL_ID)

                    // Set appropriate defaults for the notification light, sound,
                    // and vibration.

                    // Set required fields, including the small icon, the
                    // notification title, and text.
                    .setSmallIcon(R.drawable.isgicon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setChannelId(CHANNEL_ID)

                    // All fields below this line are optional.

                    // Use a default priority (recognized on devices running Android
                    // 4.1 or later)

                    // Provide a large icon, shown with the notification in the
                    // notification drawer on devices running Android 3.0 or later.
                    .setLargeIcon(pic)

                    // Set ticker text (preview) information for this notification.
                    .setTicker(ticker)

                    // Show a number. This is useful when stacking notifications of
                    // a single type.
                    .setNumber(number)

                    // If this notification relates to a past or upcoming event, you
                    // should set the relevant time information using the setWhen
                    // method below. If this call is omitted, the notification's
                    // timestamp will by set to the time at which it was shown.
                    // TODO: Call setWhen if this notification relates to a past or
                    // upcoming event. The sole argument to this method should be
                    // the notification timestamp in milliseconds.
                    //.setWhen(...)

                    // Set the pending intent to be initiated when the user touches
                    // the notification.
                    .setContentIntent(
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                    PendingIntent.FLAG_UPDATE_CURRENT))


                    // Automatically dismiss the notification when it is touched.
                    .setAutoCancel(true)
                    .build();

            notify(context, builderO);
        }

        else {
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)

                    // Set appropriate defaults for the notification light, sound,
                    // and vibration.
                    .setDefaults(Notification.DEFAULT_ALL)

                    // Set required fields, including the small icon, the
                    // notification title, and text.
                    .setSmallIcon(R.drawable.isgicon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setChannelId(CHANNEL_ID)

                    // All fields below this line are optional.

                    // Use a default priority (recognized on devices running Android
                    // 4.1 or later)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    // Provide a large icon, shown with the notification in the
                    // notification drawer on devices running Android 3.0 or later.
                    .setLargeIcon(pic)

                    // Set ticker text (preview) information for this notification.
                    .setTicker(ticker)

                    // Show a number. This is useful when stacking notifications of
                    // a single type.
                    .setNumber(number)

                    // If this notification relates to a past or upcoming event, you
                    // should set the relevant time information using the setWhen
                    // method below. If this call is omitted, the notification's
                    // timestamp will by set to the time at which it was shown.
                    // TODO: Call setWhen if this notification relates to a past or
                    // upcoming event. The sole argument to this method should be
                    // the notification timestamp in milliseconds.
                    //.setWhen(...)

                    // Set the pending intent to be initiated when the user touches
                    // the notification.
                    .setContentIntent(
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                    PendingIntent.FLAG_UPDATE_CURRENT))

                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text)
                            .setBigContentTitle(title)
                            .setSummaryText("Publication"))

                    .addAction(
                            R.drawable.ic_about,
                            "about",
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                            .setType("text/plain")
                                            .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                    PendingIntent.FLAG_UPDATE_CURRENT))


                    // Automatically dismiss the notification when it is touched.
                    .setAutoCancel(true);
            notify(context, builder.build());
        }



    }












    public void createNotification(Context context , String title, String message , Bitmap img)
    {

        mContext = context;
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(context , firstActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext,CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setLargeIcon(img)
                .setChannelId(CHANNEL_ID)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        else {
            notify(context,message,0,img);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }







    public static void notify2(final Context context,
                              final String exampleString, final int number  , Bitmap pic) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.


        //String imgurl = img;

        /*AQuery aq = new AQuery(context);
        aq.ajax(imgurl,Bitmap.class,0,new AjaxCallback<Bitmap>(){
            @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {
                super.callback(url, object, status);
                picture = object ;

            }
        });*/



        final String ticker = "Nouvelle notification";
        final String title = "ISG Sousse";
        final String text =   exampleString ;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.isgicon)
                .setContentTitle(title)
                .setContentText(text)
                .setChannelId(CHANNEL_ID)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(pic)

                // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText("Publication"))

                .addAction(
                        R.drawable.ic_about,
                        "about",
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                PendingIntent.FLAG_UPDATE_CURRENT))


                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);


        notify(context, builder.build());
    }


    //TEST
  /*  public void addNotification(Context context , String title , String msg) {

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        //ntent resultIntent = new Intent(this, NotificationReceiverActivity.class);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context;
        //stackBuilder.addParentStack(NotificationReceiverActivity.class);
        //stackBuilder.addNextIntent(resultIntent);
        //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions By default, the notification's text content is truncated to fit one line. If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle(). For example, the following code creates a larger text area"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(android.R.color.holo_red_dark));
                .addAction(R.drawable.ic_launcher_foreground, "Call", resultPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "More", resultPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "And more", resultPendingIntent);


        if (notificationManager != null) {

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }*/










    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}