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

    public static int currentposition = 0;

    public static long startTime =0;

    //public static boolean played[];

    public static boolean[] played = new boolean[200];

    public static boolean[] collect = new boolean[200];

    public static int currentAnswer = 0;

    public static int currentSound = 0;

    //public static File basedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    public static String basedir = Environment.getExternalStorageDirectory() + "/sdcard/";

    public static File basedirPhoto = Environment.getExternalStorageDirectory();

    public static File basedirSound = Environment.getExternalStorageDirectory();

    public static File BaseDir = Environment.getExternalStorageDirectory();


    //public static MyDatabase objMyDatabase;

}