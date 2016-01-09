package com.shyling.healthmanager.activity;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.model.PersonData;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;

import java.util.Calendar;
import java.util.Map;


/**
 * Created by Mars on 2015/11/3.
 */
public class PersonActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    EditText username, number, name, birthday, cellphone;
    Button modify_news, save_news;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Map<String, String> userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setContentView(R.layout.activity_person);
        username = (EditText) findViewById(R.id.tv_person_username);
        username.setEnabled(false);
        number = (EditText) findViewById(R.id.tv_person_number);
        number.setEnabled(false);
        name = (EditText) findViewById(R.id.tv_person_name);
        name.setEnabled(false);
        birthday = (EditText) findViewById(R.id.tv_person_birthDay);
        birthday.setEnabled(false);
        cellphone = (EditText) findViewById(R.id.et_person_phone);
        cellphone.setEnabled(false);
        getDataPerson();//从json中获取数据并设置
        modify_news = (Button) findViewById(R.id.modify_news);
        save_news = (Button) findViewById(R.id.save_news);
        save_news.setOnClickListener(this);
        modify_news.setOnClickListener(this);
        userMap = Utils.getUser(this);
        /*dbHelper = new DBHelper(this, "tb_userinfo.db", null, 1);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("tb_userinfo", null, "userNumber=?", new String[]{ userMap.get("_userNumber")}, null, null, null, null);
        if (cursor.moveToFirst()) {
            username.setText(cursor.getString(1));
            number.setText(cursor.getString(2));
            name.setText(cursor.getString(4));
            birthday.setText(cursor.getString(5));
            cellphone.setText(cursor.getString(6));
        }
        else {finish();}*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_news:
                username.setEnabled(true);
                username.setTextColor(0xFF686666);
                name.setEnabled(true);
                name.setTextColor(0xFF686666);
                birthday.setEnabled(true);
                birthday.setTextColor(0xFF686666);
                birthday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View source) {
                        Calendar c = Calendar.getInstance();
                        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                        new DatePickerDialog(PersonActivity.this,
                                // 绑定监听器
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker dp, int year,
                                                          int month, int dayOfMonth) {
                                        EditText show = (EditText) findViewById(R.id.tv_person_birthDay);
                                        show.setText(year + "年" + (month + 1)
                                                + "月" + dayOfMonth + "日");
                                    }
                                }
                                //设置初始日期
                                , c.get(Calendar.YEAR)
                                , c.get(Calendar.MONTH)
                                , c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                Utils.Toast("修改信息");
                break;
            case R.id.save_news:
                username.setEnabled(false);
                name.setEnabled(false);
                birthday.setEnabled(false);
                perfectInfo();

        }
    }


    private void perfectInfo() {

        String userName = username.getText().toString().trim();
        String uName = name.getText().toString().trim();
        String birthDay = birthday.getText().toString().trim();
        pushDataPerson(userName, uName, birthDay);
        /*ContentValues values = new ContentValues();
        values.put("userName",userName);
        values.put("uName",uName);
        values.put("birthDay", birthDay);
        database.update("tb_userinfo", values, "userNumber=?", new String[]{userMap.get("_userNumber")});
        Utils.Toast("保存成功");*/
    }

    /**
     * 得到数据
     */
    private void getDataPerson() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Const.path +
                "Person.json", new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.Toast(msg);
                error.printStackTrace();
            }

            @Override
            public void onSuccess(ResponseInfo<String> info) {
                String result = info.result;
                System.out.println("result" + result);
                parserData(result);
            }
        });
    }

    /**
     * 上传数据给服务器
     * @param userName 昵称
     * @param uName 姓名
     * @param birthDay  生日
     */
    private void pushDataPerson(String userName, String uName, String birthDay) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("uName", uName);
        params.addBodyParameter("birthDay", birthDay);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Const.path+"web2/LoginServlet", new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.Toast(msg);
                error.printStackTrace();

            }

            @Override
            public void onSuccess(ResponseInfo<String> info) {
                String result = info.result;
                System.out.println("result" + result);
                Utils.Toast(result);
            }
        });
    }

    /**
     * 解析数据
     *
     * @param result 数据集合
     */
    protected void parserData(String result) {
        Gson gson = new Gson();
        PersonData data = gson.fromJson(result, PersonData.class);
        if (data != null) {
            System.out.println("解析结果....." + data);
            username.setText(data.userName);
            number.setText(data.userNumber);
            name.setText(data.uName);
            birthday.setText(data.birthDay);
            cellphone.setText(data.cellPhone);
        } else {
            Utils.Toast("解析失败！！！！");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //database.close();
    }
}
