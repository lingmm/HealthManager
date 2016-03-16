package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
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
    private SharedPreferences mPre;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.LOGINSUCCESS:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //boolean isSaveSuccess = Utils.saveUser(LoginActivity.this, userNumber, passWd);
                    Utils.Toast("登陆成功");
                    finish();
                    break;
                case Const.LOGINERROR:
                    Utils.Toast("用户不存在！！！");
                    break;
                case Const.LOGINERROR_:
                    Utils.Toast("密码错误！！！");
                    break;
                case Const.URLERROR:
                    Utils.Toast("地址错误");
                    break;
                case Const.NETERROR:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    Utils.Toast("服务器异常");
                    finish();
                    break;
                default:
                    break;
            }
            boolean isSaveSuccess = Utils.saveUser(LoginActivity.this, userNumber, passWd);
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
        mPre = getSharedPreferences("userInfo",MODE_PRIVATE);
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
            EMChatManager.getInstance().login(userNumber, passWd, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.d("main", "登陆聊天服务器成功！");
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("userNumber",userNumber);
                            params.addBodyParameter("passWd",passWd);
                            HttpUtils http = new HttpUtils();
                            http.send(HttpRequest.HttpMethod.POST, Utils.url + "mLogin_login", params,
                                    new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            mPre.edit().putString("Json",responseInfo.result).commit();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(HttpException e, String s) {
                                            Utils.Toast("登陆失败");
                                        }
                                    });
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.d("main", "登陆聊天服务器失败！");
                }
            });
            //new LoginThread(userNumber, passWd, handler).start();


            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
