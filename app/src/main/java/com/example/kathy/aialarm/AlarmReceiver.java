package com.example.kathy.aialarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by kathy on 1/21/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("broadcast received", "!");
        Intent serviceIntent = new Intent(context, RingtonePlayService.class);

        boolean playRingtone = intent.getExtras().getBoolean("ringtone", false);
        serviceIntent.putExtra("ringtone", playRingtone);
        context.startService(serviceIntent);
    }
}
