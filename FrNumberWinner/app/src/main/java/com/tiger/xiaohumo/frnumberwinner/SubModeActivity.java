package com.tiger.xiaohumo.frnumberwinner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiger.xiaohumo.frnumberwinner.adapters.ModeChoiceRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SubModeActivity extends Fragment {

    @Bind(R.id.number_choice_list)
    RecyclerView recyclerView;

    @Bind(R.id.time_btn)
    CardView timeBtn;

    @Bind(R.id.date_btn)
    CardView dateBtn;

    private ModeChoiceRecyclerViewAdapter adapter;
    private NumberPlayFragment playFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_submode, container, false);
        
        ButterKnife.bind(this, rootView);

        FrWinnerApplication.PLAY_TYPE type = FrWinnerApplication.currentType;
        switch (type) {
            case NUMBER:
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new ModeChoiceRecyclerViewAdapter(getActivity(), getConfigLists());
                recyclerView.setAdapter(adapter);
                break;

            case TIME:
                timeBtn.setVisibility(View.VISIBLE);
                dateBtn.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
        return rootView;
    }

    @OnClick(R.id.time_btn)
    public void OnClickTime(){
        playFragment = new NumberPlayFragment();
        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.TIME;
        ((MainActivity) getActivity()).setFragment(playFragment);
    }

    @OnClick(R.id.date_btn)
    public void OnClickDate(){
        playFragment = new NumberPlayFragment();
        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.DATE;
        ((MainActivity) getActivity()).setFragment(playFragment);
    }


    private ArrayList<ConfigSingleItemObject> getConfigLists() {
        Type listType = new TypeToken<ArrayList<ConfigSingleItemObject>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(loadJSONFromAsset(), listType);
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("config.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
