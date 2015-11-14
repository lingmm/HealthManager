package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.model.User;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mars on 2015/11/3.
 */
public class PersonActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView username;
    TextView name;
    TextView birthday;
    TextView cellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setIcon(mipmap.ic_launcher);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("个人信息");
        }
        setContentView(R.layout.activity_person);
        username = (TextView) findViewById(R.id.tv_person_username);
        name = (TextView) findViewById(R.id.tv_person_name);
        birthday = (TextView) findViewById(R.id.tv_person_birthDay);
        cellphone = (TextView) findViewById(R.id.tv_person_phone);
        SharedPreferences sharedPreferences = getSharedPreferences("info",0);

        DBHelper dbHelper = new DBHelper(this, "tb_userinfo.db", null, 1);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("tb_userinfo", null, "userName=?", new String[]{ sharedPreferences.getString("_username",null)}, null, null, null, null);
        username.setText(cursor.getString(1));
        name.setText(cursor.getString(3));
        birthday .setText(cursor.getString(4));
        cellphone.setText(cursor.getString(5));
    }
}
