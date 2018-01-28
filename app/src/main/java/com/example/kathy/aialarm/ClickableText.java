package com.example.kathy.aialarm;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kathy on 1/28/2018.
 */

public class ClickableText {
    private View button;
    public ClickableText(View view){
        button = view;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button instanceof TextView){
                    TextView textView = (TextView)button;
                    if(textView.getTypeface().isBold())
                        textView.setTypeface(null, Typeface.NORMAL);
                    else
                        textView.setTypeface(null, Typeface.BOLD);
                }
            }
        });
    }


}
