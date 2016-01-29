package com.shyling.healthmanager.dao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.model.CheckUp;
import com.shyling.healthmanager.util.Utils;

/**
 * Created by shy on 2015/12/13.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private CheckUp[] checkUps;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView height, weight, hbp, lbp, pulse, date;

        ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            height = (TextView) v.findViewById(R.id.height);
            weight = (TextView) v.findViewById(R.id.weight);
            hbp = (TextView) v.findViewById(R.id.sbp);
            lbp = (TextView) v.findViewById(R.id.dbp);
            pulse = (TextView) v.findViewById(R.id.pulse);

        }
    }

    public HistoryListAdapter(CheckUp[] checkUps) {
        this.checkUps = checkUps;
    }

    @Override
    public int getItemCount() {
        return checkUps.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.date.setText(checkUps[position].getCheckUpDate());
        holder.height.setText(String.valueOf(checkUps[position].getHeight()));
        holder.weight.setText(String.valueOf(checkUps[position].getWeight()));
        holder.hbp.setText(String.valueOf(checkUps[position].getSbp()));
        holder.lbp.setText(String.valueOf(checkUps[position].getDbp()));
        holder.pulse.setText(String.valueOf(checkUps[position].getPulse()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Toast("点击: " + position);
            }
        });
    }
}
