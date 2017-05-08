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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.xyz.practiceandlearn.Global.BaseDir;
import static com.xyz.practiceandlearn.Global.basedir;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_ANSWER;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_CHOICE_A;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_CHOICE_B;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_CHOICE_C;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_CHOICE_D;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_DES;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_QUESTION;
import static com.xyz.practiceandlearn.TextCompletionDatabase.COLUMN_TEXTCOM_SCRIPT;
import static com.xyz.practiceandlearn.TextCompletionDatabase.TEXTCOMPLETION_QUESTION;
import static com.xyz.practiceandlearn.TextCompletionDatabase.TEXTCOMPLETION_SCRIPT;


public class TextCompletionActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private String[] strScript, strQuestion,strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD, strDes;
    private int currentposition;
    private static final int maxrow = 6;
    private boolean showAnswer;
    private MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_completion);

        File photoDB = new File(BaseDir+ "/V1/TOEIC.db");
        File photoDB2 = new File(BaseDir+ "/V1/V2/TOEIC2.db");

        if (photoDB2.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB2);
            //Toast.makeText(getBaseContext(),"Database V.2",Toast.LENGTH_SHORT).show();
        } else if (photoDB.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB);
            //Toast.makeText(getBaseContext(),"Database V.1",Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getBaseContext(),"no Data base",Toast.LENGTH_SHORT).show();
            return;
        }


//        objMyDatabase = new MyDatabase(this, photoDB);

        currentposition = 0;

        showAnswer = false;

        ScrollView scrollView = (ScrollView) findViewById(R.id.activity_text_completion);

        scrollView.smoothScrollTo(0,0);

        setupArray();

        showText();

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

    private void showText() {

        TextView txtScript = (TextView) findViewById(R.id.txtScriptTextCom);
        txtScript.setText(strScript[currentposition]);

        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestion1_TextCom);
        txtQuestion1.setText(strQuestion[currentposition*3]);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_TextCom);
        txtQuestion2.setText(strQuestion[(currentposition*3)+1]);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_TextCom);
        txtQuestion3.setText(strQuestion[(currentposition*3)+2]);

        //Choice1
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_TextCom);
        rdoA1.setText("A."+strChoiceA[currentposition*3]);
        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_TextCom);
        rdoB1.setText("B."+strChoiceB[currentposition*3]);
        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_TextCom);
        rdoC1.setText("C."+strChoiceC[currentposition*3]);
        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_TextCom);
        rdoD1.setText("D."+strChoiceD[currentposition*3]);

        //Choice2
        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_TextCom);
        rdoA2.setText("A."+strChoiceA[currentposition*3+1]);
        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_TextCom);
        rdoB2.setText("B."+strChoiceB[currentposition*3+1]);
        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_TextCom);
        rdoC2.setText("C."+strChoiceC[currentposition*3+1]);
        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_TextCom);
        rdoD2.setText("D."+strChoiceD[currentposition*3+1]);

        //Choice3
        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_TextCom);
        rdoA3.setText("A."+strChoiceA[currentposition*3+2]);
        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_TextCom);
        rdoB3.setText("B."+strChoiceB[currentposition*3+2]);
        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_TextCom);
        rdoC3.setText("C."+strChoiceC[currentposition*3+2]);
        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_TextCom);
        rdoD3.setText("D."+strChoiceD[currentposition*3+2]);

    }

    private void showAnswer() {

        Button btnAns = (Button) findViewById(R.id.btnAns6);
        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showAnswer) {

                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_TextCom);
                    txtAns1.setText(strDes[currentposition * 3]);
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_TextCom);
                    txtAns2.setText(strDes[currentposition * 3 + 1]);
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_TextCom);
                    txtAns3.setText(strDes[currentposition * 3 + 2]);

                } else {
                    TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_TextCom);
                    txtAns1.setText("");
                    TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_TextCom);
                    txtAns2.setText("");
                    TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_TextCom);
                    txtAns3.setText("");
                }
                showAnswer = !showAnswer;

            }
        });

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack6);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_text_completion);

                scrollView.smoothScrollTo(0,0);

                showAnswer = false;

                if (currentposition>0)
                    currentposition--;



                clearcheck();

                //TextView txtScript = (TextView) findViewById(R.id.txtScriptTextCom);
                //txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_TextCom);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_TextCom);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_TextCom);
                txtAns3.setText("");

                //clearlayout();

                //playSound(currentposition);

                //showScript();

                showAnswer();

                showText();
            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext6);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.activity_text_completion);

                scrollView.smoothScrollTo(0,0);

                showAnswer = false;

                if (currentposition<maxrow)
                    currentposition++;



                clearcheck();

                //TextView txtScript = (TextView) findViewById(R.id.txtScriptTextCom);
                //txtScript.setText("");
                TextView txtAns1 = (TextView) findViewById(R.id.txtDes1_TextCom);
                txtAns1.setText("");
                TextView txtAns2 = (TextView) findViewById(R.id.txtDes2_TextCom);
                txtAns2.setText("");
                TextView txtAns3 = (TextView) findViewById(R.id.txtDes3_TextCom);
                txtAns3.setText("");

                //clearlayout();

                //playSound(currentposition);

                //showScript();

                showAnswer();

                showText();
            }
        });

    }


    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the practice?")
                .setMessage("Are you sure you want to quit to practice?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TextCompletionActivity.this, MainActivity.class);
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

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroupA_TextCom);
        rdogroup1.clearCheck();
        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroupB_TextCom);
        rdogroup2.clearCheck();
        RadioGroup rdogroup3 = (RadioGroup) findViewById(R.id.rdoGroupC_TextCom);
        rdogroup3.clearCheck();
    }

    private String[] listScript() {

        String strListScript[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_SCRIPT WHERE COLUMN_TEXTCOM_SCRIPT", new String[]{"COLUMN_TEXTCOM_SCRIPT", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_SCRIPT, new String[]{COLUMN_TEXTCOM_SCRIPT}, null, null, null, null, null);
        cursor.moveToFirst();
        strListScript = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListScript[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_SCRIPT"));
            strListScript[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_SCRIPT));
            cursor.moveToNext();
        }
        cursor.close();

        return strListScript;

    }

    private String[] listQuestion() {

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_QUESTION", new String[]{"COLUMN_TEXTCOM_QUESTION", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_QUESTION}, null, null, null, null, null);
        cursor.moveToFirst();
        strListQuestion = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListQuestion[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_QUESTION"));
            strListQuestion[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_QUESTION));
            cursor.moveToNext();
        }
        cursor.close();

        return strListQuestion;

    }

    private String[] listAnswer() {

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_ANSWER", new String[]{"COLUMN_TEXTCOM_ANSWER}, null, null, null, null, null"});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_ANSWER}, null, null, null, null, null);
        cursor.moveToFirst();
        strListAnswer = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListAnswer[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_ANSWER"));
            strListAnswer[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_ANSWER));
            cursor.moveToNext();
        }
        cursor.close();

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_CHOICE_A", new String[]{"COLUMN_TEXTCOM_CHOICE_A", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_CHOICE_A}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceA = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceA[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_CHOICE_A"));
            strListChoiceA[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_CHOICE_A));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_CHOICE_B", new String[]{"COLUMN_TEXTCOM_CHOICE_B", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_CHOICE_B}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceB = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceB[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_CHOICE_B"));
            strListChoiceB[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_CHOICE_B));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_CHOICE_C", new String[]{"COLUMN_TEXTCOM_CHOICE_C", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_CHOICE_C}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceC = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceC[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_CHOICE_C"));
            strListChoiceC[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_CHOICE_C));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_CHOICE_D", new String[]{"COLUMN_TEXTCOM_CHOICE_D", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_CHOICE_D}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceD = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceD[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_CHOICE_D"));
            strListChoiceD[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_CHOICE_D));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceD;

    }

    private String[] listChoiceDes() {

        String strListChoiceDes[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM TEXTCOMPLETION_QUESTION WHERE COLUMN_TEXTCOM_DES", new String[]{"COLUMN_TEXTCOM_DES", null, null, null, null, null});
        Cursor cursor = db.query(TEXTCOMPLETION_QUESTION, new String[]{COLUMN_TEXTCOM_DES}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceDes = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceDes[i] = cursor.getString(cursor.getColumnIndex("COLUMN_TEXTCOM_DES"));
            strListChoiceDes[i] = cursor.getString(cursor.getColumnIndex(COLUMN_TEXTCOM_DES));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceDes;

    }

}
