package com.shyling.healthmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.shyling.healthmanager.service.CloudSyncService;
import com.shyling.healthmanager.util.Utils;

public class CloudSyncReceiver extends BroadcastReceiver {
    public CloudSyncReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (Utils.getNetworkAvailable(context)) {
                context.startService(new Intent(context, CloudSyncService.class));
            }
        }
    }
}
