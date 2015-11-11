package com.tiger.xiaohumo.frnumberwinner.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tiger.xiaohumo.frnumberwinner.FrWinnerApplication;
import com.tiger.xiaohumo.frnumberwinner.R;
import com.tiger.xiaohumo.frnumberwinner.interfaces.ChoiceChoosenListener;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;
import com.tiger.xiaohumo.frnumberwinner.util.NumberGenerator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiaohumo on 15/7/11.
 */
public class NumberChoicesRecyclerViewAdapter extends RecyclerView.Adapter<NumberChoicesRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private ArrayList<String> list;
    private Context context;
    private int rightIndex = 0;
    private int totalIndex = 0;
    private ChoiceChoosenListener choosenListener;
    private int swapIndex;
    private ConfigSingleItemObject mode;

    public NumberChoicesRecyclerViewAdapter(Context context, ArrayList<String> list, ConfigSingleItemObject mode) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.mode = mode;

        // swap list(0) with random value list(swapIndex)
        swapListIndex();
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.choice_item, parent, false));
    }

    public void setChoosenListener(ChoiceChoosenListener listener) {
        this.choosenListener = listener;
    }

    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, final int position) {
        holder.item.setText(list.get(position).toString());
        holder.choiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Toast toast;
                if (list.get(position) == list.get(swapIndex)) {
                    toast = Toast.makeText(context, "恭喜! 答对了", Toast.LENGTH_SHORT);
                    rightIndex++;
                } else {
                    toast = Toast.makeText(context, " 你答错啦，哈哈哈。。。", Toast.LENGTH_SHORT);
                }

                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 500);

                totalIndex++;

                list = NumberGenerator.generateNumberArray(FrWinnerApplication.number_mode_min, FrWinnerApplication.number_mode_max);
                if (choosenListener != null) {
                    choosenListener.OnNumberChoiceChoosen(rightIndex, totalIndex, list.get(0));
                }
                swapListIndex();

                notifyDataSetChanged();
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

    private void swapListIndex() {
        swapIndex = NumberGenerator.randInt(0, list.size() - 1);
        String temp = list.get(0);
        list.set(0, list.get(swapIndex));
        list.set(swapIndex, temp);
    }
}
