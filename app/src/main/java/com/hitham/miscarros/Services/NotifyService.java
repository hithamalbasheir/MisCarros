package com.hitham.miscarros.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.hitham.miscarros.Activities.MainActivity;
import com.hitham.miscarros.R;

public class NotifyService extends JobService {
    private static final String channel_id = "myChannel";
    private static final int notification_id = 69;
    public NotifyService() {
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("myFuckingTag","Job started");
        PendingIntent miInento = PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        NotifyUser(miInento);
        return true;
    }

    private void NotifyUser(PendingIntent miInento) {
        NotificationManager myManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel myChannel = new NotificationChannel(channel_id,getResources().getString(R.string.channel_name),NotificationManager.IMPORTANCE_HIGH);
            myChannel.setDescription(getResources().getString(R.string.channel_descrip));
            myManager.createNotificationChannel(myChannel);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this,channel_id)
                    .setContentText(getResources().getString(R.string.notification_content_text))
                    .setContentTitle(getResources().getString(R.string.notification_title_text))
                    .setContentIntent(miInento)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            myManager.notify(notification_id,notification.build());
        }
        else{
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentText(getResources().getString(R.string.notification_content_text))
                    .setContentTitle(getResources().getString(R.string.notification_title_text))
                    .setContentIntent(miInento)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            myManager.notify(notification_id,notification.build());
        }

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
