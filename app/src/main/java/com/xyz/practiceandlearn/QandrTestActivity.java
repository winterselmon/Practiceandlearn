package com.xyz.practiceandlearn;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import static com.xyz.practiceandlearn.Global.basedir;

import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_ANSWER_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_ANSWER_TEST;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.QuestionAndResponse_Question_TEST;

public class QandrTestActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    //private int currentposition;
    private int currentpage;
    private MediaPlayer mPlayer;
    private static final int maxrow = 29;
    private static String[] strListAnswer;

    //timer
    TextView txttime2;

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

            txttime2.setText(String.format("%d:%02d:%02d",hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qandr_test);

        objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");

        //currentposition = 0;

        currentpage = 1;

        String strNumber = String.valueOf(Global.currentposition+1)+ "/30";

        TextView txtNo = (TextView) findViewById(R.id.txtnum2);
        txtNo.setText(strNumber);


        txttime2 = (TextView) findViewById(R.id.txttime2);
        timerHandler.postDelayed(timerRunnable, 0);

        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_qandr_test);
        rdoA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_qandr_test);
        rdoB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_qandr_test);
        rdoC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        setupArray();

        playSound();

        back();

        next();


    }


    private void collectpoint() {

        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_qandr_test);
        if (rdoA2.isChecked()) {

                if (strListAnswer[Global.currentposition].equals("A")) {

                    Global.collect[Global.currentAnswer] = true;
                } else {
                    Global.collect[Global.currentAnswer] = false;
                }
            }


        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_qandr_test);
        if (rdoB2.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("B")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }

        }

        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_qandr_test);
        if (rdoC2.isChecked()) {

            if (strListAnswer[Global.currentposition].equals("C")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }

        }

    }


    private void setupArray(){


        strListAnswer = listAnswer();
    }

    private void sumScore() {
        int score = 0;
        for (int i = 0; i < 200; i++) {
            if (Global.collect[i])
                score++;
        }
        String S = Integer.toString(score);
        Toast.makeText(getBaseContext(), "Score = " + S, Toast.LENGTH_SHORT).show();
    }

    private void playSound() {

        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }
        String filePath = Global.basedir +"/AudioQandR/"+String.valueOf(Global.currentAnswer+1)+".mp3";
        //String filePath = Environment.getExternalStorageDirectory()+"/AudioQandR/"+String.valueOf(Global.currentAnswer+1)+".mp3";
        mPlayer = new MediaPlayer();

        try {
            if (!Global.played[Global.currentAnswer]) {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();
                mPlayer.start();
                Global.played[Global.currentAnswer] = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack2_qandr_test);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Global.currentAnswer == 0){
                    return;
                }
                Global.currentAnswer--;

                if (Global.currentposition == 0) {
                    if (currentpage == 0) {
                        return;
                    } else {
                        Global.currentposition = 9;
                        Intent intent = new Intent(QandrTestActivity.this, PhotosTestActivity.class);
                        startActivity(intent);
                        sumScore();
                    }

                } else {


                    Global.currentposition--;
                    if (Global.currentposition >= 0) {

                        String strNumber = String.valueOf(Global.currentposition + 1) + "/30";
                        Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                        TextView txtNo = (TextView) findViewById(R.id.txtnum2);
                        txtNo.setText(strNumber);


                    }

                    playSound();

                    clearcheck();
                }
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext2_qandr_test);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Global.currentposition++;
                Global.currentAnswer++;


                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(QandrTestActivity.this, DescriptionShortconActivity.class);
                    startActivity(intent);
                    sumScore();

                } else {

                    String strNumber = String.valueOf(Global.currentposition + 1) + "/30";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                    //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum2);
                    txtNo.setText(strNumber);

                    playSound();

                    clearcheck();


                }

            }
        });

    }

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroup2_qandr_test);
        rdogroup1.clearCheck();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    private String[] listAnswer(){

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM QuestionAndResponse_Question_TEST WHERE COLUMN_QANDR_ANSWER_TEST", new String[]{"COLUMN_QANDR_ANSWER_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(QuestionAndResponse_Question_TEST, new String[]{COLUMN_QANDR_ANSWER_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_ANSWER_TEST"));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_ANSWER_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;

    }
}
