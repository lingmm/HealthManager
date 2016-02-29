package com.shyling.healthmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.shyling.healthmanager.dao.CheckUpDAO;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Utils;

public class CloudSyncService extends Service {
    public CloudSyncService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CheckUp[] unsent = CheckUpDAO.getInstance().getAllUnsent();
        if (unsent != null && unsent.length > 0) {
            Gson gson = new Gson();
            Utils.Log(gson.toJson(unsent));
        }
        return START_NOT_STICKY;
    }
}
