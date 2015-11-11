package com.tiger.xiaohumo.frnumberwinner;

import android.app.Application;

import com.tiger.xiaohumo.frnumberwinner.util.FontsOverride;

/**
 * Created by xiaohumo on 09/11/15.
 */
public class FrWinnerApplication extends Application {

    public final static String TYPE = "TYPE";

    public enum PLAY_TYPE {
        NUMBER,
        TELEPHONE_NUMBER,
        MIX,
        TIME,
        DATE
    }

    public static int number_mode_max;
    public static int number_mode_min;
    public static int totalTime;

    public static PLAY_TYPE currentType;

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "mini.ttf");
    }
}
