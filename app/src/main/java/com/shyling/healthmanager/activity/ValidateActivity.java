package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.shyling.healthmanager.R;


/**
 * Created by Mars on 2015/11/4.
 */
public class ValidateActivity extends AppCompatActivity implements View.OnClickListener{
    Button forget_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_validate);
        forget_next = (Button) findViewById(R.id.forget_next);
        forget_next.setOnClickListener(this);
        String vuser = ((EditText)findViewById(R.id.et_vuser)).getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_next :
                Intent Intent = new Intent();
                Intent.setClass(this,ForgetActivity.class);
                startActivity(Intent);
                break;
        }
    }
}
