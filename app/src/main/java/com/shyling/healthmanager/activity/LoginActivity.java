package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.UserDao;
import com.shyling.healthmanager.util.Utils;

import java.util.Map;

/**
 * Created by Mars on 2015/11/3.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    EditText ed_Number;
    EditText ed_Password;
    Button btn_Login;
    Button btn_register;
    Intent intent;
    UserDao userDao;
    TextView tv_forget_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Map<String,String> userMap = Utils.getUser(this);
        if (userMap!=null){
            ed_Number.setText(userMap.get("_userNumber"));
            ed_Password.setText(userMap.get("_passWd"));
        }
    }

    private void initView() {
        ed_Number = (EditText) findViewById(R.id.ed_number);
        ed_Password = (EditText) findViewById(R.id.ed_password);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(ed_Number,0);
                manager.showSoftInput(ed_Password,0);
            }
        },1000);
        userDao = new UserDao(this);
        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(LoginActivity.this);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(LoginActivity.this);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login :
                processLogin();
                finish();
                break;
            case R.id.tv_forget_pwd :
                intent = new Intent();
                intent.setClass(this, ValidateActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register :
                intent = new Intent();
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void processLogin() {
        final String userNumber = ed_Number.getText().toString();
        final String passWd = ed_Password.getText().toString();
        if(TextUtils.isEmpty(userNumber)){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(passWd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else if(userDao.find(userNumber) == null){
            Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
        }else if(userDao.find(userNumber).getPassWd().equals(passWd)){
            //登陆成功
            //保存登录的用户名
            boolean isSaveSuccess = Utils.saveUser(this,userNumber,passWd);
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}
