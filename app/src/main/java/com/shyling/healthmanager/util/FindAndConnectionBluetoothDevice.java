package com.shyling.healthmanager.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.shyling.healthmanager.activity.TestingActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by shy on 2015/11/6.
 */
public class FindAndConnectionBluetoothDevice extends Thread{
    TestingActivity context;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver broadcastReceiver;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    FindedListener findedListener;
    private boolean success = false;

    public interface FindedListener{
        void onStart();
        void onConnected(BluetoothSocket socket);
        void onStop();
        void onFailed();
    }

    @TargetApi(19)
    public FindAndConnectionBluetoothDevice(final Context context){
        this.context = (TestingActivity)context;
        bluetoothAdapter = this.context.bluetoothAdapter;
        bluetoothDevices = new ArrayList<>();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(Const.DEVICENAME.equals(device.getName())){
                        device.setPin("0000".getBytes());
                        device.createBond();
                        connect(device);
                    }
                }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())){
                    if(findedListener!=null){
                        findedListener.onStart();
                    }
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                    if(findedListener!=null){
                        findedListener.onStop();
                        if(!success){
                            findedListener.onFailed();
                        }
                    }
                    context.unregisterReceiver(broadcastReceiver);
                }
            }
        };
    }

    public void setFindedListener(FindedListener findedListener){
        this.findedListener = findedListener;
    }

    public void run() {
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        Iterator<BluetoothDevice> iterator = bluetoothDevices.iterator();
        while(iterator.hasNext()){
            BluetoothDevice device = iterator.next();
            if(Const.DEVICENAME.equals(device.getName())){
                context.sendToResult(String.format("使用已配对设备：%s,地址：%s",device.getName(),device.getAddress()));
                context.sendToResult("尝试连接");
                if(connect(device)){
                    return;
                }
            }
        }
        context.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        context.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        context.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        bluetoothAdapter.startDiscovery();
    }

    public boolean connect(BluetoothDevice device){
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            findedListener.onConnected(socket);
            success = true;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            context.sendToResult("连接失败");
            return false;
        }
    }
}
