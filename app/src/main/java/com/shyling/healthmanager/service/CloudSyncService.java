package com.shyling.healthmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Option;
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

    public CloudSyncService() {
        Map<String, String> hashMap = Utils.getUser(HealthManagerApplication.healthManagerApplication);
        username = Option.of(hashMap.get("_userNumber")).getOrElse("undefined_username");
        password = Option.of(hashMap.get("_passWd")).getOrElse("undefined_password");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CheckUp[] unsent = Option.of(CheckUpDAO.getInstance().getAllUnsent()).getOrElse(new CheckUp[]{});
        String json = JSON.toJSONString(unsent);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Const.path + "data_add").header("username", username).header("password", password).post(RequestBody.create(MediaType.parse("application/json"), json)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utils.Toast("体检信息同步失败" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                CheckUpDAO.getInstance().setAllSent();
                Utils.Toast("体检信息同步完成");
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }
}
