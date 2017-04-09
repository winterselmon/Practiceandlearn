package com.xyz.practiceandlearn;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;


import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_ID_SHORTCONVERSATION_QUESTION;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_ID_SHORTCONVERSATION_SCRIPT;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_ANSWER;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_CHOICE_A;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_CHOICE_B;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_CHOICE_C;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_CHOICE_D;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_DES;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_QUESTION;
import static com.xyz.practiceandlearn.ShortConDatabase.COLUMN_SHORTCONVERSATION_SCRIPT;
import static com.xyz.practiceandlearn.ShortConDatabase.SHORTCONVERSATION_QUESTION;
import static com.xyz.practiceandlearn.ShortConDatabase.SHORTCONVERSATION_SCRIPT;

public class ShortConversationActivity extends AppCompatActivity {

    private MyDatabase objMyDatabse;
    private String sql = COLUMN_ID_SHORTCONVERSATION_SCRIPT + "<=20";
    private String sql2 = COLUMN_ID_SHORTCONVERSATION_QUESTION + "<=60";
    private String[] strScript, strQuestion,strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD, strDes;
    private int currentposition;
    private static final int maxrow = 9;
    private boolean showScript;
    private boolean showAnswer;
    private MediaPlayer mPlayer;
    private int[] mySound = {R.raw.short1, R.raw.short2, R.raw.short3, R.raw.short4, R.raw.short5, R.raw.short6, R.raw.short7, R.raw.short8, R.raw.short9, R.raw.short10,
            R.raw.short11,R.raw.short12,R.raw.short13,R.raw.short14,R.raw.short15,R.raw.short16,R.raw.short17,R.raw.short18,R.raw.short19,R.raw.short20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_conversation);

        ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_conversation);

        scrollView.setFocusable(false);

        objMyDatabse = new MyDatabase(this);

        currentposition = 0;

        showScript = false;

        showAnswer = false;

        playSound(0);

        setupArray();

        showArray();

        showScript();

        showAnswer();

        back();

        next();

        clearcheck();


        //TextView txtScript = (TextView) findViewById(R.id.txtScript);
        //txtScript.setText(strScript[currentposition]);

        //TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestionA_shortc);
        //txtQuestion1.setText(strQuestion[currentposition*3]);
        //TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_shortc);
        //txtQuestion2.setText(strQuestion[(currentposition*3)+1]);
        //TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_shortc);
        //txtQuestion3.setText(strQuestion[(currentposition*3)+2]);



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

    private void showArray(){


        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestionA_shortc);
        txtQuestion1.setText(strQuestion[currentposition*3]);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_shortc);
        txtQuestion2.setText(strQuestion[(currentposition*3)+1]);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_shortc);
        txtQuestion3.setText(strQuestion[(currentposition*3)+2]);

        //Choice1
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_shortc);
        rdoA1.setText("A."+strChoiceA[currentposition*3]);
        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_shortc);
        rdoB1.setText("B."+strChoiceB[currentposition*3]);
        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_shortc);
        rdoC1.setText("C."+strChoiceC[currentposition*3]);
        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_shortc);
        rdoD1.setText("D."+strChoiceD[currentposition*3]);

        //Choice2
        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_shortc);
        rdoA2.setText("A."+strChoiceA[currentposition*3+1]);
        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_shortc);
        rdoB2.setText("B."+strChoiceB[currentposition*3+1]);
        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_shortc);
        rdoC2.setText("C."+strChoiceC[currentposition*3+1]);
        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_shortc);
        rdoD2.setText("D."+strChoiceD[currentposition*3+1]);

        //Choice3
        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_shortc);
        rdoA3.setText("A."+strChoiceA[currentposition*3+2]);
        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_shortc);
        rdoB3.setText("B."+strChoiceB[currentposition*3+2]);
        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_shortc);
        rdoC3.setText("C."+strChoiceC[currentposition*3+2]);
        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_shortc);
        rdoD3.setText("D."+strChoiceD[currentposition*3+2]);


    }

    private void showScript() {

        Button btnScript = (Button) findViewById(R.id.btnScript);
        btnScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showScript) {
                    TextView txtScript = (TextView) findViewById(R.id.txtScript);
                    txtScript.setText(strScript[currentposition]);
                } else {
                    TextView txtScript = (TextView) findViewById(R.id.txtScript);
                    txtScript.setText("");
                }
                showScript = !showScript;

            }
        });

    }

    private void showAnswer() {

        Button btnAns = (Button) findViewById(R.id.btnAns3);
        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showAnswer) {

                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1);
                    txtAns1.setText(strDes[currentposition * 4]);
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2);
                    txtAns2.setText(strDes[currentposition * 4 + 1]);
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3);
                    txtAns3.setText(strDes[currentposition * 4 + 2]);

                } else {
                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1);
                    txtAns1.setText("");
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2);
                    txtAns2.setText("");
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3);
                    txtAns3.setText("");
                }
                showAnswer = !showAnswer;

            }
        });

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_conversation);

                scrollView.smoothScrollTo(0,0);

                showAnswer = false;

                if (currentposition>0)
                    currentposition--;



                clearcheck();

                TextView txtScript = (TextView) findViewById(R.id.txtScript);
                txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3);
                txtAns3.setText("");

                //clearlayout();

                playSound(currentposition);

                showScript();

                showAnswer();

                showArray();
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext3);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_short_conversation);

                scrollView.smoothScrollTo(0,0);


                showAnswer = false;

                if (currentposition<maxrow)
                    currentposition++;



                clearcheck();

                TextView txtScript = (TextView) findViewById(R.id.txtScript);
                txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3);
                txtAns3.setText("");

                //clearlayout();

                playSound(currentposition);

                showScript();

                showAnswer();

                showArray();
            }
        });

    }

    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroupA);
        rdogroup1.clearCheck();
        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroupB);
        rdogroup2.clearCheck();
        RadioGroup rdogroup3 = (RadioGroup) findViewById(R.id.rdoGroupC);
        rdogroup3.clearCheck();
    }

    private void playSound(int p) {
        if (mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
        }


        mPlayer = MediaPlayer.create(this,mySound[p]);
        mPlayer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }


    private String[] listScript(){

        String strListScript[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_SCRIPT, new String[]{COLUMN_SHORTCONVERSATION_SCRIPT}, sql, null, null, null, null);
        cursor.moveToFirst();
        strListScript = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++){
            strListScript[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_SCRIPT));
            cursor.moveToNext();
        }
        cursor.close();

        return strListScript;

    }

    private String[] listQuestion(){

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_QUESTION}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListQuestion = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListQuestion[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_QUESTION));
            cursor.moveToNext();
        }
        cursor.close();

        return strListQuestion;

    }

    private String[] listAnswer() {

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_ANSWER}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListAnswer = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListAnswer[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_ANSWER));
            cursor.moveToNext();
        }
        cursor.close();

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_CHOICE_A}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceA = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceA[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_CHOICE_A));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_CHOICE_B}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceB = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceB[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_CHOICE_B));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_CHOICE_C}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceC = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceC[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_CHOICE_C));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_CHOICE_D}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceD = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceD[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_CHOICE_D));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceD;

    }

    private String[] listChoiceDes() {

        String strListChoiceDes[];
        SQLiteDatabase db = objMyDatabse.getReadableDatabase();
        Cursor cursor = db.query(SHORTCONVERSATION_QUESTION, new String[]{COLUMN_SHORTCONVERSATION_DES}, sql2, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceDes = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            strListChoiceDes[i] = cursor.getString(cursor.getColumnIndex(COLUMN_SHORTCONVERSATION_DES));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceDes;

    }
}
