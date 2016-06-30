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
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.easemob.chat.EMChat;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.ShortCutSample;
import com.shyling.healthmanager.util.Utils;

public class LoadingActivity extends AppCompatActivity {
    ImageView rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        初始化环信sdk
        EMChat.getInstance().init(this);
//       创建快捷方式
        setContentView(R.layout.loading_activity);
        rlRoot = (ImageView) findViewById(R.id.iv_splash);
        ShortCutSample sample = new ShortCutSample();
        String shortCutName = getString(R.string.app_name);
        if (sample.hasShortCut(this, shortCutName)) {
            sample.deleteShortCut(this, shortCutName);
        } else {
            sample.creatShortCut(this, shortCutName, R.mipmap.icon);
        }

        // 检查网络连接
        boolean networkAvailable = Utils.isNetworkAvailable(LoadingActivity.this);
        if (networkAvailable) {
            splashNext();
        } else {
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
        }
    }
    private void splashNext() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlRoot.setAnimation(alphaAnimation);
    }

    private void jumpNextPage() {
        // 判断之前有没有退出
        boolean userLogout = Utils.getBoolean(this, "is_user_logout", false);

        if (!userLogout) {
            // 跳转到登陆
            Log.e("未等路过","-------------");
            startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
        } else {
            Log.e("等路过","***********");
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        }
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
