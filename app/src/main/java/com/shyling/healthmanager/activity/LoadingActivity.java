package com.shyling.healthmanager.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.easemob.chat.EMChat;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.ShortCutSample;
import com.shyling.healthmanager.util.Utils;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        初始化环信sdk
        EMChat.getInstance().init(this);
//       创建快捷方式
        setContentView(R.layout.loading_activity);
        ShortCutSample sample = new ShortCutSample();
        String shortCutName = getString(R.string.app_name);
        if (sample.hasShortCut(this, shortCutName)) {
            sample.deleteShortCut(this, shortCutName);
        } else {
            sample.creatShortCut(this, shortCutName, R.mipmap.icon);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                    builder.setIcon(R.mipmap.icon)         //
                            .setTitle(R.string.app_name)            //
                            .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                                intent.setComponent(component);
                                intent.setAction("android.intent.action.VIEW");
                            }
                            LoadingActivity.this.startActivity(intent);
                            finish();

                        }
                    }).setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoadingActivity.this.finish();
                        }
                    }).show();
                }else {
                    jumpNextPage();
                }

            }
        }, 1000);
    }
    private void jumpNextPage() {
        // 判断之前有没有退出
        boolean userLogout = Utils.getBoolean(this, "is_user_logout",
                false);

        if (!userLogout) {
            // 跳转到登陆
            startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        }
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
