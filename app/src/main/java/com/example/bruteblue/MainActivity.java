package com.example.bruteblue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Attributes
    private Button scanBtn, startBruteforceBtn;
    private TextView pin;
    private EditText editRange;
    private Bruteforcer bruteforcer;

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
            pin.setText(s.toString());
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
}