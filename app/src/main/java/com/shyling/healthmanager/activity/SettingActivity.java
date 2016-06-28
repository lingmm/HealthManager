package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.util.Utils;
import com.shyling.healthmanager.view.SettingItemView;

/**
 * Created by Mars on 2015/11/3.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout tv_logon,tv_person;
    private SettingItemView sivUpdate,sivPush;
    SharedPreferences spf;
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
        spf = getSharedPreferences("info",MODE_PRIVATE);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        //sivUpdate.setTitle("自动更新设置");
        boolean autoUpdate = spf.getBoolean("auto_update",true);
        JudgeCheck(autoUpdate,sivUpdate);
        sivPush = (SettingItemView) findViewById(R.id.siv_push);
        boolean autoPush = spf.getBoolean("auto_push",true);
        JudgeCheck(autoPush, sivPush);
        tv_person = (LinearLayout) findViewById(R.id.tv_person);
        //tv_person.setVisibility(View.GONE);
        tv_person.setOnClickListener(this);
        tv_logon = (LinearLayout) findViewById(R.id.tv_logon);
        tv_logon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_person:
                //个人资料
                startActivity(new Intent(this, PersonActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tv_logon:
                //注销
                //startActivity(new Intent(this, LoginActivity.class));
                Utils.setBoolean(this,"is_user_logout",true);
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                MainActivity.instance.finish();
                finish();
                break;
            case R.id.siv_update:
                if (sivUpdate.isChecked()){
                    sivUpdate.setCheck(false);
                    //sivUpdate.setDesc("自动更新已经关闭");
                    //更新spf
                    spf.edit().putBoolean("auto_update",false).commit();
                }else {
                    sivUpdate.setCheck(true);
                    //sivUpdate.setDesc("自动更新已经开启");
                    spf.edit().putBoolean("auto_update",true).commit();
                }
                break;
            case R.id.siv_push:
                if (sivPush.isChecked()){
                    sivPush.setCheck(false);
                    //sivUpdate.setDesc("自动更新已经关闭");
                    //更新spf
                    spf.edit().putBoolean("auto_push",false).commit();
                }else {
                    sivPush.setCheck(true);
                    //sivUpdate.setDesc("自动更新已经开启");
                    spf.edit().putBoolean("auto_push",true).commit();
                }
                break;

        }
    }
    private void JudgeCheck(boolean auto,SettingItemView settingItemView){
        settingItemView.setOnClickListener(this);
        if (auto) {
            //sivUpdate.setDesc("自动更新已经开启");
            settingItemView.setCheck(true);
        }else {
            //sivUpdate.setDesc("自动更新已经关闭");
            settingItemView.setCheck(false);
        }
    }
}
