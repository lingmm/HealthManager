package com.shyling.healthmanager.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Utils;

/**
 * Created by Mars on 2015/11/3.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText ed_Number, ed_Password;
    private Button btn_register, btn_Login;
    private TextView tv_forget_pwd;
    private String userNumber, passWd;
    private SharedPreferences mPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mPre = getSharedPreferences("userInfo", MODE_PRIVATE);
        String[] userMap = Utils.getUser(LoginActivity.this);
        if (userMap != null) {
            ed_Number.setText(userMap[0]);
            ed_Password.setText(userMap[1]);
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    /**
     * 网络登录
     */
    private ProgressDialog progressdialog;
    private void processLogin() {
        userNumber = ed_Number.getText().toString();
        passWd = ed_Password.getText().toString();
        if (TextUtils.isEmpty(userNumber)) {
            Utils.Toast("请输入账号");
        } else if (TextUtils.isEmpty(passWd)) {
            Utils.Toast("请输入密码");
        } else {
            //输入无误
            progressdialog = ProgressDialog.show(LoginActivity.this, "请等待...", "正在为您登陆...");
            EMChatManager.getInstance().login(userNumber, passWd, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //环信登录
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.d("main", "登陆聊天服务器成功！");
                            //服务器
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("userNumber", userNumber);
                            params.addBodyParameter("passWd", passWd);
                            HttpUtils http = new HttpUtils();
                            http.send(HttpRequest.HttpMethod.POST, Const.path + "mLogin_login", params,
                                    new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            mPre.edit().putString("Json", responseInfo.result).apply();
                                            Utils.setBoolean(LoginActivity.this,"is_user_logout",true);
                                            System.out.println("json :" + responseInfo.result);
                                            Utils.saveUser(LoginActivity.this, userNumber, passWd);
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            progressdialog.dismiss();
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(HttpException e, String s) {
                                           /* startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();*/
                                            progressdialog.dismiss();
                                            Utils.Toast("登陆失败");
                                        }
                                    });
                            /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Utils.saveUser(LoginActivity.this,userNumber,passWd);
                            finish();*/
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.d("main", "登陆聊天服务器失败！");
                    progressdialog.dismiss();
                    //Utils.Toast("登陆失败");
                }
            });
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
