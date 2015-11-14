package com.shyling.healthmanager.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.model.TestRecord;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.FindAndConnectionBluetoothDevice;
import com.shyling.healthmanager.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pl.droidsonroids.gif.GifImageView;

import static com.shyling.healthmanager.R.string.check;

/**
 * Created by shy on 2015/11/8.
 */
@TargetApi(16)
public class TestingActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    GifImageView gifImageView;
    TextView textBtn, result;
    private AlertDialog exitDialog;
    private boolean isTesting = false;
    public BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private boolean savedBluetoothState = false;
    private FindAndConnectionBluetoothDevice findAndConnectionBluetoothDevice;
    private Thread testReadThread;
    private TestRecord fetchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(check);
        }
        setContentView(R.layout.activity_testing);
        findViews();
        bindListener();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Utils.Toast(this, R.string.bluetooth_unavailable);
            finish();
            return;
        }
        savedBluetoothState = bluetoothAdapter.isEnabled();
        doInit();
    }

    private void bindListener() {
        gifImageView.setOnClickListener(this);
        textBtn.setOnClickListener(this);
    }

    private void doInit() {
        if (!bluetoothAdapter.isEnabled()) {
            Utils.Toast(this, R.string.ask_enable_bluetooth);
            bluetoothAdapter.enable();
            doInit();
            return;
        }
        sendToResult(R.string.enable_bluetooth);
        super.onStart();
        findAndConnectionBluetoothDevice = new FindAndConnectionBluetoothDevice(this);
        findAndConnectionBluetoothDevice.setFindedListener(new FindAndConnectionBluetoothDevice.FindedListener() {
            @Override
            public void onStart() {
                sendToResult(R.string.start_search);
            }

            @Override
            public void onConnected(BluetoothSocket socket) {
                sendToResult(R.string.start_test);
                doTest(socket);
            }

            @Override
            public void onStop() {
                sendToResult(R.string.stop_search);
            }

            @Override
            public void onFailed() {
                sendToResult(R.string.faild_find);
            }
        });
        findAndConnectionBluetoothDevice.start();
    }

    public void doTest(BluetoothSocket socket) {
        this.socket = socket;
        isTesting = true;
        fetchResult = new TestRecord();
        final InputStream is;
        OutputStream os;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            testReadThread = new Thread() {
                public String doReadLine(InputStream tmpis) throws IOException {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i;
                    while ((i = tmpis.read()) != '$') {
                        baos.write(i);
                    }
                    String ret = new String(baos.toByteArray());
                    baos.close();
                    return ret;
                }

                @Override
                public void run() {
                    boolean fetchedOne = false;//身高...
                    boolean fetchedTwo = false;//血压...
                    while (!fetchedOne && !fetchedTwo) {
                        try {
                            String line = doReadLine(is);
                            if (line.startsWith("W")) {
                                fetchResult.setWeight(Float.parseFloat(line.substring(2, 7)));
                                fetchResult.setHeight(Float.parseFloat(line.substring(9, 12)));
                                fetchedOne = true;
                            }
                            if (line.startsWith("B")) {
                                fetchResult.setHbp(Integer.parseInt(line.substring(1, 4)));
                                fetchResult.setLbp(Integer.parseInt(line.substring(4, 7)));
                                fetchResult.setPulse(Integer.parseInt(line.substring(7, 10)));
                                fetchedTwo = true;
                            }
                        } catch (Exception e) {
                            sendToResult("读取数据出错");
                            break;
                        }
                    }
                    sendToResult(fetchResult.toString());
                    sendToResult("体检完成");
                }
            };

            os.write("11$".getBytes());
            os.flush();
            testReadThread.start();
            textBtn.setText(R.string.abort);
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (isTesting && socket != null && socket.isConnected()) {
            try {
                textBtn.setText(R.string.test_result);
                sendToResult(R.string.abort);
                OutputStream os = socket.getOutputStream();
                os.write("10$".getBytes());
                os.flush();
                if (socket != null && socket.isConnected()) {
                    socket.close();
                }
                if (testReadThread.isAlive()) {
                    testReadThread.interrupt();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Integer integer = (Integer) v.getTag();
            try {
                Toast.makeText(this, Const.jokes[integer], Toast.LENGTH_SHORT).show();
                v.setTag(++integer);
            } catch (Exception e) {
                e.printStackTrace();
                v.setTag(0);
                this.onClick(v);
            }
        }
    }

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

    public void sendToResult(final int strid) {
        if (getMainLooper() == Looper.myLooper()) {
            result.append(getResources().getString(strid) + "\n");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.append(getResources().getString(strid) + "\n");
                }
            });
        }
    }

    private void findViews() {
        gifImageView = (GifImageView) findViewById(R.id.gifview);
        gifImageView.setImageResource(R.drawable.checking);
        textBtn = (TextView) findViewById(R.id.textBtn);
        result = (TextView) findViewById(R.id.result);

        exitDialog = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.askexittest)).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (socket != null && socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        if (bluetoothAdapter != null && !savedBluetoothState) {
            bluetoothAdapter.disable();
        }
    }

    @Override
    public void onBackPressed() {
        if (isTesting) {
            exitDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
