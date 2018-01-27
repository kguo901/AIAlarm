package com.example.kathy.aialarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    //TimePicker timePicker;
    TextView alarmState;
    Switch switch1;
    Context context;
    PendingIntent pendingIntent;
    final long PERIOD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //timePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmState = (TextView) findViewById(R.id.alarmState);
        switch1 = (Switch) findViewById(R.id.switch1);

        final Calendar calendar = Calendar.getInstance();
        final Intent intent = new Intent(this, AlarmReceiver.class);
/*
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (Build.VERSION.SDK_INT >= 23 ){
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());


                        setAlarmState("Alarm set to " + timePicker.getHour() + ":" + timePicker.getMinute());
                    }
                    else{
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        setAlarmState("Alarm set to " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                    }
                    intent.putExtra("ringtone", true);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PERIOD, pendingIntent);
                    Log.e("Main activity:", calendar.getTime().toString());
                } else {
                    setAlarmState("Off");
                    alarmManager.cancel(pendingIntent);
                    intent.putExtra("ringtone", false);
                    sendBroadcast(intent);
                }
            }
        });*/

        //Testing clock settings
        FloatingActionButton tempButton = (FloatingActionButton) findViewById(R.id.fab);
        tempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newAlarmIntent = new Intent(MainActivity.this,
                        ClockSettingsActivity.class);
                startActivity(newAlarmIntent);
            }
        });
    }

    private void setAlarmState(String text){
        alarmState.setText(text);
    }
}
