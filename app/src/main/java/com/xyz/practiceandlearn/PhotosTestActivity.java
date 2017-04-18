package com.xyz.practiceandlearn;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_ANSWER;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_ANSWER_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_A;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_A_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_B;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_B_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_C;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_C_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_D;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_D_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_DES;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;

public class PhotosTestActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private static String[] strListChoiceA, strListChoiceB, strListChoiceC, strListChoiceD, strListAnswer;
    //private int currentposition;
    private int currentpage;
    private boolean showanswer;
    private static final int maxrow = 9;
    private MediaPlayer mPlayer;


    //timer
    TextView txttime;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - Global.startTime;
            int seconds = (int) (millis / 1000);
            int minutes = (seconds % 3600) / 60;
            int hour = seconds / 3600;
            seconds = seconds % 60;

            txttime.setText(String.format("%d:%02d:%02d", hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_test);

        objMyDatabase = new MyDatabase(this);

        //Global.currentposition;

        currentpage = 0;

        String strNumber = String.valueOf(Global.currentposition + 1) + "/10";

        //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);

        TextView txtNo = (TextView) findViewById(R.id.txtnum);
        txtNo.setText(strNumber);


        txttime = (TextView) findViewById(R.id.txttime);
        if (Global.startTime == 0)
            Global.startTime = System.currentTimeMillis();

        timerHandler.postDelayed(timerRunnable, 0);


        setupArrar();

        clearlayout();

        //clearcheck();

        next();

        back();

        showpicture();

        playSound();

        collectpoint();


    }

    private void sumScore() {
        int score = 0;
        for (int i = 0; i < 200; i++) {
            if (Global.collect[i])
                score++;
        }
        String S = Integer.toString(score);
        Toast.makeText(getBaseContext(), "Score = " + S, Toast.LENGTH_LONG).show();
    }


    private void collectpoint() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdoGroup1_photo_test);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId) {
                switch (checkedId) {
                    case R.id.rdoA_photo_test:
                        Toast.makeText(getBaseContext(),"Hi",Toast.LENGTH_SHORT).show();

                        if (strListAnswer[Global.currentposition].equals("A")) {

                            Global.collect[Global.currentposition] = true;
                            Toast.makeText(getBaseContext(),"Hi",Toast.LENGTH_SHORT).show();

                        } else {
                            Global.collect[Global.currentposition] = false;

                        }
                        break;
                    case R.id.rdoB_photo_test:
                        if (strListAnswer[Global.currentposition].equals("B")) {

                            Global.collect[Global.currentposition] = true;

                        } else {
                            Global.collect[Global.currentposition] = false;

                        }
                        break;
                    case R.id.rdoC_photo_test:
                        if (strListAnswer[Global.currentposition].equals("C")) {

                            Global.collect[Global.currentposition] = true;

                        } else {
                            Global.collect[Global.currentposition] = false;

                        }
                        break;
                    case R.id.rdoD_photo_test:
                        if (strListAnswer[Global.currentposition].equals("D")) {
                            Global.collect[Global.currentposition] = true;

                        } else {
                            Global.collect[Global.currentposition] = false;

                        }
                        break;
                }
            }
        });
    }


        //RadioGroup radioCorrect = (RadioGroup) findViewById(R.id.rdoGroup1_photo_test);
        //radioCorrect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {




    private void showpicture() {

        File imgFile = new File(Environment.getExternalStorageDirectory()+"/photoTest/"+String.valueOf(Global.currentposition+1)+".png");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView img = (ImageView) findViewById(R.id.imgPhotosTest);
            //img.setBackgroundResource();

            img.setImageBitmap(myBitmap);

        }

    }

    private void playSound() {
        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }

        String filePath = Environment.getExternalStorageDirectory()+"/AudioPhoto/"+String.valueOf(Global.currentposition+1)+".mp3";
        mPlayer = new MediaPlayer();


        try {
            if (!Global.played[Global.currentposition]) {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();
                mPlayer.start();
                Global.played[Global.currentposition] = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupArrar() {

        Arrays.fill(Global.played,false);
        Arrays.fill(Global.collect,false);

        strListChoiceA = listChoiceA();
        strListChoiceB = listChoiceB();
        strListChoiceC = listChoiceC();
        strListChoiceD = listChoiceD();
        strListAnswer = listAnswer();

    }   //setup Arrar

    private void clearlayout() {

        RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA_photo_test);
        textChoiceA.setText("A.Listen and choose.");
        RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB_photo_test);
        textChoiceB.setText("B.Listen and choose.");
        RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC_photo_test);
        textChoiceC.setText("C.Listen and choose.");
        RadioButton textChoiceD = (RadioButton) findViewById(R.id.rdoD_photo_test);
        textChoiceD.setText("D.Listen and choose.");

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBackTest1);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showanswer = false;
                if (Global.currentposition == 0) {
                    return;
                }
                Global.currentposition--;
                if (Global.currentposition>=0) {

                    String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                    //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum);
                    txtNo.setText(strNumber);

                }

                showpicture();
                playSound();

                //createlistview(currentposition);

                //clearcheck();

                //playSound(currentposition);

                //clearlayout();

                //showanswer(currentposition);
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNextTest1);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.currentposition++;

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(PhotosTestActivity.this, QandrTestActivity.class);
                    startActivity(intent);
                    sumScore();
                } else {

                    String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                        //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum);
                    txtNo.setText(strNumber);

                    showpicture();

                    playSound();
                    clearcheck();


                }

            }
        });

    }

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroup1_photo_test);
        rdogroup1.clearCheck();
    }




    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    private String[] listChoiceA(){

        String strListChoiceA[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, new String[]{COLUMN_PHOTO_CHOICE_A_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceA = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_A_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceA;
    }

    private String[] listChoiceB(){

        String strListChoiceB[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, new String[]{COLUMN_PHOTO_CHOICE_B_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceB = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_B_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC(){

        String strListChoiceC[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, new String[]{COLUMN_PHOTO_CHOICE_C_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_C_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD(){

        String strListChoiceD[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, new String[]{COLUMN_PHOTO_CHOICE_D_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceD = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_D_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceD;

    }

    private String[] listAnswer(){

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION_TEST, new String[]{COLUMN_PHOTO_ANSWER_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;

    }

}
