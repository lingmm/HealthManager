package com.shyling.healthmanager.httpthread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.shyling.healthmanager.HealthManagerApplication;
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
 * 注册线程
 * Created by Mars on 2015/12/29.
 */

public class RegisterThread extends Thread {
    private String userNumber;
    private String passWd;
    private Handler handler;
    public RegisterThread(String userNumber,String passWd, Handler handler){
        this.userNumber = userNumber;
        this.passWd = passWd;
        this.handler = handler;
    }
    private void doPost(){
        Message msg = handler.obtainMessage();
        HttpURLConnection conn = null;
        //提交的数据需要经过url编码，英文和数字编码后不变
        try {
            // 调用sdk注册方法
            EMChatManager.getInstance()
                    .createAccountOnServer(userNumber, passWd);
            URL url = new URL(Const.path+"mLogin_register");
//            URL url = new URL("http://192.168.6.35:8080/health/dwz/mLogin_register");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //拼接出要提交的数据的字符串
            String data = "userNumber=" + userNumber+"&passWd="+passWd;
            Log.e("hahaha", data);
            //添加post请求的两行属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");
            //设置打开输出流
            conn.setDoOutput(true);
            //拿到输出流
            OutputStream os = conn.getOutputStream();
            //使用输出流往服务器提交数据
            os.write(data.getBytes());
            Log.e("hh", String.valueOf(conn.getResponseCode()));
            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                String text = Utils.readInputStream(is);
                if ("1".equals(text)) {
                    msg.what = Const.LOGINERROR_;
                }else if ("0".equals(text)){
                    msg.what = Const.LOGINERROR;
                }else {
                    msg.what = Const.LOGINSUCCESS;
                }
            }

        } catch (MalformedURLException e) {
            msg.what = Const.URLERROR;
            e.printStackTrace();
        } catch (IOException e) {
            msg.what = Const.NETERROR;
            e.printStackTrace();
        } catch (EaseMobException e) {
            msg.what = Const.LOGINERROR_;
            e.printStackTrace();
        } finally {
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
