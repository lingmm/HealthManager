package com.shyling.healthmanager.activity;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;


/**
 * Created by Mars on 2015/11/3.
 */
public class PersonActivity extends AppCompatActivity implements View.OnClickListener{
    ActionBar actionBar;
    TextView username;
    TextView name;
    TextView birthday;
    EditText cellphone;
    Button modify_news,save_news;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setIcon(mipmap.ic_launcher);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setContentView(R.layout.activity_person);
        username = (TextView) findViewById(R.id.tv_person_username);
        name = (TextView) findViewById(R.id.tv_person_name);
        birthday = (TextView) findViewById(R.id.tv_person_birthDay);
        cellphone = (EditText) findViewById(R.id.et_person_phone);
        cellphone.setEnabled(false);
        modify_news = (Button) findViewById(R.id.modify_news);
        save_news = (Button) findViewById(R.id.save_news);
        save_news.setOnClickListener(this);
        modify_news.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("info",0);
        dbHelper = new DBHelper(this, "tb_userinfo.db", null, 1);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("tb_userinfo", null, "userName=?", new String[]{ sharedPreferences.getString("_username",null)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            username.setText(cursor.getString(1));
            name.setText(cursor.getString(3));
            birthday.setText(cursor.getString(4));
            cellphone.setText(cursor.getString(5));
        }
        else {finish();}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_news:
                cellphone.setEnabled(true);
                Utils.Toast("修改信息");
                break;
            case R.id.save_news:
                cellphone.setEnabled(false);
                String ucellPhone = cellphone.getText().toString().trim();
                ContentValues values = new ContentValues();
                values.put("cellPhone",ucellPhone);
                database.update("tb_userinfo", values, "userName=?", new String[]{sharedPreferences.getString("_username", null)});
                database.close();
                Utils.Toast("保存成功");
        }
    }
}
