package com.example.btservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button start, stop, golist;
    ImageButton info;
    TextView state;
    //private final static int REQUEST_ENABLE_BT = 1;
    @SuppressLint({"MissingPermission", "HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        info = findViewById(R.id.imageButton);
        state = findViewById(R.id.state);
        start = findViewById(R.id.btnsearch);
        stop = findViewById(R.id.btnstop);
        golist = findViewById(R.id.lv);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this, MyService.class));
            }
        });
        info.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Information.class));
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });
        golist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListAdapter.class));
            }
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            state.setText("BT state: Your device doesn't support Bluetooth");
        } else {
            state.setText("BT version: " + bluetoothAdapter.getState());
        }
    }
}