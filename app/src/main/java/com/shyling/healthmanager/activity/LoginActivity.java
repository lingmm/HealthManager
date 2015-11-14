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

/**
 * Created by Mars on 2015/11/3.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    EditText ed_Username;
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
        ed_Username = (EditText) findViewById(R.id.ed_number);
        ed_Password = (EditText) findViewById(R.id.ed_password);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(ed_Username,0);
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
    //	监听onTouchEvent事件，关闭软键盘。
    //	getWindow().getDecorView()的意思是获取window的最前面的view。软键盘是phonewindow的跟view
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    //			关闭软键盘
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), 0);
        return super.onTouchEvent(event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login :
                processLogin();
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
        final String userName = ed_Username.getText().toString();
        final String passWd = ed_Password.getText().toString();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(passWd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else if(userDao.find(userName) == null){
            Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
        }else if(userDao.find(userName).getPassWd().equals(passWd)){
            //登陆成功
            SharedPreferences sharedPreferences = getSharedPreferences("info",0);
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putString("_username",userName);
            editor.commit();
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}
