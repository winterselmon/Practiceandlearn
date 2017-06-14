package com.xyz.practiceandlearn.Main;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by 10.10 on 3/13/2017.
 */



public class Global {

    //สร้างตัวแปรชนิด int ชื่อ currentposition เท่ากับ 0
    public static int currentposition = 0;

    //สร้างตัวแปรชนิด long ชื่อ startTime เท่ากับ 0
    public static long startTime =0;

    //สร้างตัวแปรชนิด boolean[] ชื่อ played เท่ากับ array 200 ตำแหน่ง
    public static boolean[] played = new boolean[200];

    //สร้างตัวแปรชนิด boolean[] ชื่อ collect เท่ากับ array 200 ตำแหน่ง
    public static boolean[] collect = new boolean[200];

    //สร้างตัวแปรชนิด int ชื่อ currentAnswer เท่ากับ 0
    public static int currentAnswer = 0;

    //สร้างตัวแปรชนิด int ชื่อ currentSound เท่ากับ 0
    public static int currentSound = 0;

    //สร้างตัวแปรชนิด String ชื่อ basedir ไว้ระบุตำแหน่งใน directory
    public static String basedir = Environment.getExternalStorageDirectory() + "/sdcard/";

    //สร้างตัวแปรชนิด File ชื่อ basedirPhoto ไว้ระบุตำแหน่งใน directory
    public static File basedirPhoto = Environment.getExternalStorageDirectory();

    //สร้างตัวแปรชนิด File ชื่อ basedirSound ไว้ระบุตำแหน่งใน directory
    public static File basedirSound = Environment.getExternalStorageDirectory();

    //สร้างตัวแปรชนิด File ชื่อ BaseDir ไว้ระบุตำแหน่งใน directory
    public static File BaseDir = Environment.getExternalStorageDirectory();
}