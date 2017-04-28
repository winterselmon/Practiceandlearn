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
import android.widget.ScrollView;
import android.widget.TextView;

import static com.xyz.practiceandlearn.Global.basedir;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_ANSWER;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_A;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_B;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_C;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_CHOICE_D;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_DES;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_QUESTION;
import static com.xyz.practiceandlearn.ShortTalkDatabase.COLUMN_SHORTTALK_SCRIPT;
import static com.xyz.practiceandlearn.ShortTalkDatabase.SHORTTALK_QUESTION;
import static com.xyz.practiceandlearn.ShortTalkDatabase.SHORTTALK_SCRIPT;

public class ShortTalkActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private String[] strScript, strQuestion, strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD, strDes;
    private int currentposition;
    private static final int maxrow = 9;
    private boolean showScript;
    private boolean showAnswer;
    private MediaPlayer mPlayer;
    //private int[] mySound = {R.raw.short1, R.raw.short2, R.raw.short3, R.raw.short4, R.raw.short5, R.raw.short6, R.raw.short7, R.raw.short8, R.raw.short9, R.raw.short10,
            //R.raw.short11,R.raw.short12,R.raw.short13,R.raw.short14,R.raw.short15,R.raw.short16,R.raw.short17,R.raw.short18,R.raw.short19,R.raw.short20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_talk);

        objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");

        currentposition = 0;

        showScript = false;

        showAnswer = false;

        ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk);

        scrollView.smoothScrollTo(0,0);

        setupArray();

        showArray();

        showScript();

        showAnswer();

        back();

        next();

        clearcheck();

    }

    private void setupArray() {

        strScript = listScript();
        strQuestion = listQuestion();
        strAnswer = listAnswer();
        strChoiceA = listChoiceA();
        strChoiceB = listChoiceB();
        strChoiceC = listChoiceC();
        strChoiceD = listChoiceD();
        strDes = listChoiceDes();

    }

    private void showArray() {

        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestion1_shortTalk);
        txtQuestion1.setText(strQuestion[currentposition*3]);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_shortTalk);
        txtQuestion2.setText(strQuestion[(currentposition*3)+1]);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_shortTalk);
        txtQuestion3.setText(strQuestion[(currentposition*3)+2]);

        //Choice1
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_shortTalk);
        rdoA1.setText("A."+strChoiceA[currentposition*3]);
        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_shortTalk);
        rdoB1.setText("B."+strChoiceB[currentposition*3]);
        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_shortTalk);
        rdoC1.setText("C."+strChoiceC[currentposition*3]);
        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_shortTalk);
        rdoD1.setText("D."+strChoiceD[currentposition*3]);

        //Choice2
        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_shortTalk);
        rdoA2.setText("A."+strChoiceA[currentposition*3+1]);
        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_shortTalk);
        rdoB2.setText("B."+strChoiceB[currentposition*3+1]);
        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_shortTalk);
        rdoC2.setText("C."+strChoiceC[currentposition*3+1]);
        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_shortTalk);
        rdoD2.setText("D."+strChoiceD[currentposition*3+1]);

        //Choice3
        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_shortTalk);
        rdoA3.setText("A."+strChoiceA[currentposition*3+2]);
        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_shortTalk);
        rdoB3.setText("B."+strChoiceB[currentposition*3+2]);
        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_shortTalk);
        rdoC3.setText("C."+strChoiceC[currentposition*3+2]);
        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_shortTalk);
        rdoD3.setText("D."+strChoiceD[currentposition*3+2]);

    }

    private void showScript() {

        Button btnScript = (Button) findViewById(R.id.btnScriptShortTalk);
        btnScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showScript) {
                    TextView txtScript = (TextView) findViewById(R.id.txtScriptShortTalk);
                    txtScript.setText(strScript[currentposition]);
                } else {
                    TextView txtScript = (TextView) findViewById(R.id.txtScriptShortTalk);
                    txtScript.setText("");
                }
                showScript = !showScript;

            }
        });

    }

    private void showAnswer() {

        Button btnAns = (Button) findViewById(R.id.btnAns4);
        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showAnswer) {

                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_ShortTalk);
                    txtAns1.setText(strDes[currentposition * 3]);
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_ShortTalk);
                    txtAns2.setText(strDes[currentposition * 3 + 1]);
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_ShortTalk);
                    txtAns3.setText(strDes[currentposition * 3 + 2]);

                } else {
                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_ShortTalk);
                    txtAns1.setText("");
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_ShortTalk);
                    txtAns2.setText("");
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_ShortTalk);
                    txtAns3.setText("");
                }
                showAnswer = !showAnswer;

            }
        });

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk);

                scrollView.smoothScrollTo(0,0);

                showAnswer = false;

                if (currentposition>0)
                    currentposition--;



                clearcheck();

                TextView txtScript = (TextView) findViewById(R.id.txtScriptShortTalk);
                txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_ShortTalk);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_ShortTalk);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_ShortTalk);
                txtAns3.setText("");

                //clearlayout();

                //playSound(currentposition);

                showScript();

                showAnswer();

                showArray();
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext4);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_talk);

                scrollView.smoothScrollTo(0,0);

                showAnswer = false;

                if (currentposition<maxrow)
                    currentposition++;



                clearcheck();

                TextView txtScript = (TextView) findViewById(R.id.txtScriptShortTalk);
                txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_ShortTalk);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_ShortTalk);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_ShortTalk);
                txtAns3.setText("");

                //clearlayout();

                //playSound(currentposition);

                showScript();

                showAnswer();

                showArray();
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
                        Intent intent = new Intent(ShortTalkActivity.this, MainActivity.class);
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

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroupAShortTalk);
        rdogroup1.clearCheck();
        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroupBShortTalk);
        rdogroup2.clearCheck();
        RadioGroup rdogroup3 = (RadioGroup) findViewById(R.id.rdoGroupCShortTalk);
        rdogroup3.clearCheck();
    }

    private void playSound(int p) {
        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }


        //mPlayer = MediaPlayer.create(this,mySound[p]);
        mPlayer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //mPlayer.stop();
    }

    private String[] listScript() {

        String strListScript[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SHORTTALK_SCRIPT WHERE COLUMN_SHORTTALK_SCRIPT", new String[]{"COLUMN_SHORTTALK_SCRIPT", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_SCRIPT, new String[]{COLUMN_SHORTTALK_SCRIPT}, null, null, null, null, null);
        cursor.moveToFirst();
        strListScript = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListScript[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_SCRIPT"));
            strListScript[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_SCRIPT));
            cursor.moveToNext();
        }
        cursor.close();

        return strListScript;

    }

    private String[] listQuestion() {

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_QUESTION", new String[]{"COLUMN_SHORTTALK_QUESTION", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_QUESTION}, null, null, null, null, null);
        cursor.moveToFirst();
        strListQuestion = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListQuestion[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_QUESTION"));
            strListQuestion[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_QUESTION));
            cursor.moveToNext();
        }
        cursor.close();

        return strListQuestion;

    }

    private String[] listAnswer() {

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_ANSWER", new String[]{"COLUMN_SHORTTALK_ANSWER", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_ANSWER}, null, null, null, null, null);
        cursor.moveToFirst();
        strListAnswer = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListAnswer[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_ANSWER"));
            strListAnswer[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_ANSWER));
            cursor.moveToNext();
        }
        cursor.close();

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_CHOICE_A", new String[]{"COLUMN_SHORTTALK_CHOICE_A", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_CHOICE_A}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceA = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceA[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_CHOICE_A"));
            strListChoiceA[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_A));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_CHOICE_B", new String[]{"COLUMN_SHORTTALK_CHOICE_B", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_CHOICE_B}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceB = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceB[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_CHOICE_B"));
            strListChoiceB[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_B));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_CHOICE_C", new String[]{"COLUMN_SHORTTALK_CHOICE_C", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_CHOICE_C}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceC = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceC[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_CHOICE_C"));
            strListChoiceC[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_C));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT *FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_CHOICE_D", new String[]{"COLUMN_SHORTTALK_CHOICE_D", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_CHOICE_D}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceD = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceD[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_CHOICE_D"));
            strListChoiceD[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_CHOICE_D));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceD;

    }

    private String[] listChoiceDes() {

        String strListChoiceDes[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM SHORTTALK_QUESTION WHERE COLUMN_SHORTTALK_DES", new String[]{"COLUMN_SHORTTALK_DES", null, null, null, null, null});
        Cursor cursor = db.query(SHORTTALK_QUESTION, new String[]{COLUMN_SHORTTALK_DES}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceDes = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceDes[i] = cursor.getString(cursor.getColumnIndex("COLUMN_SHORTTALK_DES"));
            strListChoiceDes[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTTALK_DES));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceDes;

    }


}
