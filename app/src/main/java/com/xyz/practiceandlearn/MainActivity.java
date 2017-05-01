package com.xyz.practiceandlearn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.xyz.practiceandlearn.Global.BaseDir;
import static com.xyz.practiceandlearn.Global.basedir;

public class MainActivity extends AppCompatActivity {

    MyDatabase objMyDatabase;
    private PhotoDatabase objPhotoDatabase;
    private QuestionAndResponseDatabase objQandR;
    private ShortConDatabase objShortCon;
    private IncompleteDatabase objIncomplete;
    private ReadingComprehensionDatabase objReading;
    private ReadingComprehensionDatabase objReadingPractice;
    private ShortTalkDatabase objShortTalkScript, objShortTalkQuestion;
    private TextCompletionDatabase objTextCompletionScript, objTextCompletionQuestion;


    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url = "http://103.212.181.17/project/V1.zip";
    private static String file_url2 = "http://103.212.181.17/project/V2.zip";


    String zipFile = BaseDir.toString() + "/V1.zip";
    String unzipLocation = BaseDir.toString() + "/V1/";

    String zipFile2 = BaseDir.toString() + "/V2.zip";
    String unzipLocation2 = BaseDir.toString() + "/V2/";
    //String unzipLocation = Environment.getExternalStorageDirectory() + "/sdcard/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File dir1 = new File(BaseDir + "/V1/V1.zip");
        //Toast.makeText(getBaseContext(),dir1.toString(),Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(),unzipLocation,Toast.LENGTH_LONG).show();
        //Global.played[1] = true;
        //if (Global.played[1])
        //    Toast.makeText(getBaseContext(),"played 1 ok",Toast.LENGTH_LONG).show();

        //Global.played[2] = false;
        //if (!Global.played[2])
        //    Toast.makeText(getBaseContext(),"played 2 false",Toast.LENGTH_LONG).show();


        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PracticeListActivity.class);
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DescriptionPhotoActivity.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir2 = new File(BaseDir + "/V2.zip");
                File dir1 = new File(BaseDir + "/V1.zip");



                if (dir2.exists()) {
                    Toast.makeText(getBaseContext(), "version 2", Toast.LENGTH_SHORT).show();
                } else if (dir1.exists()) {
                    new MainActivity.DownloadFileFromURL().execute(file_url2);
                    Toast.makeText(getBaseContext(), "version 1", Toast.LENGTH_SHORT).show();
                } else {
                    new MainActivity.DownloadFileFromURL().execute(file_url);
                }
            }
        });


        //(this, basedir.toString() + "/V1/TOEIC.db");
        //objPhotoDatabase = new PhotoDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");

        //objPhotoDatabase = new PhotoDatabase(this);
        //objQandR = new QuestionAndResponseDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objShortCon = new ShortConDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objIncomplete = new IncompleteDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objReading = new ReadingComprehensionDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objReadingPractice = new ReadingComprehensionDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objShortTalkScript = new ShortTalkDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objShortTalkQuestion = new ShortTalkDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objTextCompletionScript = new TextCompletionDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");
        //objTextCompletionQuestion = new TextCompletionDatabase(this, Global.basedir.toString() + "/V1/TOEIC.db");


        File pathdatabaseV2 = new File(basedir.toString() + "/V2/TOEIC.db");
        File pathdatabaseV1 = new File(basedir.toString() + "/V1/TOEIC.db");



        //if (pathdatabaseV1.exists() && pathdatabaseV1.isDirectory()) {
            //objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");
            //Toast.makeText(getBaseContext(),"V1",Toast.LENGTH_SHORT).show();

        }



        //File pathdatabaseV2 = new File(basedir.toString() + "/V2/TOEIC.db");
        //File pathdatabaseV1 = new File(basedir.toString() + "/V1/TOEIC.db");
        //if (pathdatabaseV2.exists() && pathdatabaseV2.isDirectory()) {

            //if (pathdatabaseV1.exists() && pathdatabaseV1.isDirectory()) {
                //Global.objMyDatabase = new MyDatabase(this, basedir.toString() + "/V1/TOEIC.db");
            //} else {
             //   Global.objMyDatabase = new MyDatabase(this, basedir.toString() + "/V2/TOEIC.db");
            //}

        //}

        //if V1
        //objMyDatabase = new MyDatabase(this, basedir.toString()+"/V2/TOEIC.db");
        //if V2
        //objMyDatabase = new MyDatabase(this, basedir.toString()+"/V1/TOEIC.db");


        //AddValues();
        //AddValuesToQandR();
        //AddValuesToShortConversation();
        //addValuesShortTalkScript();
        //addValuesShortTalkQuestion();
        //AddValuesToIncompleteSentence();
        //addValuesTextCompletionScript();
        //addValuesTextCompletionQuestion();
        //addValuesReadingComprehensionScript();


        //TEST

        //AddValuesToPhotosTest();
        //AddValuesToQandRTest();
        //AddValuesToShortConTest();
        //AddValuesToShortTalkTest();
        //AddValuesToIncompleteSentenceTest();
        //AddValuesToTextCompletionTest();
        //AddValuesToReadingComprehensionTest();



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     */
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
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

                File dir2 = new File(BaseDir + "/V2.zip");
                File dir1 = new File(BaseDir + "/V1.zip");

                if (dir2.exists()) {
                    DecompressFast decompressFast = new DecompressFast(zipFile2, unzipLocation2);
                    decompressFast.unzip();

                } else if (dir1.exists()) {
                    DecompressFast decompressFast = new DecompressFast(zipFile, unzipLocation);
                    decompressFast.unzip();

                }

                //DecompressFast decompressFast = new DecompressFast(zipFile, unzipLocation);
                //decompressFast.unzip();
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

    class DecompressFast {
        private String _zipFile;
        private String _location;


        public DecompressFast(String zipFile, String location) {
            _zipFile = zipFile;
            _location = location;
            _dirChecker("");
        }

        public void unzip() {
            try {
                FileInputStream fin = new FileInputStream(_zipFile);
                ZipInputStream zin = new ZipInputStream(fin);
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    Log.v("Decompress", "Unzipping " + ze.getName());
                    if (ze.isDirectory()) {
                        _dirChecker(ze.getName());
                    } else {
                        FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                        BufferedOutputStream bufout = new BufferedOutputStream(fout);
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = zin.read(buffer)) != -1) {
                            bufout.write(buffer, 0, read);
                        }
                        bufout.close();
                        zin.closeEntry();
                        fout.close();

                    }
                }
                zin.close();

                Toast.makeText(MainActivity.this, "Unzip complete", Toast.LENGTH_LONG).show();
                Log.d("Unzip", "Unzipping compleate. path : " + _location);


            } catch (Exception e) {
                Log.e("Decompress", "unzip", e);
                Log.d("Unzip", "Unzipping failed");
            }
        }

        private void _dirChecker(String dir) {
            File f = new File(_location + dir);

            if (!f.isDirectory()) {
                f.mkdirs();
            }
        }
    }
}
