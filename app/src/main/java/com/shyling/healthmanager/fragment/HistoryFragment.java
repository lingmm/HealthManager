package com.shyling.healthmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.dao.HistoryListAdapter;
import com.shyling.healthmanager.model.TestRecord;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) v.findViewById(R.id.historylist);
        TestRecord[] testRecords = new TestRecord[]{
                new TestRecord(), new TestRecord(), new TestRecord()
        };
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(new HistoryListAdapter(testRecords));
        return v;
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
