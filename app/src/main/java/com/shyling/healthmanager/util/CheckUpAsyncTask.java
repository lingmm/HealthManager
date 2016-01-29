package com.shyling.healthmanager.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.shyling.healthmanager.activity.CheckUpActivity;
import com.shyling.healthmanager.model.CheckUp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 异步体检
 * Created by shy on 2015/12/20.
 */
public class CheckUpAsyncTask extends AsyncTask<BluetoothDevice, String, CheckUp> implements Runnable {
    BluetoothDevice device;
    CheckUpActivity context;
    InputStream is;
    OutputStream os;
    BluetoothSocket socket;
    CheckUp record;
    Pattern patternOne,patternTwo;

    public CheckUpAsyncTask(CheckUpActivity context) {
        this.context = context;
        record = new CheckUp();
        patternOne = Pattern.compile("W:([\\d\\.]{5}) H:([\\d\\.]{5})");
        patternTwo = Pattern.compile("B(\\d{3})(\\d{3})(\\d{3})");
    }

    @Override
    protected void onPostExecute(CheckUp checkUp) {
        super.onPostExecute(checkUp);
        context.isChecking = false;
        if (checkUp != null) {
            context.sendToResult(checkUp.toString());
        } else {
            context.sendToResult("数据读取出错");
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            os.write("10$".getBytes());
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
            Utils.Log(ioe);
        }
        Utils.Toast("取消体检");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.Toast("请根据语音提示开始体检");
    }

    @Override
    protected CheckUp doInBackground(BluetoothDevice... params) {
        if (params[0] != null) {
            device = params[0];
        }
        try {
            context.bluetoothAdapter.cancelDiscovery();
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
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
            Utils.Log(e);
            return null;
        } finally {
            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                //didn't matter,pass
            }
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
//            Utils.Toast(values[0]);
            context.sendToResult(values[0]);
        }
    }

    @Override
    public void run() {
        try {
            boolean fetchOne = false, fetchTwo = false;
            while (!fetchOne  && !fetchTwo ) {
                if (isCancelled()) {
                    return;
                }
                String line = readLine();
                //match result by regex
                Matcher matcherOne = patternOne.matcher(line);
                Matcher matcherTwo = patternTwo.matcher(line);
                if(matcherOne.find()){
                    record.setWeight(Float.parseFloat(matcherOne.group(1)));
                    record.setHeight(Float.parseFloat(matcherOne.group(2)));
                    fetchOne = true;
                }
                if(matcherTwo.find()){
                    record.setSbp(Integer.parseInt(matcherTwo.group(1)));
                    record.setDbp(Integer.parseInt(matcherTwo.group(2)));
                    record.setPulse(Integer.parseInt(matcherTwo.group(3)));
                    fetchTwo = true;
                }
            }
        } catch (IOException e) {
            Utils.Log(e);
            publishProgress(e.toString());
        }
    }
}
