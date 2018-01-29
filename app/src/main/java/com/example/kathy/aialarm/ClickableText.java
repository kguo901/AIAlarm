package com.example.kathy.aialarm;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kathy on 1/28/2018.
 */

public class ClickableText {
    private View button;
    private boolean isEnabled = false;
    public ClickableText(View view){
        button = view;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnabled)
                    disableView();
                else
                    enableView();
            }
        });
    }

    public boolean isTextEnabled(){
        return isEnabled;
    }

    public void disableView(){
        if(button instanceof TextView){
            TextView textView = (TextView)button;
            textView.setTypeface(null, Typeface.NORMAL);
            isEnabled = false;
        }
    }

    public void enableView(){
        if(button instanceof TextView){
            TextView textView = (TextView)button;
            textView.setTypeface(null, Typeface.BOLD);
            isEnabled = true;
        }
    }
}
