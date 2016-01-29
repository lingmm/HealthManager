package com.shyling.healthmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.activity.CheckUpActivity;

public class CheckUpFragment extends Fragment implements View.OnClickListener {
    Button button;

    public CheckUpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_test, container, false);
        button = (Button)view.findViewById(R.id.check);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v==button){
            startActivity(new Intent(this.getContext(), CheckUpActivity.class));
        }
    }
}
