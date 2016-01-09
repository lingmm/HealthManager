package com.shyling.healthmanager.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;


/**
 * Created by Mars on 2015/11/4.
 */
public class ForgetActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_forget);
        Button btn_passwd = (Button) findViewById(R.id.forget_pwd);
        btn_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String passWd = ((EditText) findViewById(R.id.et_newpwd)).getText().toString();
                    String repassWd = ((EditText) findViewById(R.id.et_renewpwd)).getText().toString();
                    if(TextUtils.isEmpty(passWd)){
                        Utils.Toast("请输入密码");
                    }else if(TextUtils.isEmpty(repassWd)){
                        Utils.Toast("请输入密码");
                    }else if (!passWd.equals(repassWd)) {
                            Toast.makeText(ForgetActivity.this, "两次输入密码不一样!", Toast.LENGTH_LONG).show();
                            return;
                    } else {
                        sharedPreferences = getSharedPreferences("infopasswd", 0);
                        DBHelper dbHelper = new DBHelper(ForgetActivity.this, "tb_userinfo.db", null, 1);
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("passWd", repassWd);
                        database.update("tb_userinfo", values, "cellPhone=?", new String[]{sharedPreferences.getString("_phone", null)});
                        database.close();
                        Utils.Toast("修改密码成功！");
                        Intent intent = new Intent();
                        intent.setClass(ForgetActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }
}
