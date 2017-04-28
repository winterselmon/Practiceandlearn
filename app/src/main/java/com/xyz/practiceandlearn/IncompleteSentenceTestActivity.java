package com.xyz.practiceandlearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import static com.xyz.practiceandlearn.Global.basedir;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_ANSWER_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_CHOICE_A_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_CHOICE_B_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_CHOICE_C_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_CHOICE_D_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.COLUMN_INCOMPLETE_QUESTION_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.INCOMPLETE_CHOICE_TEST;
import static com.xyz.practiceandlearn.IncompleteDatabase.INCOMPLETE_QUESTION_TEST;


public class IncompleteSentenceTestActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private String[] strQuestion, strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD;
    //private int currentposition;
    private int currentpage;
    private static final int maxrow = 39;

    //timer
    TextView txttime5;

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

            txttime5.setText(String.format("%d:%02d:%02d",hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete_sentence_test);

        objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");

        //currentposition = 0;

        currentpage = 4;

        String strNumber = String.valueOf(Global.currentposition+1)+ "/40";

        TextView txtNo = (TextView) findViewById(R.id.txtnum5);
        txtNo.setText(strNumber);

        txttime5 = (TextView) findViewById(R.id.txttime5);
        timerHandler.postDelayed(timerRunnable, 0);

        RadioButton rdoA = (RadioButton) findViewById(R.id.rdoA_Incomplete_test);
        rdoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_Incomplete_test);
        rdoB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_Incomplete_test);
        rdoC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_Incomplete_test);
        rdoD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectpoint();
            }
        });

        setupArray();

        showQuestionAndChoice(0);

        back();

        next();

        clearcheck();

    }

    private void setupArray(){

        strQuestion = listQuestion();
        strAnswer = listAnswer();
        strChoiceA = listChoiceA();
        strChoiceB = listChoiceB();
        strChoiceC = listChoiceC();
        strChoiceD = listChoiceD();

    }



    private void showQuestionAndChoice(final int p){

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion_Incomplete_test);
        txtQuestion.setText(strQuestion[p]);
        RadioButton btnA = (RadioButton) findViewById(R.id.rdoA_Incomplete_test);
        btnA.setText("A."+strChoiceA[p]);
        RadioButton btnB = (RadioButton) findViewById(R.id.rdoB_Incomplete_test);
        btnB.setText("B."+strChoiceB[p]);
        RadioButton btnC = (RadioButton) findViewById(R.id.rdoC_Incomplete_test);
        btnC.setText("C."+strChoiceC[p]);
        RadioButton btnD = (RadioButton) findViewById(R.id.rdoD_Incomplete_test);
        btnD.setText("D."+strChoiceD[p]);
        //TextView txtDes = (TextView) findViewById(R.id.txtDes_Incomplete);
        //txtDes.setText(strDes[p]);

    }

    private void collectpoint() {

        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_Incomplete_test);
        if (rdoA1.isChecked()) {
            if (strAnswer[Global.currentposition].equals("A")) {

                Global.collect[Global.currentAnswer] = true;

            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_Incomplete_test);
        if (rdoB1.isChecked()) {
            if (strAnswer[Global.currentposition].equals("B")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_Incomplete_test);
        if (rdoC1.isChecked()) {
            if (strAnswer[Global.currentposition].equals("C")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }


        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_Incomplete_test);
        if (rdoD1.isChecked()) {
            if (strAnswer[Global.currentposition].equals("D")) {

                Global.collect[Global.currentAnswer] = true;
            } else {
                Global.collect[Global.currentAnswer] = false;
            }
        }
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

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack5_test);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Global.currentAnswer == 0){
                    return;
                }
                Global.currentAnswer -=  3;

                if (Global.currentposition == 0) {
                    if (currentpage == 0) {
                        return;
                    } else {
                        Global.currentposition = 9;
                        Intent intent = new Intent(IncompleteSentenceTestActivity.this, ShortTalkTestActivity.class);
                        startActivity(intent);
                    }
                } else {


                    Global.currentposition--;
                    if (Global.currentposition >= 0) {

                        String strNumber = String.valueOf(Global.currentposition + 1) + "/40";
                        Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);

                        TextView txtNo = (TextView) findViewById(R.id.txtnum5);
                        txtNo.setText(strNumber);


                    }

                    showQuestionAndChoice(Global.currentposition);

                    clearcheck();
                }


            }
        });

    }

    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNext5_test);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.currentAnswer ++;

                Global.currentposition++;

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    Intent intent = new Intent(IncompleteSentenceTestActivity.this, DescriptionTextcomActivity.class);
                    startActivity(intent);
                    sumScore();
                } else {

                    String strNumber = String.valueOf(Global.currentposition + 1) + "/40";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                    //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum5);
                    txtNo.setText(strNumber);

                    showQuestionAndChoice(Global.currentposition);

                    clearcheck();
                }

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
                        Intent intent = new Intent(IncompleteSentenceTestActivity.this, MainActivity.class);
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

        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroup_Incomplete_test);
        rdogroup2.clearCheck();
    }

    private String[] listQuestion(){

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_QUESTION_TEST WHERE COLUMN_INCOMPLETE_QUESTION_TEST", new String[]{"COLUMN_INCOMPLETE_QUESTION_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_QUESTION_TEST, new String[]{COLUMN_INCOMPLETE_QUESTION_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListQuestion = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListQuestion[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_QUESTION_TEST"));
            strListQuestion[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_QUESTION_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListQuestion;

    }

    private String[] listAnswer(){

        String strListAnswer[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_QUESTION_TEST WHERE COLUMN_INCOMPLETE_ANSWER_TEST", new String[]{"COLUMN_INCOMPLETE_ANSWER_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_QUESTION_TEST, new String[]{COLUMN_INCOMPLETE_ANSWER_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_ANSWER_TEST"));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_ANSWER_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_CHOICE_TEST WHERE COLUMN_INCOMPLETE_CHOICE_A_TEST", new String[]{"COLUMN_INCOMPLETE_CHOICE_A_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_CHOICE_TEST, new String[]{COLUMN_INCOMPLETE_CHOICE_A_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceA = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_CHOICE_A_TEST"));
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_CHOICE_A_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_CHOICE_TEST WHERE COLUMN_INCOMPLETE_CHOICE_B_TEST", new String[]{"COLUMN_INCOMPLETE_CHOICE_B_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_CHOICE_TEST, new String[]{COLUMN_INCOMPLETE_CHOICE_B_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceB = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_CHOICE_B_TEST"));
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_CHOICE_B_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_CHOICE_TEST WHERE COLUMN_INCOMPLETE_CHOICE_C_TEST", new String[]{"COLUMN_INCOMPLETE_CHOICE_C_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_CHOICE_TEST, new String[]{COLUMN_INCOMPLETE_CHOICE_C_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_CHOICE_C_TEST"));
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_CHOICE_C_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM INCOMPLETE_CHOICE_TEST WHERE COLUMN_INCOMPLETE_CHOICE_D_TEST", new String[]{"COLUMN_INCOMPLETE_CHOICE_D_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(INCOMPLETE_CHOICE_TEST, new String[]{COLUMN_INCOMPLETE_CHOICE_D_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceD = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_INCOMPLETE_CHOICE_D_TEST"));
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_INCOMPLETE_CHOICE_D_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListChoiceD;

    }

}
