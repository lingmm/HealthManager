package com.shyling.healthmanager.dao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.model.TestRecord;

import java.util.Date;

/**
 * Created by shy on 2015/12/13.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private TestRecord[] testRecords;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView height, weight, hbp, lbp, pulse, time;

        ViewHolder(View v) {
            super(v);
            time = (TextView) v.findViewById(R.id.time);
            height = (TextView) v.findViewById(R.id.height);
            weight = (TextView) v.findViewById(R.id.weight);
            hbp = (TextView) v.findViewById(R.id.hbp);
            lbp = (TextView) v.findViewById(R.id.lbp);
            pulse = (TextView) v.findViewById(R.id.pulse);

        }
    }

    public HistoryListAdapter(TestRecord[] testRecords) {
        this.testRecords = testRecords;
    }

    @Override
    public int getItemCount() {
        return testRecords.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.time.setText(new Date(testRecords[position].getTime()).toString());
        holder.height.setText(String.valueOf(testRecords[position].getHeight()));
        holder.weight.setText(String.valueOf(testRecords[position].getWeight()));
        holder.hbp.setText(String.valueOf(testRecords[position].getHbp()));
        holder.lbp.setText(String.valueOf(testRecords[position].getLbp()));
        holder.pulse.setText(String.valueOf(testRecords[position].getPulse()));
    }
}
