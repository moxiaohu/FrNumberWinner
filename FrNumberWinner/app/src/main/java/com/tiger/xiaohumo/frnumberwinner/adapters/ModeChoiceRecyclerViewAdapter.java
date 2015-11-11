package com.tiger.xiaohumo.frnumberwinner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiger.xiaohumo.frnumberwinner.FrWinnerApplication;
import com.tiger.xiaohumo.frnumberwinner.MainActivity;
import com.tiger.xiaohumo.frnumberwinner.SubModeActivity;
import com.tiger.xiaohumo.frnumberwinner.NumberPlayFragment;
import com.tiger.xiaohumo.frnumberwinner.R;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiaohumo on 28/10/15.
 */
public class ModeChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ModeChoiceRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private ArrayList<ConfigSingleItemObject> list;
    private Activity context;

    public ModeChoiceRecyclerViewAdapter(Activity context, ArrayList<ConfigSingleItemObject> list) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.choice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, final int position) {

        final ConfigSingleItemObject item = list.get(position);
        holder.item.setText(item.getTime() + " 秒" + "\n范围从" + item.getMin() + " 到" + item.getMax());
        holder.choiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NumberPlayFragment playFragment = new NumberPlayFragment();
                FrWinnerApplication.number_mode_max = item.getMax();
                FrWinnerApplication.number_mode_min = item.getMin();
                FrWinnerApplication.totalTime = item.getTime();
                        ((MainActivity) context).setFragment(playFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.choice_id)
        TextView item;

        @Bind(R.id.choice_layout)
        CardView choiceLayout;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
