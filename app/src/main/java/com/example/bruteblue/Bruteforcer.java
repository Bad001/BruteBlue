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
            if(isConnected()) {
                this.result = range;
                pin.setText(formatPin(range));
                flag = true;
            }
            else {
                pin.setText(formatPin(range));
                range++;
            }
        }
    }

    public int getResult() {
        return result;
    }

    public String formatPin(int pin) {
        String pinFormatted = "";
        int pinLength = String.valueOf(pin).length();
        if(pinLength == 0) {
            pinFormatted = "0000";
        }
        else if(pinLength == 1) {
            pinFormatted = "000"+String.valueOf(pin);
        }
        else if(pinLength == 2) {
            pinFormatted = "00"+String.valueOf(pin);
        }
        else if(pinLength == 3) {
            pinFormatted = "0"+String.valueOf(pin);
        }
        else {
            pinFormatted = String.valueOf(pin);
        }
        return pinFormatted;
    }

    public Boolean isConnected() {
        Boolean flag = false;
        // code here
        return flag;
    }
}