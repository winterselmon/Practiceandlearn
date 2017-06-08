package com.xyz.practiceandlearn.Practice;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xyz.practiceandlearn.Database.PhotoDatabase;
import com.xyz.practiceandlearn.Main.MainActivity;
import com.xyz.practiceandlearn.Database.MyDatabase;
import com.xyz.practiceandlearn.R;
import com.xyz.practiceandlearn.Version;

import java.io.File;
import java.io.IOException;

import static com.xyz.practiceandlearn.Main.Global.BaseDir;

import static com.xyz.practiceandlearn.Main.Global.basedirPhoto;
import static com.xyz.practiceandlearn.Main.Global.basedirSound;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_CHOICE;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_QUESTION;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_ANSWER;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_A;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_B;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_C;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_D;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_DES;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_CHOICE;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_QUESTION;

public class PhotosPracticeActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private int currentposition;
    private int currentsound;
    private boolean showanswer;
    private final int maxrow = 39;
    //private static String[] COLUMNS = {COLUMN_PHOTO_CHOICE_A, COLUMN_PHOTO_CHOICE_B, COLUMN_PHOTO_CHOICE_C, COLUMN_PHOTO_CHOICE_D,};

    private  String sql_ver1 = COLUMN_ID_PHOTO_CHOICE + "<=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";
    private  String sql2_ver1 = COLUMN_ID_PHOTO_QUESTION + "<=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";

    private  String sql_ver2 = COLUMN_ID_PHOTO_CHOICE + ">=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";
    private  String sql2_ver2 = COLUMN_ID_PHOTO_QUESTION + ">=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";

    private  String[] strListChoiceA, strListChoiceB, strListChoiceC, strListChoiceD, strListDes, strListAnswer;
    private MediaPlayer mPlayer, soundtrue, soundwrong;
    String correctAnswer;

    private static final String TAG = "PhotosPracticeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_practice);

        File photoDB = new File(BaseDir + "/V1/TOEIC.db");
        File photoDB2 = new File(BaseDir + "/V1/V2/TOEIC2.db");

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
        showanswer = false;

        showanswer(0);

        playSound();

        setupArrar();

        createlistview();

        play();

        next();

        back();

        clearlayout();

        clearcheck();

        Log.d(TAG, "onCreate: " + sql_ver1);
        Log.d(TAG, "onCreate: " + sql_ver2);
        Log.d(TAG, "onCreate: " + sql2_ver1);
        Log.d(TAG, "onCreate: " + sql2_ver2);

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

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Stop the practice?")
                .setMessage("Are you sure you want to quit to practice?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PhotosPracticeActivity.this, MainActivity.class);
                        startActivity(intent);
                        mPlayer.stop();

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

                if (currentposition > 0)
                    currentposition--;

                createlistview();

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


                if (currentposition < maxrow)
                    currentposition++;

                createlistview();

                clearcheck();

                playSound();
                //playSound(currentposition);

                clearlayout();
                showanswer(currentposition);

            }
        });

    }

    private void playSound() {

        File photoDB = new File(basedirSound + "/V1/AudioPhotoPratice/");
        File photoDB2 = new File(basedirSound + "/V1/V2/AudioPhotoPratice/");
        File filepath = null;


        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }

        if (Version.CURRENT.equals(Version.SECOND)) {
            filepath = new File(basedirPhoto + "/V1/AudioPhotoPratice_V2/" + String.valueOf(currentposition + 1) + ".mp3");
        } else if (Version.CURRENT.equals(Version.FIRST)) {

            filepath = new File(basedirPhoto + "/V1/AudioPhotoPratice/" + String.valueOf(currentposition + 1) + ".mp3");

        } else {
            //Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }

//        String filePath = BaseDir+"/V1/AudioPhotoPratice/"+String.valueOf(currentposition+1)+".mp3";
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(String.valueOf(filepath));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mPlayer = MediaPlayer.create(this,mySound[p]);
        //mPlayer.start();

    }

    private void playTrue() {

        File photoDB = new File(basedirSound + "/V1/soundtrue.mp3");
        File photoDB2 = new File(basedirSound + "/V1/soundtrue.mp3/");
        File filepath = null;

        if (photoDB2.exists()) {
            filepath = new File(basedirPhoto + "/V1/soundtrue.mp3");
        } else if (photoDB.exists()) {

            filepath = new File(basedirPhoto + "/V1/soundtrue.mp3");

        } else {
            //Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }
//        String filePath = basedirSound +"/V1/V2/soundtrue.mp3";

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(String.valueOf(filepath));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playWrong() {

        File photoDB = new File(basedirSound + "/V1/soundwrong.mp3");
        File photoDB2 = new File(basedirSound + "/V1/soundwrong.mp3");
        File filepath = null;

        if (photoDB2.exists()) {
            filepath = new File(basedirPhoto + "/V1/soundwrong.mp3");
        } else if (photoDB.exists()) {
            filepath = new File(basedirPhoto + "/V1/soundwrong.mp3");

        } else {
            //Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }

//        String filePath = basedirSound +"/V1/V2/soundwrong.mp3";
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(String.valueOf(filepath));
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
                    if (strListAnswer[currentposition].equals("A")) {

                        if (chkA) {
                            //playSound
                            //playTrue();
//                            Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        } else {

                            //playWrong();
//                            Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("B")) {

                        if (chkB) {
                            //playSound
                            //playTrue();
//                            Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        } else {

                            //playWrong();
//                            Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("C")) {

                        if (chkC) {
                            //playSound
                            //playTrue();
//                            Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        } else {

                            //playWrong();
//                            Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (strListAnswer[currentposition].equals("D")) {

                        if (chkD) {
                            //playSound
                            //playTrue();
//                            Toast.makeText(getBaseContext(), "r", Toast.LENGTH_LONG).show();
                        } else {

                            //playWrong();
//                            Toast.makeText(getBaseContext(), "c", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
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

    private void createlistview() {

        File photoDB = new File(basedirSound + "/V1/photoPratice/");
        File photoDB2 = new File(basedirSound + "/V1/V2/photoPratice/");
        File imgFile = null;

        if (Version.CURRENT.equals(Version.SECOND)) {
            imgFile = new File(basedirPhoto + "/V1/photoPratice_V2/" + String.valueOf(currentposition + 1) + ".png");
        } else if (Version.CURRENT.equals(Version.FIRST)) {
            imgFile = new File(basedirPhoto + "/V1/photoPratice/" + String.valueOf(currentposition + 1) + ".png");

        } else {
            //Toast.makeText(getBaseContext(),"hi3",Toast.LENGTH_SHORT).show();
            return;
        }


//        File imgFile = new File(basedir + "/V1/PhotoPratice/" + String.valueOf(currentposition + 1) + ".png");
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
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPlayer.stop();
    }

    private String[] listChoiceA() {

        String strListChoiceA[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_A", new String[]{"COLUMN_PHOTO_CHOICE_A", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_A},
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceA = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_A"));
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_A));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceA;
    }

    private String[] listChoiceB() {

        String strListChoiceB[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_B", new String[]{"COLUMN_PHOTO_CHOICE_B", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_B},
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceB = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_B"));
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_B));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceB;

    }

    private String[] listChoiceC() {

        String strListChoiceC[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_C", new String[]{"COLUMN_PHOTO_CHOICE_C",null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_C},
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceC = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_C"));
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_C));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceC;

    }

    private String[] listChoiceD() {

        String strListChoiceD[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_CHOICE_D", new String[]{"COLUMN_PHOTO_CHOICE_D", null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_D},
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceD = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_CHOICE_D"));
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_D));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceD;

    }

    private String[] listChoiceDes() {

        String strListChoiceDes[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_CHOICE WHERE COLUMN_PHOTO_DES", new String[]{"COLUMN_PHOTO_DES",null, null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_DES},
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListChoiceDes = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_DES"));
            strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_DES));
            objCursor.moveToNext();
        }   // for
        objCursor.close();

        return strListChoiceDes;

    }

    private String[] listAnswer() {

        String strListAnswer[] = null;
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //Cursor objCursor = db.rawQuery("SELECT * FROM PHOTOGRAPHS_QUESTION WHERE COLUMN_PHOTO_ANSWER", new String[]{"COLUMN_PHOTO_ANSWER",null,null, null, null, null});
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION, new String[]{COLUMN_PHOTO_ANSWER},
                Version.CURRENT.equals(Version.FIRST) ? sql2_ver1 : sql2_ver2, null, null, null, null);
        objCursor.moveToFirst();
        strListAnswer = new String[objCursor.getCount()];
        for (int i = 0; i < objCursor.getCount(); i++) {
            //strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex("COLUMN_PHOTO_ANSWER"));
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListAnswer;

    }
}


