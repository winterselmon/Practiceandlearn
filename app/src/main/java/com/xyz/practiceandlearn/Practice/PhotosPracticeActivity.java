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

    //สร้างตัวแปรชนิด MyDatabase ชื่อ objMyDatabase
    MyDatabase objMyDatabase;
    private int currentposition;
    private int currentsound;
    private boolean showanswer;
    private final int maxrow = 39;

    //กำหนดให้ทำการ query ข้อมูลจาก COLUMN_ID_PHOTO_CHOICE น้อยกว่ากว่าหรือเท่ากับ 40 และ จากตาราง PhotoDatabase และ ตรวจสอบเวอร์ชั่นปัจจุบัน
    private  String sql_ver1 = COLUMN_ID_PHOTO_CHOICE + "<=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";
    private  String sql2_ver1 = COLUMN_ID_PHOTO_QUESTION + "<=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";

    //กำหนดให้ทำการ query ข้อมูลจาก COLUMN_ID_PHOTO_CHOICE มากกว่ากว่าหรือเท่ากับ 40 และ จากตาราง PhotoDatabase และ ตรวจสอบเวอร์ชั่นปัจจุบัน
    private  String sql_ver2 = COLUMN_ID_PHOTO_CHOICE + ">=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";
    private  String sql2_ver2 = COLUMN_ID_PHOTO_QUESTION + ">=40 and " + PhotoDatabase.COLUMN_VERSION + " = '" + Version.CURRENT +"'";

    //สร้างตัวแปรชนิด string-array
    private  String[] strListChoiceA, strListChoiceB, strListChoiceC, strListChoiceD, strListDes, strListAnswer;

    //สร้างตัวแปรแบบ medaiplayer
    private MediaPlayer mPlayer, soundtrue, soundwrong;
    String correctAnswer;

    //เมื่อ เข้าสู่ activity นี้ ให้....
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_practice);

        //สร้างตัวแปร ชนิด file ไว้เก็บตำแหน่งของไฟล์ database
        File photoDB = new File(BaseDir + "/V1/TOEIC.db");
        File photoDB2 = new File(BaseDir + "/V1/V2/TOEIC2.db");

        //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ตามตำแหน่งที่ระบุไว้ข้างต้นให้ ทำการเรียกใช้ database
        if (photoDB2.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB2);
            //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ตามตำแหน่งที่ระบุไว้ข้างต้นให้ ทำการเรียกใช้ database
        } else if (photoDB.exists()) {
            objMyDatabase = new MyDatabase(this, photoDB);
        } else {
            return;
        }

        //กำหนดให้ currentposition เริ่มต้นเท่ากัย 0
        currentposition = 0;
        //กำหนดให้ showanswer มีค่าเป็น false
        showanswer = false;

        //เรียกใช้ method showanswer
        showanswer(0);

        //เรียกใช้ method playSound
        playSound();

        //เรียกใช้ method setupArrar
        setupArrar();

        //เรียกใช้ method createlistview
        createlistview();

        //เรียกใช้ method play
        play();

        //เรียกใช้ method next
        next();

        //เรียกใช้ method back
        back();

        //เรียกใช้ method clearlayout
        clearlayout();

        //เรียกใช้ method clearcheck
        clearcheck();

    }

    //สร้าง method play
    private void play() {

        //สร้างตัวแปรชนิด button อ้างอิงจาก id btnPlay
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        //ตรวจจับการกดปุ่ม
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //เมื่อกดปุุ่มจะเรียกใช้ method
                playSound();
            }
        });
    }

    //ตรวขจับการกดปุ่ม back จากตัว system หรือ hardware
    public void onBackPressed() {
        //สร้าง dialog
        new AlertDialog.Builder(this)
                //สร้าง title ให้กับ dialog
                .setTitle("Stop the practice?")
                //สร้างข้อความให้กับ dialog
                .setMessage("Are you sure you want to quit to practice?")
                //สร้างปุ่มให้กับ dialog 2 ปุ่ม คือ ปุ่ม ok และ ปุ่ม cancel
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //กำหนดตัวแปร ชนืด Intent ชื่อ intent ให้เปลี่ยน activity จาก PhotosPracticeActivity ไปยัง MainActivity
                        Intent intent = new Intent(PhotosPracticeActivity.this, MainActivity.class);
                        //เริ่มการเปลี่ยน activity
                        startActivity(intent);
                        //หยุดเล่นเสียง
                        mPlayer.stop();

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
    private void back() {

        //สร้างตัวแปรชนิด button อ้างอิงจาก id btnBack
        Button btnBack = (Button) findViewById(R.id.btnBack);
        //ตรวจสอบการกดปุ่ม เมื่อกดปุ่มให้
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //กำหนดให้ showanswer เป็น false
                showanswer = false;

                //สร้างเงื่อนไขถ้า currentposition มากกว่า 0 ให้
                if (currentposition > 0)
                    //currentposition -1
                    currentposition--;

                //เรียกใช้ method createlistview
                createlistview();

                //เรียกใช้ method clearcheck
                clearcheck();

                //เรียกใช้ method playSound
                playSound();

                //เรียกใช้ method clearlayout
                clearlayout();

                //เรียกใช้ method showanswer อ้างอิง parameter currentposition
                showanswer(currentposition);
            }
        });

    }

    //สร้าง method next
    private void next() {
        //สร้างตัวแปรชนิด button ชื่อ btnNext
        Button btnNext = (Button) findViewById(R.id.btnNext);
        //ตรวจสอบการกดปุ่ม เมื่อกดปุ่ม ให้
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //กำหนดให้ showanswer มีค่าเป็น false
                showanswer = false;
                //สร้างเงื่อนไขว่า ถ้า currentposition น้ยยกว่า maxrow ให้ currentposition เพิ่มขั้น 1
                if (currentposition < maxrow)
                    currentposition++;

                //เรียกใช้ method createlistview
                createlistview();

                //เรียกใช้ method clearcheck
                clearcheck();

                //เรียกใช้ method playSound
                playSound();

                //เรียกใช้ method clearlayout
                clearlayout();
                //เรียกใช้ method showanswer อ้างอิง parameter currentposition
                showanswer(currentposition);

            }
        });

    }

    //สร้าง method ชื่อ playSound
    private void playSound() {
        //สร้างตัวแปรชนิด file ชื่อ filepath มีค่าเป็น null
        File filepath = null;
        //สร้างเงื่อนไขว่าถ้า mPlayer ไม่เท่ากับ null
        if (mPlayer != null) {
            //หยุดเล่นเสียง
            mPlayer.stop();
            mPlayer.release();
        }

        //สร้างเงื่อนไชว่าถ้า version ปัจจุบันเป็น version.SECOND หรือ version 2 ให้
        if (Version.CURRENT.equals(Version.SECOND)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            filepath = new File(basedirPhoto + "/V1/AudioPhotoPratice_V2/" + String.valueOf(currentposition + 1) + ".mp3");
            //ถ้า version ปัจจุบันเป็น version.FIRST หรือ version 1 ให้
        } else if (Version.CURRENT.equals(Version.FIRST)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3 ทั้งหมด
            filepath = new File(basedirPhoto + "/V1/AudioPhotoPratice/" + String.valueOf(currentposition + 1) + ".mp3");
        } else {
            return;
        }
        //สส้าง class MediaPlayer  ใหม่
        mPlayer = new MediaPlayer();
        try {
            //ระบุตำแหน่งที่เก็บไฟล์
            mPlayer.setDataSource(String.valueOf(filepath));
            mPlayer.prepare();
            //เริ่มเล่นเสียง
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //สร้าง method เพื่อเอาไว้ ล้าง layout
    private void clearlayout() {

        //สร้างตัวแปร ชนิด RadioButton ชื่อ textChoiceA อ้างอิงจาก id rdoA
        RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA);
        //กำหนดค่าให้ textChoiceA มีค่าตาม text ที่กำหนดไว้
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

    //สร้าง method เพื่อเอาไว้ แสดงคำตอบ
    private void showanswer(final int p) {

        Button btnANS = (Button) findViewById(R.id.btnAns);
        btnANS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //สร้างเงื่อนไขว่าถ้า showanswer เป็น true ให้
                if (!showanswer) {
                    //สร้างตัวแปร ชนิด RadioButton ชื่อ textChoiceA อ้างอิงจาก id rdoA
                    RadioButton textChoiceA = (RadioButton) findViewById(R.id.rdoA);
                    //กำหนดค่าให้ textChoiceA มีค่าตาม text ที่กำหนดไว้
                    textChoiceA.setText(strListChoiceA[p]);
                    RadioButton textChoiceB = (RadioButton) findViewById(R.id.rdoB);
                    textChoiceB.setText(strListChoiceB[p]);
                    RadioButton textChoiceC = (RadioButton) findViewById(R.id.rdoC);
                    textChoiceC.setText(strListChoiceC[p]);
                    RadioButton textChoiceD = (RadioButton) findViewById(R.id.rdoD);
                    textChoiceD.setText(strListChoiceD[p]);
                    TextView textAnswerA = (TextView) findViewById(R.id.txtAnswerA);
                    textAnswerA.setText(strListDes[p]);
                } else {
                    clearlayout();
                }
                showanswer = !showanswer;

            }
        });

    }

    //สร้าง method setup arrar
    private void setupArrar() {

        //ให้  method listChoiceA เท่ากับ strListChoiceA
        strListChoiceA = listChoiceA();
        //ให้  method listChoiceB เท่ากับ strListChoiceB
        strListChoiceB = listChoiceB();
        //ให้  method listChoiceC เท่ากับ strListChoiceC
        strListChoiceC = listChoiceC();
        //ให้  method listChoiceD เท่ากับ strListChoiceD
        strListChoiceD = listChoiceD();
        //ให้  method listChoiceDes เท่ากับ strListDes
        strListDes = listChoiceDes();
        //ให้  method listAnswerเท่ากับ strListAnswer
        strListAnswer = listAnswer();

    }   //setup Arrar

    //สร้าง method createlistview เพื่อแสดงรูปสำหรับ part:photograph
    private void createlistview() {

        //สร้างตัวแปรชนิด fle และกำหนดให้มีค่าเป็น null
        File imgFile = null;

        //สร้างเงื่อนไขว่าถ้า เวอร์ชั่นปัจจุบันเท่ากับเวอร์ชั่น 2 ให้
        if (Version.CURRENT.equals(Version.SECOND)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3
            imgFile = new File(basedirPhoto + "/V1/photoPratice_V2/" + String.valueOf(currentposition + 1) + ".png");
            //ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่น 1 ให้
        } else if (Version.CURRENT.equals(Version.FIRST)) {
            //ระบุตำแหน่งไฟล์ที่เป็นนามสกุล .mp3
            imgFile = new File(basedirPhoto + "/V1/photoPratice/" + String.valueOf(currentposition + 1) + ".png");
        } else {
            return;
        }

        //ตรวจสอบไฟล์จากตำแหน่งที่ระบุไว้ข้างต้น ถ้าตรวจพบ ให้
        if (imgFile.exists()) {
            //สร้างตัวแปรชนิด bitmap ให้ทำแปลงโค๊ด bitmap จากภาพที่อยู่ในตำแหน่งที่กำหนด
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //สร้างตัวแปร imageview และ อ้างอิงจาก id imgphotos
            ImageView imglist = (ImageView) findViewById(R.id.imgPhotos);
            //แสดงรูปตามตำไฟล์ที่ระบุไว้ข้างต้น
            imglist.setImageBitmap(myBitmap);

        }
    }

    //สร้าง method clearcheck เพื่อ ล้างตัวเลือกใน rdogroup
    private void clearcheck() {

        RadioGroup rdogroup1 = (RadioGroup) findViewById(R.id.rdoGroup1);
        rdogroup1.clearCheck();
    }

    //เมื่อปิดแอปให้เสียงหยุด
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    //เมื่อแอปหยุดให้เสียงหยุด
    @Override
    protected void onPause() {
        super.onPause();

        mPlayer.stop();
    }

    //เมื่อแอปหยุดให้เสียงหยุด
    @Override
    protected void onStop() {
        super.onStop();

        mPlayer.stop();
    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listChoiceA() {
        //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListChoiceA[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_A},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListChoiceA = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListChoiceA[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_A));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }
        //ปิดตัวชี้
        objCursor.close();
        //คืนค่าจากตัวแปร strListChoiceA
        return strListChoiceA;
    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listChoiceB() {
        //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListChoiceB[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_B},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListChoiceB = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListChoiceB[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_B));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }   // for
        //ปิดตัวชี้
        objCursor.close();
        //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
        return strListChoiceB;

    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listChoiceC() {
    //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListChoiceC[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_C},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListChoiceC = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListChoiceC[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_C));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }   // for
        //ปิดตัวชี้
        objCursor.close();
        //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
        return strListChoiceC;

    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listChoiceD() {
    //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListChoiceD[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_CHOICE_D},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListChoiceD = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListChoiceD[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_CHOICE_D));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }   // for
        //ปิดตัวชี้
        objCursor.close();
        //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
        return strListChoiceD;

    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listChoiceDes() {
        //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListChoiceDes[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_CHOICE, new String[]{COLUMN_PHOTO_DES},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql_ver1 : sql_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListChoiceDes = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListChoiceDes[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_DES));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }   // for
        //ปิดตัวชี้
        objCursor.close();
        //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
        return strListChoiceDes;

    }
    //สร้าง method ชนิด string-array ชื่อ listChoiceA เพื่อทำการ query
    private String[] listAnswer() {
        //สร้างตัวแปร ชนิด string-array ชื่อ strListChoiceA และกำหนกให้ค่าเป็น null
        String strListAnswer[] = null;
        //ให้อ่าน database จาก objMyDatabase และเก็บไว้ตัวแปร db ชนิด SQLiteDatabase
        SQLiteDatabase db = objMyDatabase.getReadableDatabase();
        //สร้างตัวแปรชนิด cursor เพื่อ query แถว COLUMN_PHOTO_CHOICE_A จากตาราง PHOTOGRAPHS_CHOICE เพื่อจะส่งไปแสดงผล
        Cursor objCursor = db.query(PHOTOGRAPHS_QUESTION, new String[]{COLUMN_PHOTO_ANSWER},
                //query เพิ่ม ว่า ถ้าเวอร์ชั่นปัจจุบันเป็นเวอร์ชั่นแรก จะใช้ตัวแปร sql_ver1 ถ้าไม่ใช่ จะใช้ sql_ver2
                Version.CURRENT.equals(Version.FIRST) ? sql2_ver1 : sql2_ver2, null, null, null, null);
        //ให้ตัวชี้หรือ cursor ไปอยู่ที่ตัวแรก
        objCursor.moveToFirst();
        //นับจำนวนใน คอลลั่ม แล้วนำไปเก็บไว้ใน strListChoiceA
        strListAnswer = new String[objCursor.getCount()];
        //สร้างลูปขึ้นโดยให้ i เริ่มต้นเท่ากับ 0 และถ้า objCursor มีค่าน้อยกว่า i จะให้ i มีค่าเพิ่มขึ้น 1
        for (int i = 0; i < objCursor.getCount(); i++) {
            //รับค่าชนิด string ในแถว COLUMN_PHOTO_CHOICE_A ไปเก็บไว้ใน strListChoiceA[i] และมีการอ้างอิง parameter
            strListAnswer[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PHOTO_ANSWER));
            //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
            objCursor.moveToNext();
        }
        //ปิดตัวชี้
        objCursor.close();
        //ให้ตัวชี้ หรือ currsor ชี้ตัวถัดไป
        return strListAnswer;

    }
}


