package com.example.kathy.aialarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeSetter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeSetter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSetter extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView alarmState;
    Switch switch2;
    Context context;
    PendingIntent pendingIntent;
    static final int NUM_DAYS = 7;
    ClickableText dateSelection[] = new ClickableText[NUM_DAYS];
    enum Day {Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};
    int alarmHour, alarmMinute, alarmDayDisplacement = 0;

    final long PERIOD = 0;
    public TimeSetter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeSetter.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeSetter newInstance(String param1, String param2) {
        TimeSetter fragment = new TimeSetter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_setter, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        alarmState = (TextView) view.findViewById(R.id.alarmState);
        switch2 = (Switch) view.findViewById(R.id.switch2);

        dateSelection[0] = new ClickableText(view.findViewById(R.id.monday));
        dateSelection[1] = new ClickableText(view.findViewById(R.id.tuesday));
        dateSelection[2] = new ClickableText(view.findViewById(R.id.wednesday));
        dateSelection[3] = new ClickableText(view.findViewById(R.id.thursday));
        dateSelection[4] = new ClickableText(view.findViewById(R.id.friday));
        dateSelection[5] = new ClickableText(view.findViewById(R.id.saturday));
        dateSelection[6] = new ClickableText(view.findViewById(R.id.sunday));

        final Calendar calendar = Calendar.getInstance();
        final Intent intent = new Intent(getActivity(), AlarmReceiver.class);

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                updateAlarmTime();
                setCalendar(calendar);
                intent.putExtra("ringtone", true);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PERIOD, pendingIntent);
                Log.e("Final", calendar.getTime().toString());
            } else {
                setAlarmState("Off");
                alarmManager.cancel(pendingIntent);
                intent.putExtra("ringtone", false);
                getActivity().sendBroadcast(intent);
            }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setAlarmState(String text){
        alarmState.setText(text);
    }

    private void setCalendar(Calendar calendar){
        Date alarmDate = getClosestDate(getNextEnabledDay());
        if(alarmDate != null) {
            calendar.setTime(alarmDate);

            calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
            calendar.set(Calendar.MINUTE, alarmMinute);

            setAlarmState("Alarm set to " + alarmHour + ":" + alarmMinute);
        }
    }

    private Day getNextEnabledDay(){
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
            /*Day currentDay = getCurrentDay();
            int displacement = day.ordinal() - currentDay.ordinal();
            if (displacement < 0) {
                displacement = Math.abs(displacement) + NUM_DAYS;
            }*/
            c.add(Calendar.DATE, alarmDayDisplacement);
            Log.e("getClosestDate", c.getTime().toString());
            return c.getTime();
        } else {
            return null;
        }
    }

    private Day getCurrentDay(){
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
}
