package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.httpthread.ValidateThread;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.DBHelper;
import com.shyling.healthmanager.util.Utils;


/**
 * Created by Mars on 2015/11/4.
 */
public class ValidateActivity extends AppCompatActivity{
    private Button forget_next;
    private String vphone;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.LOGINSUCCESS:
                    startActivity(new Intent(ValidateActivity.this, ForgetActivity.class));
                    Utils.Toast("验证成功");
                    break;
                case Const.LOGINERROR:
                    Utils.Toast("手机号有误");
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
        setContentView(R.layout.activity_validate);
        forget_next = (Button) findViewById(R.id.forget_next);
        vphone = ((EditText)findViewById(R.id.et_vphone)).getText().toString();
        forget_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //本地
                /*DBHelper dbHelper = new DBHelper(ValidateActivity.this,"tb_userinfo.db",null,1);
                String vphone = ((EditText)findViewById(R.id.et_vphone)).getText().toString();
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from tb_userinfo where cellPhone like ?", new String[]{"%"
                        + vphone + "%"});
                if(TextUtils.isEmpty(vphone)){
                    Utils.Toast("请输入联系方式");
                }else if (cursor.moveToFirst()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("infopasswd",0);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("_phone", cursor.getString(5));
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(ValidateActivity.this, ForgetActivity.class);
                    startActivity(intent);
                }else {
                    Utils.Toast("输入有误");
                }*/


                //网络

                new ValidateThread(vphone, handler).start();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
}
