package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tiger.xiaohumo.frnumberwinner.adapters.ChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.DateChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.NumberChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.TelePhoneNumberRecycleViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.TimeChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.interfaces.ChoiceChoosenListener;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;
import com.tiger.xiaohumo.frnumberwinner.objects.Record;
import com.tiger.xiaohumo.frnumberwinner.util.NumberGenerator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaohumo on 27/10/15.
 */
public class NumberPlayFragment extends Fragment {

    @Bind(R.id.number_choice_list)
    RecyclerView recyclerView;

    @Bind(R.id.question_number)
    TextView questionNumber;

    @Bind(R.id.timer)
    TextView timerTxtV;

    @Bind(R.id.start_btn)
    Button btnStart;

    private static int totalTime = 0;

    private int questionIndex = 0;
    private static Random random = new Random();
    private static int target;

    private ChoicesRecyclerViewAdapter adapter;
    private NumberChoicesRecyclerViewAdapter numAdapter;
    private TelePhoneNumberRecycleViewAdapter teleAdapter;
    private TimeChoicesRecyclerViewAdapter timeAdapter;
    private DateChoicesRecyclerViewAdapter dateAdapter;
    private int MY_DATA_CHECK_CODE = 0;
    private static int index = 0;
    Timer timer = new Timer();
    private int totalQuestions;
    private int totalRightAnswers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_fr_number_test, container, false);


        ButterKnife.bind(this, rootView);
        if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TELEPHONE_NUMBER ||
                FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TIME ||
                FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.DATE||
                FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.MIX) {
            totalTime = 60;
        } else {
            totalTime = FrWinnerApplication.totalTime;
        }

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        return rootView;
    }

    @OnClick(R.id.start_btn)
    public void OnClickStart() {
        btnStart.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<String> list = null;
        switch (FrWinnerApplication.currentType){
            case TELEPHONE_NUMBER:
                list = NumberGenerator.generateTelePhoneArray();
                speakTelePhoneNumber(list.get(0));
                break;
            case TIME:
                list = NumberGenerator.generateTimeList();
                speakNumber(list.get(0));
                break;
            case DATE:
                list = NumberGenerator.generateDateList();
                speakNumber(list.get(0));
                break;
            case NUMBER:
                list = NumberGenerator.generateNumberArray(FrWinnerApplication.number_mode_min, FrWinnerApplication.number_mode_max);
                speakNumber(list.get(0));
                break;
        }
        adapter = new ChoicesRecyclerViewAdapter(getActivity(), list);
        adapter.setChoosenListener(new OnChooseModeListener());
        recyclerView.setAdapter(adapter);

//
//        if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TELEPHONE_NUMBER) {
//
//            ArrayList<String> lists = NumberGenerator.generateTelePhoneArray();
//            speakTelePhoneNumber(lists.get(0));
//
//
//        } else if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TIME) {
//
//            ArrayList<String> timeList = NumberGenerator.generateTimeList();
//            speakNumber(timeList.get(0));
//            timeAdapter = new TimeChoicesRecyclerViewAdapter(getActivity(), timeList);
//            timeAdapter.setChoosenListener(new OnChooseModeListener());
//            recyclerView.setAdapter(timeAdapter);
//
//        } else if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.DATE) {
//            ArrayList<String> timeList = NumberGenerator.generateDateList();
//            speakNumber(timeList.get(0));
//            dateAdapter = new DateChoicesRecyclerViewAdapter(getActivity(), timeList);
//            dateAdapter.setChoosenListener(new OnChooseModeListener());
//            recyclerView.setAdapter(dateAdapter);
//        } else {
//            ArrayList<String> integers = NumberGenerator.generateNumberArray(FrWinnerApplication.number_mode_min, FrWinnerApplication.number_mode_max);
//
//            speakNumber(integers.get(0));
//            numAdapter = new NumberChoicesRecyclerViewAdapter(getActivity(), integers, modeObject);
//            numAdapter.setChoosenListener(new OnChooseModeListener());
//            recyclerView.setAdapter(numAdapter);
//        }

        CountDownTask task = new CountDownTask(totalTime);
        timer.schedule(task, 1000, 1000);       // timeTask
    }

    private void speakNumber(String speech) {
        MainActivity.myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
    }


    private void speakTelePhoneNumber(String teleNumber) {

        for (int i = 0; i < teleNumber.length() / 2; i++) {
            speakNumber(teleNumber.substring(i * 2, (i * 2 + 2)));
        }
    }

    private void showResult() throws JSONException {

        WorldRecordsLoadController recorder = new WorldRecordsLoadController(getActivity());
        String currMode = null;
        switch (FrWinnerApplication.currentType){
            case NUMBER:
                currMode = "number";
                break;
            case TELEPHONE_NUMBER:
                currMode = "tele";
                break;
            case TIME:
                currMode = "time";
                break;
            case DATE:
                currMode = "date";
                break;
            case MIX:
                currMode = "mix";
                break;
        }
        recorder.GetMaxRecord(currMode);



        int wrong = totalQuestions - totalRightAnswers;
        int credit = totalRightAnswers - wrong;

        recorder.uploadRecord("null", currMode, credit);

        String comment = "";
        if (credit > 12) {
            comment = "超神了, 收下我的膝盖";
        } else if (credit <= 12 && credit > 9) {
            comment = "屌屌的";
        } else if (credit <= 9 && credit > 5) {
            comment = "加油阿亲!";
        } else if (credit <= 5 && credit > 2) {
            comment = "呵呵, 一般般";
        } else if (credit <= 2 && credit >= 0) {
            comment = "大哥/大姐，你睡着了么...";
        } else if (credit < 0) {
            comment = "你就是个逗比";
        }
        new AlertDialog.Builder(getActivity()).setTitle("结果").setMessage("答对" + totalRightAnswers +
                "\n 答错 " + wrong +
                " \n 用时" + totalTime + "秒" +
                "\n 总分 " + credit +
                "\n" + comment).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).show();
    }

    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

        timer.cancel();
        timer.purge();
        getActivity().finish();
    }

    class CountDownTask extends TimerTask {
        private int totalSeconds;

        public CountDownTask(int totalTime) {
            totalSeconds = totalTime;
        }

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {

                    totalSeconds--;
                    timerTxtV.setText("" + totalSeconds);
                    if (totalSeconds < 0) {
                        timer.cancel();
                        Toast.makeText(getActivity(), "Game Over", Toast.LENGTH_SHORT).show();
                        try {
                            showResult();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private class OnChooseModeListener implements ChoiceChoosenListener{

        @Override
        public void OnChoiceChoosen(int rightIndex, int totalIndex, String number) {
            questionNumber.setText(rightIndex + "/" + (totalIndex));
            questionIndex = index;
            totalRightAnswers = rightIndex;
            totalQuestions = totalIndex;
            MainActivity.myTTS.stop();

            switch (FrWinnerApplication.currentType){
                case NUMBER:
                case TIME:
                case DATE:
                    speakNumber(number);
                    break;
                case TELEPHONE_NUMBER:
                    speakTelePhoneNumber(number);
                    break;
                default:break;
            }
        }
    }
}


