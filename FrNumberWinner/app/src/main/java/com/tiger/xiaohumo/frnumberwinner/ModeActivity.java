package com.tiger.xiaohumo.frnumberwinner;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by xiaohumo on 29/10/15.
 */
public class ModeActivity extends Fragment {

    public static final int[] modeList = new int[]{R.drawable.number, R.drawable.time, R.drawable.tele, R.drawable.mix};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_type, container, false);
        ButterKnife.bind(rootView, getActivity());

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_mode);
        gridView.setAdapter(new GridAdapter(getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Fragment submode = null;
                switch (position) {
                    case 0:
                        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.NUMBER;
                        submode = new SubModeActivity();
                        break;
                    case 1:
                        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.TIME;
                        submode = new SubModeActivity();
                        break;
                    case 2:
                        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.TELEPHONE_NUMBER;
                        submode = new NumberPlayFragment();
                        break;
                    case 3:
                        FrWinnerApplication.currentType = FrWinnerApplication.PLAY_TYPE.MIX;
                        submode = new SubModeActivity();
                        break;
                    default:
                        break;
                }
                ((MainActivity) getActivity()).setFragment(submode);
            }
        });
        return rootView;
    }
}

