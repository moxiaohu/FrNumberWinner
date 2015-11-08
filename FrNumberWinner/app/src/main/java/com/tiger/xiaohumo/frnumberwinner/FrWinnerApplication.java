package com.tiger.xiaohumo.frnumberwinner;

import android.app.Application;

import com.tiger.xiaohumo.frnumberwinner.util.FontsOverride;

/**
 * Created by xiaohumo on 09/11/15.
 */
public class FrWinnerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "mini.ttf");
    }
}
