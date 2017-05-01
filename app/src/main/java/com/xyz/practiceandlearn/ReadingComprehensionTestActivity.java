package com.xyz.practiceandlearn;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
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

import static com.xyz.practiceandlearn.Global.BaseDir;
import static com.xyz.practiceandlearn.Global.basedir;

import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_ANSWER_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_CHOICE_A_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_CHOICE_B_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_CHOICE_C_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_CHOICE_D_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_QUESTION_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.COLUMN_READING_SCRIPT_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.READINGCOMPREHENSION_QUESTION_TEST;
import static com.xyz.practiceandlearn.ReadingComprehensionDatabase.READINGCOMPREHENSION_SCRIPT_TEST;


public class ReadingComprehensionTestActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private String[] strScript, strQuestion,strAnswer, strChoiceA, strChoiceB, strChoiceC, strChoiceD;
    //private int currentposition;
    private int currentpage;
    private static final int maxrow = 15;

    //timer
    TextView txttime7;

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

            txttime7.setText(String.format("%d:%02d:%02d",hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_comprehension_test);

        File photoDB = new File(BaseDir+ "/V1/TOEIC.db");
        File photoDB2 = new File(BaseDir+ "/V1/V2/TOEIC2.db");

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

        //currentposition = 0;

        currentpage = 6;

        String strNumber = String.valueOf(Global.currentposition+1)+ "/16";

        TextView txtNo = (TextView) findViewById(R.id.txtnum7);
        txtNo.setText(strNumber);

        ScrollView scrollView = (ScrollView) findViewById(R.id.activity_reading_comprehension_test);

        scrollView.smoothScrollTo(0,0);

        txttime7 = (TextView) findViewById(R.id.txttime7);
        timerHandler.postDelayed(timerRunnable, 0);

        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_Reading_test);
        rdoA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_Reading_test);
        rdoB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_Reading_test);
        rdoC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_Reading_test);
        rdoD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_Reading_test);
        rdoA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_Reading_test);
        rdoB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_Reading_test);
        rdoC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_Readingm_test);
        rdoD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_Reading_test);
        rdoA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_Reading_test);
        rdoB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_Reading_test);
        rdoC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });

        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_Reading_test);
        rdoD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectpoint();
            }
        });


        setupArray();

        showText();

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

    }

    private void showText() {

        TextView txtScript = (TextView) findViewById(R.id.txtScriptReading_test);
        txtScript.setText(strScript[Global.currentposition]);

        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestion1_Reading_test);
        txtQuestion1.setText(strQuestion[Global.currentposition*3]);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestionB_Reading_test);
        txtQuestion2.setText(strQuestion[(Global.currentposition*3)+1]);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestionC_Reading_test);
        txtQuestion3.setText(strQuestion[(Global.currentposition*3)+2]);

        //Choice1
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_Reading_test);
        rdoA1.setText("A."+strChoiceA[Global.currentposition*3]);
        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_Reading_test);
        rdoB1.setText("B."+strChoiceB[Global.currentposition*3]);
        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_Reading_test);
        rdoC1.setText("C."+strChoiceC[Global.currentposition*3]);
        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_Reading_test);
        rdoD1.setText("D."+strChoiceD[Global.currentposition*3]);

        //Choice2
        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_Reading_test);
        rdoA2.setText("A."+strChoiceA[Global.currentposition*3+1]);
        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_Reading_test);
        rdoB2.setText("B."+strChoiceB[Global.currentposition*3+1]);
        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_Reading_test);
        rdoC2.setText("C."+strChoiceC[Global.currentposition*3+1]);
        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_Readingm_test);
        rdoD2.setText("D."+strChoiceD[Global.currentposition*3+1]);

        //Choice3
        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_Reading_test);
        rdoA3.setText("A."+strChoiceA[Global.currentposition*3+2]);
        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_Reading_test);
        rdoB3.setText("B."+strChoiceB[Global.currentposition*3+2]);
        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_Reading_test);
        rdoC3.setText("C."+strChoiceC[Global.currentposition*3+2]);
        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_Reading_test);
        rdoD3.setText("D."+strChoiceD[Global.currentposition*3+2]);

    }

    private void back() {

        Button btnBack = (Button) findViewById(R.id.btnBack7_test);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Global.currentAnswer == 0){
                    return;
                }
                Global.currentAnswer -= 3;

                if (Global.currentposition == 0) {
                    if (currentpage == 0) {
                        return;
                    } else {
                        Global.currentposition = 3;
                        Intent intent = new Intent(ReadingComprehensionTestActivity.this, TextComTestActivity.class);
                        startActivity(intent);
                    }
                } else {


                    ScrollView scrollView = (ScrollView) findViewById(R.id.activity_reading_comprehension_test);

                    scrollView.smoothScrollTo(0,0);

                    Global.currentposition--;
                    if (Global.currentposition >= 0) {

                        String strNumber = String.valueOf(Global.currentposition + 1) + "/16";
                        Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);

                        TextView txtNo = (TextView) findViewById(R.id.txtnum7);
                        txtNo.setText(strNumber);


                    }

                    showText();

                    clearcheck();
                }
            }
        });

    }

    private void next() {

        final Button btnNext = (Button) findViewById(R.id.btnNext7_test);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Global.currentAnswer += 3;

                Global.currentposition++;

                if (Global.currentposition == maxrow) {
                    btnNext.setText("Finish");
                }

                if (Global.currentposition > maxrow) {
                    Global.currentposition = 0;
                    sumScore();
                    Intent intent = new Intent(ReadingComprehensionTestActivity.this, ShowScoreActivity.class);
                    startActivity(intent);
                } else {

                    ScrollView scrollView = (ScrollView) findViewById(R.id.activity_reading_comprehension_test);

                    scrollView.smoothScrollTo(0,0);


                    String strNumber = String.valueOf(Global.currentposition + 1) + "/16";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);
                    //Toast.makeText(this, strNumber, Toast.LENGTH_LONG);
                    TextView txtNo = (TextView) findViewById(R.id.txtnum7);
                    txtNo.setText(strNumber);

                    showText();
                    clearcheck();
                }

            }
        });

    }

    private void collectpoint() {

        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_Reading_test);
        if (rdoA1.isChecked()) {
            if (strAnswer[(Global.currentposition*3)].equals("A")) {

                Global.collect[(Global.currentAnswer)] = true;

            } else {
                Global.collect[(Global.currentAnswer)] = false;
            }
        }


        RadioButton rdoB1 = (RadioButton) findViewById(R.id.rdoB_Reading_test);
        if (rdoB1.isChecked()) {
            if (strAnswer[(Global.currentposition*3)].equals("B")) {

                Global.collect[(Global.currentAnswer)] = true;
            } else {
                Global.collect[(Global.currentAnswer)] = false;
            }
        }


        RadioButton rdoC1 = (RadioButton) findViewById(R.id.rdoC_Reading_test);
        if (rdoC1.isChecked()) {
            if (strAnswer[(Global.currentposition*3)].equals("C")) {

                Global.collect[(Global.currentAnswer)] = true;
            } else {
                Global.collect[(Global.currentAnswer)] = false;
            }
        }


        RadioButton rdoD1 = (RadioButton) findViewById(R.id.rdoD_Reading_test);
        if (rdoD1.isChecked()) {
            if (strAnswer[(Global.currentposition*3)].equals("D")) {

                Global.collect[(Global.currentAnswer)] = true;
            } else {
                Global.collect[(Global.currentAnswer)] = false;
            }
        }


        RadioButton rdoA2 = (RadioButton) findViewById(R.id.rdoA2_Reading_test);
        if (rdoA2.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+1].equals("A")) {

                Global.collect[(Global.currentAnswer)+1] = true;

            } else {
                Global.collect[(Global.currentAnswer)+1] = false;
            }
        }


        RadioButton rdoB2 = (RadioButton) findViewById(R.id.rdoB2_Reading_test);
        if (rdoB2.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+1].equals("B")) {

                Global.collect[(Global.currentAnswer)+1] = true;
            } else {
                Global.collect[(Global.currentAnswer)+1] = false;
            }
        }


        RadioButton rdoC2 = (RadioButton) findViewById(R.id.rdoC2_Reading_test);
        if (rdoC2.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+1].equals("C")) {

                Global.collect[(Global.currentAnswer)+1] = true;
            } else {
                Global.collect[(Global.currentAnswer)+1] = false;
            }
        }


        RadioButton rdoD2 = (RadioButton) findViewById(R.id.rdoD2_Readingm_test);
        if (rdoD2.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+1].equals("D")) {

                Global.collect[(Global.currentAnswer)+1] = true;
            } else {
                Global.collect[(Global.currentAnswer)+1] = false;
            }
        }


        RadioButton rdoA3 = (RadioButton) findViewById(R.id.rdoA3_Reading_test);
        if (rdoA3.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+2].equals("A")) {

                Global.collect[(Global.currentAnswer)+2] = true;

            } else {
                Global.collect[(Global.currentAnswer)+2] = false;
            }
        }


        RadioButton rdoB3 = (RadioButton) findViewById(R.id.rdoB3_Reading_test);
        if (rdoB3.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+2].equals("B")) {

                Global.collect[(Global.currentAnswer)+2] = true;
            } else {
                Global.collect[(Global.currentAnswer)+2] = false;
            }
        }


        RadioButton rdoC3 = (RadioButton) findViewById(R.id.rdoC3_Reading_test);
        if (rdoC3.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+2].equals("C")) {

                Global.collect[(Global.currentAnswer)+2] = true;
            } else {
                Global.collect[(Global.currentAnswer)+2] = false;
            }
        }


        RadioButton rdoD3 = (RadioButton) findViewById(R.id.rdoD3_Reading_test);
        if (rdoD3.isChecked()) {
            if (strAnswer[(Global.currentposition*3)+2].equals("D")) {

                Global.collect[(Global.currentAnswer)+2] = true;
            } else {
                Global.collect[(Global.currentAnswer)+2] = false;
            }
        }

    }

    private void sumScore(){
        int score = 0;
        for ( int i = 0; i < 200; i++){
            if (Global.collect[i])
                score++;
        }
        String S = Integer.toString(score);
        Toast.makeText(getBaseContext(),"Score = " + S ,Toast.LENGTH_LONG).show();
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the test?")
                .setMessage("Are you sure you want to quit to test?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ReadingComprehensionTestActivity.this, MainActivity.class);
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

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroupA_Reading_test);
        rdogroup1.clearCheck();
        RadioGroup rdogroup2 = (RadioGroup) findViewById(R.id.rdoGroupB_Reading_test);
        rdogroup2.clearCheck();
        RadioGroup rdogroup3 = (RadioGroup) findViewById(R.id.rdoGroupC_Reading_test);
        rdogroup3.clearCheck();
    }

    private String[] listScript(){

        String strListScript[];
        try{
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_SCRIPT_TEST WHERE COLUMN_READING_SCRIPT_TEST", new String[]{"COLUMN_READING_SCRIPT_TEST", null, null, null, null, null});
        Cursor objCursor = db.query(READINGCOMPREHENSION_SCRIPT_TEST, new String[]{COLUMN_READING_SCRIPT_TEST}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListScript = new String[objCursor.getCount()];
        for (int i=0; i<objCursor.getCount(); i++){
            //strListScript[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_READING_SCRIPT_TEST"));
            strListScript[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_READING_SCRIPT_TEST));
            objCursor.moveToNext();
        }
        objCursor.close();

        } catch (SQLException sqle) {
            throw sqle;
        }

        return strListScript;

    }

    private String[] listQuestion() {

        String strListQuestion[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_QUESTION_TEST", new String[]{"COLUMN_READING_QUESTION_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_QUESTION_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListQuestion = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListQuestion[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_QUESTION_TEST"));
            strListQuestion[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_QUESTION_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListQuestion;

    }

    private String[] listAnswer() {

        String strListAnswer[];
        try {
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_ANSWER_TEST", new String[]{"COLUMN_READING_ANSWER_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_ANSWER_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListAnswer = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListAnswer[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_ANSWER_TEST"));
            strListAnswer[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_ANSWER_TEST));
            cursor.moveToNext();
        }
        cursor.close();

    } catch (SQLException sqle) {
        throw sqle;
    }

        return strListAnswer;

    }

    private String[] listChoiceA() {

        String strListChoiceA[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_CHOICE_A_TEST", new String[]{"COLUMN_READING_CHOICE_A_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_CHOICE_A_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceA = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceA[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_CHOICE_A_TEST"));
            strListChoiceA[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_CHOICE_A_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceA;

    }

    private String[] listChoiceB() {

        String strListChoiceB[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_CHOICE_B_TEST", new String[]{"COLUMN_READING_CHOICE_B_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_CHOICE_B_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceB = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceB[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_CHOICE_B_TEST"));
            strListChoiceB[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_CHOICE_B_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_CHOICE_C_TEST", new String[]{"COLUMN_READING_CHOICE_C_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_CHOICE_C_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceC = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceC[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_CHOICE_C_TEST"));
            strListChoiceC[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_CHOICE_C_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[];
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM READINGCOMPREHENSION_QUESTION_TEST WHERE COLUMN_READING_CHOICE_D_TEST", new String[]{"COLUMN_READING_CHOICE_D_TEST", null, null, null, null, null});
        Cursor cursor = db.query(READINGCOMPREHENSION_QUESTION_TEST, new String[]{COLUMN_READING_CHOICE_D_TEST}, null, null, null, null, null);
        cursor.moveToFirst();
        strListChoiceD = new String[cursor.getCount()];
        for (int i=0; i<cursor.getCount(); i++) {
            //strListChoiceD[i] = cursor.getString(cursor.getColumnIndex("COLUMN_READING_CHOICE_D_TEST"));
            strListChoiceD[i] = cursor.getString(cursor.getColumnIndex(COLUMN_READING_CHOICE_D_TEST));
            cursor.moveToNext();
        }
        cursor.close();

        return strListChoiceD;

    }

}
