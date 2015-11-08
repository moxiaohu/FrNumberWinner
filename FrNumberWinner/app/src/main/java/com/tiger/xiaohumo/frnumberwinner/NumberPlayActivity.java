package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tiger.xiaohumo.frnumberwinner.adapters.NumberChoicesRecyclerViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.adapters.TelePhoneNumberRecycleViewAdapter;
import com.tiger.xiaohumo.frnumberwinner.interfaces.ChoiceChoosenListener;
import com.tiger.xiaohumo.frnumberwinner.objects.ConfigSingleItemObject;
import com.tiger.xiaohumo.frnumberwinner.util.NumberGenerator;

import java.util.ArrayList;
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
public class NumberPlayActivity extends Activity implements TextToSpeech.OnInitListener, ChoiceChoosenListener{

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

    private NumberChoicesRecyclerViewAdapter adapter;
    private TelePhoneNumberRecycleViewAdapter teleAdapter;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    private static int index = 0;
    Timer timer = new Timer();
    private int totalQuestions;
    private int totalRightAnswers;
    private ConfigSingleItemObject modeObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_number_test);

        ButterKnife.bind(this);
        if (getIntent().getSerializableExtra(ModeActivity.TYPE) == ModeActivity.PLAY_TYPE.TELEPHONE_NUMBER){
            totalTime = 60;
        }else {
            modeObject = getIntent().getExtras().getParcelable(SubModeActivity.LAUNCHMODE);

            totalTime = modeObject.getTime();
        }

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    @OnClick(R.id.start_btn)
    public void OnClickStart() {
        btnStart.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().getSerializableExtra(ModeActivity.TYPE) == ModeActivity.PLAY_TYPE.TELEPHONE_NUMBER){

            ArrayList<ArrayList<String>> lists = NumberGenerator.generateTelePhoneArray();
            speakTelePhoneNumber(lists.get(0));

            teleAdapter = new TelePhoneNumberRecycleViewAdapter(this, lists);
            teleAdapter.setChoosenListener(this);
            recyclerView.setAdapter(teleAdapter);

        }else {
            ArrayList<Integer> integers = NumberGenerator.generateNumberArray(modeObject.getMin(), modeObject.getMax());

            speakNumber(String.valueOf(integers.get(0)));
            adapter = new NumberChoicesRecyclerViewAdapter(this, integers, modeObject);
            adapter.setChoosenListener(this);
            recyclerView.setAdapter(adapter);

        }

        CountDownTask task = new CountDownTask(totalTime);
        timer.schedule(task, 1000, 1000);       // timeTask
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.FRENCH)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.FRENCH);
        }else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    private void speakNumber(String speech) {
        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void OnNumberChoiceChoosen(int rightIndex, int total, int target) {
        questionNumber.setText(rightIndex + "/" + (total));
        questionIndex = index;
        totalRightAnswers = rightIndex;
        totalQuestions = total;
        speakNumber(String.valueOf(target));
    }

    @Override
    public void OnTelephoneChoosen(int rightIndex, int totalIndex, ArrayList<String> teleNumber) {
        questionNumber.setText(rightIndex + "/" + (totalIndex));
        questionIndex = index;
        totalRightAnswers = rightIndex;
        totalQuestions = totalIndex;
        myTTS.stop();
        speakTelePhoneNumber(teleNumber);
    }

    private void speakTelePhoneNumber(ArrayList<String> teleNumber){

        for (int i = 0; i < teleNumber.size(); i++) {
                speakNumber(teleNumber.get(i));
        }
    }

    private void showResult() {

        int wrong = totalQuestions - totalRightAnswers;
        int credit = totalRightAnswers - wrong;

        String comment = "";
        if (credit > 12 ){
            comment = "超神了, 收下我的膝盖";
        }else if (credit <= 12 && credit > 9){
            comment = "屌屌的";
        }else if (credit <= 9 && credit > 5){
            comment = "加油阿亲!";
        }else if(credit <= 5 && credit > 2){
            comment = "呵呵, 一般般";
        }else if (credit <= 2 && credit >=0){
            comment = "大哥/大姐，你睡着了么...";
        }else if (credit < 0){
            comment = "你就是个逗比";
        }
        new AlertDialog.Builder(this).setTitle("结果").setMessage("答对" + totalRightAnswers +
                                                                "\n 答错 " + wrong +
                                                                " \n 用时" + totalTime + "秒" +
                                                                "\n 总分 " + credit +
                                                                "\n" + comment).
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

        timer.cancel();
        timer.purge();
        finish();
    }

    class CountDownTask extends TimerTask{
        private int totalSeconds;
        public CountDownTask(int totalTime){
            totalSeconds = totalTime;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {

                    totalSeconds--;
                    timerTxtV.setText(""+totalSeconds);
                    if(totalSeconds < 0) {
                        timer.cancel();
                        Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                        showResult();
                    }
                }
            });
        }
    }
}


