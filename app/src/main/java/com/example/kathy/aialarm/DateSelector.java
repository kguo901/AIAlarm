package com.example.kathy.aialarm;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kathy on 1/30/2018.
 */

public class DateSelector {

    private static final int NUM_DAYS = 7;
    private ClickableText dateSelection[] = new ClickableText[NUM_DAYS];
    private enum Day {Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};
    private int alarmHour, alarmMinute, alarmDayDisplacement = 0;
    private TimePicker timePicker;
    private Switch weeklyRepeatSwitch;
    private boolean switchIsON = false;
    private TextView alarmState;
    private Context context;

    public DateSelector(View view){
        dateSelection[0] = new ClickableText(view.findViewById(R.id.monday));
        dateSelection[1] = new ClickableText(view.findViewById(R.id.tuesday));
        dateSelection[2] = new ClickableText(view.findViewById(R.id.wednesday));
        dateSelection[3] = new ClickableText(view.findViewById(R.id.thursday));
        dateSelection[4] = new ClickableText(view.findViewById(R.id.friday));
        dateSelection[5] = new ClickableText(view.findViewById(R.id.saturday));
        dateSelection[6] = new ClickableText(view.findViewById(R.id.sunday));

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        alarmState = (TextView) view.findViewById(R.id.alarmState);
        weeklyRepeatSwitch = (Switch) view.findViewById(R.id.repeatWeeklySwitch);
        weeklyRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setAlarmRepeat(true);
                } else {
                    setAlarmRepeat(false);
                }
            }
        });

        context = view.getContext();
        /*LocalBroadcastManager.getInstance(context).registerReceiver(mYourBroadcastReceiver,
                new IntentFilter("alarm-triggered"));*/
    }

    public void setAlarmState(String text){
        alarmState.setText(text);
    }

    public void setCalendar(Calendar calendar){
        updateAlarmTime();
        Date alarmDate = getClosestDate(getNextEnabledDay());
        if(alarmDate != null) {
            calendar.setTime(alarmDate);

            calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
            calendar.set(Calendar.MINUTE, alarmMinute);

            setAlarmState("Alarm set to " + alarmHour + ":" + alarmMinute);
        }
    }

    public void setAlarmRepeat(boolean setting){
        if(switchIsON != setting)
            switchIsON = !switchIsON;
    }

    public Day getNextEnabledDay(){
        alarmDayDisplacement = 0;
        Day day = getCurrentDay();
        Calendar currentTime = Calendar.getInstance();
        boolean test = compareTime(currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), alarmHour, alarmMinute)>0;
        if(!dateSelection[day.ordinal()].isTextEnabled()
                || test){
            int displacement = 1;
            int nextClosestDay;
            boolean foundNextEnabled = false;
            while (displacement < NUM_DAYS + 1 && !foundNextEnabled){
                nextClosestDay = (day.ordinal() + displacement)%NUM_DAYS;
                if(dateSelection[nextClosestDay].isTextEnabled()){
                    day = Day.values()[nextClosestDay];
                    foundNextEnabled = true;
                } else
                    displacement++;
            }
            if(foundNextEnabled)
                alarmDayDisplacement = displacement;
            else
                dateSelection[getCurrentDay().ordinal()].enableView();
        }
        Log.e("getNextEnabledDay",day.name());
        return day;
    }

    private Date getClosestDate(Day day){
        if(day != null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, alarmDayDisplacement);
            Log.e("getClosestDate", c.getTime().toString());
            return c.getTime();
        } else {
            return null;
        }
    }

    public Day getCurrentDay(){
        switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return Day.Monday;
            case Calendar.TUESDAY:
                return Day.Tuesday;
            case Calendar.WEDNESDAY:
                return Day.Wednesday;
            case Calendar.THURSDAY:
                return Day.Thursday;
            case Calendar.FRIDAY:
                return Day.Friday;
            case Calendar.SATURDAY:
                return Day.Saturday;
            case Calendar.SUNDAY:
                return Day.Sunday;
            default:
                return null;
        }
    }

    //returns 1 if first time is greater than second
    //returns 0 if both times are equal
    //returns -1 if second time is greather than first
    private int compareTime(int hour1, int minute1, int hour2, int minute2){
        int hourDiff = hour1 - hour2;
        int minuteDiff = minute1 - minute2;
        if(hourDiff > 0)
            return 1;
        else if(hourDiff < 0)
            return -1;
        else{
            Log.e("minute 1", Integer.toString(minute1));
            Log.e("minute 2", Integer.toString(minute1));
            Log.e("minute 1- minute 2", Integer.toString(minuteDiff));
            Log.e("minuteDiff > 0", Boolean.toString(minuteDiff > 0));
            Log.e("minuteDiff < 0", Boolean.toString(minuteDiff < 0));
            Log.e("minuteDiff = 0", Boolean.toString(minuteDiff == 0));
            if(minuteDiff > 0)
                return 1;
            else if(minuteDiff < 0)
                return -1;
            else
                return 0;
        }
    }

    private void updateAlarmTime(){
        if (Build.VERSION.SDK_INT >= 23) {
            alarmHour = timePicker.getHour();
            alarmMinute = timePicker.getMinute();
        } else {
            alarmHour = timePicker.getCurrentHour();
            alarmMinute = timePicker.getCurrentMinute();
        }
    }
   /* private final BroadcastReceiver mYourBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.e("dateSelector", "received");

            //TODO: if no dates are enabled, turn alarm switch off
            onDestroy();
        }
    };
*/
    public boolean repeatWeekly(){
        return switchIsON;
    }

    public void disableDay(Day day){
        dateSelection[day.ordinal()].disableView();
    }
    private void onDestroy(){
       // LocalBroadcastManager.getInstance(context).unregisterReceiver(mYourBroadcastReceiver);
    }
}
