package com.xyz.practiceandlearn;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
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

import static com.xyz.practiceandlearn.Global.basedirPhoto;
import static com.xyz.practiceandlearn.Global.basedirSound;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE_TEST;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;


import static com.xyz.practiceandlearn.Global.basedir;
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
//import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_DES;
//import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE;
//import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE_TEST;
//import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION;
//import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;

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

//        String photoDb2 = basedir.toString() + "V2/TOEIC.db";
//        String photoDb = basedir.toString() + "V1/TOEIC.db";
        File photoDB = new File(basedirSound+ "/V1/TOEIC.db");
        File photoDB2 = new File(basedirSound + "/V1/V2/TOEIC2.db");

        if (photoDB2.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB2);
            Toast.makeText(getBaseContext(),"hi",Toast.LENGTH_SHORT).show();
        } else if (photoDB.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB);
            Toast.makeText(getBaseContext(),"hi2",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }


//        objMyDatabase = new MyDatabase(this, photoDb );

        //Global.currentposition;

        currentpage = 0;

        String strNumber = String.valueOf(Global.currentposition + 1) + "/10";

        //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);

        TextView txtNo = (TextView) findViewById(R.id.txtnum);
        txtNo.setText(strNumber);


        txttime = (TextView) findViewById(R.id.txttime);
        //if (Global.startTime == 0)
            //Global.startTime = System.currentTimeMillis();

        timerHandler.postDelayed(timerRunnable, 0);

        RadioButton rdoA = (RadioButton) findViewById(R.id.rdoA_photo_test);
        rdoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_photo_test);
        rdoB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_photo_test);
        rdoC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_photo_test);
        rdoD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        setupArrar();

        clearlayout();

        clearcheck();

        next();

        back();

        showpicture();

        playSound();

    }


    private void collectpoint() {

        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_photo_test);
        if (rdoA1.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("A")) {

                Global.collect[Global.currentAnswer] = true;
                Toast.makeText(getBaseContext(),"True",Toast.LENGTH_LONG).show();

            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_photo_test);
        if (rdoB1.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("B")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_photo_test);
        if (rdoC1.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("C")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_photo_test);
        if (rdoD1.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("D")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }
    }


    private void showpicture() {

        File photoDB = new File(basedirSound+ "/V1/photoTest/");
        File photoDB2 = new File(basedirSound + "/V1/V2/photoTest/");
        File imgFile = null;

        if (photoDB2.exists()) {
            imgFile = new File(basedirPhoto +"/V1/V2/photoTest/"+String.valueOf(Global.currentposition+1)+".png");
        } else if (photoDB.exists()) {

            imgFile = new File(basedirPhoto +"/V1/photoTest/"+String.valueOf(Global.currentposition+1)+".png");

        } else {
            Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }

//        File imgFile = new File(basedirPhoto +"/V1/photoTest/"+String.valueOf(Global.currentposition+1)+".png");



        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView img = (ImageView) findViewById(R.id.imgPhotosTest);
            //img.setBackgroundResource();

            img.setImageBitmap(myBitmap);

        }

    }

    private void playSound() {
        File photoDB = new File(basedirSound+ "/V1/AudioPhoto/");
        File photoDB2 = new File(basedirSound + "/V1/V2/AudioPhoto/");
        File filepath = null;

        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }

        if (photoDB2.exists()) {
            filepath = new File(basedirPhoto +"/V1/V2/AudioPhoto/"+String.valueOf(Global.currentposition+1)+".mp3");
        } else if (photoDB.exists()) {

            filepath = new File(basedirPhoto +"/V1/AudioPhoto/"+String.valueOf(Global.currentposition+1)+".mp3");

        } else {
            Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }

//        String filePath = basedirSound +"/V1/AudioPhoto/"+String.valueOf(Global.currentSound+1)+".mp3";
        mPlayer = new MediaPlayer();


        try {
            if (!Global.played[Global.currentSound]) {
                mPlayer.setDataSource(String.valueOf(filepath));
                mPlayer.prepare();
                mPlayer.start();
                Global.played[Global.currentSound] = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupArrar() {

        //Arrays.fill(Global.played, false);
        //Arrays.fill(Global.collect,false);

        //Toast.makeText(getBaseContext(),"setup array",Toast.LENGTH_LONG).show();
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
                if (Global.currentAnswer == 0) {
                    return;
                }
                Global.currentAnswer--;
                Global.currentposition--;
                Global.currentSound--;
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

    private void sumScore() {
        int score = 0;
        for (int i = 0; i < 200; i++) {
            if (Global.collect[i])
                score++;
        }
        String S = Integer.toString(score);
        Toast.makeText(getBaseContext(), "Score = " + S, Toast.LENGTH_LONG).show();
    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNextTest1);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.currentposition++;
                Global.currentAnswer++;
                Global.currentSound++;

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(PhotosTestActivity.this, DescriptionQandRActivity.class);
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

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the test?")
                .setMessage("Are you sure you want to quit to test?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PhotosTestActivity.this, MainActivity.class);
                        mPlayer.stop();
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    private String[] listChoiceA(){


        String strListChoiceA[] = null;
        //Toast.makeText(getBaseContext(),"before getRead",Toast.LENGTH_LONG).show();
        //SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        try {
            //Toast.makeText(getBaseContext(),"before getRead",Toast.LENGTH_LONG).show();
            SQLiteDatabase db = objMyDatabase.getReadableDatabase();

            //Toast.makeText(getBaseContext(),"after getRead",Toast.LENGTH_LONG).show();
            Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST , new String[]{COLUMN_PHOTO_CHOICE_A_TEST},null, null, null, null, null,null);
            //Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, new String[]{COLUMN_PHOTO_CHOICE_A_TEST}, null, null, null, null, null);
            //Toast.makeText(getBaseContext(),"after query",Toast.LENGTH_LONG).show();
            objCursor.moveToFirst();
            //Toast.makeText(getBaseContext(),"after movetofrist",Toast.LENGTH_LONG).show();
            strListChoiceA = new String[objCursor.getCount()];
            //Toast.makeText(getBaseContext(),strListChoiceA,Toast.LENGTH_LONG).show();
            for (int i=0; i<objCursor.getCount(); i++){
                strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_A_TEST));
                objCursor.moveToNext();
            }   // for
            objCursor.close();


        }catch(SQLException sqle){
            throw sqle;
        }
        return strListChoiceA;
    }

    private String[] listChoiceB(){

        //String TABLE_NAME = "PHOTOGRAPHS_CHOICE_TEST";
        //String COL_NAME = "COLUMN_PHOTO_CHOICE_B_TEST";
        String strListChoiceB[] = null;
        try {
            //Toast.makeText(getBaseContext(), "before getRead B", Toast.LENGTH_LONG).show();
            SQLiteDatabase db = objMyDatabase.getReadableDatabase();
            //Toast.makeText(getBaseContext(), "after getRead B", Toast.LENGTH_LONG).show();
            Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, null, null, null, null, null, COLUMN_PHOTO_CHOICE_B_TEST, null);
            //Toast.makeText(getBaseContext(), "after query B", Toast.LENGTH_LONG).show();
            //Cursor objCursor = db.rawQuery("SELECT" + COL_NAME + "FROM" + TABLE_NAME ,new String[]{COL_NAME, null, null, null, null, null});
            objCursor.moveToFirst();
            //Toast.makeText(getBaseContext(), "after movetofrist B", Toast.LENGTH_LONG).show();
            strListChoiceB = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {
                //strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COL_NAME));
                strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_B_TEST));
                objCursor.moveToNext();
            }   // for
            objCursor.close();

        }catch (SQLException sqle){
            throw sqle;
        }

        return strListChoiceB;

    }

    private String[] listChoiceC(){

        //String TABLE_NAME = "PHOTOGRAPHS_CHOICE_TEST";
        //String COL_NAME = "COLUMN_PHOTO_CHOICE_C_TEST";
        String strListChoiceC[] = null;
        try {

        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST,null ,null, null, null, null,COLUMN_PHOTO_CHOICE_C_TEST, null);
        //Cursor objCursor = db.rawQuery("SELECT" + COL_NAME + "FROM" + TABLE_NAME ,new String[]{COL_NAME, null, null, null, null, null});
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COL_NAME));
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_C_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        }catch (SQLException sqle){
            throw sqle;
        }

        return strListChoiceC;

    }

    private String[] listChoiceD(){

        //String TABLE_NAME = "PHOTOGRAPHS_CHOICE_TEST";
        //String COL_NAME = "COLUMN_PHOTO_CHOICE_D_TEST";

        String strListChoiceD[] = null;
        try{

        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE_TEST, null, null, null, null, null,COLUMN_PHOTO_CHOICE_D_TEST,null);
        //Cursor objCursor = db.rawQuery("SELECT" + COL_NAME + "FROM" + TABLE_NAME ,new String[]{COL_NAME, null, null, null, null, null});
        objCursor.moveToFirst();
        strListChoiceD = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COL_NAME));
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_D_TEST));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

    }catch (SQLException sqle){
        throw sqle;
    }

        return strListChoiceD;

    }

    private String[] listAnswer(){

        //String TABLE_NAME = "PHOTOGRAPHS_CHOICE_TEST";
        //String COL_NAME = "COLUMN_PHOTO_ANSWER_TEST";

        String strListAnswer[];
        try {
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION_TEST, new String[]{COLUMN_PHOTO_ANSWER_TEST}, null, null, null, null,null);
        //Cursor objCursor = db.rawQuery("SELECT" + COL_NAME + "FROM" + TABLE_NAME ,new String[]{COL_NAME, null, null, null, null, null});
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COL_NAME));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        }catch(SQLException sqle){
            throw sqle;
        }

        return strListAnswer;

    }
}
