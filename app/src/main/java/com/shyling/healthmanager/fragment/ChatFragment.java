package com.shyling.healthmanager.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.activity.ChatListActivity;
import com.shyling.healthmanager.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ListView list = (ListView) view.findViewById(R.id.chat_list);
        //刷新效果
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.wi_swipeRefesh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        //将医生item中的数据放入
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        for (int i = 0; i < Utils.doctor.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemImage", Utils.dotorImage[i]);//加入图片
            map.put("ItemName", Utils.doctorName[i]);
            map.put("ItemPhone", "联系方式："+Utils.doctor[i]);
            listItem.add(map);
        }
        //将医生列表显示在ListView中
        SimpleAdapter mSimp = new SimpleAdapter(this.getActivity(), listItem, R.layout.chat_list_item,
                new String[]{"ItemImage"
                        , "ItemName", "ItemPhone"},
                new int[]{R.id.iv_image, R.id.name, R.id.number});
        /*ArrayAdapter<String> phone = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, doctor);//显示列表*/
        list.setAdapter(mSimp);
        //设置Item监听的监听事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                mBundle.putString("ItemName", Utils.doctorName[position]);
                mBundle.putString("ItemPhone", Utils.doctor[position]);
                startActivity(new Intent(getContext(),
                        ChatListActivity.class).putExtra("bundleExtra", mBundle));
                //Utils.Toast("你点击了第" + (position + 1) + "行");
            }
        });
        return view;
    }
}
