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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Set;

public class ListAdapter extends AppCompatActivity implements RVadapter.ItemClickListener {
    /*Button btnSearch;
    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ArrayList<String> pairedDeviceArrayList;
    ArrayAdapter<String> pairedDeviceAdapter;
    public static BluetoothSocket clientSocket;*/
    RVadapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headsetes);
        ArrayList<String> names = new ArrayList<>();
        names.add("a");
        names.add("b");
        names.add("c");
        names.add("d");
        names.add("e");
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RVadapter(this, names);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "name: " + adapter.getItem(position) + ", position: " + position, Toast.LENGTH_SHORT).show();
    }
}


/*
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
        });*/
/*    @SuppressLint("MissingPermission")
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
    }*/

