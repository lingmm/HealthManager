package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.httpthread.RegisterThread;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;


/**
 * Created by Mars on 2015/11/4.
 */
public class RegisterActivity extends AppCompatActivity {
    Button btn_post;
    DBHelper dbHelper;
    private EditText et_number,et_passWd,et_repassWd;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.LOGINSUCCESS:
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                    Utils.Toast("注册成功");
                    break;
                case Const.LOGINERROR:
                    Utils.Toast("用户已存在");
                    break;
                case Const.URLERROR:
                    Utils.Toast("地址错误");
                    break;
                case Const.NETERROR:
                    Utils.Toast("网络异常");
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/
        setContentView(R.layout.activity_register);
        initUI();


    }

    private void initUI() {
        et_number = (EditText) findViewById(R.id.et_number);
        et_passWd = (EditText) findViewById(R.id.et_pwd);
        et_repassWd = (EditText) findViewById(R.id.et_repwd);
        dbHelper = new DBHelper(this,"tb_userinfo.db",null,1);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNumber = et_number.getText().toString();
                String passWd = et_passWd.getText().toString();
                String repassWd = et_repassWd.getText().toString();
                if (TextUtils.isEmpty(userNumber)) {
                    Utils.Toast("账号不能为空");
                } else if (TextUtils.isEmpty(passWd)) {
                    Utils.Toast("请输入密码");
                } else if (!passWd.equals(repassWd)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一样!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //本地注册
                    /*DBHelper.addUser(dbHelper.getReadableDatabase(), null, userNumber, passWd, null, null, userNumber);
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    boolean isSaveSuccess = Utils.saveUser(RegisterActivity.this, userNumber, passWd);
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);*/

                    //网络注册
                    new RegisterThread(userNumber, passWd, handler).start();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }


        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        /*if(dbHelper != null){
            dbHelper.close();
        }*/
    }
}
