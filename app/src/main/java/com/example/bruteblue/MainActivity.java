package com.example.bruteblue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Attributes
    private Button scanBtn, startBruteforceBtn;
    private TextView pin;
    private EditText editRange;
    private Bruteforcer bruteforcer;

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
        startBruteforceBtn = findViewById(R.id.BruteforceBtn);
        pin = findViewById(R.id.Pin);
        editRange = findViewById(R.id.inputRange);
        editRange.setGravity(Gravity.CENTER_HORIZONTAL);
        editRange.addTextChangedListener(inputTextWatcher);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        startBruteforceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRange.getText().toString().isEmpty()) {
                    bruteforcer.start(0);
                }
                else {
                    bruteforcer.start(Integer.valueOf(pin.getText().toString()));
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
}