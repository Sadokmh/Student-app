package com.example.sadokmm.student.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class ReceiverServiceActuNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        // get the bundles in the message
        final Bundle bundle = intent.getExtras();
        // check the action equal to the action we fire in broadcast,
        if (intent.getAction().equalsIgnoreCase("com.example.sadokmm.student.Broadcasts"))
        //read the data from the intent
        {

            AQuery aq = new AQuery(context);
            aq.ajax(bundle.getString("img"), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap object, AjaxStatus status) {
                    super.callback(url, object, status);

                }
            });


            /*MessageActuNotification actuNotify= new MessageActuNotification();
            actuNotify.notify(context,bundle.getString("liste"),223 , bundle.getString("img"));}*/
            /*ArrayList<Actualite> list = (ArrayList<Actualite>) bundle.get("liste");
            Toast.makeText(context, "ok", Toast.LENGTH_LONG).show();*/
        }
    }
}



