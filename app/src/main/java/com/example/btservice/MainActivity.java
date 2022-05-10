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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String adress;
    List<Integer> namebt;
    List<String>list = new ArrayList<String>();
    BluetoothHeadset headset;
    BluetoothSocket socket;
    BluetoothDevice btdev;
    Button start, stop, golist;
    TextView state, name, connect, dev;
    //private final static int REQUEST_ENABLE_BT = 1;
    @SuppressLint({"MissingPermission", "HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dev = findViewById(R.id.exp);
        state = findViewById(R.id.state);
        name = findViewById(R.id.namedevice);
        connect = findViewById(R.id.connect);
        start = findViewById(R.id.btnsearch);
        stop = findViewById(R.id.btnstop);
        golist = findViewById(R.id.lv);
        /*golist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListAdapter.class);
                startActivity(intent);
            }
        });*/
        /*btdev.connectGatt(CONTEXT_IGNORE_SECURITY, int boolean autoConnect, B)
        headset.getConnectionState(btdev);*/

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this, MyService.class));
            }
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
        //adress = bluetoothAdapter.getAddress();
        //namebt = bluetoothAdapter.getName();
        //namebt.add(headset.getConnectionState(btdev));

        if (bluetoothAdapter == null){
            state.setText("BT state: Your device doesn't support Bluetooth");
            connect.setText("Connect: false");
            dev.setText("My Device Adress: null");
        }else {
            //name.setText(1);
            state.setText("BT state: " + bluetoothAdapter.getState());
            connect.setText("Connect: true");
            dev.setText("My Device Adress: " + bluetoothAdapter.getAddress());
                /*
                adress = bluetoothAdapter.getAddress();
            namebt = bluetoothAdapter.getName();
                name.setText(namebt);
                connect.setText(adress);
               dev.setText(btdev.getType());
               name.setText("Device name: " + btdev.getName());
                connect.setText("Connect: " + btdev.getAddress());
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);*/
/*            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            boolean scanning;
            Handler handler = new Handler();
            final long SCAN_PERIOD = 10000;
            LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();
            ScanCallback leScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    leDeviceListAdapter.addDevice(result.getDevice());
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            };
            private void scanLeDevice() {
                if (!scanning) {
                    // Stops scanning after a predefined scan period.
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scanning = false;
                            bluetoothLeScanner.stopScan(leScanCallback);
                        }
                    }, SCAN_PERIOD);

                    scanning = true;
                    bluetoothLeScanner.startScan(leScanCallback);
                } else {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }*/
        }
}
}