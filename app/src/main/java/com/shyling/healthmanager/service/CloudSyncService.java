package com.shyling.healthmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.shyling.healthmanager.HealthManagerApplication;
import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Const;
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
        username = hashMap.get("_userNumber");
        password = hashMap.get("_passWd");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CheckUp[] unsent = CheckUpDAO.getInstance().getAllUnsent();
        if (unsent != null && unsent.length > 0) {
            Gson gson = new Gson();
            String json = gson.toJson(unsent);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Const.path).header("username", username).header("password", password).post(RequestBody.create(MediaType.parse("application/json"), json)).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Utils.Toast("上传失败" + e.getMessage());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    CheckUpDAO.getInstance().setAllSent();
                    Utils.Toast("上传成功");
                }
            });
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }
}
