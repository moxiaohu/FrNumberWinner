package com.tiger.xiaohumo.frnumberwinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by xiaohumo on 11/11/15.
 */
public class GridAdapter extends BaseAdapter {
    private Context mContext;

    public GridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return ModeActivity.modeList.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder;
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            holder = new Holder();
            convertView = inflater.inflate(R.layout.mode_grid_item, null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.mode_image);
            holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setPadding(8, 8, 8, 8);

            holder.textView = (TextView)convertView.findViewById(R.id.mode_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        switch (ModeActivity.modeList[position]){
            case R.drawable.number :
                holder.textView.setText(R.string.number);
                break;

            case R.drawable.tele :
                holder.textView.setText(R.string.tele);
                break;

            case R.drawable.time :
                holder.textView.setText(R.string.time);
                break;

            case R.drawable.mix :
                holder.textView.setText(R.string.mix);
                break;
            default:break;
        }

        holder.imageView.setImageResource(ModeActivity.modeList[position]);
        return convertView;
    }

    class Holder {
        public ImageView imageView;
        public TextView textView;
    }
}