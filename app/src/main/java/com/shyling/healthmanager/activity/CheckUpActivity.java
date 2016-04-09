package com.shyling.healthmanager.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.CheckUpAsyncTask;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Utils;

import java.lang.reflect.Method;

import pl.droidsonroids.gif.GifImageView;

/**
 * 体检ing Activity
 * Created by shy on 2015/11/8.
 */

public class CheckUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_ENABLE_BLUETOOTH = 130;
    GifImageView gifImageView;
    TextView textBtn, result;
    private AlertDialog exitDialog;
    public BluetoothAdapter bluetoothAdapter;
    private boolean savedBluetoothState = false, founded = false;
    public boolean isChecking = false;
    private CheckUpAsyncTask checkUpAsyncTask;

    //2个HENU?不考虑
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
                founded = false;
                sendToResult("开始搜索设备");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                if (founded) {
                    sendToResult("搜索完成");
                } else {
                    sendToResult("未搜索到设备");
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Const.DEVICENAME.equals(device.getName())) {
                    founded = true;
                    int state = device.getBondState();
                    if (state == BluetoothDevice.BOND_NONE) {
                        Utils.Toast("配对密码为:0000");
                        if (Build.VERSION.SDK_INT >= 19) {
                            device.createBond();
                        } else {
                            //API Level < 19使用反射调用
                            try {
                                Method createBond = BluetoothDevice.class.getMethod("createBond");
                                createBond.setAccessible(true);
                                createBond.invoke(device);
                            } catch (Exception e) {
                                //没方法就算了
                            }
                        }
                    } else if (state == BluetoothDevice.BOND_BONDED) {
                        connect(device);
                    }
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Const.DEVICENAME.equals(device.getName())) {
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDED:
                            connect(device);
                            break;
                        case BluetoothDevice.BOND_NONE:
                            sendToResult("配对失败");
                            break;
                    }
                }
            }
        }
    };

    private void connect(BluetoothDevice device) {
        isChecking = true;
        checkUpAsyncTask = new CheckUpAsyncTask(this);
        checkUpAsyncTask.execute(device);
    }

    public void onCheckUpFinished(CheckUp checkUp){
        CheckUpDAO checkUpDAO = CheckUpDAO.getInstance();
        checkUpDAO.add(checkUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        findViews();
        bindListener();

        IntentFilter catchBluetoothFilter = new IntentFilter();
        catchBluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        catchBluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        catchBluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        catchBluetoothFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //其他原因引起的搜索设备导致的开始体检暂时不管
        registerReceiver(broadcastReceiver, catchBluetoothFilter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Utils.Toast(R.string.bluetooth_unavailable);
            finish();
            return;
        }

        savedBluetoothState = bluetoothAdapter.isEnabled();

        //only start at onCreate
        checkBluetooth();
    }

    private void bindListener() {
        gifImageView.setOnClickListener(this);
        textBtn.setOnClickListener(this);
    }

    private void checkBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Utils.Toast(R.string.ask_enable_bluetooth);
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
        } else {
            onActivityResult(REQUEST_ENABLE_BLUETOOTH, RESULT_OK, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            if (data != null) {
                sendToResult(R.string.enable_bluetooth);
            }
            bluetoothAdapter.startDiscovery();
        } else if (resultCode == RESULT_CANCELED) {
            sendToResult(R.string.enable_bluetooth_failed);
        }
    }

    @Override
    public void onClick(View v) {
    }

    /*
    Send to TextView,whether threads.
     */
    public void sendToResult(final String str) {
        if (getMainLooper() == Looper.myLooper()) {
            result.append(str + "\n");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.append(str + "\n");
                }
            });
        }
    }

    /*
    @see #sendToResult(String)
     */
    public void sendToResult(int strid) {
        sendToResult(getString(strid));
    }


    private void findViews() {
        gifImageView = (GifImageView) findViewById(R.id.gifview);
        gifImageView.setImageResource(R.drawable.checking);
        textBtn = (TextView) findViewById(R.id.textBtn);
        result = (TextView) findViewById(R.id.result);

        exitDialog = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.askexittest)).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkUpAsyncTask != null && checkUpAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            checkUpAsyncTask.cancel(true);
        }
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled() && !savedBluetoothState) {
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.disable();
        }
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (isChecking) {
            exitDialog.show();
        } else {
            finish();
        }
    }
}