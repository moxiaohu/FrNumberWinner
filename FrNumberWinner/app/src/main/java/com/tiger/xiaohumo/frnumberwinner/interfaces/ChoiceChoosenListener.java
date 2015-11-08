package com.tiger.xiaohumo.frnumberwinner.interfaces;

import java.util.ArrayList;

/**
 * Created by xiaohumo on 26/10/15.
 */
public interface ChoiceChoosenListener {
    public void OnNumberChoiceChoosen(int rightIndex, int totalIndex, int targetValue);
    public void OnTelephoneChoosen(int rightIndex, int totalIndex, ArrayList<String> teleNumber);
}
