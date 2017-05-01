package com.xyz.practiceandlearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.xyz.practiceandlearn.Global.BaseDir;
import static com.xyz.practiceandlearn.Global.basedir;
import static com.xyz.practiceandlearn.Global.basedirPhoto;
import static com.xyz.practiceandlearn.Global.basedirSound;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_ID_QANDR_CHOICE;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_ID_QANDR_QUESTION;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_ANSWER;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_CHOICE_A;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_CHOICE_B;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_CHOICE_C;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_DES;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.COLUMN_QANDR_QUESTION;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.QUESTIONANDRESPONSE_CHOICE;
import static com.xyz.practiceandlearn.QuestionAndResponseDatabase.QuestionAndResponse_Question;

public class QandrPracticeActivity extends AppCompatActivity {


    MyDatabase objMyDatabase;
    private String sql = COLUMN_ID_QANDR_QUESTION + "<=30";
    private String sql2 = COLUMN_ID_QANDR_CHOICE + "<=30";
    private static String[] strQuestion, strChoiceA, strChoiceB, strChoiceC, strChoiceDes, strAnswer;
    private boolean showanswer;
    private int currentposition;
    private MediaPlayer mPlayer,soundtrue,soundwrong;
    private static final int maxrow = 29;
    private int[] mySound = {R.raw.qandr1,R.raw.qandr2,R.raw.qandr3,R.raw.qandr4,R.raw.qandr5,R.raw.qandr6,R.raw.qandr7,R.raw.qandr8,R.raw.qandr9,R.raw.qandr10,
            R.raw.qandr11,R.raw.qandr12,R.raw.qandr13,R.raw.qandr14,R.raw.qandr15,R.raw.qandr16,R.raw.qandr17,R.raw.qandr18,R.raw.qandr19,R.raw.qandr20,
            R.raw.qandr21,R.raw.qandr22,R.raw.qandr23,R.raw.qandr24,R.raw.qandr25,R.raw.qandr26,R.raw.qandr27,R.raw.qandr28,R.raw.qandr29,R.raw.qandr30};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qandr_practice);

        File photoDB = new File(BaseDir + "/V1/TOEIC.db");
        File photoDB2 = new File(BaseDir + "/V1/V2/TOEIC2.db");

        if (photoDB2.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB2);
            Toast.makeText(getBaseContext(),"Database V.2",Toast.LENGTH_SHORT).show();
        } else if (photoDB.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB);
            Toast.makeText(getBaseContext(),"Database V.1",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(),"no Data base",Toast.LENGTH_SHORT).show();
            return;
        }

//        objMyDatabase = new MyDatabase(this, photoDB);

        currentposition = 0;
        showanswer = false;
        showanswer(0);

        playSound();

        setupArray();

        play();

        next();

        back();

        clearlayout();

        clearcheck();

    }

    private void play() {

        Button btnPlay = (Button) findViewById(R.id.btnPlay2);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playSound();
                //playSound(currentposition);

            }
        });

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showanswer = false;

                if (currentposition>0)
                    currentposition--;

                clearcheck();

                playSound();
                //playSound(currentposition);

                clearlayout();

                showanswer(currentposition);
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext2);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showanswer = false;

                if (currentposition<maxrow)
                    currentposition++;

                clearcheck();

                playSound();
                //playSound(currentposition);

                clearlayout();

                showanswer(currentposition);
            }
        });

    }

    private void playSound() {

        File photoDB = new File(basedirSound+ "/V1/AudioQandRPratice/");
        File photoDB2 = new File(basedirSound + "/V1/V2/AudioQandRPratice/");
        File filepath = null;

        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }

        if (photoDB2.exists()) {
            filepath = new File(basedirPhoto +"/V1/V2/AudioQandRPratice/"+String.valueOf(currentposition+1)+".mp3");
        } else if (photoDB.exists()) {

            filepath = new File(basedirPhoto +"/V1/AudioQandRPratice/"+String.valueOf(currentposition+1)+".mp3");

        } else {
            Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }
//        String filePath = basedir+"/V1/AudioQandRPratice/"+String.valueOf(currentposition+1)+".mp3";
        mPlayer = new MediaPlayer();


        try{
            mPlayer.setDataSource(String.valueOf(filepath));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void showanswer(final int p) {

        Button btnANS = (Button) findViewById(R.id.btnAns2);
        btnANS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!showanswer) {
                    TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion_QandR);
                    txtQuestion.setText(strQuestion[p]);
                    RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA2);
                    textChoiceA.setText(strChoiceA[p]);
                    RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB2);
                    textChoiceB.setText(strChoiceB[p]);
                    RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC2);
                    textChoiceC.setText(strChoiceC[p]);
                    TextView textAnswerA = (TextView) findViewById(R.id.txtDes);
                    textAnswerA.setText(strChoiceDes[p]);

                    Boolean chkA = textChoiceA.isChecked();
                    Boolean chkB = textChoiceB.isChecked();
                    Boolean chkC = textChoiceC.isChecked();
                    if (strAnswer[currentposition].equals("A")){

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
                    if (strAnswer[currentposition].equals("B")){

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
                    if (strAnswer[currentposition].equals("C")){

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


                }else {
                    clearlayout();
                }
                showanswer = !showanswer;

            }
        });

    }

    private void setupArray(){

        strQuestion = listQuestion();
        strChoiceA = listChoiceA();
        strChoiceB = listChoiceB();
        strChoiceC = listChoiceC();
        strChoiceDes = listChoiceDes();
        strAnswer = listAnswer();

    }

    private void clearcheck() {

        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroup2);
        rdogroup2.clearCheck();
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the test?")
                .setMessage("Are you sure you want to quit to test?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(QandrPracticeActivity.this, MainActivity.class);
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

    private void clearlayout(){

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion_QandR);
        txtQuestion.setText("Listen to a question and choose the best response.");
        RadioButton rdoA = (RadioButton) findViewById(R.id.rdoA2);
        rdoA.setText("A.Listen and choose.");
        RadioButton rdoB = (RadioButton) findViewById(R.id.rdoB2);
        rdoB.setText("B.Listen and choose.");
        RadioButton rdoC = (RadioButton) findViewById(R.id.rdoC2);
        rdoC.setText("C.Listen and choose.");
        TextView txtChoiceDes = (TextView) findViewById(R.id.txtDes);
        txtChoiceDes.setText("");

    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    private String[] listQuestion(){

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("QuestionAndResponse_Question WHERE COLUMN_QANDR_QUESTION", new String[]{"COLUMN_QANDR_QUESTION", null, null, null, null, null});
        Cursor objCursor = db.query(QuestionAndResponse_Question, new String[]{COLUMN_QANDR_QUESTION}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListQuestion = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListQuestion[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_QUESTION"));
            strListQuestion[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_QUESTION));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListQuestion;
    }

    private String[] listAnswer(){

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("QuestionAndResponse_Question WHERE COLUMN_QANDR_ANSWER", new String[]{"COLUMN_QANDR_ANSWER", null, null, null, null, null});
        Cursor objCursor = db.query(QuestionAndResponse_Question, new String[]{COLUMN_QANDR_ANSWER}, sql, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_ANSWER"));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_ANSWER));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;
    }

    private String[] listChoiceA(){

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM QUESTIONANDRESPONSE_CHOICE WHERE COLUMN_QANDR_CHOICE_A", new String[]{"COLUMN_QANDR_CHOICE_A", null, null, null, null, null});
        Cursor objCursor = db.query(QUESTIONANDRESPONSE_CHOICE, new String[]{COLUMN_QANDR_CHOICE_A}, sql2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceA = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_CHOICE_A"));
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_CHOICE_A));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB(){

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM QUESTIONANDRESPONSE_CHOICE WHERE COLUMN_QANDR_CHOICE_B", new String[]{"COLUMN_QANDR_CHOICE_B", null, null, null, null, null});
        Cursor objCursor = db.query(QUESTIONANDRESPONSE_CHOICE, new String[]{COLUMN_QANDR_CHOICE_B}, sql2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceB = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_CHOICE_B"));
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_CHOICE_B));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC(){

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("QUESTIONANDRESPONSE_CHOICE WHERE COLUMN_QANDR_CHOICE_C", new String[]{"COLUMN_QANDR_CHOICE_C", null, null, null, null, null});
        Cursor objCursor = db.query(QUESTIONANDRESPONSE_CHOICE, new String[]{COLUMN_QANDR_CHOICE_C}, sql2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_CHOICE_C"));
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_CHOICE_C));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceDes(){

        String strListChoiceDes[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM QUESTIONANDRESPONSE_CHOICE WHERE COLUMN_QANDR_DES", new String[]{"COLUMN_QANDR_DES", null, null, null, null, null});
        Cursor objCursor = db.query(QUESTIONANDRESPONSE_CHOICE, new String[]{COLUMN_QANDR_DES}, sql2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceDes = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_QANDR_DES"));
            strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_QANDR_DES));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceDes;

    }

}
