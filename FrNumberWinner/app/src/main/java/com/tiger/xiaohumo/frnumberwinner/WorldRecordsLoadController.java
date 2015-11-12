package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.tiger.xiaohumo.frnumberwinner.objects.Record;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaohumo on 12/11/15.
 */
public class WorldRecordsLoadController {

    public static final String NAME = "name";
    public static final String MODE = "mode";
    public static final String SCORE = "score";

    private Activity context;

    public WorldRecordsLoadController(Activity context) {
        this.context = context;
    }

    final public Record GetMaxRecord(String mode) {
        String getMaxRecordUrl = MainActivity.SERVER_URL + "/records/" + mode + "/max";
        Log.d(" getmaxurl", getMaxRecordUrl);
        final Record record = new Record();

        final AQuery aq = new AQuery(context);
        aq.ajax(getMaxRecordUrl, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject str, AjaxStatus status) {
                Log.d(" response", str.toString());
                try {
                    record.setMode(str.getString(MODE));
                    record.setName(str.getString(NAME));
                    record.setScore(str.getString(SCORE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                context.runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        Toast.makeText(context, "此模式最高分纪录为" + record.getScore(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return record;
    }

    final public void uploadRecord(String name, String mode, int score) throws JSONException {
        String uploadUrl = MainActivity.SERVER_URL + "/records";
        Log.d(" uploadUrl", uploadUrl);
        final Record record = new Record();

        JSONObject input = new JSONObject();
        input.putOpt(NAME, name);
        input.putOpt(MODE, mode);
        input.putOpt(SCORE, score);

        final AQuery aq = new AQuery(context);

        aq.post(uploadUrl, input, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject str, AjaxStatus status) {
                Log.d(" response", str.toString());
            }
        });
    }
}
