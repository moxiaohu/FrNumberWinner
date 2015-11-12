package com.tiger.xiaohumo.frnumberwinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.tiger.xiaohumo.frnumberwinner.util.NumberGenerator;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaohumo on 31/10/15.
 */
public class TeleInputActivity extends Activity implements TextToSpeech.OnInitListener{

    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    private ArrayList<String> currNumber;

    @Bind(R.id.tele_number_edttxt)
    EditText teleInputTxt;
    private String [] headset = new String[]{"06", "07", "08", "01"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_input);

        ButterKnife.bind(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        generateTeleNumber();
    }

    private void generateTeleNumber() {
        String head = headset[NumberGenerator.randInt(0, headset.length - 1)];
//        currNumber = NumberGenerator.generateOneTelePhoneNumber(head);
    }

    @OnClick(R.id.listen_tele_number)
    public void OnClickListenNumber(){

        speakNumber(currNumber);
    }

    @OnClick(R.id.check_btn)
    public void OnClickCheck(){
        String number = "";
        for (int i = 0; i < currNumber.size(); i++) {
            number+= currNumber.get(i);
        }

        final Toast toast;
        if (teleInputTxt.getText().toString().equals(number)){
            new AlertDialog.Builder(this).setTitle("恭喜答对啦").setMessage("听下一个号码?").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateTeleNumber();
                            teleInputTxt.setText("");
                            speakNumber(currNumber);
                        }
                    }).show();
        }else {
            toast = Toast.makeText(this, " 你答错啦，哈哈哈。。。", Toast.LENGTH_SHORT);
            toast.show();
        }
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

    private void speakNumber(ArrayList<String> speech) {

        for (int i = 0; i < currNumber.size(); i++) {
            myTTS.speak(currNumber.get(i), TextToSpeech.QUEUE_ADD, null);
        }
    }
}
