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

import com.tiger.xiaohumo.frnumberwinner.adapters.DateChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.NumberChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.TelePhoneNumberRecycleViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.TimeChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.interfaces.ChoiceChoosenListener;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;
import com.tiger.xiaohumo.frnumberwinner.util.NumberGenerator;

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

    private NumberChoicesRecyclerViewAdapter numAdapter;
    private TelePhoneNumberRecycleViewAdapter teleAdapter;
    private TimeChoicesRecyclerViewAdapter timeAdapter;
    private DateChoicesRecyclerViewAdapter dateAdapter;
    private int MY_DATA_CHECK_CODE = 0;
    private static int index = 0;
    Timer timer = new Timer();
    private int totalQuestions;
    private int totalRightAnswers;
    private ConfigSingleItemObject modeObject;


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

        if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TELEPHONE_NUMBER) {

            ArrayList<ArrayList<String>> lists = NumberGenerator.generateTelePhoneArray();
            speakTelePhoneNumber(lists.get(0));

            teleAdapter = new TelePhoneNumberRecycleViewAdapter(getActivity(), lists);
            teleAdapter.setChoosenListener(new OnChooseModeListener() {
            });
            recyclerView.setAdapter(teleAdapter);

        } else if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.TIME) {

            ArrayList<String> timeList = NumberGenerator.generateTimeList();
            speakNumber(timeList.get(0));
            timeAdapter = new TimeChoicesRecyclerViewAdapter(getActivity(), timeList);
            timeAdapter.setChoosenListener(new OnChooseModeListener());
            recyclerView.setAdapter(timeAdapter);

        } else if (FrWinnerApplication.currentType == FrWinnerApplication.PLAY_TYPE.DATE) {
            ArrayList<String> timeList = NumberGenerator.generateDateList();
            speakNumber(timeList.get(0));
            dateAdapter = new DateChoicesRecyclerViewAdapter(getActivity(), timeList);
            dateAdapter.setChoosenListener(new OnChooseModeListener());
            recyclerView.setAdapter(dateAdapter);
        } else {
            ArrayList<String> integers = NumberGenerator.generateNumberArray(FrWinnerApplication.number_mode_min, FrWinnerApplication.number_mode_max);

            speakNumber(integers.get(0));
            numAdapter = new NumberChoicesRecyclerViewAdapter(getActivity(), integers, modeObject);
            numAdapter.setChoosenListener(new OnChooseModeListener());
            recyclerView.setAdapter(numAdapter);
        }

        CountDownTask task = new CountDownTask(totalTime);
        timer.schedule(task, 1000, 1000);       // timeTask
    }

    private void speakNumber(String speech) {
        MainActivity.myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
    }


    private void speakTelePhoneNumber(ArrayList<String> teleNumber) {

        for (int i = 0; i < teleNumber.size(); i++) {
            speakNumber(teleNumber.get(i));
        }
    }

    private void showResult() {

        int wrong = totalQuestions - totalRightAnswers;
        int credit = totalRightAnswers - wrong;

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
                        showResult();
                    }
                }
            });
        }
    }

    private class OnChooseModeListener implements ChoiceChoosenListener{

        @Override
        public void OnNumberChoiceChoosen(int rightIndex, int total, String target) {
        questionNumber.setText(rightIndex + "/" + (total));
        questionIndex = index;
        totalRightAnswers = rightIndex;
        totalQuestions = total;
        speakNumber(target);
        }

        @Override
        public void OnTelephoneChoosen(int rightIndex, int totalIndex, ArrayList<String> teleNumber) {
        questionNumber.setText(rightIndex + "/" + (totalIndex));
        questionIndex = index;
        totalRightAnswers = rightIndex;
        totalQuestions = totalIndex;
        MainActivity.myTTS.stop();
        speakTelePhoneNumber(teleNumber);
        }

        @Override
        public void OnTimeChoiceChoosen(int rightIndex, int totalIndex, String teleNumber) {
        questionNumber.setText(rightIndex + "/" + (totalIndex));
        questionIndex = index;
        totalRightAnswers = rightIndex;
        totalQuestions = totalIndex;
        MainActivity.myTTS.stop();
        speakNumber(teleNumber);
        }
    }

}


