package com.shyling.healthmanager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.util.DBHelper;

import java.util.Calendar;


/**
 * Created by Mars on 2015/11/4.
 */
public class RegisterActivity extends AppCompatActivity {
    Button btn_post;
    DBHelper dbHelper;
    private EditText et_userName;
    private EditText et_passWd;
    private EditText et_repassWd;
    private EditText et_uName;
    private EditText et_birthDay;
    private EditText et_cellPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_register);
        initUI();


    }

    private void initUI() {
        et_userName = (EditText) findViewById(R.id.et_user);
        et_passWd = (EditText) findViewById(R.id.et_pwd);
        et_repassWd = (EditText) findViewById(R.id.et_repwd);
        et_uName = (EditText) findViewById(R.id.et_name);
        et_birthDay = (EditText) findViewById(R.id.et_date);
        et_birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(RegisterActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                EditText show = (EditText) findViewById(R.id.et_date);
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
        et_cellPhone = (EditText) findViewById(R.id.et_phone);
        dbHelper = new DBHelper(this,"tb_userinfo.db",null,1);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_userName.getText().toString();
                String passWd = et_passWd.getText().toString();
                String repassWd = et_repassWd.getText().toString();
                if (!passWd.equals(repassWd)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一样!", Toast.LENGTH_LONG).show();
                    return;
                }
                String uName = et_uName.getText().toString();
                String birthDay = et_birthDay.getText().toString();
                String cellPhone = et_cellPhone.getText().toString();
                addUser(dbHelper.getReadableDatabase(), userName, passWd, uName, birthDay, cellPhone);
                Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }


        });
    }
        private void addUser(SQLiteDatabase db, String userName, String passWd, String uName, String birthDay, String cellPhone) {
            //添加用户
            String[] user =new String[]{userName, passWd, uName, birthDay, cellPhone};
            db.execSQL("insert into tb_userinfo values(null,?,?,?,?,?)", user );
        }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(dbHelper != null){
            dbHelper.close();
        }
    }
    class PostClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {finish();}
    }
}
