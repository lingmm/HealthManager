package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.util.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mars on 2015/11/3.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout tv_person;
    LinearLayout tv_logon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_setting);
        tv_person = (LinearLayout) findViewById(R.id.tv_person);
        tv_person.setOnClickListener(this);
        tv_logon = (LinearLayout) findViewById(R.id.tv_logon);
        tv_logon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_person:
                //个人资料
                intent = new Intent();
                intent.setClass(this, PersonActivity.class);//指定类名
                startActivity(intent);
                break;
            case R.id.tv_logon:
                //登录跳转
                finish();
                intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);

        }
    }
}
