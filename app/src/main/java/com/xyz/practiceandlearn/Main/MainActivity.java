package com.xyz.practiceandlearn.Main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xyz.practiceandlearn.Database.IncompleteDatabase;
import com.xyz.practiceandlearn.Database.MyDatabase;
import com.xyz.practiceandlearn.Database.PhotoDatabase;
import com.xyz.practiceandlearn.Database.QuestionAndResponseDatabase;
import com.xyz.practiceandlearn.Database.ReadingComprehensionDatabase;
import com.xyz.practiceandlearn.Database.ShortConDatabase;
import com.xyz.practiceandlearn.Database.ShortTalkDatabase;
import com.xyz.practiceandlearn.Database.TextCompletionDatabase;
import com.xyz.practiceandlearn.Description.DescriptionPhotoActivity;
import com.xyz.practiceandlearn.Practice.PracticeListActivity;
import com.xyz.practiceandlearn.R;
import com.xyz.practiceandlearn.Version;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.xyz.practiceandlearn.Main.Global.basedirSound;

public class MainActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private PhotoDatabase objPhotoDatabase;
    private QuestionAndResponseDatabase objQandR;
    private ShortConDatabase objShortCon;
    private IncompleteDatabase objIncomplete;
    private ReadingComprehensionDatabase objReading;
    private ReadingComprehensionDatabase objReadingPractice;
    private ShortTalkDatabase objShortTalkScript;
    private TextCompletionDatabase objTextCompletionScript;

    //สร้าง progressDialog ชื่อ pDialog
    private ProgressDialog pDialog;
    //กำหนดตัวแปรชนิด int ชื่อ progress_bar_type ให้มีค่าเริ่มต้นเท่ากับ 0
    public static final int progress_bar_type = 0;

    //path url ไว้สำหรับ download
    private static String file_url = "http://103.212.181.17/project/V1.zip";
    private static String file_url2 = "http://103.212.181.17/project/V2.zip";

    //สร้างตัวแปรชนิด string ชื่อ KEY_VERSION ให้มีค่าเท่ากับ version
    public static final String KEY_VERSION = "Version";

    //สร้างตัวแปร ชนิด string ให้มีค่าเท่ากับ Global.BaseDir.toString() + "/V1.zip"
    String zipFile = Global.BaseDir.toString() + "/V1.zip";
    //สร้างตัวแปร ชนิด string ให้มีค่าเท่ากับ Global.BaseDir.toString() + "/V1/";
    String unzipLocation = Global.BaseDir.toString() + "/V1/";

    //สร้างตัวแปร ชนิด string ให้มีค่าเท่ากับ Global.BaseDir.toString() + "/V2.zip";
    String zipFile2 = Global.BaseDir.toString() + "/V2.zip";
    //สร้างตัวแปร ชนิด string ให้มีค่าเท่ากับ Global.BaseDir.toString() + "/V2/";
    String unzipLocation2 = Global.BaseDir.toString() + "/V2/";

    //สร้างตัวแปร ชนิด string ให้เก็บค่า http://103.212.181.17/project/V2.zip
    String customURL = "http://103.212.181.17/project/V2.zip";

    //สร้างตัวแปร ชนิด TextView ชื่อ txtVersion
    TextView txtVersion;

    //เมื่อ เข้าสู่ activity นี้ ให้....
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ผูก txtversion ด้วย id txtversion
        txtVersion = (TextView) findViewById(R.id.txtversion);
        //สร้างเงื่อนไขว่า ถ้า Version.CURENT มีค่าตรงกับ Version.FIRST ให้
        if (Version.CURRENT.equals(Version.FIRST)) {
            //set ค่า txtVersion ให้เป็นคำว่า Version 1
            txtVersion.setText("Version 1");
            //ถ้าไม่ใช่ ให้
        } else {
            //set ค่า txtVersion ให้เป็นคำว่า Version 2
            txtVersion.setText("Version 2");
        }

        //สร้างตัวแปร File ชื่อ dirV1 มีค่าเท่ากับ ตำแหน่งไฟล์ Global.BaseDir + "/V1.zip"
        File dirV1 = new File(Global.BaseDir + "/V1.zip");

        //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ dirV1 ให้ไม่ต้องทำอะไร
        if (dirV1.exists()) {
            //ถ้าไม่พอไฟล์ให้
        } else {
            //set ค่า txtVersion ให้เป็นคำว่า Version 1
            txtVersion.setText("Version 1");
            //ให้เริ่มการดาวโหลดไฟล์จาก url
            new MainActivity.DownloadFileFromURL().execute(file_url);
        }
        //สร้างตัวแปร ชนิด button ชื่อ btn อ้างอิงจาก id button
        Button btn = (Button) findViewById(R.id.button);

        //กำหนดว่าเมื่อกดปุ่มจะให้ทำอะไร
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //กำหนดตัวแปร ชนืด Intent ชื่อ intent ให้เปลี่ยนจากหน้า mainActivity ไปยังหน้า PracticeListActivity
                Intent intent = new Intent(MainActivity.this, PracticeListActivity.class);
                //เริ่มการเปลี่ยน activity
                startActivity(intent);
            }
        });

        //สร้างตัวแปร ชนิด button ชื่อ btn อ้างอิงจาก id button2
        Button btn2 = (Button) findViewById(R.id.button2);
        //กำหนดว่าเมื่อกดปุ่มจะให้ทำอะไร
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //กำหนดตัวแปร ชนืด Intent ชื่อ intent ให้เปลี่ยนจากหน้า mainActivity ไปยังหน้า DescriptionPhotoActivity
                Intent intent = new Intent(MainActivity.this, DescriptionPhotoActivity.class);
                //เริ่มการเปลี่ยน activity
                startActivity(intent);
            }
        });

        //สร้างตัวแปร ชนิด button ชื่อ btn อ้างอิงจาก id button3
        Button btn3 = (Button) findViewById(R.id.button3);
        //กำหนดว่าเมื่อกดปุ่มจะให้ทำอะไร
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //สร้างตัวแปร ชนิด TextView ชื่อ txtVersion อ้างอิงจาก id txtversion
                TextView txtVersion = (TextView) findViewById(R.id.txtversion);
                //สร้างเงื่อนไขว่าถ้า Version.CURRENT มีค่าตรงกับ Version.FIRST ให้
                if (Version.CURRENT.equals(Version.FIRST)) {
                    //กำหนดให้ Version.CURRENT = Version.SECOND
                    Version.CURRENT = Version.SECOND;
                    //set ค่า txtVersion ให้เป็นคำว่า Version 2
                    txtVersion.setText("Version 2");
                    //ถ้าไม่ตรงกันให้
                } else {
                    //กำหนดให้ Version.CURRENT = Version.SECOND
                    Version.CURRENT = Version.FIRST;
                    //set ค่า txtVersion ให้เป็นคำว่า Version 2
                    txtVersion.setText("Version 1");
                }
            }
        });
    }

    @Override

    //สร้าง method ชื่อ onCreateDialog มี parameter ชื่อ id ชนิด int
    protected Dialog onCreateDialog(int id) {
        //สร้างเงื่อนไขหลัก โดย อิง parameter ชื่อ id
        switch (id) {
            //ถ้า progress_bar_type เป็น 0
            case progress_bar_type: // we set this to 0
                //สร้าง ProgressDialog ใหม่
                pDialog = new ProgressDialog(this);
                //สร้างข้อความใน Dialog Downloading file. Please wait...
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                //สร้างค่าสูงสุดอยู่ที่ 100
                pDialog.setMax(100);
                //กำหนด style ให้ dialog เป็นแบบ horizontal
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //กำหนดให้ปิด dialog ได้
                pDialog.setCancelable(true);
                //แสดง dialog
                pDialog.show();
                //คืนค่า dialog
                return pDialog;
            //กำหนดค่าเดิม
            default:
                //คืนค่าเป็น null
                return null;
        }
    }

    /**
     * Background Async Task to download file
     */
    //สร้าง class ชื่อ DownloadFileFromURL และมีการใช้ class AsyncTask
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        //สร้าง method onPreExecute คือเมื่อเริ่มทำงานให้ show dialog
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        //สร้าง method doInBackground คือ ให้ทำการดาวโหลดไฟล์โดยอ้างอิงจาก url ข้างต้น
        protected String doInBackground(String... f_url) {
            //สร้างตัวแปป ชนิด int ชื่อ count
            int count;
            //สร้างตัวตรวจสอบ try catch คือถ้าโค๊ดใน try ไม่มีปัญหาก็จะทำงานตามปกติ
            try {
                //สร้างตัวแปร ชนิด URL ชื่อ url อ้างอิง paremeter f_url
                URL url = new URL(f_url[0]);
                //ให้ตัวแปร url เปิด connection และเก็บไว้ในตัวแปร conection
                URLConnection conection = url.openConnection();
                //ให้ conection ทำการเชื่อมต่อ
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream คือ กำหนดตำแหน่งไฟล์ที่ทำการดาวโหลดมา
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()
                        + "/V1.zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                //กำหนด path สำหรับระบุตำแหน่งของไฟล์หลังจาก unzip
                File dir2 = new File(Global.BaseDir + "/V2.zip");
                File dir1 = new File(Global.BaseDir + "/V1.zip");

                //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ dirV2 ให้ทำการเรียกใช้คลาส DecompressFast เพื่อ unzip ไฟล์
                if (dir2.exists()) {
                    DecompressFast decompressFast = new DecompressFast(zipFile2, unzipLocation2);
                    decompressFast.unzip();
                    //สร้างเงื่อนไขว่าถ้าตรวจพบไฟล์ dirV1 ให้ทำการเรียกใช้คลาส DecompressFast เพื่อ unzip ไฟล์
                } else if (dir1.exists()) {
                    DecompressFast decompressFast = new DecompressFast(zipFile, unzipLocation);
                    decompressFast.unzip();
                }
                //โค๊ตเกิด error ให้ แสดง Log เพื่อที่จะตรวจสอบ error ได้
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }
    }

    //สร้าง class onBackPressed เพื่อ ตรวจสอบการกดปุ่ม back ของตัว system
    public void onBackPressed() {
        //สร้าง alertDialog
        new AlertDialog.Builder(this)
                //กำหนด title ให้กับ dialog
                .setTitle("Stop the app?")
                //กำหนดข้อความ ให้กับ dialog
                .setMessage("Are you sure you want to exit?")
                //เพิ่มปุ่ม ok ให้กับ dialog
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //เมื่อกด ok ให้จบการทำงานของ application
                        finish();
                        moveTaskToBack(true);
                    }
                })
                //เพิ่มปุ่ม cancel ให้กับ dialog เมื่อกดจะอยู่ที่หน้าเดิม
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //กำหนดไอคอนให้กับ dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                //โชว dialog
                .show();
    }

    //สร้าง class DecompressFast
    class DecompressFast {
        //สร้างตัวแปร ชนิด string ชื่อ _zipFile
        private String _zipFile;
        //สร้างตัวแปร ชนิด string ชื่อ _location
        private String _location;

        //เรียกใช้คลาส DecompressFast
        public DecompressFast(String zipFile, String location) {
            //กำหนดให้ _zipFile เก็บ zipFile
            _zipFile = zipFile;
            //กำหนดให้ _location เก็บ location
            _location = location;
            _dirChecker("");
        }

        //สร้าง method unzip
        public void unzip() {
            try {
                //รับข้อมูลจากไฟล์
                FileInputStream fin = new FileInputStream(_zipFile);
                //รับข้อแบบ zip ไฟล์
                ZipInputStream zin = new ZipInputStream(fin);
                //กำหนดให้ ZipEntry มีค่าเป็น null
                ZipEntry ze = null;
                //สร้างลูป while ถ้า ze ค่าไม่ใช่ null ให้
                while ((ze = zin.getNextEntry()) != null) {
                    //แสดงค่า log
                    Log.v("Decompress", "Unzipping " + ze.getName());
                    //สร้างเงื่อนไขว่า ถ้า ze มีอยู่ใน Directory ให้ _dirChecker มีค่าเป็น ชื่อ zipentry
                    if (ze.isDirectory()) {
                        _dirChecker(ze.getName());
                        //ถ้า ze ไม่อยู่ใน Directory ให้
                    } else {
                        //ส่งข้อมูลชนิด file ไปยัง location ที่กำหนด
                        FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                        //ขนาดข้อมูลที่ส่งไป
                        BufferedOutputStream bufout = new BufferedOutputStream(fout);
                        //กำหนดขนาด buffer 1024 byte
                        byte[] buffer = new byte[1024];
                        //สร้างตัวแปรชนิด int ชื่อ read มีค่าเท่ากับ 0
                        int read = 0;
                        //สร้างลูป ถ้า read ไม่ใช่ -1 ให้
                        while ((read = zin.read(buffer)) != -1) {
                            //เขียนไฟล์ลงใน location ที่กำหนด
                            bufout.write(buffer, 0, read);
                        }
                        //หยุดการทำงาน หรือก็คือ แตกไฟล์เสร็จแล้ว
                        bufout.close();
                        zin.closeEntry();
                        fout.close();

                    }
                }
                //หยุดรับข้อมูล zip ไฟล์
                zin.close();
                Log.d("Unzip", "Unzipping compleate. path : " + _location);

                //โค๊ตเกิด error ให้ แสดง Log เพื่อที่จะตรวจสอบ error ได้
            } catch (Exception e) {
                Log.e("Decompress", "unzip", e);
                Log.d("Unzip", "Unzipping failed");
            }
        }

        //สร้าง metod _dirChecker ไว้เก็บตำแหน่งไฟล์
        private void _dirChecker(String dir) {
            File f = new File(_location + dir);

            if (!f.isDirectory()) {
                f.mkdirs();
            }
        }
    }
}
