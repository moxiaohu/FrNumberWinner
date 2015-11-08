package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaohumo on 29/10/15.
 */
public class ModeActivity extends Activity {

    public final static String TYPE = "TYPE";

    public enum PLAY_TYPE {
        NUMBER,
        TELEPHONE_NUMBER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.number_type_btn)
    public void OnClickNumBtn() {
        Intent intent = new Intent(this, SubModeActivity.class);
        intent.putExtra(TYPE, PLAY_TYPE.NUMBER);
        startActivity(intent);
    }

    @OnClick(R.id.tele_input_btn)
    public void OnClickTeleInputBtn() {
        Intent intent = new Intent(this, TeleInputActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tele_btn)
    public void OnClickTeleBtn() {

        new AlertDialog.Builder(this).setTitle("游戏规则").setMessage("时间为六十秒!你将会听到的是法国电话号码，开始吧亲!").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ModeActivity.this, NumberPlayActivity.class);
                        intent.putExtra(TYPE, PLAY_TYPE.TELEPHONE_NUMBER);
                        startActivity(intent);
                    }
                }).show();
    }
}
