package com.xyz.practiceandlearn;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class QandrTestActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    //private int currentposition;
    private int currentpage;
    private MediaPlayer mPlayer;
    private static final int maxrow = 29;

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

        objMyDatabase = new MyDatabase(this);

        //currentposition = 0;

        currentpage = 1;

        String strNumber = String.valueOf(Global.currentposition+1)+ "/30";

        TextView txtNo = (TextView) findViewById(R.id.txtnum2);
        txtNo.setText(strNumber);


        txttime2 = (TextView) findViewById(R.id.txttime2);
        timerHandler.postDelayed(timerRunnable, 0);



        playSound();

        back();

        next();

    }


    private void playSound() {

        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }
        String filePath = Environment.getExternalStorageDirectory()+"/AudioQandR/"+String.valueOf(Global.currentposition+1)+".mp3";
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

        Button btnBack = (Button) findViewById(R.id.btnBack2_qandr_test);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Global.currentposition == 0) {
                    if (currentpage == 0) {
                        return;
                    } else {
                        Global.currentposition = 9;
                        Intent intent = new Intent(QandrTestActivity.this, PhotosTestActivity.class);
                        startActivity(intent);
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

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(QandrTestActivity.this, ShortConTestActivity.class);
                    startActivity(intent);
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
}
