package com.tiger.xiaohumo.frnumberwinner.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaohumo on 28/10/15.
 */
public class ConfigSingleItemObject implements Parcelable{
    private int min;
    private int max;
    private int time;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static final Parcelable.Creator<ConfigSingleItemObject> CREATOR = new Creator() {

        @Override
        public ConfigSingleItemObject createFromParcel(Parcel source) {

            ConfigSingleItemObject object = new ConfigSingleItemObject();
            object.setMin(source.readInt());
            object.setMax(source.readInt());
            object.setTime(source.readInt());
            return object;
        }

        @Override
        public ConfigSingleItemObject[] newArray(int size) {
            return new ConfigSingleItemObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(min);
        dest.writeInt(max);
        dest.writeInt(time);
    }
}
