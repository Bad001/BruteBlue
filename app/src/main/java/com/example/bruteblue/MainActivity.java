package com.example.bruteblue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Attributes
    private Button scanBtn, startBruteforceBtn;
    private TextView pin, yourTarget;
    private EditText editRange;
    private Bruteforcer bruteforcer;
    private ListView deviceList;
    ArrayList list = new ArrayList();
    IntentFilter filter;
    ArrayList<BluetoothDevice> mArrayAdapter = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter BA;
    private BluetoothDevice target;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Permission allowed",Toast.LENGTH_SHORT).show();
            scanBtn.setEnabled(true);
            startBruteforceBtn.setEnabled(true);
        }
        else {
            Intent intent = new Intent(this, InformActivity.class);
            startActivity(intent);
            scanBtn.setEnabled(false);
            startBruteforceBtn.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bruteforcer = new Bruteforcer(this);
        scanBtn = findViewById(R.id.ScanBtn);
        yourTarget = findViewById(R.id.selectedTarget);
        progressBar = findViewById(R.id.progressBar1);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        startBruteforceBtn = findViewById(R.id.BruteforceBtn);
        startBruteforceBtn.setEnabled(false);
        BA = BluetoothAdapter.getDefaultAdapter();
        pin = findViewById(R.id.Pin);
        editRange = findViewById(R.id.inputRange);
        deviceList = findViewById(R.id.listView);
        editRange.setGravity(Gravity.CENTER_HORIZONTAL);
        editRange.addTextChangedListener(inputTextWatcher);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BA.isEnabled()) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                    list.clear();
                    mArrayAdapter.clear();
                    yourTarget.setText("Target:");
                    BA.startDiscovery();
                    registerReceiver(mReceiver, filter);
                    scanBtn.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            scanBtn.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    },10000);// set time as per your requirement
                }
                else {
                    BA.enable();
                }
            }
        });
        startBruteforceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRange.getText().toString().isEmpty()) {
                    bruteforcer.start(0,target);
                }
                else {
                    bruteforcer.start(Integer.valueOf(editRange.getText().toString()),target);
                }
            }
        });
    }

    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(s.toString().isEmpty()) {
                pin.setText(bruteforcer.formatPin(0));
            }
            else {
                pin.setText(bruteforcer.formatPin(Integer.valueOf(s.toString())));
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, final Intent intent) {
            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the recently discovered device in a list
                mArrayAdapter.add(bluetoothDevice);
                list.add(bluetoothDevice.getName());
                deviceList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list));
            }
            deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                    // Get the BluetoothDevice corresponding to the clicked item
                    target = mArrayAdapter.get(pos);
                    yourTarget.setText("Target: "+ list.get(pos).toString()+"\nMac: "+mArrayAdapter.get(pos));
                    startBruteforceBtn.setEnabled(true);
                }
            });
        }
    };
}