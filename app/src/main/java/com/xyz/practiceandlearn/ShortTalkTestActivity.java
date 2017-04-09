package com.xyz.practiceandlearn;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_ANSWER;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_ANSWER_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_A;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_A_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_B;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_B_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_C;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_C_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_D;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_D_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_QUESTION;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_QUESTION_TEST;
import static com.xyz.practiceandlearn.ShortTalkDatabase.SHORTTALK_QUESTION;
import static com.xyz.practiceandlearn.ShortTalkDatabase.SHORTTALK_QUESTION_TEST;

public class ShortTalkTestActivity extends AppCompatActivity {

    private MyDatabase objMyDatabase;
    private String[] strQuestion, strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD;
    //private int currentposition;
    private int currentpage;
    private static final int maxrow = 9;
    private MediaPlayer mPlayer;

    //timer
    TextView txttime4;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - Global.startTime;
            int seconds = (int) (millis / 1000);
            int minutes = (seconds % 3600)/60;
            int hour    = seconds / 3600;
            seconds = seconds % 60;

            txttime4.setText(String.format("%d:%02d:%02d",hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_talk_test);

        objMyDatabase = new MyDatabase(this);

        //currentposition = 0;

        currentpage = 3;

        String strNumber = String.valueOf(Global.currentposition+1)+ "/10";

        TextView txtNo = (TextView) findViewById(R.id.txtnum4);
        txtNo.setText(strNumber);

        ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk_test);

        scrollView.smoothScrollTo(0,0);

        txttime4 = (TextView) findViewById(R.id.txttime4);
        timerHandler.postDelayed(timerRunnable, 0);


        setupArray();

        showArray();

        clearcheck();

        playSound();

        back();

        next();

    }

    private void playSound() {

        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }
        String filePath = Environment.getExternalStorageDirectory()+"/AudioShortTalk/"+String.valueOf(Global.currentposition+1)+".mp3";
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack4_test);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (Global.currentposition == 0) {
                    if (currentpage == 0) {
                        return;
                    } else {
                        Global.currentposition = 9;
                        Intent intent = new Intent(ShortTalkTestActivity.this, ShortConTestActivity.class);
                        startActivity(intent);
                    }
                } else {

                    ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk_test);

                    scrollView.smoothScrollTo(0,0);

                    Global.currentposition--;
                    if (Global.currentposition >= 0) {

                        String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                        Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);

                        TextView txtNo = (TextView) findViewById(R.id.txtnum4);
                        txtNo.setText(strNumber);


                    }

                    playSound();

                    clearcheck();

                    showArray();
                }
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext4_test);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Global.currentposition++;

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(ShortTalkTestActivity.this, IncompleteSentenceTestActivity.class);
                    startActivity(intent);
                } else {


                    ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk_test);

                    scrollView.smoothScrollTo(0,0);

                    String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                    //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum4);
                    txtNo.setText(strNumber);

                    playSound();

                    clearcheck();

                    showArray();
                }

            }
        });

    }

    private void setupArray() {

        strQuestion = listQuestion();
        strAnswer = listAnswer();
        strChoiceA = listChoiceA();
        strChoiceB = listChoiceB();
        strChoiceC = listChoiceC();
        strChoiceD = listChoiceD();


    }

    private void showArray(){


        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestion1_shortTalk_test);
        txtQuestion1.setText(strQuestion[Global.currentposition*3]);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_shortTalk_test);
        txtQuestion2.setText(strQuestion[(Global.currentposition*3)+1]);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_shortTalk_test);
        txtQuestion3.setText(strQuestion[(Global.currentposition*3)+2]);

        //Choice1
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_shortTalk_test);
        rdoA1.setText("A."+strChoiceA[Global.currentposition*3]);
        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_shortTalk_test);
        rdoB1.setText("B."+strChoiceB[Global.currentposition*3]);
        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_shortTalk_test);
        rdoC1.setText("C."+strChoiceC[Global.currentposition*3]);
        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_shortTalk_test);
        rdoD1.setText("D."+strChoiceD[Global.currentposition*3]);

        //Choice2
        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_shortTalk_test);
        rdoA2.setText("A."+strChoiceA[Global.currentposition*3+1]);
        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_shortTalk_test);
        rdoB2.setText("B."+strChoiceB[Global.currentposition*3+1]);
        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_shortTalk_test);
        rdoC2.setText("C."+strChoiceC[Global.currentposition*3+1]);
        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_shortTalk_test);
        rdoD2.setText("D."+strChoiceD[Global.currentposition*3+1]);

        //Choice3
        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_shortTalk_test);
        rdoA3.setText("A."+strChoiceA[Global.currentposition*3+2]);
        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_shortTalk_test);
        rdoB3.setText("B."+strChoiceB[Global.currentposition*3+2]);
        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_shortTalk_test);
        rdoC3.setText("C."+strChoiceC[Global.currentposition*3+2]);
        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_shortTalk_test);
        rdoD3.setText("D."+strChoiceD[Global.currentposition*3+2]);


    }

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroupAShortTalk_test);
        rdogroup1.clearCheck();
        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroupBShortTalk_test);
        rdogroup2.clearCheck();
        RadioGroup rdogroup3 = (RadioGroup) findViewById(R.id.rdoGroupCShortTalk_test);
        rdogroup3.clearCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }


    private String[] listQuestion() {

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_QUESTION_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListQuestion = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListQuestion[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_QUESTION_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListQuestion;

    }

    private String[] listAnswer() {

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_ANSWER_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListAnswer = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListAnswer[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_ANSWER_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_CHOICE_A_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceA = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceA[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_A_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_CHOICE_B_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceB = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceB[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_B_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_CHOICE_C_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceC = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceC[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_C_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor cursor = db.query(SHORTTALK_QUESTION_TEST, new String[]{COLUMN_SHORTTALK_CHOICE_D_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceD = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceD[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_D_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceD;

    }
}
