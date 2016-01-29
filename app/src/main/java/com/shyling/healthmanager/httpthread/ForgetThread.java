package com.shyling.healthmanager.httpthread;

import android.os.Handler;
import android.os.Message;

import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 验证线程
 * Created by Mars on 2015/12/29.
 */

public class ForgetThread extends Thread {
    private String ed_Password;
    private Handler handler;
    public ForgetThread(String ed_Password, Handler handler){
        this.ed_Password = ed_Password;
        this.handler = handler;
    }
    private void doPost(){
        Message msg = handler.obtainMessage();
        HttpURLConnection conn = null;
        //提交的数据需要经过url编码，英文和数字编码后不变
        try {
            URL url = new URL(Const.path+"web2/RegisterServlet");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //拼接出要提交的数据的字符串
            String data = "passWd=" + URLEncoder.encode(ed_Password);
            //添加post请求的两行属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");

            //设置打开输出流
            conn.setDoOutput(true);
            //拿到输出流
            OutputStream os = conn.getOutputStream();
            //使用输出流往服务器提交数据
            os.write(data.getBytes());
            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                String text = Utils.readInputStream(is);
                if ("SUCESS".equals(text)) {
                    msg.what = Const.LOGINSUCCESS;
                }
            }

        } catch (MalformedURLException e) {
            msg.what = Const.URLERROR;
            e.printStackTrace();
        } catch (IOException e) {
            msg.what = Const.NETERROR;
            e.printStackTrace();
        }finally {
            handler.sendMessage(msg);
            if (conn!=null){
                conn.disconnect();
            }
        }
    }

    @Override
    public void run() {
        doPost();
    }

}
