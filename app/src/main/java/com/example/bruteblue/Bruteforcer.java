package com.example.bruteblue;

import android.content.Context;
import android.widget.TextView;

public class Bruteforcer {

    // Attributes
    private int result;
    private Context context;

    public Bruteforcer(Context context) {
        this.context = context;
    }

    public void start(int range) {
        boolean flag = false;
        TextView pin = (TextView) ((MainActivity)context).findViewById(R.id.Pin);
        while(range <= 9999 && !flag) {
            // Try this pin
            if(range == 9999) {
                this.result = range;
                pin.setText(String.valueOf(range));
                flag = true;
            }
            else {
                pin.setText(String.valueOf(range));
                range++;
            }
        }
    }

    public int getResult() {
        return result;
    }
}