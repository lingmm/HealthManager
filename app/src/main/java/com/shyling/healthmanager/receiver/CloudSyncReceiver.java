package com.shyling.healthmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shyling.healthmanager.service.CloudSyncService;
import com.shyling.healthmanager.util.Utils;

public class CloudSyncReceiver extends BroadcastReceiver {
    public CloudSyncReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.Toast("Notice");
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null) {
                Utils.Log("No active network");
            } else {
                if (networkInfo.isConnected()) {
                    context.startService(new Intent(context, CloudSyncService.class));
                } else {
                    Utils.Log("Network not connected");
                }
            }
        }
    }
}
