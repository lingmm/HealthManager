package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.easemob.chat.EMChat;
import com.shyling.healthmanager.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        初始化环信sdk
//        EMChat.getInstance().init(this);

        setContentView(R.layout.loading_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingActivity.this.finish();
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        },1000);
    }
}
