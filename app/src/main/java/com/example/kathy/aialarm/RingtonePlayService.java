package com.example.kathy.aialarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kathy on 1/22/2018.
 */

public class RingtonePlayService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    Uri uri;
    Ringtone r;
    static boolean serviceStarted = false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ringtone service", "started");

        boolean playRingtone = intent.getExtras().getBoolean("ringtone", false);
        if(!serviceStarted && playRingtone){
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            r = RingtoneManager.getRingtone(getApplicationContext(), uri);
            r.play();

            createNotification();
            serviceStarted = true;
        } else if (serviceStarted && !playRingtone) {
            r.stop();
            serviceStarted = false;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Ringtone service stopped.", Toast.LENGTH_SHORT);
    }

    private void createNotification(){
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle("AI Alarm")
                .setContentText("Rise and shine!")
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(uri)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = notificationBuilder.build();

        notificationManager.notify(0, notification);
    }
}
