package com.example.kathy.aialarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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

        Intent intent1 = new Intent("alarm-triggered");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
}
