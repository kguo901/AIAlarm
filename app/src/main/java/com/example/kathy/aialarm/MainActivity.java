package com.example.kathy.aialarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

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
