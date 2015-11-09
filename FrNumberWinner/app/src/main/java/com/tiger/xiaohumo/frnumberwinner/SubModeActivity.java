package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiger.xiaohumo.frnumberwinner.adapters.ModeChoiceRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.NumberChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SubModeActivity extends Activity {

    @Bind(R.id.number_choice_list)
    RecyclerView recyclerView;

    @Bind(R.id.time_btn)
    CardView timeBtn;

    @Bind(R.id.date_btn)
    CardView dateBtn;

    public final static String LAUNCHMODE = "MODEL";
    private ModeChoiceRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ModeActivity.PLAY_TYPE type = (ModeActivity.PLAY_TYPE) getIntent().getSerializableExtra(ModeActivity.TYPE);
        switch (type) {
            case NUMBER:
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new ModeChoiceRecyclerViewAdapter(this, getConfigLists());
                recyclerView.setAdapter(adapter);
                break;

            case TIME_OR_DATE:
                timeBtn.setVisibility(View.VISIBLE);
                dateBtn.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
    }

    @OnClick(R.id.time_btn)
    public void OnClickTime(){
        Intent intent = new Intent(this, NumberPlayActivity.class);
        intent.putExtra(ModeActivity.TYPE, ModeActivity.PLAY_TYPE.TIME);
        startActivity(intent);
    }

    @OnClick(R.id.date_btn)
    public void OnClickDate(){
        Intent intent = new Intent(this, NumberPlayActivity.class);
        intent.putExtra(ModeActivity.TYPE, ModeActivity.PLAY_TYPE.DATE);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new AlertDialog.Builder(this).setTitle("游戏规则").setMessage("游戏规则非常简单，选择模式，点击开始，根据听到的数字选择答案，练习法语数字听力啦啦啦!").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            InputStream is = getAssets().open("config.json");

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
