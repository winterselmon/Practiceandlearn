package com.xyz.practiceandlearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
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

import static com.xyz.practiceandlearn.Global.basedir;

import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_ID_PHOTO_CHOICE;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_ID_PHOTO_QUESTION;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_ANSWER;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_A;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_B;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_C;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_CHOICE_D;
import static com.xyz.practiceandlearn.PhotoDatabase.COLUMN_PHOTO_DES;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_CHOICE;
import static com.xyz.practiceandlearn.PhotoDatabase.PHOTOGRAPHS_QUESTION;

public class PhotosPracticeActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private int currentposition;
    private boolean showanswer;
    private static final int maxrow = 39;
    //private static String[] COLUMNS = {COLUMN_PHOTO_CHOICE_A, COLUMN_PHOTO_CHOICE_B, COLUMN_PHOTO_CHOICE_C, COLUMN_PHOTO_CHOICE_D,};
    private static String sql = COLUMN_ID_PHOTO_CHOICE + "<=40";
    private static String sql2 = COLUMN_ID_PHOTO_QUESTION + "<=40";
    private static String[] strListChoiceA, strListChoiceB, strListChoiceC, strListChoiceD, strListDes, strListAnswer;
    int[] myTarget = {R.drawable.photo1,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.photo5,R.drawable.photo6,R.drawable.photo7,R.drawable.photo8,R.drawable.photo9,R.drawable.photo10,
            R.drawable.photo11,R.drawable.photo12,R.drawable.photo13,R.drawable.photo14,R.drawable.photo15,R.drawable.photo16,R.drawable.photo17,R.drawable.photo18,R.drawable.photo19,R.drawable.photo20,
            R.drawable.photo21,R.drawable.photo22,R.drawable.photo23,R.drawable.photo24,R.drawable.photo25,R.drawable.photo26,R.drawable.photo27,R.drawable.photo28,R.drawable.photo29,R.drawable.photo30,
            R.drawable.photo31,R.drawable.photo32,R.drawable.photo33,R.drawable.photo34,R.drawable.photo35,R.drawable.photo36,R.drawable.photo37,R.drawable.photo38,R.drawable.photo39,R.drawable.photo40};
    private MediaPlayer mPlayer,soundtrue,soundwrong;
    int[] mySound = {R.raw.audiophoto1,R.raw.audiophoto2,R.raw.audiophoto3,R.raw.audiophoto4,R.raw.audiophoto5,R.raw.audiophoto6,R.raw.audiophoto7,R.raw.audiophoto8,R.raw.audiophoto9,R.raw.audiophoto10,
            R.raw.audiophoto11,R.raw.audiophoto12,R.raw.audiophoto13,R.raw.audiophoto14,R.raw.audiophoto15,R.raw.audiophoto16,R.raw.audiophoto17,R.raw.audiophoto18,R.raw.audiophoto19,R.raw.audiophoto20,
            R.raw.audiophoto21,R.raw.audiophoto22,R.raw.audiophoto23,R.raw.audiophoto24,R.raw.audiophoto25,R.raw.audiophoto26,R.raw.audiophoto27,R.raw.audiophoto28,R.raw.audiophoto29,R.raw.audiophoto30,
            R.raw.audiophoto31,R.raw.audiophoto32,R.raw.audiophoto33,R.raw.audiophoto34,R.raw.audiophoto35,R.raw.audiophoto36,R.raw.audiophoto37,R.raw.audiophoto38,R.raw.audiophoto39,R.raw.audiophoto40};
    String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_practice);

        objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");

        //currentposition = 0;
        showanswer = false;
        showanswer(0);

        playSound();

        setupArrar();

        createlistview(0);

        play();

        next();

        back();

        clearlayout();

        clearcheck();


    }

    private void play() {

        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playSound();
                //playSound(currentposition);

            }
        });

    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the test?")
                .setMessage("Are you sure you want to quit to test?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PhotosPracticeActivity.this, MainActivity.class);
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


    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showanswer = false;

                if (currentposition>0)
                    currentposition--;

                createlistview(currentposition);

                clearcheck();

                playSound();
                //playSound(currentposition);

                clearlayout();

                showanswer(currentposition);
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showanswer = false;



                if (currentposition<maxrow)
                    currentposition++;

                    createlistview(currentposition);

                    clearcheck();

                    playSound();
                    //playSound(currentposition);

                    clearlayout();
                    showanswer(currentposition);

            }
        });

    }

    private void playSound() {
        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }

        String filePath = basedir+"/V1/AudioPhotoPratice/"+String.valueOf(currentposition+1)+".mp3";
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mPlayer = MediaPlayer.create(this,mySound[p]);
        //mPlayer.start();

    }

    private void playTrue(){
        String filePath = basedir+"/V1/soundtrue.mp3";
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playWrong(){
        String filePath = basedir+"/V1/soundwrong.mp3";
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearlayout() {

        RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA);
        textChoiceA.setText("A.Listen and choose.");
        RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB);
        textChoiceB.setText("B.Listen and choose.");
        RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC);
        textChoiceC.setText("C.Listen and choose.");
        RadioButton textChoiceD = (RadioButton) findViewById(R.id.rdoD);
        textChoiceD.setText("D.Listen and choose.");
        TextView textAnswerA = (TextView) findViewById(R.id.txtAnswerA);
        textAnswerA.setText("");
    }

    private void showanswer(final int p) {

        Button btnANS = (Button) findViewById(R.id.btnAns);
        btnANS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!showanswer) {
                    RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA);
                    textChoiceA.setText(strListChoiceA[p]);
                    RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB);
                    textChoiceB.setText(strListChoiceB[p]);
                    RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC);
                    textChoiceC.setText(strListChoiceC[p]);
                    RadioButton textChoiceD = (RadioButton) findViewById(R.id.rdoD);
                    textChoiceD.setText(strListChoiceD[p]);
                    TextView textAnswerA = (TextView) findViewById(R.id.txtAnswerA);
                    textAnswerA.setText(strListDes[p]);

                    Boolean chkA = textChoiceA.isChecked();
                    Boolean chkB = textChoiceB.isChecked();
                    Boolean chkC = textChoiceC.isChecked();
                    Boolean chkD = textChoiceD.isChecked();
                    if (strListAnswer[currentposition].equals("a")){

                        if (chkA){
                            //playSound
                            playTrue();
                            //Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        }
                        else {

                            playWrong();
                            //Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("b")){

                        if (chkB){
                            //playSound
                            playTrue();
                            //Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        }
                        else {

                            playWrong();
                            //Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("c")){

                        if (chkC){
                            //playSound
                            playTrue();
                            //Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        }
                        else {

                            playWrong();
                            //Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("d")){

                        if (chkD){
                            //playSound
                            playTrue();
                            //Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        }
                        else {

                            playWrong();
                            //Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }


                }else {
                    clearlayout();
                }
                showanswer = !showanswer;

            }
        });

    }

    private void setupArrar() {

        strListChoiceA = listChoiceA();
        strListChoiceB = listChoiceB();
        strListChoiceC = listChoiceC();
        strListChoiceD = listChoiceD();
        strListDes = listChoiceDes();
        strListAnswer = listAnswer();

    }   //setup Arrar

    private void createlistview(int p) {

        File imgFile = new File(basedir + "/V1/PhotoPratice/" + String.valueOf(currentposition + 1) + ".png");

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imglist = (ImageView) findViewById(R.id.imgPhotos);
            imglist.setImageBitmap(myBitmap);

        }
    }

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroup1);
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
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_A", new String[]{"COLUMN_PHOTO_CHOICE_A", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_A}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceA = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_A"));
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_A));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceA;
    }

    private String[] listChoiceB(){

        String strListChoiceB[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_B", new String[]{"COLUMN_PHOTO_CHOICE_B", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_B}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceB = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_B"));
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_B));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC(){

        String strListChoiceC[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_C", new String[]{"COLUMN_PHOTO_CHOICE_C",null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_C}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_C"));
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_C));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD(){

        String strListChoiceD[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_D", new String[]{"COLUMN_PHOTO_CHOICE_D", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_D}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceD = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_D"));
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_D));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceD;

    }

    private String[] listChoiceDes(){

        String strListChoiceDes[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_DES", new String[]{"COLUMN_PHOTO_DES",null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_DES}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceDes = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_DES"));
            strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_DES));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceDes;

    }

    private String[] listAnswer(){

        String strListAnswer[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_QUESTION WHERE COLUMN_PHOTO_ANSWER", new String[]{"COLUMN_PHOTO_ANSWER",null,null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION, new String[]{COLUMN_PHOTO_ANSWER}, sql2, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_ANSWER"));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;

    }
}


