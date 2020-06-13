package com.hitham.miscarros.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notify notifier = new Notify(context);
        NotificationCompat.Builder unaPutaBuilder = notifier.getChannelNotification();
        notifier.getManager().notify(69,unaPutaBuilder.build());
    }


}
