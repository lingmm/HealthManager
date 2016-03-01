package com.shyling.healthmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.ShortCutSample;
import com.shyling.healthmanager.util.Utils;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        初始化环信sdk
//        EMChat.getInstance().init(this);
//       创建快捷方式
        setContentView(R.layout.loading_activity);
        ShortCutSample sample = new ShortCutSample();
        String shortCutName = getString(R.string.app_name);
        if (sample.hasShortCut(this, shortCutName)) {
            sample.deleteShortCut(this, shortCutName);
        } else {
            sample.creatShortCut(this, shortCutName, R.mipmap.ic_launcher);
        }

        final ConnectivityManager mcManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mtManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 检查网络连接
        final NetworkInfo info = mcManager.getActiveNetworkInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingActivity.this.finish();
                if (info == null || !mcManager.getBackgroundDataSetting()){
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    Utils.Toast("没有网络");
                }else {
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 1000);
    }
}
