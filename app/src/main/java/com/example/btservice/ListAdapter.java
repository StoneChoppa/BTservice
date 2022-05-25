package com.example.btservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.Set;

public class ListAdapter extends AppCompatActivity {
    Button btnSearch;
    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ArrayList<String> pairedDeviceArrayList;
    ArrayAdapter<String> pairedDeviceAdapter;
    public static BluetoothSocket clientSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);


        listView = findViewById(R.id.lv);

        btnSearch = findViewById(R.id.devices);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionGranted()) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothEnabled()) {
                        findBluetooth();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    boolean bluetoothEnabled() {
        if (bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
            return false;
        }
    }

    private boolean permissionGranted() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH)
                == PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.BLUETOOTH_ADMIN) == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN}, 0);
            return false;
        }
    }


    @SuppressLint("MissingPermission")
    public void findBluetooth() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (((Set<?>) pairedDevice).size() > 0) {
            pairedDeviceArrayList = new ArrayList<>();
            for (BluetoothDevice device : pairedDevice) {

                pairedDeviceArrayList.add(device.getAddress() + "/" + device.getName());
            }
            pairedDeviceAdapter = new ArrayAdapter<String>(this,
                    R.layout.item_device, R.id.item_device_textView, pairedDeviceArrayList);
            Dialog dialog = new Dialog(ListAdapter.this);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    final String itemMAC = listView.getItemAtPosition(i).toString().split("/", 2)[0];

                    new Thread() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {

                            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(itemMAC);
                            try {


                                @SuppressLint("MissingPermission")
                                BluetoothSocket socket = device.createRfcommSocketToServiceRecord
                                        (device.getUuids()[0].getUuid());
                                socket.connect();
                                if (socket.isConnected()) {

                                    Log.e("ok", "ne ok");
                                    clientSocket = socket;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
        }
    }
}
