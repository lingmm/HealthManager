package com.shyling.healthmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Optional;
import com.shyling.healthmanager.util.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class CloudSyncService extends Service {
    String username;
    String password;
    static Handler helperForToast = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Utils.Toast("体检信息同步完成");
            }
            if (msg.what == 1) {
                Utils.Toast("体检信息同步失败");
            }
        }
    };

    public CloudSyncService() {
        Map<String, String> hashMap = Utils.getUser(HealthManagerApplication.healthManagerApplication);
        username = Optional.of(hashMap.get("_userNumber")).getOrElse("undefined_username");
        password = Optional.of(hashMap.get("_passWd")).getOrElse("undefined_password");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CheckUp[] unsent = Optional.of(CheckUpDAO.getInstance().getAllUnsent()).getOrElse(new CheckUp[]{});
        String json = JSON.toJSONString(unsent);
        Utils.Log(json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Const.path + "data_add").header("username", username).header("password", password).post(RequestBody.create(MediaType.parse("application/json"), json)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utils.Log(e);
                Message.obtain(helperForToast, 1).sendToTarget();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                CheckUpDAO.getInstance().setAllSent();
                Message.obtain(helperForToast, 0).sendToTarget();
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }
}
