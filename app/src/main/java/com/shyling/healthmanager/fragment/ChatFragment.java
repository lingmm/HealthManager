package com.shyling.healthmanager.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shyling.healthmanager.R;
import com.shyling.healthmanager.activity.ChatListActivity;
import com.shyling.healthmanager.adapter.ChatListAdapter;
import com.shyling.healthmanager.model.DocInfo;
import com.shyling.healthmanager.model.UserInfo;
import com.shyling.healthmanager.util.Const;
import com.shyling.healthmanager.util.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<DocInfo> mDoctor;
    private MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ListView list = (ListView) view.findViewById(R.id.chat_list);
        //刷新效果
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.wi_swipeRefesh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
        //将医生item中的数据放入
        mDoctor = new ArrayList<>();
        getDoctorList();
        myAdapter = new MyAdapter();
        //将医生列表显示在ListView中
        list.setAdapter(myAdapter);
        return view;
    }

    private void getDoctorList() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Const.path+"mLogin_m_getDocList", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                byte[] b = responseInfo.result.getBytes();
                Log.e("获取成功 : 数据result--", responseInfo.result);
                try {
                    String json = new String(b, "UTF-8");
                    Log.e("获取成功 : 数据--", json);
                    Gson gson = new Gson();
                    mDoctor = gson.fromJson(json, new TypeToken<List<DocInfo>>() {
                    }.getType());
                    if (mDoctor != null) {
                    } else {
                        getDoctorList();
                        Log.e("TEST 空了:", mDoctor + "为空，刷新吧");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDoctorList();
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDoctor.size();
        }

        @Override
        public Object getItem(int position) {
            return mDoctor.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.chat_list_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
            TextView docName = (TextView) view.findViewById(R.id.name);
            TextView docNumber = (TextView) view.findViewById(R.id.number);
            BitmapUtils bitmapUtils = new BitmapUtils(getContext());
            // 加载网络图片
            bitmapUtils.display(imageView, "http://bbs.lidroid.com/static/image/common/logo.png");
            docName.setText(mDoctor.get(position).getDocName());
            docNumber.setText(mDoctor.get(position).getContact());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ItemName", mDoctor.get(position).getDocName());
                    mBundle.putString("ItemPhone", mDoctor.get(position).getContact());
                    startActivity(new Intent(getContext(),
                            ChatListActivity.class).putExtra("bundleExtra", mBundle));
                }
            });
            return view;
        }
    }
}
