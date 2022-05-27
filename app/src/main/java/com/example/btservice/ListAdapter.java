package com.example.btservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListAdapter extends AppCompatActivity /*implements RVadapter.ItemClickListener*/ {
    RVadapter adapter;
    RecyclerView rv;

/*
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
*/


    private ListView lv;
    private static final int ID_RESULT = 1;
    private ArrayAdapter<String> BTArrayAd;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter myBluetoothAdapter;
    private boolean choice_pir = false;
    final String LOG_TAG = "myLogs";
    private Parcelable[] uuidExtra = null;
    private LinkedHashSet<Parcelable> uuidsExtraFindDevices = new LinkedHashSet<>();
    private ProgressDialog mProgressDlg;

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED: {

                    mProgressDlg.show();
                }
                break;

                case BluetoothDevice.ACTION_FOUND: {

                    Log.d(LOG_TAG, "Устройство найдено");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(device);
                    BTArrayAd.add(device.getName() + "\n" + device.getAddress());


                }
                break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: {

                    mProgressDlg.dismiss();
                    Log.d(LOG_TAG, "Поиск закончен");
                    if (!mDeviceList.isEmpty()) {
                        BluetoothDevice device = mDeviceList.remove(0);
                        boolean result = device.fetchUuidsWithSdp();
                    }
                }
                break;

                case BluetoothDevice.ACTION_UUID: {

                    Log.d(LOG_TAG, "Вставка UUID");
                    BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                    Log.d(LOG_TAG, "DeviceExtra address - " + deviceExtra.getAddress());
                    if (uuidExtra != null) {
                        for (Parcelable p : uuidExtra) {
                            Log.d(LOG_TAG, "uuidExtra - " + p);
                        }

                        uuidsExtraFindDevices.add(uuidExtra[uuidExtra.length - 1]);

                    } else {
                        Log.d(LOG_TAG, "uuidExtra is still null");
                    }
                    if (!mDeviceList.isEmpty()) {
                        BluetoothDevice device = mDeviceList.remove(0);
                        boolean result = device.fetchUuidsWithSdp();
                    }
                }
                break;

            }
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                myBluetoothAdapter.cancelDiscovery();
            }

        });

        lv = (ListView) findViewById(R.id.list);
        BTArrayAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        BTArrayAd.setNotifyOnChange(true);
        lv.setAdapter(BTArrayAd);
        BTArrayAd.clear();
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        registerReceiver(bReceiver, filter);

        myBluetoothAdapter.startDiscovery();
        Log.d(LOG_TAG, "Поиск запущен");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                String address = String.valueOf(parent.getAdapter().getItem(position));
                int num = address.indexOf('\n');
                address = address.substring(num + 1);
                String[] data = new String[2];
                data[0] = address;
                Iterator<Parcelable> itr = uuidsExtraFindDevices.iterator();
                int count = 0;
                while (itr.hasNext()) {
                    if (count == position)
                        data[1] = itr.next().toString();
                    count++;
                }

                intent.putExtra("find", data);

                if (choice_pir)
                    setResult(RESULT_CANCELED, intent);
                else
                    setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "pause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(LOG_TAG, "stop");
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
            choice_pir = true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bReceiver);
        Log.d(LOG_TAG, "destroy");
        super.onDestroy();
    }

    /*@Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "name: " + adapter.getItem(position) + ", position: " + position, Toast.LENGTH_SHORT).show();
    }*/

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