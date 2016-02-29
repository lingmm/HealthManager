package com.shyling.healthmanager.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.HistoryListAdapter;
import com.shyling.healthmanager.model.CheckUp;

import java.util.Date;
import java.util.Random;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefesh);
        recyclerView = (RecyclerView) v.findViewById(R.id.historylist);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new HistoryListAdapter(getTest()));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(new HistoryListAdapter(getTest()));
        return v;
    }

    public static CheckUp[] getTest() {
        int j = 20;
        CheckUp[] records = new CheckUp[j];
        Random r = new Random();
        for (int i = 0; i < j; i++) {
            CheckUp t = new CheckUp();
            t.setHeight(r.nextInt(200));
            t.setWeight(r.nextInt(100));
            t.setSbp(r.nextInt(200));
            t.setDbp(r.nextInt(100));
            t.setPulse(r.nextInt(100));
            t.setCheckUpDate(new Date().toString());
            records[i] = t;
        }
        return records;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
