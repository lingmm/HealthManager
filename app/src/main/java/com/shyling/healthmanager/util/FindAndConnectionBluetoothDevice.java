package com.shyling.healthmanager.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shyling.healthmanager.activity.TestingActivity;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by shy on 2015/11/6.
 */
public class FindAndConnectionBluetoothDevice extends Thread {
    TestingActivity testingActivity;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver broadcastReceiver;
    FindedListener findedListener;

    public interface FindedListener {
        void onStart();

        void onConnected(BluetoothSocket socket);

        void onStop();
    }

    public FindAndConnectionBluetoothDevice(final Context context) {
        this.testingActivity = (TestingActivity) context;
        bluetoothAdapter = this.testingActivity.bluetoothAdapter;
    }

    public void setFindedListener(FindedListener findedListener) {
        this.findedListener = findedListener;
    }

    @TargetApi(19)
    public void run() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (Const.DEVICENAME.equals(device.getName())) {
                        context.registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context ctx, Intent intent) {
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);

                                if (state != BluetoothDevice.BOND_BONDING) {
                                    context.unregisterReceiver(this);
                                    if (state == BluetoothDevice.BOND_BONDED) {
                                        connect(device);
                                    } else {
                                        testingActivity.sendToResult("配对失败");
                                    }
                                }
                            }
                        }, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
                        testingActivity.sendToResult("尝试配对: " + device.getAddress());
//                        device.setPairingConfirmation(false);
//                        device.setPin("0000".getBytes());
                        device.createBond();
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
                    if (findedListener != null) {
                        findedListener.onStart();
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                    if (findedListener != null) {
                        findedListener.onStop();
                    }
                    context.unregisterReceiver(broadcastReceiver);
                }
            }
        };

        bluetoothAdapter.cancelDiscovery();
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bluetoothDevices) {
            if (Const.DEVICENAME.equals(device.getName())) {
                testingActivity.sendToResult(String.format("使用已配对设备：%s,地址：%s", device.getName(), device.getAddress()));
                testingActivity.sendToResult("尝试连接");
                if (connect(device)) {
                    return;
                }
            }
        }
        IntentFilter catchBluetoothFilter = new IntentFilter();
        catchBluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        catchBluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        catchBluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        testingActivity.registerReceiver(broadcastReceiver, catchBluetoothFilter);
        bluetoothAdapter.startDiscovery();
    }

    public boolean connect(BluetoothDevice device) {
        try {
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            findedListener.onConnected(socket);
            return true;
        } catch (Exception e) {
            testingActivity.sendToResult("连接失败: " + e.toString());
            return false;
        }
    }
}
