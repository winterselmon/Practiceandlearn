package com.xyz.practiceandlearn.Test;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xyz.practiceandlearn.Database.PhotoDatabase;
import com.xyz.practiceandlearn.Description.DescriptionQandRActivity;
import com.xyz.practiceandlearn.Main.Global;
import com.xyz.practiceandlearn.Main.MainActivity;
import com.xyz.practiceandlearn.Database.MyDatabase;
import com.xyz.practiceandlearn.R;
import com.xyz.practiceandlearn.Version;

import java.io.File;
import java.io.IOException;

import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_CHOICE;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_CHOICE_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_QUESTION;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_ID_PHOTO_QUESTION_TEST;
import static com.xyz.practiceandlearn.Main.Global.basedirPhoto;
import static com.xyz.practiceandlearn.Main.Global.basedirSound;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_CHOICE_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;


import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_ANSWER_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_A_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_B_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_C_TEST;
import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_CHOICE_D_TEST;
//import static com.xyz.practiceandlearn.Database.PhotoDatabase.COLUMN_PHOTO_DES;
//import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_CHOICE;
//import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_CHOICE_TEST;
//import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_QUESTION;
//import static com.xyz.practiceandlearn.Database.PhotoDatabase.PHOTOGRAPHS_QUESTION_TEST;

public class PhotosTestActivity extends AppCompatActivity {

    //กำหนดให้ทำการ query ข้อมูลจาก COLUMN_PHOTO_ANSWER_TEST น้อยกว่ากว่าหรือเท่ากับ 10 และ จากตาราง PhotoDatabase และ ตรวจสอบเวอร์ชั่นปัจจุบัน
    private  String sql_ver1 = COLUMN_PHOTO_ANSWER_TEST + "<=10 and " + PhotoDatabase.COLUMN_VERSION_TEST + " = '" + Version.CURRENT +"'";

    //กำหนดให้ทำการ query ข้อมูลจาก COLUMN_PHOTO_ANSWER_TEST มากกว่ากว่าหรือเท่ากับ 10 และ จากตาราง PhotoDatabase และ ตรวจสอบเวอร์ชั่นปัจจุบัน
    private  String sql_ver2 = COLUMN_PHOTO_ANSWER_TEST + ">=10 and " + PhotoDatabase.COLUMN_VERSION_TEST + " = '" + Version.CURRENT +"'";


    //สร้างตัวแปรชนิด MyDatabase ชื่อ objMyDatabase
    MyDatabase objMyDatabase;
    //สร้างตัวแปรชนิด string-array
    private static String[] strListAnswer;
    private final int maxrow = 9;
    private MediaPlayer mPlayer;


    //สร้างตัวแปรชนิด textview ชื่อ txttime
    TextView txttime;
    //runs without a timer by reposting this handler at the end of the runnable
    //สร้างตัวแปรไว้คอยจัดการ thread
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        //เมื่อเริ่มทำงาน
        @Override
        public void run() {
            //กำหนดตัวแปรชนิด long เก็บค่า 7200000
            long time = 7200000;
            //กำหนดให้ ตัวแปรที่เริ่มต้นลบด้วยเวลาที่เดินอยู่ในปัจจุบันลบกับเวลาที่เริ่มต้น
            long millis = time-(System.currentTimeMillis() - Global.startTime) ;
            //กำหนดให้ตัวแปร millis หาร 1000 และนำค่าที่ได้ไปเก็บไว้ในตัวแปร second
            int seconds = (int) (millis / 1000);
            //กำหนดให้ตัวแปร seconds mod 3600 และนำค่าที่ได้ไปหาร 60 และนำไปเก็บไว้ในตัวแปร minutes
            int minutes = (seconds % 3600) / 60;
            //กำหนดให้ตัวแปร seconds/3600 และนำค่าที่ได้ไปเก็บไว้ในตัวแปร hour
            int hour = seconds / 3600;
            //กำหนดให้ตัวแปร seconds mod 60 และนำค่าที่ได้ไปเก็บไว้ในตัวแปร seconds
            seconds = seconds % 60;

            //กำหนดค่าให้ txttime มีค่าตาม text ที่กำหนดไว้ ในที่นี้คือแสดงนาฬิกานับถอยหลังเริ่มตั้งแต่ 2:00:00
            txttime.setText(String.format("%d:%02d:%02d", hour, minutes, seconds));
            //กำหนด delay ไว้ที่ ครึ่งวิ
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_test);

        //สร้างตัวแปร ชนิด file ไว้เก็บตำแหน่งของไฟล์ database
        File photoDB = new File(basedirSound+ "/V1/TOEIC.db");
        File photoDB2 = new File(basedirSound + "/V1/V2/TOEIC2.db");

        //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ตามตำแหน่งที่ระบุไว้ข้างต้นให้ ทำการเรียกใช้ database
        if (photoDB2.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB2);
        } else if (photoDB.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB);
        } else {
            return;
        }

        //สร้างตัวแปร string เพื่อให้แสดงเลขข้อ 1/10 ถึง 10/10
        String strNumber = String.valueOf(Global.currentposition + 1) + "/10";

        //สร้างตัวแปร ชนิด TextView ชื่อ txtNo อ้างอิงจาก id txtnum
        TextView txtNo = (TextView) findViewById(R.id.txtnum);
        //กำหนดค่าให้ txtNo มีค่าตาม string ที่กำหนดไว้
        txtNo.setText(strNumber);

        //สร้างตัวแปร ชนิด TextView ชื่อ txttime อ้างอิงจาก id txttime
        txttime = (TextView) findViewById(R.id.txttime);

        //กำหนด delay ไว้ที่ 0 วิ
        timerHandler.postDelayed(timerRunnable, 0);


        //สร้างตัวแปร rdoA โดยให้ผูกกับ id rdoA_photo_test
        RadioButton rdoA = (RadioButton) findViewById(R.id.rdoA_photo_test);
        //กำหนดให้เมื่อกดปุ่มแล้วจะเรียกใช้ method collectpoint
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


    //method เก็บคะแนน
    private void collectpoint() {

        //สร้างตัวแปร rdoA โดยให้ผูกกับ id rdoA_photo_test
        RadioButton rdoA1 = (RadioButton) findViewById(R.id.rdoA_photo_test);
        //ตรวจสอบว่าถ้ากดปุ่ม rdoA1 แล้วค่าที่อยู่ใน strListAnswer ตามตำแหน่งข้อปัจจุบัน ตรงกับ A ให้ ตัวแปร collect มีค่าเป็น true แต่ถ้าไม่ใช่ให้เป็น false
        if (rdoA1.isChecked()) {
            if (strListAnswer[Global.currentposition].equals("A")) {

                Global.collect[Global.currentAnswer] = true;
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
        //กำหนดให้ตัว imgFile มีค่าเป็น null
        File imgFile = null;

        //สร้างเงื่อนไชว่าถ้า version ปัจจุบันเป็น version.SECOND หรือ version 2 ให้
        if (Version.CURRENT.equals(Version.SECOND)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            imgFile = new File(basedirPhoto +"/V1/photoTest_V2/"+String.valueOf(Global.currentposition+1)+".png");
            //ถ้า version ปัจจุบันเป็น version.FIRST หรือ version 1 ให้
        } else if (Version.CURRENT.equals(Version.FIRST)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            imgFile = new File(basedirPhoto +"/V1/photoTest/"+String.valueOf(Global.currentposition+1)+".png");
        } else {
            return;
        }
        //ตรวจสอบไฟล์จากตำแหน่งที่ระบุไว้ข้างต้น ถ้าตรวจพบ ให้
        if(imgFile.exists()){
            //สร้างตัวแปรชนิด bitmap ให้ทำการแปลงโค๊ด bitmap จากภาพที่อยู่ในตำแหน่งที่กำหนด
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //สร้างตัวแปร imageview และ อ้างอิงจาก id imgphotos
            ImageView img = (ImageView) findViewById(R.id.imgPhotosTest);
            //แสดงรูปตามตำแหน่งไฟล์ที่ระบุไว้ข้างต้น
            img.setImageBitmap(myBitmap);

        }

    }

    //สร้าง method ชื่อ playSound
    private void playSound() {
        //สร้างตัวแปรชนิด file ชื่อ filepath มีค่าเป็น null
        File filepath = null;
        //สร้างเงื่อนไขว่าถ้า mPlayer ไม่เท่ากับ null
        if (mPlayer !=null){
            //หยุดเล่นเสียง
            mPlayer.stop();
            mPlayer.release();
        }

        //สร้างเงื่อนไชว่าถ้า version ปัจจุบันเป็น version.SECOND หรือ version 2 ให้
        if (Version.CURRENT.equals(Version.SECOND)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            filepath = new File(basedirPhoto +"/V1/AudioPhoto_V2/"+String.valueOf(Global.currentposition+1)+".mp3");
            //ถ้า version ปัจจุบันเป็น version.FIRST หรือ version 1 ให้
        } else if (Version.CURRENT.equals(Version.FIRST)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            filepath = new File(basedirPhoto +"/V1/AudioPhoto/"+String.valueOf(Global.currentposition+1)+".mp3");
        } else {
            return;
        }
        //สส้าง class MediaPlayer  ใหม่
        mPlayer = new MediaPlayer();
        try {
            //สร้างเงื่อนไขว่าถ้า ตำแหน่งปัจจุบัน ยังไม่ได้เเล่นเสียง ให้
            if (!Global.played[Global.currentSound]) {
                //ระบุตำแหน่งที่เก็บไฟล์
                mPlayer.setDataSource(String.valueOf(filepath));
                mPlayer.prepare();
                //เริ่มเล่นเสียง
                mPlayer.start();
                //กำหนดให้ตำหแน่งนี้ มีค่าเป็น true
                Global.played[Global.currentSound] = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupArrar() {

        //นำค่าที่ได้จาก method listAnswer ไปเก็บไว้ในตัวแปป strListAnswer
        strListAnswer = listAnswer();

    }   //setup Arrar

    private void clearlayout() {

        //สร้างตัวแปร ชนิด RadioButton ชื่อ textChoiceA
        RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA_photo_test);
        //กำหนดค่าให้ textChoiceA มีค่าตาม text ที่กำหนดไว้
        textChoiceA.setText("A.Listen and choose.");
        RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB_photo_test);
        textChoiceB.setText("B.Listen and choose.");
        RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC_photo_test);
        textChoiceC.setText("C.Listen and choose.");
        RadioButton textChoiceD = (RadioButton) findViewById(R.id.rdoD_photo_test);
        textChoiceD.setText("D.Listen and choose.");

    }

    //method back
    private void back() {

        //สร้างตัวแปรชนิด button อ้างอิงจาก id btnBack
        Button btnBack = (Button) findViewById(R.id.btnBackTest1);
        //ตรวจสอบการกดปุ่ม เมื่อกดปุ่มให้
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //สร้างเงื่อนไขถ้า currentposition 0 ให้ คืนค่า
                if (Global.currentposition == 0) {
                    return;
                }
                //สร้างเงื่อนไขถ้า currentAnswer 0 ให้ คืนค่า
                if (Global.currentAnswer == 0) {
                    return;
                }
                //ให้ currentAnswer ลดค่าลง 1
                Global.currentAnswer--;
                //ให้ currentAcurrentpositionnswer ลดค่าลง 1
                Global.currentposition--;
                //ให้ currentSound ลดค่าลง 1
                Global.currentSound--;
                //สร้างเงื่อนไขถ้า currentposition มีค่ามากกว่า 0 ให้
                if (Global.currentposition>=0) {
                    //ให้แสดงตำแหน่งของข้อปัจจุบันโดยมีจำนวนข้อมากที่สุด 10 ข้อ
                    String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);

                    TextView txtNo = (TextView) findViewById(R.id.txtnum);
                    txtNo.setText(strNumber);

                }

                //เรียกใช้ method showpicture
                showpicture();
                //เรียกใช้ method playSound
                playSound();

            }
        });

    }

    //สร้าง method next
    private void next() {

        Button btnNext = (Button) findViewById(R.id.btnNextTest1);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ให้ currentAnswer เพิ่มค่าขึ้น 1
                Global.currentposition++;
                //ให้ currentAnswer เพิ่มค่าขึ้น 1
                Global.currentAnswer++;
                //ให้ currentSound เพิ่มค่าขึ้น 1
                Global.currentSound++;

                //สร้างเงื่อนไขว่า ถ้า currentposition มากกว่า maxrow ให้
                if (Global.currentposition > maxrow) {
                    //ให้ currentAnswer มีค่าเป็น 0
                    Global.currentposition = 0;
                    //กำหนดตัวแปร ชนืด Intent ชื่อ intent ให้เปลี่ยน activity จาก mainActivity ไปยัง PracticeListActivity
                    Intent intent = new Intent(PhotosTestActivity.this, DescriptionQandRActivity.class);
                    //เริ่มการเปลี่ยน activity
                    startActivity(intent);

                    //ถ้าไม่ตรงตรมเงื่อนไขข้างต้น ให้
                } else {

                    //ให้แสดงตำแหน่งของข้อปัจจุบันโดยมีจำนวนข้อมากที่สุด 10 ข้อ
                    String strNumber = String.valueOf(Global.currentposition + 1) + "/10";
                    Toast.makeText(getBaseContext().getApplicationContext(), strNumber, Toast.LENGTH_LONG);

                    TextView txtNo = (TextView) findViewById(R.id.txtnum);
                    txtNo.setText(strNumber);

                    //เรียกใช้ method showpicture
                    showpicture();

                    //เรียกใช้ method playSound
                    playSound();
                    //เรียกใช้ method clearcheck
                    clearcheck();
                }
            }
        });
    }

    private void clearcheck() {

        //เครียค่าใน RadioGroup
        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroup1_photo_test);
        rdogroup1.clearCheck();
    }

    //ตรวขจับการกดปุ่ม back จากตัว system หรือ hardware
    public void onBackPressed(){
        //สร้าง dialog
        new AlertDialog.Builder(this)
                //สร้าง title ให้กับ dialog
                .setTitle("Stop the test?")
                //สร้างข้อความให้กับ dialog
                .setMessage("Are you sure you want to quit to test?")
                //สร้างปุ่มให้กับ dialog 2 ปุ่ม คือ ปุ่ม ok และ ปุ่ม cancel
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //กำหนดตัวแปร ชนืด Intent ชื่อ intent ให้เปลี่ยน activity จาก PhotosTestActivity ไปยัง MainActivity
                        Intent intent = new Intent(PhotosTestActivity.this, MainActivity.class);
                        //หยุดเล่นเสียง
                        mPlayer.stop();
                        //เริ่มการเปลี่ยน activity
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //สร้าง icon ให้กับ dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                //แสดง dialog
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();

        //หยุดเล่นเสียง
        mPlayer.stop();
    }


    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listAnswer(){
    //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListAnswer[];
        try {
            //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
            //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION_TEST, new String[]{COLUMN_PHOTO_ANSWER_TEST},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null,null);
            //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
            //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListAnswer = new String[objCursor.getCount()];
            //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i=0; i<objCursor.getCount(); i++){
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER_TEST));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }
            //ปิดตัวชี้
        objCursor.close();

        }catch(SQLException sqle){
            throw sqle;
        }

        return strListAnswer;

    }
}
