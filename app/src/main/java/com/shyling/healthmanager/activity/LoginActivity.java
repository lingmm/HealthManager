package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.UserDao;
import com.shyling.healthmanager.httpthread.LoginThread;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Utils;

import java.util.Map;

/**
 * Created by Mars on 2015/11/3.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText ed_Number, ed_Password;
    private Button btn_register, btn_Login;
    private UserDao userDao;
    private TextView tv_forget_pwd;
    private String userNumber, passWd;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.LOGINSUCCESS:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    boolean isSaveSuccess = Utils.saveUser(LoginActivity.this, userNumber, passWd);
                    Utils.Toast("登陆成功");
                    finish();
                    break;
                case Const.LOGINERROR:
                    Utils.Toast("用户不存在！！！");
                    break;
                case Const.URLERROR:
                    Utils.Toast("地址错误");
                    break;
                case Const.NETERROR:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Utils.Toast("网络异常");
                    finish();
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Map<String, String> userMap = Utils.getUser(LoginActivity.this);
        if (userMap != null) {
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
                manager.showSoftInput(ed_Number, 0);
                manager.showSoftInput(ed_Password, 0);
            }
        }, 1000);
        userDao = new UserDao(LoginActivity.this);
        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(LoginActivity.this);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(LoginActivity.this);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(LoginActivity.this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                processLogin();
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(LoginActivity.this, ValidateActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    /**
     * 本地登陆
     */
    /*private void processLogin() {
        userNumber = ed_Number.getText().toString();
        passWd = ed_Password.getText().toString();
        if (TextUtils.isEmpty(userNumber)) {
            Utils.Toast("请输入账号");
        } else if (TextUtils.isEmpty(passWd)) {
            Utils.Toast("请输入密码");
        } else if (userDao.find(userNumber) == null) {
            Utils.Toast("用户不存在");
        } else if(userDao.find(userNumber).getPassWd().equals(passWd)){
            //保存登录的用户名
            boolean isSaveSuccess = Utils.saveUser(this,userNumber,passWd);
            //Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }else{
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }*/

    /**
     * 网络登录
     */
    private void processLogin() {
        userNumber = ed_Number.getText().toString();
        passWd = ed_Password.getText().toString();
        if (TextUtils.isEmpty(userNumber)) {
            Utils.Toast("请输入账号");
        } else if (TextUtils.isEmpty(passWd)) {
            Utils.Toast("请输入密码");
        } else {
            //输入无误
            new LoginThread(userNumber, passWd, handler).start();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
