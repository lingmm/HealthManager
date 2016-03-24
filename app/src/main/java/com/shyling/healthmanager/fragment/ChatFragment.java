package com.shyling.healthmanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.activity.ChatListActivity;

public class ChatFragment extends Fragment {

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mView.findViewById(R.id.chat_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatListActivity.class));
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            //判断条目是否有缓存
            if(convertView == null){
                //把布局文件填充成一个View对象
                v = View.inflate(getContext(), R.layout.chat_list_item, null);
            }
            else{
                v = convertView;
            }
            TextView tv_name = (TextView) v.findViewById(R.id.name);
            TextView tv_bumber = (TextView) v.findViewById(R.id.number);
            for (int i = 0;i<5;i++){
                tv_name.setText("李医生"+i);
                tv_bumber.setText("1573688"+i+i+i+i);
            }
            return v;
        }
    }
}
