package com.shyling.healthmanager.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.shyling.healthmanager.activity.TestingActivity;
import com.shyling.healthmanager.model.TestRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 异步体检
 * Created by shy on 2015/12/20.
 */
public class TestAsyncTask extends AsyncTask<BluetoothDevice, String, TestRecord> implements Runnable {
    BluetoothDevice device;
    TestingActivity context;
    InputStream is;
    OutputStream os;
    BluetoothSocket socket;
    TestRecord record;

    public TestAsyncTask(TestingActivity context) {
        this.context = context;
        record = new TestRecord();
    }

    @Override
    protected void onPostExecute(TestRecord testRecord) {
        super.onPostExecute(testRecord);
        context.isTesting = false;
        if (testRecord != null) {
            context.sendToResult(testRecord.toString());
        } else {
            context.sendToResult("数据读取出错");
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Utils.Toast("取消");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.Toast("开始");
    }

    @Override
    protected TestRecord doInBackground(BluetoothDevice... params) {
        if (params[0] != null) {
            device = params[0];
        }
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            context.bluetoothAdapter.cancelDiscovery();
            socket.connect();
            is = socket.getInputStream();
            os = socket.getOutputStream();
            os.write("11$".getBytes());
            os.flush();
            Thread thread = new Thread(this);
            thread.start();
            thread.join();
            return record;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            publishProgress(e.toString());
            return null;
        }
    }

    private String readLine() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != '$') {
            baos.write(i);
        }
        return baos.toString();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values[0] != null) {
            Utils.Toast(values[0]);
        }
    }

    @Override
    public void run() {
        try {
            boolean fetchOne = false, fetchTwo = false;
            while (true) {
                if (isCancelled()) {
                    return;
                }
                if (fetchOne && fetchTwo) {
                    break;
                } else {
                    String line = readLine();
                    if (line.startsWith("W")) {
                        record.setWeight(Float.parseFloat(line.substring(2, 7)));
                        record.setHeight(Float.parseFloat(line.substring(10, 15)));
                        fetchOne = true;
                    }
                    if (line.startsWith("B")) {
                        record.setHbp(Integer.parseInt(line.substring(1, 4)));
                        record.setLbp(Integer.parseInt(line.substring(4, 7)));
                        record.setPulse(Integer.parseInt(line.substring(7, 10)));
                        fetchTwo = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(e.toString());
        }
    }

    @Override
    protected void onCancelled(TestRecord testRecord) {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (socket != null && socket.isConnected()) {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
