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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

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

    String zipFile = Environment.getExternalStorageDirectory() + "/V1.zip";
    String unzipLocation = Environment.getExternalStorageDirectory() + "/sdcard/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Global.played[1] = true;
        //if (Global.played[1])
        //    Toast.makeText(getBaseContext(),"played 1 ok",Toast.LENGTH_LONG).show();

        //Global.played[2] = false;
        //if (!Global.played[2])
        //    Toast.makeText(getBaseContext(),"played 2 false",Toast.LENGTH_LONG).show();



        objPhotoDatabase = new PhotoDatabase(this);
        objQandR = new QuestionAndResponseDatabase(this);
        objShortCon = new ShortConDatabase(this);
        objIncomplete = new IncompleteDatabase(this);
        objReading = new ReadingComprehensionDatabase(this);
        objReadingPractice = new ReadingComprehensionDatabase(this);
        objShortTalkScript = new ShortTalkDatabase(this);
        objShortTalkQuestion = new ShortTalkDatabase(this);
        objTextCompletionScript = new TextCompletionDatabase(this);
        objTextCompletionQuestion = new TextCompletionDatabase(this);

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
                Intent intent = new Intent(MainActivity.this, PhotosTestActivity.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory() + "V1.zip");
                if (dir.exists() && dir.isDirectory()) {
                    Toast.makeText(MainActivity.this,"Last varsion",Toast.LENGTH_LONG).show();
                }else {
                    new MainActivity.DownloadFileFromURL().execute(file_url);

                }
            }
        });


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


    }

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
     * */
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
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
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
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
                DecompressFast decompressFast = new DecompressFast(zipFile, unzipLocation);
                decompressFast.unzip();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
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
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());
                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1){
                        bufout.write(buffer,0,read);
                    }
                    bufout.close();
                    zin.closeEntry();
                    fout.close();

                }
            }
            zin.close();

            Toast.makeText(MainActivity.this,"Unzip complete",Toast.LENGTH_LONG).show();
            Log.d("Unzip","Unzipping compleate. path : " + _location);


        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
            Log.d("Unzip","Unzipping failed");
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}



    private void AddValues(){


        // photo_question
        objPhotoDatabase.AddValuesToPhotographsQuestion("1","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("2","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("3","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("4","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("5","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("6","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("7","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("8","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("9","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("10","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("11","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("12","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("13","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("14","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("15","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("16","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("17","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("18","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("19","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("20","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("21","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("22","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("23","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("24","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("25","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("26","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("27","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("28","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("29","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("30","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("31","a");
        objPhotoDatabase.AddValuesToPhotographsQuestion("32","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("33","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("34","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("35","d");
        objPhotoDatabase.AddValuesToPhotographsQuestion("36","c");
        objPhotoDatabase.AddValuesToPhotographsQuestion("37","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("38","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("39","b");
        objPhotoDatabase.AddValuesToPhotographsQuestion("40","d");


        // photo_choice
        objPhotoDatabase.AddValuesToPhotographsChoice("1","He's lighting a fire", "He's riding a bicycle.", "He's working with a wheel.", "He's getting into a car.",
                "A.The man is not lighting a fire. \n"+ "B.The man is standing on the floor, not riding a bicycle. \n"+
                "C.The man is doing something to a wheel attached to a machine and there are many wheels piled up behind him,so he is probably a tire fitter. He's working with a wheel best describes the picture.\n"+
                "D.The wheels  may be from cars, but the man is not getting into a car.", "1");
        objPhotoDatabase.AddValuesToPhotographsChoice("2", "There's construction equipment on the field.", "There are buildings by the road.", "There's a lot of traffic on the highway.", "There are signs along the roadside.",
                "A.In the picture there is a field with some heavy equipment on it, probably construction equipment.\n"+ "B.The equipment may be for constructing a road, but there is no road in the picture and there are no buildings.\n"+
                        "C.The picture does not show traffic on a highway.\n"+ "D.The picture does not show a roadside with signs along it.", "2");
        objPhotoDatabase.AddValuesToPhotographsChoice("3", "The man is washing the windows.", "There are trees along the edge of the street.", "People are lined up to get into the building.", "It's crowded on the walkway.",
                "A.The man in the picture is walking, not washing the windows.\n"+
                "B.In the picture there is a line of trees planted along the sidewalk next to a street, so the best description is There are trees along the edge of the street.\n"+
                "C.There are no people lined up to get into the building.\n"+
                "D.The walkway is not crowded.", "3");
        objPhotoDatabase.AddValuesToPhotographsChoice("4", "They're chopping wood.", "They're putting on their jackets.", "They're under a tree.", "They're hiking in the woods.",
                "A.The people are not chopping wood.\n"+
                "B.Some of the people are wearing jackets, but they are not putting on their jackets.\n"+
                "C.The picture shows some people under a tree.\n"+
                "D.The tree is not in the woods and the people are not hiking.", "4");
        objPhotoDatabase.AddValuesToPhotographsChoice("5", "The boat is leaving the dock.", "The people are near some water.", "The people are getting into the water.", "The boat is being loaded with cargo.",
                "A.The picture does not show a boat leaving a dock.\n"+
                "B.The picture shows two people on a structure over some water, so they are near some water.\n"+
                "C.The people are not getting into the water.\n"+
                "D.There is no boat being loaded with cargo.", "5");
        objPhotoDatabase.AddValuesToPhotographsChoice("6", "She's reading the labels on some boxes.", "She's sorting mail into the slots on the wall.", "She's wrapping up a package.", "She's standing in line at the service window.",
                "A.The woman in the picture seems to be reading the labels on the boxes, so (A) best describes the picture.\n"+
                "B.This may be a mail sorting office, but the woman is not sorting the mail into the slots on the wall.\n"+
                "C.She is looking at the packages, not wrapping up a package.\n"+
                "D.The picture does not show anybody standing in line at a service window.", "6");
        objPhotoDatabase.AddValuesToPhotographsChoice("7", "The flowers have been arranged in vases.", "The tables have been set for dinner.", "The chairs are stacked against the wall.", "The restaurant has an outdoor seating area.",
                "A.There are no flowers in vases in the picture.\n"+
                "B.The tables have no knives and forks, or plates and glasses on them, so they have not been set for dinner.\n"+
                "C.The chairs are around the tables, not stacked against the wall.\n"+
                "D.The tables are outside. They have tablecloths on them and chairs arranged around them, so this is most likely the outdoor seating area of a restaurant.", "7");
        objPhotoDatabase.AddValuesToPhotographsChoice("8", "The lights are being installed above the pictures.", "There's a statue in the corner.", "There are many visitors in the gallery.", "The pictures are mounted on the wall.",
                "A.There are lights above the pictures, but they are not being installed now.\n"+
                "B.There is a tree in the corner, not a statue.\n"+
                "C.This may be a gallery, but there are no visitors.\n"+
                "D.The picture is of a display of pictures on a wall, so The pictures are mounted on the wall best describes what we see.", "8");
        objPhotoDatabase.AddValuesToPhotographsChoice("9", "She's trying on a shirt.", "She's holding an item of clothing.", "She's cleaning out her closet.", "She's paying for some new clothes.",
                "A.She is not trying on a shirt.\n"+
                "B.The woman is standing next to a rack of clothes. She is holding an item of clothing.\n"+
                "C.The picture is not of a woman cleaning out her closet.\n"+
                "D.She is not paying for some clothes, though she is probably in a shop.", "9");
        objPhotoDatabase.AddValuesToPhotographsChoice("10", "A bridge is under construction.", "A train has pulled into the station.", "There are many lanes of traffic.", "The people are getting out of their vehicles.",
                "A.There is a bridge over the road, but it is not under construction.\n"+
                "B.The picture does not show a train at a station.\n"+
                "C.The picture shows a road with many lanes of traffic going in different directions.\n"+
                "D.The picture does not show people getting out of their vehicles.", "10");
        objPhotoDatabase.AddValuesToPhotographsChoice("11", "She's writing on the board.", "She's facing the chair.", "She's reading a book.", "She's buying a pen.",
                "A.The woman in the picture is facing a board and holding a pen. She appears to be writing, so (A) is correct.\n"+
                "B.The woman is standing beside a chair, not facing the chair.\n"+
                "C.She is not reading a book.\n"+
                "D.She is holding a pen, not buying a pen.", "11");
        objPhotoDatabase.AddValuesToPhotographsChoice("12", "He's cutting a tree.", "He's hanging a picture frame.", "He's putting away his tools.", "He's doing construction work.",
                "A.There is no tree in the picture and he is not cutting anything.\n"+
                "B.He is working near the ground, so he is not hanging a picture frame.\n"+
                "C.Tools can be seen, but he is not putting away his tools.\n"+
                "D.A man is working in a building that is under construction. There are tools around him and he is measuring something, so (D) is the best description.", "12");
        objPhotoDatabase.AddValuesToPhotographsChoice("13", "She's wrapping a package.", "She's carrying a box.", "She's moving some books.", "She's folding the paper in half.",
                "A.She is holding a package, not wrapping a package.\n"+
                "B.The picture shows a woman with a box in her hand,so she's carrying a box best describes the picture.\n"+
                "C.She is moving boxes, not moving some books.\n"+
                "D.She is holding a paper, not folding a paper.", "13");
        objPhotoDatabase.AddValuesToPhotographsChoice("14", "The vehicles are stuck in traffic.", "The students are writing something down.", "The motorbikes are parked in rows.", "The windows have been opened.",
                "A.This is a parking place for motorbikes, so the motorbikes are parked, not stuck in traffic.\n"+
                "B.There are no students writing.\n"+
                "C.There are many motorbikes parked beside one another,so (C) is the best description.\n"+
                "D.There are some buildings , but we cannot see whether their windows have been opened.", "14");
        objPhotoDatabase.AddValuesToPhotographsChoice("15", "The drawers are being opened.", "The table is set for a meal.", "The seats are all taken.", "The fruit is being sliced.",
                "A.The drawers in the cabinet are not being opened They are closed.\n"+
                "B.There is a table with a tablecloth,knives and forks, and glasses, so the table is set for a meal best describes the picture.\n"+
                "C.Nobody is sitting in the chairs, so the seats are all taken is not correct.\n"+
                "D.The fruit in the picture is not being sliced.", "15");
        objPhotoDatabase.AddValuesToPhotographsChoice("16", "She's paying for her food.", "She's pushing a shopping cart.", "She's carrying a suitcase.", "She's buying a car.",
                "A.The woman is not at the checkout paying for her food.\n"+
                "B.The woman is in a large store. She's pushing a shopping cart best describes what we see.\n"+
                "C.She is carrying a bag on her shoulder, not carrying a suitcase.\n"+
                "D.She is pushing a shopping cart, not buying a car.", "16");
        objPhotoDatabase.AddValuesToPhotographsChoice("17", "Some people are sitting on benches.", "Some people are riding bicycles.", "Some people are fixing a traffic light.", "Some people are sitting on steps.",
                "A.There are no people sitting on benches.\n"+
                "B.The picture shows some people crossing a road and two of them are riding bicycles, so (B)is the best description.\n"+
                "C.The picture does not show people fixing a traffic light.\n"+
                "D.There are no people sitting on steps.", "17");
        objPhotoDatabase.AddValuesToPhotographsChoice("18", "She's washing her hands.", "She's turning on a fan.", "She's reaching to pick something up.", "She's recording information on a clipboard.",
                "A.She is not washing her hands.\n"+
                "B.There is not a fan near her, so she is not turning on a fan.\n"+
                "C.The woman's arm is stretched out and it looks as if she is going to pick something up, so she's reaching to pick something up is the best description.\n"+
                "D.She is not writing anything down, so she is not recording information on a clipboard.", "18");
        objPhotoDatabase.AddValuesToPhotographsChoice("19", "The man is climbing a stone wall.", "Some workers are cleaning up some rocks.", "The man is looking at a book.", "Stones across the road are blocking traffic.",
                "A.He is standing next to a stone wall, not climbing a stone wall.\n"+
                "B.There are no workers cleaning up rocks.\n"+
                "C.A man is looking down at something in his hands, most likely a book,so the man is looking at a book best describes the picture.\n"+
                "D.The picture does not show stones blocking traffic.", "19");
        objPhotoDatabase.AddValuesToPhotographsChoice("20", "Workers are building a road.", "People are loading a cargo ship.", "Buildings are facing the harbor.", "Water is flowing down a hill.",
                "A.The picture does not show workers building a road.\n"+
                "B.We cannot see people loading a cargo ship in the harbor.\n"+
                "C.The windows of the buildings look onto the harbor,so the buildings are facing the best describes the picture.\n"+
                "D.There is water, but it is not flowing down a hill.", "20");
        objPhotoDatabase.AddValuesToPhotographsChoice("21", "The picnic area ls outside the park.", "The cars are parked in the shade.", "There is a statue in the middle of the square.", "There aren't any cars in the parking lot.",
                "D.There are no motor vehicles in the parking area (parking lot) and so (B) no cars are parked in-the shade. The space is neither (A) a park nor (C) a square.", "21");
        objPhotoDatabase.AddValuesToPhotographsChoice("22", "She's examining a sample under a microscope.", "The magnifying glass is kept in the drawer.", "She's looking through some files.", "She's having her eyesight checked.",
                "A.The lab technician is working with a microscope and not (B) a magnifying glass.\n"+
                "C.She is looking through the instrument, not some flies.\n"+
                "D.She is examining something,not having her eyes examined.", "22");
        objPhotoDatabase.AddValuesToPhotographsChoice("23", "The painting's on the living room wall.", "The artwork's being sold at auction.", "The visitors are admiring the collections.", "She's painting a picture.",
                "C.The people are looking at the paintings on display in a museum and not (A) in a private home.\n"+
                "B.The works of art are not for sale.\n"+
                "D.A woman is taking a photograph and not painting a picture.", "23");
        objPhotoDatabase.AddValuesToPhotographsChoice("24", "He's serving their meal.", "The couple is sitting side by side.", "The dining car is full.", "The waiter's training the new staff.",
                "B.The couple is sitting next to each other and the waiter is taking their order not (A) serving a meal.\n"+
                "C.The other tables are empty.\n"+
                "D.The waiter is working on a train but is not training staff.", "24");
        objPhotoDatabase.AddValuesToPhotographsChoice("25", "They're moving the desk.", "They're watching a television show.", "She's developing the film.", "The woman is facing the camera.",
                "A.She is seated at her desk and is not moving it.\n"+
                "B.They are recording a television broadcast, not watching a TV show.\n"+
                "C.The cameraman is filming the woman and she is not developing a film.\n"+
                "D.The news presenter is sitting in front of the camera.", "25");
        objPhotoDatabase.AddValuesToPhotographsChoice("26", "They're resting at the top of the hill.", "The gate has been left open.", "They're hiking through the countryside.", "They're loading the bikes onto a rack.",
                "A.They are cycling not resting.\n"+
                "B.The cyclists are going through an open gate.\n"+
                "C.They are biking and not hiking.\n"+
                "D.They cannot be loading the bikes since they are riding them.", "26");
        objPhotoDatabase.AddValuesToPhotographsChoice("27", "The workers are wearing hard hats.", "One of the men is smoking a pipe.", "The men are installing new software.", "They're filling up at the gas station.",
                "A.The men are wearing protective headgear (hardhats).\n"+
                "C.They are installing heavy equipment not software and (B) neither man is smoking a pipe.\n"+
                "D.They are working in an oilfield and are not at the gas station.", "27");
        objPhotoDatabase.AddValuesToPhotographsChoice("28", "He's studying with a friend.", "Class is being held in the computer lab.", "He's checking out a book at the library.", "The laptop is set up on the table.",
                "A.He is sitting alone and not studying with a friend.\n"+
                "B.He is not sitting in a computer lab classroom.\n"+
                "C.There is a book on the table but he is not checking it out from the library.\n"+
                "D.The man is working on his laptop which is open in front of him.", "28");
        objPhotoDatabase.AddValuesToPhotographsChoice("29", "The passengers are on the platform.", "The train is pulling away from the station.", "They're packing their bags.", "They're boarding the plane.",
                "A.They are on the platform.\n"+
                "B.The train is stopped.\n"+
                "C.The people are carrying their bags and not packing them.\n"+
                "D.They are in a train station so they cannot be boarding a plane.", "29");
        objPhotoDatabase.AddValuesToPhotographsChoice("30", "The audience is seated in front of the stage.", "They're listening to his speech.", "He's delivering some equipment.", "They're exchanging addresses.",
                "B.The man is talking to members of the audience who (A) are standing next to the stage and not in front of it.\n"+
                "C.He is delivering a speech, not equipment.\n"+
                "D.He is addressing the audience, not exchanging addresses.", "30");
        objPhotoDatabase.AddValuesToPhotographsChoice("31", "The fuel truck is by the jet.", "The crane is flying low.", "The plan is to buy fuel.", "The playing field is wet.",
                "A.The fuel truck is by the jet.\n"+
                "Choice (B) is incorrect because the plane's fuel tank may be low, but the plane is not flying low. It also confuses the similar sounding word crane for plane.\n"+
                "Choice (C) confuses the similar sounds plan with plane and uses the associated word fuel.\n"+
                "Choice (D) confuses the similar sounds playing and plane and jet and wet", "31");
        objPhotoDatabase.AddValuesToPhotographsChoice("32", "The driver is behind the wheel.", "The electrician is around the corner.", "The technician is at the controls.", "The chef is near the stove.",
                "C.The technician is looking at the gauges and dials to check how the systems are functioning.\n"+
                "Choices (A), (B), and (D) misidentify the occupation and location.", "32");
        objPhotoDatabase.AddValuesToPhotographsChoice("33", "The audience is listening to a concert.", "The speaker is addressing the group.", "The workers are returning to their jobs.", "The musicians are watching the conductor.",
                "Choice (A) is incorrect because the audience is listening to the speaker, not to a concert.\n"+
                "B.The speaker is addressing the group.\n"+
                "Choice (C) is a different context; the audience may be workers, but they are sitting, not returning.\n"+
                "Choice (D) is out of context; these are not musicians in an orchestra with a conductor.", "33");
        objPhotoDatabase.AddValuesToPhotographsChoice("34", "They're rubbing their fingers.", "They're stretching their legs.", "They're leading a band.", "They're shaking hands.",
                "Choice (A) confuses rubbing their fingers and shaking hands.\n"+
                "Choice (B) misidentifies the action and the body part.\n"+
                "Choice (C) confuses the similar sounds band and hand.\n"+
                "D.The people are shaking hands.", "34");
        objPhotoDatabase.AddValuesToPhotographsChoice("35", "The conductor is on the train.", "The captain is on course.", "The teacher is behind the desk.", "The trainer is in front of the group.",
                "Choice (A) confuses the similar sounds conductor with instructor, train with trainer.\n"+
                "Choice (B) conf uses the word course, which has the meaning of a ship's chosen path and a training session.\n"+
                "Choice (C) associates teacher with trainer and is incorrect because the woman is standing by a chart, not behind a desk.\n"+
                "D.The trainer is in front of the group.", "35");
        objPhotoDatabase.AddValuesToPhotographsChoice("36", "The closets are full of clothes.", "The drawers contain supplies.", "The storage bins are open.", "The boxes are empty.",
                "Choices (A) and (B) are out of context.\n"+
                "C.The bus's storage bins are open.\n"+
                "Choice (D) is incorrect because the bins, not boxes, are empty.", "36");
        objPhotoDatabase.AddValuesToPhotographsChoice("37", "The fireplace is tall and narrow.", "The smoke fumes are coming from the chimney.", "The skyscraper is on fire.", "The construction worker was fired.",
                "Choice (A) confuses fireplace and chimney.\n"+
                "B.Smoke is coming from the chimney.\n"+
                "Choice (C) is incorrect because there is smoke coming from the chimney, not fire coming from a skyscraper.\n"+
                "Choice (D) confuses the similar words fired (to be terminated) and fire (burning flames) by associating fire with smoke.", "37");
        objPhotoDatabase.AddValuesToPhotographsChoice("38", "They're making coffee.", "They're having a discussion.", "They're washing their cups.", "They're using a calculator.",
                "Choice (A) is incorrect because there are coffee cups, but no one is making coffee.\n"+
                "B.The people are having a discussion.\n"+
                "Choice (C) repeats cups, which are on the table.\n"+
                "Choice (D) is incorrect because they are not using a calculator.", "38");
        objPhotoDatabase.AddValuesToPhotographsChoice("39", "He's buying an umbrella.", "He's clearing the table.", "He's serving the diners.", "He's waiting by the restroom.",
                "Choice (A) confuses the similar phrases buying an umbrella and by an umbrella.\n"+
                "B.The waiter is clearing the table.\n"+
                "Choice (C) is something a waiter would do but is not what he's doing in the picture.\n"+
                "Choice (D) confuses the similar sounding words waiting for waiter and restroom for restaurant.", "39");
        objPhotoDatabase.AddValuesToPhotographsChoice("40", "The trainer is setting his watch.", "The tourist is checking his bag.", "The pilot is washing his hands.", "The traveler is waiting for a train.",
                "Choice (A) confuses the similar sounds train and trainer and repeats the word watch, which the man is looking at.\n"+
                "Choice (B) uses the associated words tourist and bag, but the man is checking the time, not his bag.\n"+
                "Choice (C) confuses the associated word washing for watching, and the man is holding up his hand to check the time. He is probably not a pilot of a plane.\n"+
                "D.A traveler is sitting on his suitcase on the platform waiting for a train.", "40");



    }

    private void AddValuesToQandR(){

        // QandR Question
        objQandR.addValuseToQuestionAndResponseQuestion("1", "Who took my umbrella?", "c");
        objQandR.addValuseToQuestionAndResponseQuestion("2", "Have you seen the mop anywhere?", "b");
        objQandR.addValuseToQuestionAndResponseQuestion("3", "Parking is expensive here, isn't it?", "c");
        objQandR.addValuseToQuestionAndResponseQuestion("4", "Do you prefer a window or an aisle seat?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("5", "Why is this door unlocked?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("6", "Take your coat with you.", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("7", "Where should we go for dinner tonight?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("8", "I thought you were in New Zealand on business this week.", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("9", "When are you starting your new job?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("10", "What is the new computer programmer's name?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("11", "Do you have a pencil I can borrow?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("12", "Where can I find the sweaters?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("13", "Why don't we go to the beach this weekend?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("14", "Do we get off at the next station or the one after that?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("15", "Don't you have to catch a ten o'clock flight?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("16", "How long should we keep notices on the bulletin board?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("17", "Why didn't you come to the party?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("18", "You sent that memo out last week,didn't you?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("19", "Why didn't we get the Fujimaki contract?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("20", "Can you make sure there's a slide projector in the boardroom?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("21", "Do you know who that man was?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("22", "I can drive you to the airport tomorrow if you need transportation.", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("23", "Do you need the afternoon to finalize the budget, or can you help me plan the presentation?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("24", "What kind of refreshments are they planning for the reception?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("25", "Aren't we going to collect money for a gift?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("26", "How many callers responded to our radio advertisement?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("27", "Did you get the pay raise you asked for?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("28", "Can you handle incoming phone calls while I go to lunch?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("29", "Hasn't she read the market share report yet?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("30", "I hear you'll be retiring soon.", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("31", "Do you want some lemon with your tea?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("32", "Whose jacket is this?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("33", "Where can I find the printer cartridges?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("34", "What time does the reception start?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("35", "Do you want to unpack supplies, or would you rather help customers?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("36", "Didn't we go to the same university?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("37", "I'm leaving early again today.", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("38", "Don't you think you might need an umbrella?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("39", "The mail has been delivered already,hasn't it?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("40", "How many employees are expected to attend the staff training?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("41", "It's hard to find a taxiaround here.", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("42", "When will you finish editing that report?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("43", "Should we try the new restaurant across the street?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("44", "Why do you think the flight was canceled?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("45", "Will you notify everyone of the change?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("46", "I've just arranged to have my car repaired tomorrow.", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("47", "Do you know where I can get my computer fixed?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("48", "Why don't we buy new chairs for the waiting room?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("49", "Susan's out on vacation, isn't she?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("50", "When was the client billed for the new designs?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("51", "Haven't we received our concert tickets yet?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("52", "Why were you so late to the morning session?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("53", "Who was the director talking to this morning?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("54", "Would you like to review the final draft,or should I send it now?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("55", "What areas will the inspectors be checking tomorrow?", "B");
        objQandR.addValuseToQuestionAndResponseQuestion("56", "We should recruit volunteers to help plan the company picnic.", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("57", "Would you care to join us for dinner after the board meeting?", "A");
        objQandR.addValuseToQuestionAndResponseQuestion("58", "We aren't going to be done on time, are we?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("59", "How often do you hear from your former colleagues?", "C");
        objQandR.addValuseToQuestionAndResponseQuestion("60", "Did you remember to bring your passport?", "A");


        // QandR Choice
        objQandR.addValuseToQuestionAndResponseChoice("1", "I'll read it later.", "The blue one.", "I did, by mistake.",
                "A.also answers the question who, but it refers to an action in the future and is about reading something, not taking something.\n"+
                "B.does not answer the question who.\n"+
                "C.The question asks who took the man's umbrella. I did means I took it.", "1");
        objQandR.addValuseToQuestionAndResponseChoice("2", "It was nice to see you,too.", "I left it in the storage room.", "Put them on the top shelf.",
                "A.does not give any information about the mop.\n"+
                "B.The first woman is looking for the mop, which is a cleaning utensil.She is not sure if the other woman knows where it is.In (B) the second woman gives the location of the mop.\n"+
                "C.The second woman  is giving instructions about where to put several things, them, not stating where a single thing,the mop, is.", "2");
        objQandR.addValuseToQuestionAndResponseChoice("3", "No, it's over there.", "It just arrived.", "Yes, the rates just went up.",
                "A.is incorrect because it responds to a question about the location of something.\n"+
                "B.is incorrect because parking cannot arrive.\n"+
                "C.The woman asks for confirmation of her statement about the high cost of parking in the area by using the negative question tag isn't it? In (C) the man agrees with yes and adds some information about the rates, probably the parking  rates.", "3");
        objQandR.addValuseToQuestionAndResponseChoice("4", "A window, please.", "You can sit here.", "Yes, I see it, too.",
                "A.They are probably on a train or a plane. The first man asks which seat the other man prefers, or likes better.In (A) the second man states his preference for a window seat.\n"+
                "B.The man is offering a seat, not answering the question and stating a preference.\n"+
                "C.Answers a question about whether the man can see something, not a question about seat preference.", "4");
        objQandR.addValuseToQuestionAndResponseChoice("5", "Because I've just unlocked it.", "It's by the front door.", "No, not usually.",
                "A.The key question word is why, which asks the reason for something.(A) is the only response that gives a reason.\n"+
                "B.gives the location of something, not the reason for something.\n"+
                "C.does not answer the question why.", "5");
        objQandR.addValuseToQuestionAndResponseChoice("6", "I've never been there.", "Is it that cold outside?", "Higher than that.",
                "A.is a response to information about a particular place, so it is not appropriate here.\n"+
                "B.In the statement, the man advises the woman to take her coat. In (B) the woman is checking whether a coat is really  necessary.\n"+
                "C.gives information about the best position for something, so it is not appropriate here.", "6");
        objQandR.addValuseToQuestionAndResponseChoice("7", "I feel like having Chinese food.", "Yes, we really should.", "I don't have any problem with it.",
                "A.The first speaker asks for a suggestion about the restaurant where they will have dinner.In (A) the second speaker indirectly suggests going to a Chinese restaurant.\n"+
                "(B) and (C) both answer the question of whether or not the two speakers should do something, not where they should eat.", "7");
        objQandR.addValuseToQuestionAndResponseChoice("8", "This is the stronger proposal.", "My trip was canceled.", "We appreciate your business.",
                "A.is not correct.\n"+
                "B.The first woman is probably surprised to see the other woman because she thought she was away.In (B), the second woman explains why she is not away.\n"+
                "C.The second woman is thanking the other person for her business. The first woman did not mention doing business together.", "8");
        objQandR.addValuseToQuestionAndResponseChoice("9", "For the last two years.", "No, at the end.", "At the beginning of May.",
                "A.answers a question about how long something has been happening.\n"+
                "B.does not correctly answer this when question.\n"+
                "C.The key question word is when, so a date is the correct answer.", "9");
        objQandR.addValuseToQuestionAndResponseChoice("10", "I use a laptop.", "We haven't been introduced.", "I'll need a copy of the program.",
                "A.states the type of computer the second person uses.The first person did not ask about this.\n"+
                "B.The question asks for the name of the new computer programmer, probably a new colleague. In (B) the man implies that he does not know the person's name.\n"+
                "C.is about a computer program, not a computer programmer.", "10");
        objQandR.addValuseToQuestionAndResponseChoice("11", "I'm sorry, all I have is a pen.", "Yes, I took out a bank loan.", "I'll be working tomorrow.",
                "A.In this question, the man asks to borrow a pencil.(A) is an appropriate response if the woman only has a pen.\n"+
                "B.When a person takes out a bank loan, they borrow money from a bank. The man asked to borrow a  pencil, not money.\n"+
                "C.The question was not about what the other person will be doing tomorrow.", "11");
        objQandR.addValuseToQuestionAndResponseChoice("12", "No, I don't think it is.", "On the second floor.", "Thanks, where did you find it?",
                "A.does not answer the question where.\n"+
                "B.The key question word is where, so a location is the correct answer.The speakers are probably in a department store.\n"+
                "C.is a response you might make when somebody has found something you had lost.", "12");
        objQandR.addValuseToQuestionAndResponseChoice("13", "Sorry,I didn't know.", "That's a great idea.", "Because I was there.",
                "A.is an apology, not a response to a suggestion.\n"+
                "B.With her question,the first woman suggests going to the beach.(B) is an appropriate way of agreeing enthusiastically to a suggestion.\n"+
                "C.is a response to a question asking for the reason why something did or did not happen in the past.", "13");
        objQandR.addValuseToQuestionAndResponseChoice("14", "Yes,I think so.", "The week after next.", "Let me look at the map.",
                "A.is not a correct response to a question including or.\n"+
                "B.is an answer to a question about when something will happen.\n"+
                "C.The man asks the woman at which station or where they should get off. (C) is an appropriate response if the woman needs to check the map first.", "14");
        objQandR.addValuseToQuestionAndResponseChoice("15", "No, I dropped them on the floor.", "You can pick it up at the airport.", "It's been delayed until this afternoon.",
                "A.is incorrect because the question was not about catching objects, but about catching a flight.\n"+
                "B.A person does not pick up a flight.\n"+
                "C.The man asks the question to check the time of the woman's flight. He may be surprised that the woman has not left for the airport.In (C) information about the time of the flight is given.", "15");
        objQandR.addValuseToQuestionAndResponseChoice("16", "About five centimeters long.", "He said it would.", "For about two weeks.",
                "A.The man does not ask for a measurement.\n"+
                "B.This choice does not answer the question how long ...?\n"+
                "C.The question how long asks about the length of time that the notices should be kept on the board.In (C) for is used with a period of time to answer the question how long.", "16");
        objQandR.addValuseToQuestionAndResponseChoice("17", "I simply forgot about it.", "That's right, it didn't.", "The invitation was nice.",
                "A.The first woman wants to know why the second woman did not go to the party. (A) is a possible reason for not going to a party.\n"+
                "B.That's right shows agreement with something the other person said, not the reason for an action. Also, it didn't does not respond to anything in the question.\n"+
                "C.does not give a logical reason for not going to the party.", "17");
        objQandR.addValuseToQuestionAndResponseChoice("18", "It's an attractive scent.", "No,you told me to wait, remember?", "She was out for a few days.",
                "A.is incorrect because the question is not about scent, which means perfume.\n"+
                "B.The first man is checking that the other man sent the memo last week.In (B),the other man responds that he did not send the memo and gives the reason why.\n"+
                "C.is incorrect because the question is not about another person,she, being out, or away.", "18");
        objQandR.addValuseToQuestionAndResponseChoice("19", "Yes,I just heard the news.", "We wanted too much money.", "No, she hasn't been contacted.",
                "A.This shows agreement. It does not give a reason.\n"+
                "B.A question with why asks for the reason for an action.In this caseit asks for the reason why their company did not get the contract.(B) gives a logical reason for this.\n"+
                "C.does not give a reason.Also, the pronoun she does not refer to anything in the question.", "19");
        objQandR.addValuseToQuestionAndResponseChoice("20", "He's on the Board of Directors.", "No, not that I'm aware of.", "I've already set it up.",
                "A.is not an appropriate response to a request for someone to do something.\n"+
                "B.is a response to a question asking if there is a slide projector in the boardroom.\n"+
                "C.The question is a request for the woman to prepare some equipment.Itin (C) refers to the equipment, which the woman has already set up.", "20");
        objQandR.addValuseToQuestionAndResponseChoice("21", "I saw him,too.", "That was very meaningful.", "The new assistant manager.",
                "A.has the pronoun him, which could refer to that man, but it does not answer the question who.\n"+
                "B.is about a thing, that, not a person.\n"+
                "C.The key question word is who. The man uses the phrase Do you know beforeit to make the question more in direct. (C) best answers the question who.", "21");
        objQandR.addValuseToQuestionAndResponseChoice("22", "Thank you,but it's been arranged.", "Yes,I can take you there.", "That's the station over there.",
                "A.The first man offers to drive the second man to the airport. (A) is a polite refusal of the offer.\n"+
                "B.This is a response to a request for transportation, such as Could you please take me to the airport? not the response to an offer.\n"+
                "C.gives information about the location of the station. The first man did not ask for such information.", "22");
        objQandR.addValuseToQuestionAndResponseChoice("23", "He's been planning on it.", "The finance meeting is in the boardroom.", "I should be able to help you by 3 P.M.",
                "A.refers to he, but a man is not mentioned in the question.\n"+
                "B.This choice gives the location of a finance meeting. There is no mention of a meeting in the question.\n"+
                "C.In the question,the woman asks for help with her presentation,but she also asks if the man needs to do other work in the afternoon instead. (C) is an appropriate response to the request for help.", "23");
        objQandR.addValuseToQuestionAndResponseChoice("24", "Reception is bad in this area.", "Just dessert with coffee and tea.", "That was very kind of him.",
                "A.Reception means the ability to receive a radio or television signal, so it is incorrect.\n"+
                "B.The question is about arrangements for a reception, which is a formal social occasion.The man asks what kind of refreshments, or things to eat and drink, are planned, so (B) is the best response.\n"+
                "C.is in the past tense, so it cannot be a response to a question about something that is being planned.", "24");
        objQandR.addValuseToQuestionAndResponseChoice("25", "Yes, we should do that.", "It's so nice of you to think of me.", "I've been there.",
                "A.The question with Aren't we going to implies that the woman expects that they will collect money for a gift for someone. In (A) the man agrees with the expectation.\n"+
                "B.is incorrect because it is a way of thanking somebody for a gift.\n"+
                "C.ends with there, so it is about a place. The question is not about a place.", "25");
        objQandR.addValuseToQuestionAndResponseChoice("26", "More than we expected.", "Sorry, I'll lower the volume.", "It's a four-color brochure.",
                "A.This is an appropriate answer to the question How many callers...?\n"+
                "B.is not an appropriate response to a question about how many.\n"+
                "C.gives a number, but it is about a brochure, not a radio advertisement.", "26");
        objQandR.addValuseToQuestionAndResponseChoice("27", "No,I don't have any questions.", "Yes,effective as of next week.", "Yes,four people are coming.",
                "A.is not about getting a pay raise.\n"+
                "B.The question asks if the man received the pay raise that he asked for. In (B), the man says yes, meaning he got the pay raise. Effective as of is used to say when the pay raise will begin.\n"+
                "C.The question did not ask about the number of people coming to something.", "27");
        objQandR.addValuseToQuestionAndResponseChoice("28", "Yes, he came in early this morning.", "No,it broke off when I dropped the cup.", "Yes, I'll be free in an hour.",
                "A.The question did not ask about when somebody came in.\n"+
                "B.The question was not about a broken handle on a cup.\n"+
                "C.The question is a request for help.The first man needs somebody to handle, or deal with phone calls when he is at lunch.In (C) the man agrees to help and says when he will be available.", "28");
        objQandR.addValuseToQuestionAndResponseChoice("29", "But she has brown hair.", "She's been too busy.", "I bought it in a shop.",
                "A.There is nothing in the question about somebody's hair color.\n"+
                "B.The question asks whether she, another person, has read a particular report. (B) implies that she has not read the report and gives the reason for this.\n"+
                "C.This response does not refer to she or to a business report.", "29");
        objQandR.addValuseToQuestionAndResponseChoice("30", "Yes,in about a month.", "I always get sleepy after lunch.", "It's almost twelve o'clock.",
                "A.The woman has heard the news of the man's retirement and wants to know more about it.In (A) the man confirms that he is retiring and says when.\n"+
                "B.The woman was talking about the man retiring, or finishing his working life, not being tired.\n"+
                "C.The woman did not ask what the time was.", "30");





    }

    private void AddValuesToShortConversation(){

        // Shortc Script
        objShortCon.addValuesToShortConversationScript("1",
                "M: I was wondering if we received the contact from MS. Park? She said last night that she'd fax it here today. \n \n"+
                "W: It hasn't arrived yet. Perhaps we should call her if we don't get it by lunchtime? \n \n"+
                "M: Well, It's only ten o'clock in the morning, and she's very reliable. The Seoul office is running so much better since she became the manager");
        objShortCon.addValuesToShortConversationScript("2",
                "M: Mrs. Anderson, welcome back! How was the vacation?\n \n" +
                "W: It was great. I stayed at this tiny hotel by the lake hiked during the day,and visited local restaurants in the evenings. It was wonderful. Thanks for watering my plants when I was away.\n \n" +
                "M: That's not a problem at all. You were such a great help when we went on vacation the last time.Oh, here are all your newspapers,by the way. We picked them up for you while you were away.");
        objShortCon.addValuesToShortConversationScript("3",
                "W: I have a question before we call the first applicant in.Will we be done here by noon? I told a client I would meet him to discuss some business over lunch.\n \n" +
                "M: The last interview starts at 11 o'clock, so we should be done in plenty of time. I have a doctor's appointment this afternoon, anyway.\n \n" +
                "W: OK, but if it looks like that 11 o'clock interview is going to go much more than an hour, I'll need to step out to give my client a call. I might just have to ask him to reschedule.");
        objShortCon.addValuesToShortConversationScript("4",
                "M: If the rain doesn't stop within the next two hours, we should consider postponing Maria's retirement party.\n \n" +
                "W: I think it's too late to postpone it. The caterer and florist are set to arrive in an hour, so we'd better come up with an alternative to holding it in the garden.\n \n" +
                "M: Why don't we have them set up in the conference room instead? It's a shame to move it indoors, but what other choices do we have?\n \n" +
                "W: OK. I'll go to my office and call everyone to let them know that we're moving the party from the garden to the conference room");
        objShortCon.addValuesToShortConversationScript("5",
                "W: Dr.Chen and I are going to eat at that vegetarian restaurant by the station. Would you like to come along?\n \n" +
                "M: You mean the one on Logan Street? I went there about two months ago, and the service was pretty slow.\n \n" +
                "W: Well, I heard they hired a new manager a couple of weeks ago, so maybe it's better now.Plus, it's close enough to walk to, so we won't have to drive.");
        objShortCon.addValuesToShortConversationScript("6",
                "M: I just checked my hotel room, and I can't find the blue folder I took to the effective training strategies session yesterday. It's not in my briefcase either.\n \n" +
                "W: Maybe you left it in the effective training strategies session? I think I'm attending a seminar in the same auditorium today. Should I have a look for you?\n \n" +
                "M: That would be nice of you, thanks. It has all the notes I took for the article I'm writing for Pacific Business Review.\n \n" +
                "W: I'll meet you back here in the hotel lobby at ten, and I'll let you know if I find it.");
        objShortCon.addValuesToShortConversationScript("7",
                "W: Hi, I'd like the vegetable soup, the large salad with chicken, and a medium coffee. Oh, and a slice of chocolate cake.\n \n" +
                "M: That will be seven dollars and twenty-five cents. But just to let you know, we have a discount on desserts today. You can buy two pieces of pie or cake and get one free.\n" +
                "W: In that case I'll take one piece of peach pie, and two pieces of chocolate cake wrapped to go.\n \n" +
                "M: OK, your total is eight dollars and fifty cents.Please move to the end of the counter to pick up your order.");
        objShortCon.addValuesToShortConversationScript("8",
                "M: This is Leo Schultz with Core Bank Credit. I'm calling to verify some unusual activity on your business credit card account.\n \n" +
                "W: What kind of activity? Do we need to be worried? \n \n" +
                "M: Well, Ms. Yamada, I wanted to confirm three large transactions made in euros, posted in Dublin. Are you aware of these international purchases?\n \n" +
                "W: Oh, yes. We ordered books from three different booksellers there last week.\n \n");
        objShortCon.addValuesToShortConversationScript("9",
                "W1: Ms. Wong,I had a chance over the weekend to read your proposal for cutting down on our heating costs. It looks very promising. In fact, I'd like to implement some of the changes by April or even sooner.\n \n"+
                "W2: Thank you,Ms. Sanchez.Do you think we should present some of my ideas at the board meeting next week? \n \n" +
                "W1: Definitely.I think the chairperson will be especially interested in your thoughts on using energy more efficiently. \n \n"+
                "W2: That's wonderful.I just want to see the university be able to spend more on educating our students and less on operating costs.");
        objShortCon.addValuesToShortConversationScript("10",
                "W: How's development of the new coffeemaker going? I thought it would be ready in January.\n \n" +
                "M: Well, we were having difficulty with the automatic timer, but we took care of it in April.\n \n" +
                "W: Great! Then it should be ready for the September trade show in Paris?\n \n" +
                "M: Yes. Marie's already started the advertising campaign. We're running an advertisement in Good Food Magazine in June.");
        objShortCon.addValuesToShortConversationScript("11",
                "M: Is that the menu for the employees'cafeteria? I want to see what they're serving for lunch today. \n \n"+
                "W: I think this is yesterday's menu.They have a different one for each day of the week,you know.They always post it on the employee Web site. \n \n"+
                "M: Oh,thanks.I'll go to the Web site and get today's menu.");
        objShortCon.addValuesToShortConversationScript("12",
                "W1: I'm so thrilled that Mariana Lambeck has agreed to come to our store for a book signing and lecture next week. \n \n"+
                "W2: How do you suggest we advertise the event? \n \n"+
                "W1: Let's create a flyer and put it up around town tomorrow.If we advertise on television or in the newspaper we may get too large a crowd for the space we have available. \n \n"+
                "W2: We might get a lot of people anyway; if she won't be here until next week,that's a lot of time for world to get around.");
        objShortCon.addValuesToShortConversationScript("13",
                "M: Does the number ten train stop at Baldwin Station? \n \n" +
                "W: It usually does,but not for the next five weeks.We're doing major repair work on the tracks there and the station's going to be closed most of that time. \n \n" +
                "M: At this time of the night,you can get it where the other city bus lines stop,in front of the main entrance to the terminal.But during peak hours,it stops at the side entrance.The next one leaves in about fifteen minutes.");
        objShortCon.addValuesToShortConversationScript("14",
                "W: The general contractor called to see if he can visit the building tomorrow morning at 9:00. \n \n"+
                "M: Does he need to see both the inside and the outside? i can be there to let him in,but i can't stay for more than half an hour;I've got to get to another meeting at 10:00. \n \n" +
                "W: I think he just wants to verify some of the exterior locations for the restoration work.You probably won't need to stay for long. \n \n" +
                "M: OK,nine o'clock is fine,then.But please make sure he knows that i only have about thirty minutes.");
        objShortCon.addValuesToShortConversationScript("15",
                "M: The new shipment of T-shirts should arrive today.If i'm pleased with the quality,I think i'll call the manufacturer tomorrow and order fifty more. \n \n " +
                "W: We're going to receive two hundred shirts;do we really need to order more? \n \n"+
                "M: With the annual fall sale starting next month,businees should be on the rise. \n \n"+
                "W: That's true-and sales are up this month,too.");
        objShortCon.addValuesToShortConversationScript("16",
                "W: Would it be OK if i took a few hours off next Friday? My parents are coming to visit and i need to pick them up at the airport. \n \n "+
                "M: Yes,that should be fine.We do need to form a construction committee and start planning the company picnic next week,but there should be plenty of time for that. \n \n"+
                "W: Thanks.I'll be happy to work late on Thursday if necessary.");
        objShortCon.addValuesToShortConversationScript("17",
                "M: So how does everything look,Doctor? \n \n" +
                "W: We've just got all of your laboratory results back,and it looks like you're in good health,Mr. Jackson.Sorry it took us a couple of weeks;the lab is very backed up with work this month. \n \n"+
                "M: No problem.Do you have any specific recommendations? \n \n"+
                "W: Yes, you probably should be getting more exercise.try to work on that before your next appointment.See you in six months.");
        objShortCon.addValuesToShortConversationScript("18",
                "W: Hi,Jason? It's Kathie Lam at Diamond Electronics.I'm calling to tell you that your interview last Thursday went very well,and everyone on the staff really enjoyed meeting you.We'd like to offer you the job at the salary you requested. \n \n"+
                "M: Wow,that's great.When can i start? \n \n"+
                "W: Well,let's see,tomorrow's Friday.You could start on Monday,if you like-we'd need you to come in Friday to complete some paperwork. \n \n"+
                "M: That should be fine.See you tomorrow,then.");
        objShortCon.addValuesToShortConversationScript("19",
                "M1: Hey,Bob,that machine i bought at your showroom the other day? it really increased the productivity of my factor, in fact by as much as 20 percent. \n \n"+
                "M2: I'm glad to hear that,AL.I hope this means you'll be shopping more often at my showroom. \n \n" +
                "M1: I probably will. Come to think of it,my workers need some new safety equipment.You know,hard hats,face masks ... that sort of thing. \n \n "+
                "M2: Well,you might want to stop by our showroom next week.That's when our annual discount sale starts,so you'll probably find a few bargains.");
        objShortCon.addValuesToShortConversationScript("20",
                "M: Diana,can i talk to you for a minute? i want to make sure i understand exactly what you want me to do before i get started. \n \n"+
                "W: Well,we're going to be reprinting an old textbook.i'd like you to review it and mark any places where changes need to be made. \n \n"+
                "M: OK,so i'll be editing it.And how long do i have to do it? \n \n "+
                "W: I need it by the end of the day tomorrow,but i don't think it will take you more than a few hours.");


        // ShortC Question

        objShortCon.addValuesToShortConversationQuestion("1", "when does the conversation take place?",     "A", "In the morning", "Around midday", "In the late afternoon", "At night",
                "A The man says that it's ten o'clock in the morning.\n"+
                        "Thus, the correct answer cannot be (B) around midday (C) in the late afternoon or (D) at night", "1");
        objShortCon.addValuesToShortConversationQuestion("2", "What are the speakers waiting for?",         "D", "A call from a customer", "A job application", "A food delivery", "A contract",
                "A. The woman suggests calling Ms. Park, but they are not waiting for a call from a customer.\n"+
                        "B. The speakers do not talk about a job application.\n"+
                        "C. They hope to receive the contract by lunchtime, but they do not mention a food delivery\n"+
                        "D.The man asks if they have received the contract from MS. Park and the woman answers that it hasn't arrived yet, so they are waiting for a contract.", "1");
        objShortCon.addValuesToShortConversationQuestion("3", "What does the woman suggest?",               "B", "Sending a fax", "Making a phone call", "Hiring a new manager", "Flying to Seoul",
                "A. Ms. Park intended to fax the contract to the speakers, but the woman speaking does not suggest sending a fax.\n"+
                        "B. The woman says 'perhaps we should call her' in other words, she suggests making a phone call.\n"+
                        "C. The man mentions Ms. Park's new role as manager, but the woman does not suggest hiring a new manager.\n"+
                        "D. Seoul is where Ms. Park works. The woman does not suggest flying there.", "1");
        objShortCon.addValuesToShortConversationQuestion("4", "What has the woman just done?",              "A", "Returned from vacation", "Made a dinner reservation", "Read an interesting book", "Bought some house plants",
                "A.The man asks the woman about her vacation and she tells him what she did on her vacation,so she has probably just returned from vacation\n"+
                        "B.The woman does not talk about making a dinner reservation.\n"+
                        "C.The woman does not talk about reading a book\n"+
                        "D.The speakers mention the woman's plants,but the woman does not say she has recently bought some plants.", "2");
        objShortCon.addValuesToShortConversationQuestion("5", "Why does the woman thank the man?",          "B", "He sent her a postcard.", "He took care of her plants", "He arranged her hotel accommodation.", "He painted her house.",
                "A.Neither speaker mentions postcards.\n"+
                        "B.The woman thanks the man for watering her plants.In other words, he took care of them while she was away.\n"+
                        "C.The woman tells the man about the hotel she stayed in,but does not imply that he arranged it for her.\n"+
                        "D.The man is probably a neighbor.There is no mention of him painting the house", "2");
        objShortCon.addValuesToShortConversationQuestion("6", "What does the man give the woman?",          "C", "A key", "A hiking map", "Some newspapers", "Some water",
                "A.He does not give her a key\n"+
                        "B.He does not give her a hiking map\n"+
                        "C.We often say 'here is' or 'here are' when we are giving something to somebody.The man is giving the woman her newspapers.\n"+
                        "D.The woman thanks the man for watering her plants.He does not give the woman some water", "2");
        objShortCon.addValuesToShortConversationQuestion("7", "When does the last interview start?",        "C", "At 9:00", "At 10:00", "At 11:00", "12:00",
                "A.The last interview does not start at 9:00\n"+
                        "B.The last interview does not start at 10:00\n"+
                        "C.The conversation takes place before the first interview.The question is about the time of the last interview and the man says it starts at 11:00.\n"+
                        "D.The speakers hope that the last interview will be completed by noon,or 12:00,but it start at 11:00.", "3");
        objShortCon.addValuesToShortConversationQuestion("8", "Who is the man planning to visit this afternoon?", "D", "A client", "A job applicant", "A relative", "A doctor",
                "A.It is the woman who is planning to see a client,not the man.\n"+
                        "B.Neither of the speakers is visiting a job applicant.The applicants are coming to their office.\n"+
                        "C.There is no mention of a relative.\n"+
                        "D.The man says he has a doctor's appointment in the afternoon.", "3");
        objShortCon.addValuesToShortConversationQuestion("9", "Why might the woman make a telephone call?", "C", "To arrange a job interview", "To request some paperwork", "To change a meeting time", "To purchase some supplies",
                "A.The woman may step out of the interview, or leave the interview briefly,in order to call her client.She is not going to arrange a job interview.\n"+
                        "B.The woman is not going to telephone about some paperwork.\n"+
                        "C.The woman says she might have to call her client to reschedule their lunchtime meeting,or in other words change the meeting time.\n"+
                        "D.The woman is not going to telephone about some supplies.", "3");
        objShortCon.addValuesToShortConversationQuestion("10", "What is the problem with the party?", "A", "The weather is bad.", "There is a shortage of food.", "Space is limited.", "There is a scheduling conflict.",
                "A.The man suggests postponing the party,or holding it at a later time or date,if the rain doesn't stop,so the bad weather is the problem\n"+
                        "B.The woman mentions the caterer,but there is no mention of a shortage of food.\n"+
                        "C.There is not a problem with the amount of space fox the party.\n"+
                        "D.A scheduled conflict means two events have been scheduled to take place at same time.This has not happened here.", "4");
        objShortCon.addValuesToShortConversationQuestion("11", "Why is the party being held for Maria?", "B", "She received a promotion.", "She is retiring.", "She is relocating.", "She is getting married.",
                "A.Thus,she has not received a promotion,or higher position\n"+
                        "B.The speakers are discussing Maria's retirement party.The party is being held because Maria is retiring.In other words,she reached the age when she will finish working.\n"+
                        "C.She is not relocating,or going to work in a different place.\n"+
                        "D.Nor is she getting married", "4");
        objShortCon.addValuesToShortConversationQuestion("12", "Where was the party originally scheduled to take place?", "C", "In a restaurant", "In a conference room", "In a garden", "In an apartment",
                "A.The man talks about moving the party indoors.\n"+
                        "B.Are all indoor locations,so none of these can be the location where the party was originally scheduled to take place\n"+
                        "C.The woman talks about finding an alternative to holding it (the party) in the garden,so the party was originally scheduled to take place in a garden.\n"+
                        "D.Are all indoor locations,so none of these can be the location where the party was originally scheduled to take place", "4");
        objShortCon.addValuesToShortConversationQuestion("13", "What are the speakers discussing?", "D", "A hiking trip", "A hiring decision", "A train schedule", "A local restaurant",
                "A.They then go on to discuss whether it is a good restaurant.They are not talking about a hiking trip\n"+
                        "B.They are not talking about a hiring decision\n"+
                        "C.They are not talking about a train schedule.\n"+
                        "D.The woman invites the man to join her and Dr.Chen at a vegetarian restaurant.She says that it is close enough to walk to,so it is a local restaurant.", "5");
        objShortCon.addValuesToShortConversationQuestion("14", "How long ago did the visit the place being discussed?", "C", "Two days ago", "Two weeks ago", "Two months ago", "Two years ago",
                "A.He visited it two months ago,not two days ago.\n"+
                        "B.He visited it two months ago,not Two weeks ago\n"+
                        "C.The man says he went there two months ago.The word there refers to the restaurant they are discussing.\n"+
                        "D.He visited it two months ago,not two years ago", "5");
        objShortCon.addValuesToShortConversationQuestion("15", "How will the speakers probably get to their destination?", "A", "By walking", "By taking the train", "By Driving", "By taking a bus",
                "A.The speakers will most likely walk to their destinations,the restaurant,since the woman mentions the fact that it's close enough to walk to and therefore they won't have to drive.\n"+
                        "B.We can infer from what the woman says that they do not intend to get there by taking the train\n"+
                        "C.We can infer from what the woman says that they do not intend to get there by taking driving\n"+
                        "D.We can infer from what the woman says that they do not intend to get there by taking the a bus", "5");
        objShortCon.addValuesToShortConversationQuestion("16", "Where does this conversation take place?", "A", "At a hotel", "At an office supplies store", "At a train station", "At a restaurant",
                "A.At the beginning of the conversation the man says he just checked his hotel room for something and at the end the woman refers to the hotel lobby as here,so the speakers must be in the hotel.\n"+
                        "B.There is nothing in the conversation to indicate that they could be at an office supplies store\n"+
                        "C.There is nothing in the conversation to indicate that they could be at a train station\n"+
                        "D.There is nothing in the conversation to indicate that they could be at a restaurant", "6");
        objShortCon.addValuesToShortConversationQuestion("17", "What is the man looking for?", "C", "A hotel room", "A briefcase", "A folder", "An article",
                "A.He is not looking for a hotel room,he has been looking in his hotel room for the folder\n"+
                        "B.He looked in his briefcase for the folder,so we know he is not looking for his briefcase.\n"+
                        "C.The man says he can't find his blue folder and he checked his hotel room for it,so he is looking for a folder\n"+
                        "D.The folder contains notes he made for an article.He is not looking for the article,which he has not yet written.", "6");
        objShortCon.addValuesToShortConversationQuestion("18", "What does the woman offer to do?", "B", "Pay for breakfast", "Look for a lost item", "Organize a training session", "Write a magazine article",
                "A.They do not talk about having breakfast\n"+
                        "B.The woman asks if she should have a look for the folder in the auditorium where the man last had it.She is offering to look for the lost item.\n"+
                        "C.They talk about a training session the man went to.The woman does not offer to organize a training session.\n"+
                        "D.The man is going to write a magazine article,not the woman.", "6");
        objShortCon.addValuesToShortConversationQuestion("19", "What is being offered at a discounted price?", "B", "Financial advice", "Desserts", "Garden tools", "Drinks",
                "A.The man suggests how the woman might save money on her food,but he is not offering her financial advice at a discounted price.\n"+
                        "B.The man says they have a discount on desserts today.This means desserts are being offered at a discounted price.\n"+
                        "C.Garden tools are not being sold here.\n"+
                        "D.Desserts are being offered at a discounted price,not drinks.", "7");
        objShortCon.addValuesToShortConversationQuestion("20", "Where are the speakers?", "C", "At a bank", "At a farm", "In a cafeteria", "In a factory",
                "A.People do not generally order food at a bank.\n"+
                        "B.People do not generally order food at a farm.\n"+
                        "C.The woman is placing a food order and the man is serving her with food,so they must be in a cafeteria.\n"+
                        "D.People do not generally order food in a factory.", "7");
        objShortCon.addValuesToShortConversationQuestion("21", "What will the woman probably do next?", "D", "Open an account", "Pour a cup of coffee", "Prepare some food", "Claim her order",
                "A.It is unlikely that she could an account in a cafeteria.\n"+
                        "B.The man does not ask her to pour her own coffee.\n"+
                        "C.The man,who is the cafeteria assistant,will probably prepare some food,not the woman.\n"+
                        "D.At the end of the conversation,the man asks the woman to pick up,or claim,her order,so this most likely is what she will do next.", "7");
        objShortCon.addValuesToShortConversationQuestion("22", "Who most likely is the man?", "A", "A bank representative", "A small-business owner", "A book publisher", "A travel agent",
                "A.The man gives the name of the organization he works for,Core Bank Credit.This and the fact that he is calling Ms.Yamada to check on transactions on her credit card account indicate that he is most likely a bank representative.\n"+
                        "B.It is evident that the man works for a bank,which is not a small business.\n"+
                        "C.A book publisher does not check on people's bank transactions.\n"+
                        "D.The man works for a bank,not a travel agency", "8");
        objShortCon.addValuesToShortConversationQuestion("23", "What is the purpose of the call", "D", "To request a transfer of funds", "To verify a travel itinerary", "To ask about postal rates", "To discuss credit card charges",
                "A.To request a transfer of funds means to ask to move money from one account to another.A bank customer might ask to do this,not a bank employee.\n"+
                        "B.The man calls the woman to verify credit card charges,not travel arrangements.\n"+
                        "C.Postal rates are not mentioned by either speaker.\n"+
                        "D.The man states the purpose of his phone call,which is to verify,or check,some unusual activity on her credit card account.He then asks the woman whether she is aware of,or knows about,some purchases made in another country.Thus,he calls to discuss credit card charges with her.", "8");
        objShortCon.addValuesToShortConversationQuestion("24", "What does the woman tell the man?", "A", "She recently purchased some books.", "She already opened an account.", "She needs to send several packages.", "She wants to stay in London for a week.",
                "A.The woman tells the man that she ordered,or purchased,some books last week,which is recently.\n"+
                        "B.She does not talk about opening an account.\n"+
                        "C.She ordered some books,so she is probably expecting to receive some packages,not send packages.\n"+
                        "D.She does not mention London.", "8");
        objShortCon.addValuesToShortConversationQuestion("25", "Where do the speakers probably work?", "A", "At a university", "At a power plant", "At a manufacturing company", "At a publishing company",
                "A.The fact that Ms. Wong says she wants the university to be able to spend more on educating our students implies that the two woman probably work at a university.\n"+
                        "B.The fact that Ms. Wong says she wants the university to be able to spend more on educating our students implies that the two woman probably work at a power plant.\n"+
                        "C.The fact that Ms. Wong says she wants the university to be able to spend more on educating our students implies that the two woman probably work at a manufacturing company\n"+
                        "D.A publishing company are not places where students are educated.", "9");
        objShortCon.addValuesToShortConversationQuestion("26", "What is the proposal about?", "C", "Adopting a flexible work schedule", "Appointing new board members", "Reducing energy costs", "Recycling paper in the office",
                "A.They talk about making changes,but not to the work schedule.\n"+
                        "B.The proposal will be presented at aboard meeting,but it is not about appointing new board members.\",\"D.Recycling paper is not a way of reducing energy costs.\n"+
                        "C.Ms.Wong's proposal is about cutting down on heating costs.Cutting down means reducing and heating uses energy,so these costs are energy costs.\n"+
                        "D.Recycling paper is not a way of reducing energy costs.", "9");
        objShortCon.addValuesToShortConversationQuestion("27", "When will the proposal be presented to the board?", "B", "This morning", "Next week", "In two weeks", "In two months",
                "A.The board meeting does not take place this morning.\n"+
                        "B.Ms Wong asks about presenting the ideas in her proposal at the board meeting next week and Ms.Sanchez agrees.\n"+
                        "C.The board meeting does not take place in two weeks.\n"+
                        "D.The board meeting does not take place in two months.", "9");
        objShortCon.addValuesToShortConversationQuestion("28", "What is the conversation about?", "D", "A vacation", "A television", "A coffee break", "A new product",
                "A.They talk about a trade show in Paris,not about a vacation.\n"+
                        "B.They do not mention a television show.\n"+
                        "C.The new product is a coffeemaker.They are not talking about a coffee break,which is a time allocated for workers to have coffee.\n"+
                        "D.The woman asks about development of the new coffeemaker and the speaker talk about preparing it for a trade show and starting an advertising campaign.Therefore,the conversation is about a new product.", "10");
        objShortCon.addValuesToShortConversationQuestion("29", "What was the problem?", "B", "An advertisement contained errors.", "A device was not working", "Presenters were late for a trade show.", "D. some food was delivered late.",
                "A.The man talks about the advertising campaign,but he does not mention any problem with it.\n"+
                        "B.The man talks about having difficulty with the automatic timer,which is a device on the coffeemaker.\n"+
                        "C.The trade show they speak about has not yet taken place.\n"+
                        "D.They do not refer to a food delivery.", "10");
        objShortCon.addValuesToShortConversationQuestion("30", "When is the trade show?", "D", "In January", "In April", "In June", "In September",
                "A.They is no mention of a trade show in January.\n"+
                        "B.There is no mention of a trade show in April.\n"+
                        "C.There is no mention of a trade show in June.\n"+
                        "D.The woman mentions the September trade show,so we know the trade show will be in September.", "10");
        objShortCon.addValuesToShortConversationQuestion("31", "What are the speakers discussing?", "C", "A cafeteria's bus business hours", "A new food store", "A cafeteria menu", "A change in food prices",
                "A.They are not discussing the cafeteria's business hours.\n"+
                        "B.They are talking about the cafeteria,not a food store.\n"+
                        "C.The man asks if the woman is looking at a menu for the employees'cafeteria and she tells him how to get today's menu,so they are discussing a cafeteria menu.\n"+
                        "D.They do not mention a change in food prices.", "11");
        objShortCon.addValuesToShortConversationQuestion("32", "What is the problem?", "D", "The man is late for lunch.", "The man is not hungry.", "The man forgot some money.", "The man has outdated information.",
                "A.He is not late for lunch.\n"+
                        "B.They do not say that the man is not hungry.\n"+
                        "C.The speakers do not mention money.\n"+
                        "D.They are looking at yesterday's menu,so the problem is that the man has outdated information.", "11");
        objShortCon.addValuesToShortConversationQuestion("33", "What will the man probably do next?", "B", "Leave for work.", "Visit a Web site.", "Change his work schedule.", "Go to the post office",
                "A.He does not say he will leave for work.\n"+
                        "B.The man will probably visit a Web site next because at the end of the conversation he says he'll go to the Web site and get today's menu.\n"+
                        "C.There is no mention of the man changing his work schedule.\n"+
                        "D.The man is not planning to go to the post office.", "11");
        objShortCon.addValuesToShortConversationQuestion("34", "Where will the event probably take place?", "A", "At a bookstore", "Visit a Web site", "At a television station", "At a newspaper office",
                "A.The first woman says someone is coming to our store for a book signing and lecture,so the event will probably take place at a bookstore.\n"+
                        "B.It will not take place at a restaurant.\n"+
                        "C.They mention advertising the event on television,not holding it at a television station.\n"+
                        "D.They mention advertising the event in the newspaper,not holding it at a newspaper office.", "12");
        objShortCon.addValuesToShortConversationQuestion("35", "When will the event take place?", "C", "Today", "Tomorrow", "Next week", "Next month",
                "A.The event will not take place today.\n"+
                        "B.The event will not take place tomorrow.\n"+
                        "C.The first woman says Mariana Lambeck will come for the book signing and lecture next week.\n"+
                        "D.The event will not take place month.", "12");
        objShortCon.addValuesToShortConversationQuestion("36", "How do the woman plan on promoting the event?", "B", "By advertising on television", "By posting flyers", "By creating a Web site", "By advertising in a newspaper",
                "A.They decide not to advertise on television.\n"+
                        "B.The first woman says let's create a flyer and put it up around town.Posting means putting up,so they plan to promote the event by post flyers.\n"+
                        "C.They do not talk about creating a Web site.\n"+
                        "D.They do not plan to advertise the event in a newspaper.", "12");
        objShortCon.addValuesToShortConversationQuestion("37", "Why will Baldwin Station be closed?", "B", "It is late at night.", "The tracks are under repair.", "The station is no longer used.", "A public event is being held there.",
                "A.The station is not closed because it is late at night.It will be closed all the time for the next five weeks.\n"+
                        "B.The woman says the train does not stop at Baldwin Station because they are doing major repair work on the tracks there,so (B) is correct.\n"+
                        "C.The station is no longer used is incorrect because it will probably reopen in five weeks.\n"+
                        "D.There is no mention of a public event.", "13");
        objShortCon.addValuesToShortConversationQuestion("38", "According to the woman,where can the man board the shuttle bus?", "A", "At the main entrance", "At the side entrance", "Across the street", "Inside the station",
                "A.The woman says the man can board the shuttle bus in front of the main entrance to the terminal,so (A) is the correct answer.\n"+
                        "B.The bus stops at the side entrance during peak hours,not at night.\n"+
                        "C.he does not say the man can board the shuttle bus across the street.\n"+
                        "D.She does not say the man can board the shuttle bus inside the station.", "13");
        objShortCon.addValuesToShortConversationQuestion("39", "When will the next bus leave?", "C", "In five minutes", "In ten minutes", "In fifteen minute", "In twenty minutes",
                "A.The next bus will not leaves in five minutes.\n"+
                        "B.The next bus will not leaves in ten minutes.\n"+
                        "C.The woman says the next one leaves in about fifteen minutes,meaning the next bys,so (C) is correct\n"+
                        "D.The next bus will not leaves in twenty minutes.", "13");
        objShortCon.addValuesToShortConversationQuestion("40", "What are the speakers discussing?", "C", "The location of a business", "The materials for a presentation", "A meeting with a contractor", "A proposed product design",
                "A.The contractor may want to check some of the exterior locations,but they are not discussing the location of a business.\n"+
                        "B.They do not mention a presentation.\n"+
                        "C.The woman says the general contractor called to see if he can visit the building and they talk about the timing of the visit.\n"+
                        "D.They are not discussing a proposed product design.", "14");
        objShortCon.addValuesToShortConversationQuestion("41", "When will the visitor arrive?", "B", "At 8:30 A.M.", "At 9:00 A.M.", "At 9:30 A.M.", "At 10:00 A.M.",
                "A.The visitor will not arrive at 8:30 A.M.\n"+
                        "B.The contractor suggested visiting the building at 9:00 and the man who os speaking says nine o'clock in fine,so the visitor will arrive at 9:00 A.M.\n"+
                        "C.The visitor will not arrive at 9:30 A.M.\n"+
                        "D.The visitor will not arrive at 10:00 A.M.", "14");
        objShortCon.addValuesToShortConversationQuestion("42", "Why is the man concerned?", "D", "He is locked out of a building.", "He needs driving directions.", "He has missed an appointment.", "He has a busy schedule.",
                "A.He does not say he is locked out of a building.\n"+
                        "B.The man does not need driving directions.\n"+
                        "C.The man is concerned that he could miss an appointment at 10:00,not because he has missed an appointment.\n"+
                        "D.The man says he has another meeting at 10:00 and he can only stay with the visitor for thirty minutes,so he is concerned because he has a busy schedule.", "14");
        objShortCon.addValuesToShortConversationQuestion("43", "What are the speakers discussing?", "D", "Cutting business costs", "Designing a new product", "Decorating a store", "Ordering more merchandise",
                "A.They are not talking about cutting business costs.\n"+
                        "B.They are not discussing designing a new product.\n"+
                        "C.The speakers may be in a store,but they are not discussing decorating a store.\n"+
                        "D.The man says he thinks he will order more T-shirts,which are merchandise,and they talk about the idea,so they are discussing ordering more merchandise.", "15");
        objShortCon.addValuesToShortConversationQuestion("44", "When will the yearly fall sale begin?", "D", "Today", "Tomorrow", "Next week", "Next month",
                "A.The yearly fall sale will not begin today.\n"+
                        "B.The yearly fall sale will not begin tomorrow.\n"+
                        "C.The yearly fall sale will not begin next week.\n"+
                        "D.The man refers to the annual fall sale starting next month.Annual meas yearly,so (D) is correct.", "15");
        objShortCon.addValuesToShortConversationQuestion("45", "What do the speakers suggest about their business?", "B", "It just opened recently", "Its sales are increasing", "Its employees are satisfied", "It owns a factory",
                "A.They do not suggest that the business just opened recently.\n"+
                        "B.The man says business should be on the rise,which means going up,and the woman says sales are up this month,so they suggest that sales are increasing.\n"+
                        "C.Its employees are satisfied.\n"+
                        "D.It owns a factory.", "15");
        objShortCon.addValuesToShortConversationQuestion("46", "What are the speakers discussing?", "C", "Construction of a new airport", "An employee award banquet", "A work schedule", "A conference agenda",
                "A.The woman says she has to go to the airport,but they are not discussing construction of a new airport.\n"+
                        "B.There is no mention of an employee award banquet.\n"+
                        "C.The woman asks to take a few hours off work next Friday and the man talks about next week's work,so they are discussing a work schedule.\n"+
                        "D.There is no mention a conference agenda.", "16");
        objShortCon.addValuesToShortConversationQuestion("47", "What will happen next week?", "C", "Work later than usual", "A construction project will end", "Planning for a picnic will begin", "A package will arrive.",
                "A.The woman says that her parents are coming to visit her next week,not that clients will arrive for a visit.\n"+
                        "B.The man says they need to from a construction committee,not that a construction project will end.\n"+
                        "C.The man says they need to start planning the company picnic next week,so (C) is correct.\n"+
                        "D.They do not talk about a package.", "16");
        objShortCon.addValuesToShortConversationQuestion("48", "What does the woman offer to do on Thursday?", "A", "Work later than usual", "Pick up clients from the airport", "Lead a committee meeting", "Host a company dinner",
                "A.The woman says she will be happy to work late on Thursday,so she offers to work later than usual.\n"+
                        "B.She does not offer to pick up clients from the airport.\n"+
                        "C.She does not offer to lead a committee meeting.\n"+
                        "D.She does not offer to host a company dinner.", "16");
        objShortCon.addValuesToShortConversationQuestion("49", "Where does this conversation take place?", "A", "At a doctor's office", "At a fitness center", "At a pharmacy", "At a health food store",
                "A.The man calls the woman doctor and the woman talks about the man's health,so they are at a doctor's office.\n"+
                        "B.The woman recommends more exercise,but they are not at a fitness center now.\n"+
                        "C.A pharmacy sells medicines,but they are not at a pharmacy.\n"+
                        "D.They are discussing the man's health,but they are not at a health food store.", "17");
        objShortCon.addValuesToShortConversationQuestion("50", "What does the woman recommend?", "A", "Exercising more", "Eating healthier foods", "Getting a new job", "Conducting laboratory tests",
                "A.The woman tells the man he probably should be getting more exercise,so (A) is correct.\n"+
                        "B.She does not say anything about eating healthier foods.\n"+
                        "C.She does not recommend getting a new job.\n"+
                        "D.She talks about tests that have been done.She is not recommending conducting laboratory tests in the future.", "17");
        objShortCon.addValuesToShortConversationQuestion("51", "When will the man probably return?", "C", "In two weeks", "In one months", "In six months", "In one year",
                "A.There is nothing in the conversation to indicate that he will return in two weeks.\n"+
                        "B.There is nothing in the conversation to indicate that he will return in one month.\n"+
                        "C.The woman mentions the man's next appointment and says see you in six months,so the man will probably return in six months.\n"+
                        "D.There is nothing in the conversation to indicate that he will return in one year.", "17");
        objShortCon.addValuesToShortConversationQuestion("52", "Why did the woman call the man?", "A", "To offer him a job", "To explain company benefits", "To request time off", "To invite him to a party",
                "A.The woman says to the man we'd like to offer you the job,so (A) is correct.\n"+
                        "B.She does not mention company benefits.\n"+
                        "C.The woman does not request time off.She is not the man's employee.\n"+
                        "D.No party is mentioned.", "18");
        objShortCon.addValuesToShortConversationQuestion("53", "When will the speakers probably see each other again?", "C", "On Tuesday", "On Thursday", "On Friday", "On monday",
                "A.Tuesday is not mentioned.\n"+
                        "B.The interview was last Thursday.\n"+
                        "C.The woman says that tomorrow is Friday and the man says he will see her tomorrow.Also the woman asks the man to come in Friday.\n"+
                        "D.The man will start on Monday after completing paperwork on Friday.", "18");
        objShortCon.addValuesToShortConversationQuestion("54", "What does the man need to do tomorrow?", "A", "Complete some from", "Send the woman an e-mail", "Contact another staff member", "Conduct an interview",
                "A.Tomorrow is Friday and the woman asks the man to come in then to complete some paperwork,which could mean forms.\n"+
                        "B.Tomorrow he does not need to send the woman an e-mail.\n"+
                        "C.Tomorrow he does not need to contact another staff member.\n"+
                        "D.Tomorrow he does not need to conduct an interview.", "18");
        objShortCon.addValuesToShortConversationQuestion("55", "What are the speakers discussing?", "D", "Opening a new showroom", "Changing employee benefits", "Decreasing factory production", "Purchasing industrial equipment",
                "A.Bob has a showroom.They are not discussing opening a new showroom.\n"+
                        "B.They do not talk about changing employee benefits.\n"+
                        "C.They do not talk about decreasing factory production.\n"+
                        "D.The first man talks about a machine he bought at Bob's showroom which increased productivity, so it is industrial equipment.They also talk about safety equipment the man needs.", "19");
        objShortCon.addValuesToShortConversationQuestion("56", "What kind of employees are mentioned?", "D", "Accountants", "Security guards", "Product designers", "Factory workers",
                "A.Accountants are not mentioned.\n"+
                        "B.Security guards are not mentioned.\n"+
                        "C.product designers are not mentioned.\n"+
                        "D.The first man refers to my factory and then mentions that my workers need some new safety equipment,so he mentions factory workers.", "19");
        objShortCon.addValuesToShortConversationQuestion("57", "What will happen next week?", "A", "A sale will start", "A show will be performed", "A security inspection will be held", "A business will close down",
                "A.Bob says next week is when our annual discount sale starts,so (A) is the correct answer\n"+
                        "B.Bob has a showroom.They do not say a show will be performed next week.\n"+
                        "C.Safety equipment is mentioned,but they do not say a security inspection will be held next week.\n"+
                        "D.They do not say a business will close down next week.", "19");
        objShortCon.addValuesToShortConversationQuestion("58", "Why does the man want to speak with the woman?", "D", "To arrange payment for work", "To tell her about a printing problem", "To ask her to review a document", "To clarify some instructions",
                "A.He does not ask about payment.\n"+
                        "B.A book is going to be reprinted,but the man does not mention a printing problem.\n"+
                        "C.The man does not ask the woman to review a document.\n"+
                        "D.The man says i want to make sure i understand exactly what you want me to do,so he wants to clarify some instructions.", "20");
        objShortCon.addValuesToShortConversationQuestion("59", "What kind of project will the man be working on?", "D", "Writing an essay", "Publishing a magazine", "Marketing a textbook", "Editing a book",
                "A.He will not be writing an essay.\n"+
                        "B.He will not be publishing a magazine.\n"+
                        "C.He is asked to mark places where changes need to be made in the textbook,not market a textbook.\n"+
                        "D.The woman asks the man to review a book and mark changes that need to be made,which is editing a book.The man confirms that he will be editing it.", "20");
        objShortCon.addValuesToShortConversationQuestion("60", "When does the man need to finish the project?", "C", "In two hours", "By the end of today", "Tomorrow", "In a few days",
                "A.He is not asked to finish it in two hours.\n"+
                        "B.He is not asked to finish it by the end of today.\n"+
                        "C.The woman says she needs it by the ebd of the day tomorrow,so he needs to finish it tomorrow.\n"+
                        "D.He is not asked to finish it in a few days.", "20");


        // ShortC Choice




    }

    private void addValuesShortTalkScript() {

        objShortTalkScript.AddValuesToShortTalkScript("1","Hi, Paul, this is Christine Roberts. I'm concerned that the oil pan in my car is leaking, so I wanted to see if you could take a look at it this afternoon. For the last couple of weeks, I've been noticing that my oil underneath the car in my garage. I hope you'll be able to get back to me soon, as I'll have to leave in two hours. If you think you could fit me into your schedule today, I could drop the car off on my way to work and you can just give me a call to let me know how much it will cost for you to fix it. My number is 555-1058. Thank a lot. Bye.");
        objShortTalkScript.AddValuesToShortTalkScript("2","Good morning. This is Harold Chang for Movie Tuesday. In this session, I'll be interviewing Maura O'Connor, star of the new comedy film, Forever and a Day Later. It opens here is London on Thursday. Ms. O'Connor is known primarily as a dramatic actress. Is her career headed in a new direction? We'll get her views on doing comedy, what it's like to work with her costar Derek Jones, and her adventures while filming and traveling in Australia. So stay tuned to this station for Maura O'Conner, speaking about her role in Forever and a Day Later, which premieres in London on Thursday. And now, it's time for the weather and traffic report.");
        objShortTalkScript.AddValuesToShortTalkScript("3","You have reached Tristar's automated account information line. Our records indicate that you have a current balance of forty-two euros. Your next payment is due on January 18th. If you would like to make a payment now, please press one. For details of the charges on your bill, please press two. To speak to a customer service representative, please press nine. To repeat these options, please stay on the line.");
        objShortTalkScript.AddValuesToShortTalkScript("4","Now we're entering the laboratory portion of the Alyeska Electronic Piano factory. Our lab places a great deal of emphasis on both sound quality and keyboard mechanics, and we're always researching ways to appoximate what a real piano does with our latest model for the last five years, and we expect to release it in two more years. You can imagine how labor-intensive all this research is.");
        objShortTalkScript.AddValuesToShortTalkScript("5","Because our company is concerned about the environment, there are some changes as to how office waste will be handled. Starting next Friday, receptacles will be placed throughout the office for recycling. Please note that all red containers are for anything that is plastic, while all green containers are meant for glass items. These containers will be emptied each Wednesday and their contents sent to a recycling facility. As you leave, please take one of the papers on the table at the back of the room. It summarizes all of the information I've just given you. Remember, the program doesn't begin until Friday. Thank you for your cooperation with this effort.");
        objShortTalkScript.AddValuesToShortTalkScript("6","Attention, passengers awaiting Crane airways flight 7 from Shanghai to Osaka. We regret to inform you that we must delay take-off again. We had hoped that by now the weather conditions in and around Osaka would have improved, but we're told they have not. At this time we'd like to provide hotel accommodations for all ticketed passengers. Outside terminal 6, a bus is waiting to take you to the pavilion Hotel, where you will spend the night. Crane Airways agents will distribute meal vouchers as you board the bus. The vouchers are good for one free dinner and one free breakfast. At 8 A.M. the bus will leave the hotel to bring you back to the terminal. We have rescheduled the flight for 10 o'clock tomorrow morning. So, please be waiting out in front of the hotel tomorrow morning at 8 A.M.");
        objShortTalkScript.AddValuesToShortTalkScript("7","Next week, we'll begin conducting a survey to find out how much people are willing to pay for luggage. Previous survey tell us that customers are frustrated when they can't quickly find things in their suitcases are designed. We've come up with a suitcase model that addresses that problem, but we need to find out if people are willing to pay a price high enough to make selling it profitable.");
        objShortTalkScript.AddValuesToShortTalkScript("8","Now that our morning meet-and-greet is coming to an end, please go to the table at the back of the room to pick up your training packet, which includes course materials, articles, and biographies of our instructors. Then find a seat and take a minute to review the write-up of Mr. Yi Chang, our first speaker, to familiarize yourself with his 1-2-3 Sales! techniques before he comes out to give his seminar.");
        objShortTalkScript.AddValuesToShortTalkScript("9","Good evening, and welcome to Micheal's. We're so happy to have you dining with us tonight. To start off your evening, I can recommend a fresh garden salad with your choice of dressing. Oh, and all of our garden vegetables are purchased fresh form local farmers. For our main course specials this evening, we can offer you either a choice of grilled seafood or vegetarian dishes. I'll give you a little more time to look over the menu and make your choices but, before I go, what can i get you to drink this evening?");
        objShortTalkScript.AddValuesToShortTalkScript("10","Hello, Ms Dai. This is Marie Nakata calling on behalf of Kochimo Studios. I received your cover letter, resume, and reference list on Monday. Your credentials seem very impressive. I contacted Ms. Ando, the editor of Singapore Today Magazine, and she said that the freelance work you did for the magazine was phenomenal. I would like to schedule an interview with you for Friday morning at 9 o'clock. Please bring with you a portfolio of your work so that I can have a more extensive look at the types of photos you have taken. I will be out of the office all day Thursday, so please try to contact me before then to confirm that you could come in on Friday. I look forward to meeting with you.");

    }

    private void addValuesShortTalkQuestion(){

        objShortTalkQuestion.AddValuesToShortTalkQuestion("1","Who is the speaker most likely calling?","c", "Her supervisor","Her secretary","A mechanic","A salesperson",
                "A. The woman would not be likely to ask her supervisor.\n" +
                        "B. The woman would not be likely to ask her secretary.\n" +
                        "C. The woman is leaving a message for Paul. She explains about a problem with her car. She wants to know if Paul could take a look at it and asks him to let her know how much it will cost for him to fix it,so she is most likely calling a mechanic. \n" +
                        "D. The woman would not be likely to ask her salesperson to fix her car.","1");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("2","What problem is the speaker reporting?","b", "A broken switch","A leak","A scheduling conflict","A travel delay",
                "A. She does not mention a problem with a broken switch.\n" +
                        "B. The woman says she think the oil pan in her car is leaking.She mentions that the oil is running low and she finds oil underneath her car, which are both signs of an oil leak. \n" +
                        "C. She hopes that Paul can fit her into his schedule,but she does not mention a scheduling conflict.\n" +
                        "D. She says when she will leave home,but does not mention a travel delay.","1");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("3","When does the speaker request a response?","a", "Within a few hours","Within a day","Within two days","Within a week",
                "A. She asks Paul to get back to her,or respond to her request,soon and says she will be leaving in two hours.Thus,she wants him to respond within a few hours,because a few means two or three. \n" +
                        "B. She does not request a response within a day. \n" +
                        "C. She does not request a response within two days. \n" +
                        "D. She does not request a response within a week.","1");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("4","Who most likely is the speaker?","d", "An actor","A film director","A travel agent","A radio announcer",
                "A. The speaker is interviewing an actress. He is not an actor himself. \n" +
                        "B. He is interviewing the star of a film. The speaker is not a film director. \n " +
                        "C. The actress will talk about traveling in Australia, but the speaker is not a travel agent. \n" +
                        "D. The key phrases stay tuned to this station (radio station) and now it's time fot the weather and traffic report,indicate that the speaker is a radio announcer.","2");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("5","What is stated about the film?","b", "It is a drama.","It is a comedy.","It is an adventure film.","It is a documentary.",
                "A. The film they will discuss is not a drama. \n" +
                        "B. The interview with the actress Maura O'Connor is mainly about her role in the film Forever and a Day Later,which the speaker says is a comedy film. \n" +
                        "C. The film they will discuss is not an adventure film. \n" +
                        "D. The film they will discuss is not a documentary.","2");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("6","When is the movie's London premiere?","c", "On Tuesday","On Wednesday","On Thursday","On Friday",
                "A. The London premieres is not on Tuesday. \n " +
                        "B. The London premieres is not on Wednesday. \n " +
                        "C. The speaker says the film premieres in London on Thursday,so this is the correct answer. \n" +
                        "D. The London premieres is not on Friday.","2");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("7","What is the recording mainly about?","a", "Account information","Mailing instructions","Hours of operation","Order information",
                "A. The speaker announces that the caller has reaches an account information line and then gives information about the caller's account, such as the current balance and the date the next payment is due. \n" +
                        "B. The speaker gives some instructions, but the message is not about mailing instructions. \n" +
                        "C. The speaker does not give any hours of operation. \n" +
                        "D. The message is not about an order.","3");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("8","What will happen on january 18th?","c", "An office will be closed.","An order will be shipped.","A payment will be due.","An account will be opened.",
                "C. In the message,the speaker says your next payment is due on January 18. \n" +
                        "There is no mention of anything else happening on January 18,so(A),(B),and(D) are incorrect.","3");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("9","How can the listener reach a customer service representative?","d", "By calling another number","By staying on the line","By saying the word \"zero\"","By selecting option \"nine\"",
                "A. The listener is instructed to press a particular number on their phone, not to hang up and call another number. \n" +
                        "B. By staying on the line,the listener would hear all of the option again,not reach a customer service representative. \n" +
                        "C. Saying the word \"zero\" is not mentioned in the message. \n" +
                        "D. The listener is instructed to press nine to speak to a customer service representative. In other words, the listener should select option \"nine\".","3");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("10","Where is the speaker?","a", "In a keyboard factory","At a concert hall","In a biology laboratory","At an electronics store",
                "A. At the start of the talk,the speaker announces that they are entering the laboratory portion of the Alyeska Electronic Piano factory,so the speaker is in an electronic piano or keyboard factory. \n" +
                        "B. The speaker is in a factory where they manufacture musical instruments,not at a concert hall. \n" +
                        "C. The speaker is entering a laboratory,but it is not a biology laboratory. \n" +
                        "D. The factory manufactures electronic pianos. It is not an electronics store.","4");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("11","What is the speaker discussing?","b", "An upcoming performance","Company research plans","Machine operating instructions","Factory quality inspections",
                "A. A future musical performance is not mentioned. \n" +
                        "B. The speaker is discussing research that the company has done and will continue over the next two years to develop electronic keyboards that behave similarly to real pianos. \n" +
                        "C. The speaker is not discussing the instructions for operating a machine. \n" +
                        "D. She talks about the company's emphasis on sound quality,but is not discussing factory quality inspections.","4");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("12","When will the newest model probably be released?","c", "In two months","In one year","In two years","In five years",
                "A. She does not indicate that the newest model is likely to be released in two months. \n" +
                        "B. She does not indicate that the newest model is likely to be released in one year. \n" +
                        "C. Talking about the latest model,the speaker says they expect to release it tn two more years,which means it will probably be released in two years. \n" +
                        "D. They have been developing the latest model for the last five year,but they are planning to release it in two year.","4");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("13","What is the speaker discussing?","b", "A product catalog","A recycling system","A document filling plan","An art display",
                "A. The speaker does not talk about a product catalog. \n" +
                        "B. The speaker discusses changes in how office waste will be handled waste. The speaker talks about receptacles for recycling and says their contents will be sent to a recycling facility. \n" +
                        "C. The speaker is not talking about a filling plan. \n" +
                        "D. There is no mention of an art display.", "5");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("14","When will a change in procedures begin?","d", "On Tuesday","On Wednesday","On Thursday","On Friday",
                "A. The change in procedure will begin on Friday,not Tuesday. \n" +
                        "B. This is when the recycling containers will be emptied, not when the new system will begin. \n" +
                        "C. The change in procedure will begin on Friday, not Thursday.\n" +
                        "D. The speaker refers to the changers as starting next Friday and repeats at the end of the talk that the program doesn't begin until Friday,which means it begins on Friday.","5");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("15","What is the audience asked to do?","c", "Visit the speaker's office","Select item to order","Pick up a sheet of paper","Reorganize their offices",
                "A. An office is the context for the announcement,but the audience is not asked to visit the speaker's office. \n" +
                        "B. There is no mention of ordering items. \n" +
                        "C. The speaker asks the listeners to take one of the papers as they leave.Pick up means the same as take in this context. \n" +
                        "D. The recycling receptacles will be in the office,but the listeners are not asked to reorganize their offices.","5");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("16","What is the purpose of the talk?","c", "To describe the weather in Shanghai","To request that passengers board the plane","To announce a flight delay","To ask for volunteers to take a later flight",
                "A. The weather in Osaka is mentioned because it is causing the delay,but the weather in Shanghai is not described. \n" +
                        "B. Passengers are not asked to board the plane. They are asked to board a bus. \n" +
                        "C. The speaker announces that they must delay take-off of flight 7 and gives instructions for passengers planning to take that flight. \n" +
                        "D. A volunteer offers to do something. Here the speaker is informing passengers that they must take a later flight.","6");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("17","What will the airline give passengers?","b", "City maps","Meal vouchers","Bus timetables","Rail passes",
                "A. The speaker does not say passengers will be given city maps. \n " +
                        "B. Meal vouchers will be distributed, or given,to the passengers as they board the bus. \n" +
                        "C. The airline has arranged transportation by bus from the airport to the hotel and back. The airline will not give the passengers bus timetables. \n " +
                        "D. The speaker does not mention rail passes.","6");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("18","When will the bus leave the hotel?","c", "At 6 A.M.","At 7 A.M.","At 8 A.M.","At 10 A.M.",
                "A. The bus will not leave at 6 A.M. \n" +
                        "B. The bus will not leave at 7 A.M. \n" +
                        "C. Passengers are informed that the bus will leave the hotel at 8 A.M. the next day. \n" +
                        "D. The flight is scheduled to leave at 10 A.M. , not the bus.","6");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("19","What kind of company is conducting a survey?","c", "A clothing store","A package delivery service","A suitcase manufacturer","A travel agency",
                "A. The survey is not being conducted by a clothing store. \n" +
                        "B. The survey is not being conducted by a package delivery service. \n" +
                        "C. The company has developed a new suitcase model and is conducting a survey to find out how much people are willing to pay for luggage,so the company is a suitcase manufacturer. \n" +
                        "D. Suitcase are used for traveling, but it is the suitcase manufacturer that is conducting the survey, not a travel agency.","7");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("20","What have customers complained about in previous survey?","d", "High prices","Limited choice of sizes","Crowded store","Poor product design",
                "A. The survey being announced is about price, but previous surveys looked at other factors. \n" +
                        "B. The speaker does not mention that customers have complained about size in previous surveys. \n" +
                        "C. Crowded stores are not mentioned. \n" +
                        "D. Previous surveys have found that people get frustrated when they cannot find things in their suitcases. The speaker says this means they don't like the way the suitcases are designed, In other words. they have complained about poor product design.","7");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("21","What has the company created?","a", "A new product","A Web site","A catalog","An instruction manual",
                "A. We've come up with in the talk means we've created. The suitcase model the speaker mentions is a new product created by the company. \n" +
                        "B. The speaker does not say that the company he works for has created a Web site. \n" +
                        "C. The speaker does not say that the company he works for has created a catalog. \n" +
                        "D. The speaker does not say that the company he works for has created an instruction manual.","7");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("22","Who most likely is being addressed?","a", "Participants in a training seminar","Instructors for a writing course","Reporters at a press conference","Visitors to a museum",
                "A. The speaker is most likely addressing participants in a training seminar because she asks the listeners to pick up a training packet, talks about course materials and instructors and announces the first speaker, who will give a seminar. \n" +
                        "B. The speaker talks about the instructors. She is not addressing the instructors, which means talking to them. Also, this is not a writing course. \n" +
                        "C. Reporters at a press conference would not be given training packets and would not be attending a seminar. \n" +
                        "D. Visitors to a museum are not likely to attend a seminar on sales techniques.","8");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("23","What are audience members asked to do?","c", "Leave the room","Write a report","Pick up some papers","Introduce themselves to Mr. Chang",
                "A. The speaker does not ask anyone to leave the room. \n" +
                        "B. Audience members are asked to review a write-up or read a description of the first speaker,not to write a report. \n" +
                        "C. The speaker asks the audience members to pick up a training packet. The contents of the training packet are papers. \n" +
                        "D. The speaker asks audience members to familiarize themselves with Mr. Chang's sales techniques by reading the information, not by introducing themselves to him.","8");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("24","What will Mr. Chang probably do?","d", "Describe a recent event","Distribute course materials","Introduce a speaker","Discuss sales techniques",
                "A. There is no mention of a recent event so it is unlikely that Mr. Chang will describe one. \n" +
                        "B. The audience members are asked to pick up the course materials themselves. \n" +
                        "C. The woman giving this speech is introducing the speaker. Mr. Chang is unlikely to introduce a speaker. \n" +
                        "D. The speaker asks the participants to rea about Mr. Chang's sales techniques before he gives his seminar, so he will probably discuss his sales techniques.","8");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("25","Who most likely is the speaker?","a", "A waiter","A restaurant customer","A chef","A radio food critic",
                "A. The speaker says we're so happy to have you dining with us,then describes the food available and asks what the listeners would like to drink,so he is most likely a waiter. \n" +
                        "B. The speaker is talking to restaurant customers. He is not a customer himself. \n" +
                        "C. A chef does not generally take the customer's orders. \n" +
                        "D. The speaker is telling the customers about the food available in this restaurant, not talking about food on a radio program.","9");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("26","What does the speaker say about the vegetables?","d", "They are streamed.","They are inexpensive.","They are not available.","They are locally grown.",
                "A. Steaming is a way of cooking vegetables.The speaker does not say how the vegetables are cooked. \n" +
                        "B. The speaker does not mention the price of the vegetables. \n" +
                        "C. Since he is recommending the vegetables, they must be available. \n" +
                        "D. The speaker describes the vegetables as purchased fresh from local farmers, which implies they are locally grown.","9");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("27","What does the speaker ask for?","a", "A beverage order","A bill","A restaurant menu","A recipe",
                "A. At the end of the talk, the speaker asks what he can get the customers to drink.A beverage is a drink,so he asks for the customer'beverage order. \n" +
                        "B. A waiter does not ask for a bill. \n" +
                        "C. A waiter does not ask for a restaurant menu.\n" +
                        "D. A recipe gives instructions for cooking a dish. A waiter does not need a recipe.","9");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("28","Who most likely is Ms. Dai?","b", "An advertising salesperson","A photographer","A receptionist","A writer",
                "A. There is no mention of advertising or selling advertising salesperson. \n" +
                        "B. The speaker says wants to see the types of photos Ms.Dai has taken,so Ms.Dai is most likely a photographer. \n" +
                        "C. The speaker does not say Ms. Dai has worked as a receptionist. \n" +
                        "D. The speaker mentions work Ms. Dai has done for a magazine, but she does not describe it as writing.","10");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("29","What does the caller ask Ms. Dai to bring with her?","a", "A portfolio","A list of references","A resume","A cover letter",
                "A. The speaker asks Ms.Dai to bring a portfolio of her work,which is a collection of samples of her work. \n" +
                        "B. The speaker refers to list of references Ms.Dai has already sent her, so she does not ask Ms.Dai to bring this with her. \n" +
                        "C. The speaker has also already received a resume. \n" +
                        "D. A job applicant sends a cover letter with a job application and the speaker says she has already received that.","10");
        objShortTalkQuestion.AddValuesToShortTalkQuestion("30","When will the interview most likely take place?","d", "On Monday","On Wednesday","On Thursday","On Friday",
                "A. Monday is the day when the speaker received the application. She does not suggest holding the interview on a Monday. \n" +
                        "B. She does not ask to schedule the interview on Wednesday. \n" +
                        "C. She does not ask to schedule the interview on Thursday. \n" +
                        "D. The speaker would like to schedule the interview for Friday,so it is most likely that it will take place on Friday.","10");


    }

    private void AddValuesToIncompleteSentence(){


        //Incomplete Question
        objIncomplete.addValuesToIncompleteQuestion("1", "Ms.Walters ............ to another branch, so your new financial advisor will be Mr.Merenda.", "C");
        objIncomplete.addValuesToIncompleteQuestion("2", "The restaurant on Main Street offers a wide selection of gourmet desserts ............ several regions of the world.", "C");
        objIncomplete.addValuesToIncompleteQuestion("3", "............interested in viewing an apartment should contact the property manager to arrange an appointment.", "B");
        objIncomplete.addValuesToIncompleteQuestion("4", "A growing ............ in the cosmetics industry is the use of natural and organic ingredients.", "D");
        objIncomplete.addValuesToIncompleteQuestion("5", "Because of its ............ melodies and upbeat rhythms, Toby Nathan's music has broad appeal.", "A");
        objIncomplete.addValuesToIncompleteQuestion("6", "The park service asks visitors to behave ............ and show respect for wildlife.", "A");
        objIncomplete.addValuesToIncompleteQuestion("7", "It is not the company's policy to grant sick leave ............ overtime pay to part-time employees.", "D");
        objIncomplete.addValuesToIncompleteQuestion("8", "The ............ of the Board of Directors is scheduled for Monday.", "A");
        objIncomplete.addValuesToIncompleteQuestion("9", "Last year, the number of new university-level textbooks ................... by American publishers dropped for the second year in a row.", "B");
        objIncomplete.addValuesToIncompleteQuestion("10", "Traffic congestion is ...................than usual because of road construction,so it will take us at least an hour to get to the meeting.", "D");
        objIncomplete.addValuesToIncompleteQuestion("11", "Investors who lose faith in a company ................... sell off their stocks and invest elsewhere.", "D");
        objIncomplete.addValuesToIncompleteQuestion("12", "CTC announced on Monday that a European media group is expected to ...................its online music store.", "A");
        objIncomplete.addValuesToIncompleteQuestion("13", "The Tourist Board of Western Quebec is developing a marketing ...................to help them increase tourism to the region.", "A");
        objIncomplete.addValuesToIncompleteQuestion("14", "Negotiators should be aware that the Prime Minister has a very ...................manner of speaking.", "B");
        objIncomplete.addValuesToIncompleteQuestion("15", "Jean Mallet has been selected to replace Henri Valois,................... is retiring as president and executive officer of Marteux Pharmaceutical Corporation.", "D");
        objIncomplete.addValuesToIncompleteQuestion("16", "In order to suppress harmful insects and weeds, garden maintenance companies must choose the right products and apply them ................... .", "C");
        objIncomplete.addValuesToIncompleteQuestion("17", "McGrath Publications has not published a best seller ...................Simon Porter's book The Point was released eight years ago.", "A");
        objIncomplete.addValuesToIncompleteQuestion("18", "The CEO will use her ................... in determining how the reorganization of the company will be conducted.", "D");
        objIncomplete.addValuesToIncompleteQuestion("19", "For more than three decades, Beecham Construction has helped clients ...................their ideas into beautifully executed projects.", "B");
        objIncomplete.addValuesToIncompleteQuestion("20", "...................by the audience's positive reaction to its music, the Gary Jones Band played well past midnight.", "A");
        objIncomplete.addValuesToIncompleteQuestion("21", "................... you are buying or selling a house, be sure to use a real estate agent whose knowledge of the local market is comprehensive.", "C");
        objIncomplete.addValuesToIncompleteQuestion("22", "The application process for loans from lnhouse Financing is easier than ................... , eliminating most of the typical paperwork.", "D");
        objIncomplete.addValuesToIncompleteQuestion("23", "Income from online advertising has been growing, but is still a ................... small part of overall newspaper revenue.", "D");
        objIncomplete.addValuesToIncompleteQuestion("24", "Companies that care more about customers than investors often achieve ................... growth and high rates of long-term financial gain.", "C");
        objIncomplete.addValuesToIncompleteQuestion("25", "The ................... from most of our readers was positive,though many wondered why we wanted to transform a layout that was already so appealing.", "B");
        objIncomplete.addValuesToIncompleteQuestion("26", "The houses on the street are fairly close to ................... ;however, the fences that surround each property help to ensure privacy.", "A");
        objIncomplete.addValuesToIncompleteQuestion("27", "The young fashion designer wanted to create dress styles ................... different from those of her contemporaries.", "D");
        objIncomplete.addValuesToIncompleteQuestion("28", "The Action Shot X52 underwater camera is recommended ...................depths of up to two hundred feet.", "C");
        objIncomplete.addValuesToIncompleteQuestion("29", "Monthly reports from all divisions of the company must be delivered to the human resources office ................... by 5 P.M . today.", "B");
        objIncomplete.addValuesToIncompleteQuestion("30", "Many environmental analysts recommend that nations reduce their ...................on non-renewable energy sources.", "B");
        objIncomplete.addValuesToIncompleteQuestion("31", "...................the firm's notable achievements this past year was the opening of a new research and development center in Seoul.", "B");
        objIncomplete.addValuesToIncompleteQuestion("32", "The revival of the ferry service to Seawise Island was initially viewed as a ...................notion by many, but it turned out to be profitable.", "D");
        objIncomplete.addValuesToIncompleteQuestion("33", "According to a survey ...................by the Fielding Institute, advertising on the Internet accounted for 10% of total advertising.", "A");
        objIncomplete.addValuesToIncompleteQuestion("34", "Public speaking experts agree that it is better to express simple ideas ................... than to use complex structures with no persuasive point.", "C");
        objIncomplete.addValuesToIncompleteQuestion("35", "Work in excess of 8 hours per day, ................... authorized by the client, will be invoiced at 1.3 times the regular hourly rate.", "A");
        objIncomplete.addValuesToIncompleteQuestion("36", "A particularly ................... drawback of this book is the almost complete lack of useful illustrations or tables.", "A");
        objIncomplete.addValuesToIncompleteQuestion("37", "From the first measurement to the last stitch,the Sagamore brothers have been ...................custom-made shirts for fifty years in their New Haven workshop.", "C");
        objIncomplete.addValuesToIncompleteQuestion("38", "Researcher Clement Chappelle was awarded £11,000 by the Ogden County Council to analyze the ................... of removing dams along the River Bourne.", "B");
        objIncomplete.addValuesToIncompleteQuestion("39", "In spite of the rainy weather, last evening's holiday reception was ...................attended by staff researchers and administrators.", "A");
        objIncomplete.addValuesToIncompleteQuestion("40", "The Web-based marketplace is drawing thousands of customers away from leading companies, despite an ...................inflated market.", "D");
        objIncomplete.addValuesToIncompleteQuestion("41", "The travel agency will make your travel ............. and send your tickets to the office by the end of the week.", "C");
        objIncomplete.addValuesToIncompleteQuestion("42", "Dr. Viella Diop is best known for her ............. contributions to the field of physics.", "C");
        objIncomplete.addValuesToIncompleteQuestion("43", "Because of the severe weather, Mr. Kim asked if ............. could leave the office a little earlier than usual.", "A");
        objIncomplete.addValuesToIncompleteQuestion("44", "If you ............. additional assistance, please do not hesitate to contact us.", "C");
        objIncomplete.addValuesToIncompleteQuestion("45", "The Smithson Bank is well-known for the ............. welcome that it extends to all new employees.", "A");
        objIncomplete.addValuesToIncompleteQuestion("46", "Either the organization's sponsors will pay for the building addition ............. we will have to raise the money ourselves.", "A");
        objIncomplete.addValuesToIncompleteQuestion("47", "Our recruiter will be traveling to several universities to interview graduating students for ............. in our marketing department.", "B");
        objIncomplete.addValuesToIncompleteQuestion("48", "The Fountainview Hotel has rooms available for anyone who plans on ............. in Detroit during the annual conference.", "C");
        objIncomplete.addValuesToIncompleteQuestion("49", "Please .............your supervisor as soon as possible in the event of a machinery failure.", "D");
        objIncomplete.addValuesToIncompleteQuestion("50", "The All-Bright safety vest is designed ............. bikers who travel at night.", "A");



        //Incomplete Choice
        objIncomplete.addValuesToIncompleteChoice("1", "transfer", "transferring", "has transferred", "transferable",
                "A.The simple present tense transfer cannot be used here because the action happened in the past.\n"+
                "B.The gerund transferring is incorrect because a verb tense is needed here.\n"+
                "C.The present perfect tense has transferred is needed because Ms.Walters' move happened at an unspecified timein the past, but has an effect in the present.\n"+
                "D.The adjective transferable is incorrect as a verb is needed to describe the action of Ms.Walters.", "1");
        objIncomplete.addValuesToIncompleteChoice("2", "with", "by", "from", "until",
                "A.With shows that one thing accompanies another, for example when we say \"I'd like chocolate sauce with my ice-cream.\" That is not the case here.\n"+
                "B.By is used to indicate who or what performed an action. Regions of the world did not perform an action here.\n"+
                "C.The preposition from is used here to talk about the origin of something, in this case the desserts.\n"+
                "D.Until is a preposition of time. A preposition of space is needed here.", "2");
        objIncomplete.addValuesToIncompleteChoice("3", "These", "Those", "This", "That",
                "A.These is incorrect here because it is used to refer to particular people.\n"+
                "B.The demonstrative pronoun those should be used here to mean any people.\n"+
                "C.This is a singular pronoun and is used to refer to a particular thing or person, so it is incorrect here.\n"+
                "D.That is singular.The sentence is directed at more than one person.", "3");
        objIncomplete.addValuesToIncompleteChoice("4", "product", "scent", "sale", "trend",
                "D.Trend should be used for a change in the type of ingredients used in cosmetics. Using the adjective growing with the noun trend is an acceptable collocation;that is, these words are often  used together. A growing (A) product, (B) scent, and (C) sale are not words that are used together,so these choices are incorrect.", "4");
        objIncomplete.addValuesToIncompleteChoice("5", "simple", "patient", "kind", "blank",
                "A.An adjective to describe melodies or tunes is needed. Melodies can be described as simple.(B) Patient and (C) kind are adjectives which describe people, so they are incorrect.\n"+
                "D.The adjective blank means having nothing written or recorded on it. It is not used to describe melodies .", "5");
        objIncomplete.addValuesToIncompleteChoice("6", "responsibly", "responsible", "responsibility", "responsibilities",
                "A.An adverb is needed after the verb.The adverb responsibly describes how visitors are expected to behave.\n"+
                "B.The adjective responsible cannot be used to describe the verb.\n"+
                "(C) Responsibility and (D) Responsibilities are nouns. A noun is not used after the verb behave.", "6");
        objIncomplete.addValuesToIncompleteChoice("7", "yet", "if", "but", "or",
                "A.Yet can be a conjunction meaning nevertheless and is generally used to introduce a verb clause.Overtime pay to part-time employees is not a verb clause, so yet is incorrect.\n"+
                "B.If also introduces a verb clause, so it is not correct here.\n"+
                "C.The conjunction but is used to show contrast. Sick leave and overtime pay are not contrasting ideas here.They are both benefits given by an employer.\n"+
                "D.We use the conjunction or to link two or more things in a sentence containing not or another word with negative meaning.", "7");
        objIncomplete.addValuesToIncompleteChoice("8", "election", "elected", "elects", "electable",
                "A.The noun election is needed with the definite article the.\n"+
                "B.Elected is the past participle of elect. A noun is needed here, not a verb.\n"+
                "C.Elects is also a verb, so it is incorrect.\n"+
                "D.Electable is an adjective, so it is not correct here.", "8");
        objIncomplete.addValuesToIncompleteChoice("9", "priced", "sold", "marked", "instructed",
                "A.Priced would need to be followed by an actual price e.g. priced at less than $40, so it is incorrect.\n"+
                "B.Sold, the past participle of sell, forms a participle clause with the words by American publishers to describe new university-level textbooks.\n"+
                "C.Marked would not form a meaningful participle clause with by American publishers.\n"+
                "D.Students are instructed by books. Instructed cannot be used to describe textbooks.", "9");
        objIncomplete.addValuesToIncompleteChoice("10", "badly", "bad", "worst", "worse",
                "A.An adjective is needed, not an adverb.\n"+
                "B.The adjective bad is incorrect because it is not the comparative form of the adjective.\n"+
                "C.Worst is the superlative form of bad, so it is incorrect.\n"+
                "D.The comparative adjective worse should be used to complete the comparison worse than usual, describing traffic congestion.", "10");
        objIncomplete.addValuesToIncompleteChoice("11", "exactly", "greatly", "approximately", "typically",
                "D.An adverb can be used before sell off to comment on the investors' action of selling off their stocks.Typically here means generally or usually.\n"+
                "The adverbs (A) exactly, (B) greatly, and (C) approximately cannot be used to describe sell off.", "11");
        objIncomplete.addValuesToIncompleteChoice("12", "buy", "buying", "bought", "has bought",
                "A.The verb following expect should be a verb infinitive with to, so buy is correct here.\n"+
                "B.This is incorrect because buying is a gerund, not an infinitive.\n"+
                "C.Bought is the simple past form of the verb to buy, not the infinitive.\n"+
                "D.The present perfect has bought is also incorrect.", "12");
        objIncomplete.addValuesToIncompleteChoice("13", "proposal", "permission", "appliance", "employment",
                "A.Proposal is the correct noun to use with marketing. A marketing proposal is a plan for marketing or promoting something,in this case tourism in a particular region.\n"+
                "B.We do not generally use permission with marketing.Also, permission  is not something that can be developed.\n"+
                "C.An appliance is a piece of equipment. We do not talk about a marketing appliance.\n"+
                "D.We do not use employment with marketing, so this choice is incorrect.", "13");
        objIncomplete.addValuesToIncompleteChoice("14", "mutual", "direct", "adjacent", "existing",
                "A.Mutual means shared by more than one person, so it cannot be used for a single person, the Prime Minister\n"+
                "B.A manner of speaking is a person's way of speaking. A person who has a direct manner of speaking generally says exactly what they mean.\n"+
                "C.Adjacent means next to, so it cannot describe someone's manner of speaking.\n"+
                "D.Existing cannot be modified by very and cannot describe manner of speaking.", "14");
        objIncomplete.addValuesToIncompleteChoice("15", "that", "it", "which", "who",
                "A.The relative pronoun that can be used for a person, but only in a defining relative clause. Here, the relative clause after the comma is a non-defining relative clause.\n"+
                "B.It is not a relative pronoun and also refers to a thing, not a person.\n"+
                "C.The relative pronoun which cannot be used to refer to a person.\n"+
                "D.A relative pronoun which refers to a person, Henri Valois, is needed, so who is the correct choice.", "15");
        objIncomplete.addValuesToIncompleteChoice("16", "correction", "corrected", "correctly", "correcting",
                "C.The adverb correctly should be used here to talk about the manner of applying the products.\n"+
                "The adverb correctly modifies the verb apply.Neither (A) correction, which is a noun, nor (B) corrected, a past participle, nor (D) correcting , a verb gerund, can be used to modify apply.", "16");
        objIncomplete.addValuesToIncompleteChoice("17", "since", "under", "between", "during",
                "A.The conjunction since should be used to introduce the clause containing the verb was released. The verb in the main clause, in this case has not published, is often in the present perfect tense.\n"+
                "B.Under is used when a book is published using a different name from that of the author e.g. This book was published under the name of Gerald Green. That is not the case here.\n"+
                "C.Between can be used as a time preposition, but two points in time have to be mentioned.\n"+
                "D.During is a time preposition and so it cannot be used to introduce the clause containing the verb was released.", "17");
        objIncomplete.addValuesToIncompleteChoice("18", "discretionary", "discrete", "discretely", "discretion",
                "A.Discretionary is an adjective, not a noun.\n"+
                "B.Discrete is also an adjective, not a noun.\n"+
                "C.Discretely is an adverb, not a noun.\n"+
                "D.A noun is needed after her as the grammatical object  of the verb will use. Using your  discretion means using your own judgment to decide what to do in a particular situation.", "18");
        objIncomplete.addValuesToIncompleteChoice("19", "prevail", "transform", "inspire", "involve",
                "A.To prevail means that something commonly exists.It does not need an object and is not used with into.\n"+
                "B.A verb is required to describe the process of changing ideas into well-executed projects. To transform means to change.It has an object, followed by into.\n"+
                "C.To inspire means to give somebody an idea. It is not used with the preposition into.\n"+
                "D.Involve has the preposition in after its object, not into e.g. Many researchers were involved in the development of the new product.", "19");
        objIncomplete.addValuesToIncompleteChoice("20", "Delighted", "Delightedly", "Delightful", "Delight",
                "A.The past participle delighted should be used to complete the clause at the beginning of the sentence and give the meaning Because the band was delighted by ... .\n"+
                "B.The adverb delightedly cannot be used with by to describe the effect the audience's reaction had on the band.\n"+
                "C.The adjective delightful can be used to describe music, but cannot be used with by and a noun.\n"+
                "D.Delight  is a noun. A noun is incorrect here.", "20");
        objIncomplete.addValuesToIncompleteChoice("21", "Until", "Mainly", "Whether", "Only",
                "A.The preposition until is incorrect because it would mean that you should only use a real estate agent up to the point of buying or selling a house.A real estate agent is in fact used throughout the transaction.\n"+
                "B.Mainly is an adverb,so it is incorrect here.\n"+
                "C.Whether should be used with or as a double conjunction to mean that the advice to use a real estate agent is true in both situations, buying and selling a house.\n"+
                "D.Only can be an adverb or adjective, so it is not the correct part of speech to use here.", "21");
        objIncomplete.addValuesToIncompleteChoice("22", "once", "never", "not", "ever",
                "A.Once cannot be used alone to complete the comparison here.The phrase would have to be easier than it once was.\n"+
                "B.The negative never meaning not ever is not correct here.\n"+
                "C.Not cannot be used to complete the comparison here.\n"+
                "D.The adverb ever should be used, meaning at any other time. The phrase easier than ever is used to compare the new application process with previous processes.", "22");
        objIncomplete.addValuesToIncompleteChoice("23", "nearly", "closely", "precisely", "relatively",
                "D.Certain adverbs can be used to modify the adjective small. The adverb relatively, which means fairly is suitable here.\n"+
                "The adverbs (A) nearly, (B) closely, and (C) precisely cannot be used to modify small, so none of these choices is correct.", "23");
        objIncomplete.addValuesToIncompleteChoice("24", "chief", "prior", "significant", "official",
                "A.Chief means main or most important. It is not used to describe growth.\n"+
                "B.Prior is an adjective meaning previous . This meaning is not appropriate here.\n"+
                "C.The adjective significant can be used to describe the growth of a company. It means quite large.\n"+
                "D.We say the figures relating to a company's growth are official if they are verified by an accountant, but growth cannot be official.", "24");
        objIncomplete.addValuesToIncompleteChoice("25", "inquiry", "feedback", "intention", "hesitation",
                "A.An inquiry cannot be described as positive.\n"+
                "B.The sentence is probably about a publication such as a magazine or newspaper.When readers give their opinion of a publication,it is called feedback, and we talk about positive and negative feedback.\n"+
                "C.An intention is something a person plans to do.The sentence does not refer to a plan.\n"+
                "D.Hesitation would not be used with the preposition from  and would not be described as  positive.", "25");
        objIncomplete.addValuesToIncompleteChoice("26", "one another", "another", "the other", "other",
                "A.The words one another are needed to make the phrase, fairly close to one another, which means that each house is quite close to the next one.(B) another and (C) the other cannot be used to refer to several houses,so they are incorrect.\n"+
                "D.The word other is grammatically incorrect here because it would need to be followed by houses or it would need to be in the plural,which is others.", "26");
        objIncomplete.addValuesToIncompleteChoice("27", "recognize", "recognizing", "recognizable", "recognizably",
                "D.The adverb recognizably can be used to modify the adjective different.\n"+
                "The three other choices are incorrect because (A) is a verb,(8) is a verb gerund, and (C) is an adjective and none of these can be used to modify an adjective.", "27");
        objIncomplete.addValuesToIncompleteChoice("28", "as", "but", "for", "out",
                "A.The preposition as is sometimes used with recommend, but then it is used to indicate the role in which something might be useful e.g. \"I recommend this hotel as a conference venue since it has all the necessary facilities\".\n"+
                "B. and (D) are incorrect because a preposition is needed with recommend.\n"+
                "C.For is the dependent preposition used with recommend to indicate the purpose or situation for which something is considered useful.", "28");
        objIncomplete.addValuesToIncompleteChoice("29", "recently", "promptly", "formerly", "briefly",
                "A.The adverb recently is used for something that happened a short time ago in the past,so it is not correct here.\n"+
                "B.The adverb promptly should be used to say that the reports should be del vered punctually.\n"+
                "C.Formerly also refers to the past because it means before now.\n"+
                "D.Briefly cannot describe the manner of delivering something.", "29");
        objIncomplete.addValuesToIncompleteChoice("30", "dependently", "dependence", "dependent", "depend",
                "B.A nounis needed after their as the grammatical object of the verb reduce. Dependenceis a noun.\n"+
                "(A) The adverb dependently, (C) the adjective dependent , and (D) the verb depend are incorrect after their.", "30");
        objIncomplete.addValuesToIncompleteChoice("31", "Into", "Among", "Despite", "Around",
                "A.Into is not used to mean one of several.\n"+
                "B.The preposition among can be used with the plural noun achievements to mean one of the firm's achievements.\n"+
                "C.Despite is incorrect here because it expresses contrast, which is not intended here, and it does not fit with the structure of the sentence.\n"+
                "D.Around does not mean one of several.", "31");
        objIncomplete.addValuesToIncompleteChoice("32", "mobile", "talkative", "dedicated", "foolish",
                "D.An adjective which can describe a notion, or idea, should be used here. Foolish, which means unwise, is a suitable adjective.\n"+
                "A notion is not generally described as (A) mobile, (8) talkative, or (C) dedicated.", "32");
        objIncomplete.addValuesToIncompleteChoice("33", "conducted", "conductor", "conducting", "conducts",
                "A.Conducted, the past participle of the verb conduct, should be used with by the Fielding Institute to describe the survey.\n"+
                "B.The noun conductor cannot be used here.\n"+
                "C.The present participle conducting is incorrect because it has an active meaning. A past participle is needed here because it has a passive meaning.\n"+
                "D.The simple present tense conducts is incorrect. A past participle is needed.", "33");
        objIncomplete.addValuesToIncompleteChoice("34", "comprehend", "comprehensible", "comprehensibly", "comprehensibility",
                "C.The adverb comprehensibly should be used to modify the verb to express, saying how the ideas should be expressed.\n"+
                "An adverb is needed here, not (A) the verb comprehend, (B) the adjective comprehensible, or (D) the noun comprehensibility.", "34");
        objIncomplete.addValuesToIncompleteChoice("35", "when", "as if", "so that", "than",
                "A.The conjunction when should be used before authorized by the client to describe the conditions that must exist for the higher rate of pay to be received.\n"+
                "B.As if cannot be used to introduce a conditional clause.\n"+
                "C.So that is used to introduce a clause describing purpose,not a conditional clause.\n"+
                "D.Than is used in a comparison. No comparison is made here.", "35");
        objIncomplete.addValuesToIncompleteChoice("36", "frustrating", "frustratingly", "frustrated", "frustration",
                "A.The adjective frustrating is needed to describe the noun drawback. Frustrating means causing frustration.\n"+
                "B.Frustratingly is an adverb, not an adjective.\n"+
                "C.Frustrated has a passive meaning and would be used to describe the way the reader feels, not a drawback of the book.\n"+
                "D.Frustration is a noun, not an adjective.", "36");
        objIncomplete.addValuesToIncompleteChoice("37", "assigning", "calculating", "creating", "describing",
                "A.Assigning would not be used to describe the work the brothers have been doing in their workshop.\n"+
                "B.The work of tailors is not to calculate shirts,so calculating is incorrect.\n"+
                "C.The Sagamore brothers are evidently tailor.The word creating fits best with the object custom-made shirts, forming the present perfect continuous tense of the verb.\n"+
                "D.Describing is not the main work carried out by tailors in a workshop.", "37");
        objIncomplete.addValuesToIncompleteChoice("38", "uncertainty", "feasibility", "quantity", "flexibility",
                "A.We do not talk about analyzing the uncertainty of a project.\n"+
                "B.Before a projectis carried out it is necessary to investigate, or analyze, the feasibility of it; that is, determine whether the project is possible and  achievable.\n"+
                "C.The noun quantity cannot be used before of removing dams.\n"+
                "D.The noun flexibility does not fit with of removing dams.", "38");
        objIncomplete.addValuesToIncompleteChoice("39", "well", "quite", "many", "some",
                "A.The adverb well can be used to modify attended. If an event is well attended it means many people attended it.\n"+
                "B.Quite is not used with attended, though we do say quite well attended.\n"+
                "C.The adverb well is needed, not the adjective many.\n"+
                "D.The determiner some cannot be used with attended.", "39");
        objIncomplete.addValuesToIncompleteChoice("40", "even", "else", "urgently", "already",
                "D.The adverb already should be used here to describe the adjective inflated. Already indicates that the market became inflated even before customers moved to the Web-based marketplace.\n"+
                "(A) Even, (B) else, and (C) urgently cannot be used to describe inflated market.", "40");
        objIncomplete.addValuesToIncompleteChoice("41", "release", "experiences", "reservations", "diagram",
                "A.Travel release is not an acceptable noun combination, so (A) is incorrect.\n"+
                "B.A travel agency makes travel reservations, not travel experiences.\n"+
                "C.A travel agency makes travel reservations for its customers and sends out tickets, so (C) is correct.\n"+
                "D.We do not talk about a travel diagram.", "41");
        objIncomplete.addValuesToIncompleteChoice("42", "signify", "significance", "significant", "significantly",
                "C.An adjective is needed to describe the noun contributions. Significant, meaning important, is an adjective.\n"+
                "(A) Signify is a verb,(B) significance is a noun, and (D) significantly is an adverb, so none of these choices is correct.", "42");
        objIncomplete.addValuesToIncompleteChoice("43", "he", "him", "himself", "his",
                "A.The subject pronoun he is needed to refer to Mr.Kim. He is the subject of the verb could leave.\n"+
                "B.The masculine pronoun him cannot be used because it is an object pronoun.\n"+
                "C.Himself is a reflexive pronoun, which is not required here.\n"+
                "D.The possessive pronoun his is incorrect.", "43");
        objIncomplete.addValuesToIncompleteChoice("44", "to require", "requiring", "require", "requires",
                "A.To require is the infinitive, not a verb tense.\n"+
                "B.Requiring is the gerund, not a verb tense.\n"+
                "C.Require, the simple present tense of the verb, is needed in this conditional clause.\n"+
                "D.Requires is simple present,but it is third person singular,so cannot be used with the subject you.", "44");
        objIncomplete.addValuesToIncompleteChoice("45", "warm", "warmth", "warmly", "warmed",
                "A.The adjective warm is used to describe the noun welcome. A warm welcome is a friendly welcome.\n"+
                "B.Warmth is a noun, so it is incorrect.\n"+
                "C.Warmly is an adverb.\n"+
                "D.Warmed can be an adjective, but it cannot describe welcome.", "45");
        objIncomplete.addValuesToIncompleteChoice("46", "or", "but", "and", "nor",
                "A.The conjunction or is correct because either ... or is a double conjunction used to mention two alternative actions.\n"+
                "B.The combination either ... but is not grammatical.\n"+
                "C.The combination either ... and is not grammatical.\n"+
                "D.Nor is used with neither, not either.", "46");
        objIncomplete.addValuesToIncompleteChoice("47", "occupation", "positions", "performance", "talents",
                "A.People are interviewed for positions , not for occupation.\n"+
                "B.A noun meaningjobs is needed.Positions are jobs.\n"+
                "C.Performance does not meanjobs , but when somebody has a job we can talk about their performance  in their job.\n"+
                "D.Talents are a person's special abilities. They are not jobs.", "47");
        objIncomplete.addValuesToIncompleteChoice("48", "stay", "to stay", "staying", "stayed",
                "A.Stay is the base form of the verb, not the gerund.\n"+
                "B.To stay is the infinitive with to, so it is incorrect.\n"+
                "C.The gerund of the verb (verb + -ing)is needed after a preposition. On is a preposition, so (C) is correct.\n"+
                "D.The simple past tense stayed is incorrect.", "48");
        objIncomplete.addValuesToIncompleteChoice("49", "announce", "express", "declare", "notify",
                "A.To have a similar meaning to tell, the form of the verb required here is to announce something to somebody.\n"+
                "B.Express cannot be used with your supervisor as its object.\n"+
                "C.Declare cannot be used here because it cannot have supervisor as its direct object. We declare something to somebody.\n"+
                "D.A verb meaning tell or inform should be used.Notify has this meaning and is used with a person as its object.", "49");
        objIncomplete.addValuesToIncompleteChoice("50", "for", "of", "among", "from",
                "A.The preposition for  is used after designed to introduce the purpose or person that a design is aimed at.\n"+
                "The prepositions (B) of, (C) among, and (D) from are not used in this way after designed.", "50");




    }

    private void addValuesTextCompletionScript(){

        objTextCompletionScript.AddValuesToTextCompletionScript("1","To: Susan Olivieri \n" +
                "From: Ray Chen,Accounts Manager \n" +
                "Subject: Speedy Cars \n" +
                "Date: July 11");

        objTextCompletionScript.AddValuesToTextCompletionScript("2","DOCTOR NAMED TO \"NOTAVLE YOUNG PROFESSIONALS\" LIST");

        objTextCompletionScript.AddValuesToTextCompletionScript("3","To: Residents of Prairie Green Apartments \n" +
                "From: Joan Sakamoto, property manager \n" +
                "Date: April 8 \n" +
                "Subject: Painting of buildings");
        objTextCompletionScript.AddValuesToTextCompletionScript("4","Ms. Una Vali \n" +
                "Director of Community Relations \n" +
                "Technology Systems, Inc. \n" +
                "Littleton, NY 11708");

        objTextCompletionScript.AddValuesToTextCompletionScript("5","To: All employees \n" +
                "From: Parking Services \n" +
                "Date: July 10 \n" +
                "Re: Parking garage cleaning");

        objTextCompletionScript.AddValuesToTextCompletionScript("6","Deer Sky Breeze Representative");

        objTextCompletionScript.AddValuesToTextCompletionScript("7","To: Verbotec Employees <staff@verbotecinc.com> \n" +
                "From: Stacy Lim <slim@verbotecinc.com> \n" +
                "Date: September 20 \n" +
                "Re: Position available");

    }

    private void addValuesTextCompletionQuestion(){

        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("1","Enclosed please find your quarterly invoice for taxi services from April through June. Please be reminded that our rates for all trips ......... .The change in prices,which took effect on May 1,is reflected on the enclosed invoice.","D", "are to increase", "would have increased", "will increase", "have increased",
                "A. Are to increase is used for something that is planned for the future. \n" +
                        "B. Would have increased is used in a past conditional sentence. This is not a conditional sentence. It is clear that is not a conditional sentence. It is clear that the increase has happened. \n" +
                        "C. Will increase is a future tense, so is incorrect here. \n" +
                        "D. The memo,dated July 11, states that the change in prices took effect, or started,on May 1.The present perfect tense should be used for increase because it links the past,when the increase was introduced,with the present,when it is still effective.","1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("2","All checks must be made payable to Speedy Cars, Inc. Payment must be received by 5 P.M. on the indicated due date. Please mail your payment at least seven business days before the due date to ensure that it arrives on time.For all billing inquiries please call 1-800-555-5807.There is no ........... for calling this number.","A", "complaint", "record", "charge", "value",
                "A. This sentence is about an action by the company,not by a customer. A complaint is usually made by a customer,so complaint is incorrect. \n" +
                        "B. Record does not refer to a payment and is followed by of. \n" +
                        "C. Companies often have special phone numbers which allow customers to call the company without paying for the call.The noun charge,meaning payment, is required after no. \n" +
                        "D. Value cannot be used to mean the payment made for a call.","1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("3","Thank you for trusting Speedy Cars with your business.We strive to provide quick,....... service that takes you wherever you need to go.","B", "courtesy", "courteous", "courteously", "courteousness",
                "A. Courtesy is a noun,so it is not correct here. \n" +
                        "B. An adjective should be used to describe the noun service. Here two adjectives, quick and courteous, meaning polite are used. \n" +
                        "C. The adverb courteously is not correct here. \n" +
                        "D. The noun courteousness is not correct here.","1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("4","Veronica Lew,M.D., of First Community Medical Center, was recently featured in a list of \"Fifty Notable Young Professionals\" in City News Magazine.She and the 49 other listed..........from a list of 500 candidates.","C", "will choose", "are choosing", "were chosen", "been chosen",
                "A. The future active will choose is incorrect because the sentence is not about something Dr.Lew and the others on the list will do themselves. \n" +
                        "B. Are choosing refers to something happening now and is in the active voice, so it is incorrect. \n" +
                        "C. Like the other verbs in the first paragraph,choose should be in the passive voice,to describe something that happened to Dr.Lew. Were chosen is simple past passive. \n" +
                        "D. Been chosen is incorrect because it is not simple past tense.","2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("5","The 500 candidates had been nominated for.........contributions to the fields of business, science and medicine, scholarship,sports, and the arts.","B", "themselves", "their", "theirs", "them",
                "A. The reflexive pronoun themselves is incorrect because it does not show possession. \n" +
                        "B. The plural possessive pronoun their is needed here to refer to the contributions of the candidates. \n" +
                        "C. The pronoun theirs is not used directly before a noun. \n" +
                        "D. The pronoun them does not show possession and is used in place of a noun, not before a noun.","2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("6","Dr. lew, a Professor of Internal Medicine, is the third physician form First Community Medical Center to be given this......... .She is a frequent speaker at medical conferences around the world. Her textbook, Practicing Internal Medicine, has just been published by Medical Publications, Inc.","A", "honor", "amount", "salary", "pride",
                "A. The noun honor,meaning special recognition,should be used here because being included in the list described is a way of recognizing a person's special achievements. \n" +
                        "B. Amount would refer to money. There is no mention of money here. \n" +
                        "C. Salary also refers to money,so it is incorrect. \n" +
                        "D. Dr.Lew may feel pride at receiving this honor,but we do not say she is given pride.","2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("7","On April 16 our building services contractors will begin repainting Prairie Green's apartment buildings. Most of their work will take place Monday through Friday between the hours of 10:00 A.M. and 4:00 P.M. Please remove all objects from your windows and balconies......... April 16 and avoid touching the building's outside walls while the painting is being done.","B", "after", "before", "until", "since",
                "A. Objects should be removed from windows and balconies to avoid interfering with the repainting.It would not make sense to remove them after the painting has been done. \n" +
                        "B. The memo says the repainting of the apartment buildings will begin on April 16. The preposition before should be used here to make it clear when objects should be removed. \n" +
                        "C. Until does not fit logically with remove all objects. \n" +
                        "D. Since indicates the point in the past when an action began. The memo is not about the past.","3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("8","You should.........be cautious when leaving and entering your apartment during this time as the painters'ladders might block access to doorways and steps.","A", "also", "once", "nearly", "soon",
                "A. Also should be used here to introduce a second point in the memo. \n" +
                        "B. Once cannot be used before be cautious and a person would not be advised to be cautious once. \n " +
                        "C. The adverb nearly is not with be cautious. \n" +
                        "D. Soon would be unnecessary because during this time makes it clear when people should be cautious.","3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("9","We apologize for this temporary inconvenience, but we trust that you will be......... with the result!","A", "pleased", "pleasing", "pleasant", "pleasantly",
                "A. The adjective pleased should be used to modify you,meaning the residents. It describes the way the manager hopes the residents will feel. \n" +
                        "B. Pleasing would be used to describe the result,not the feelings of the residents \n" +
                        "C. Pleasant would not be used to describe the feelings of the residents. \n" +
                        "D. The adverb pleasantly cannot be used to modify the pronoun you.","3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("10","The York State Department of Commerce is pleased to inform you that your organization's grant application has been accepted. Technology Systems, Inc., has been awarded $2 million to establish three technology instruction centers to provide computer facilities and classes to communities in the state.The centers will.........more than 20,000 residents.","D", "conduct", "determine", "house", "serve",
                "A. The centers will conduct classes,but conduct cannot be used with the object residents. \n" +
                        "B. Determine means to decide something. It cannot be used with residents here. \n" +
                        "C. The centers will not house, which means provide accommodation for, residents. \n" +
                        "D. The verb serve should be used here to say that the technology instruction centers will be useful to residents.","4");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("11","As agreed, each center will have its own full-time.........,receiving a salary still to be determined.","C", "direct", "directed", "director", "direction",
                "A. Direct is a verb or adjective. A noun is needed here. \n" +
                        "B. Directed is the past participle of the verb. \n" +
                        "C. A noun referring to a person is needed with the adjective full-time. A director is the person in charge of an educational institution. \n" +
                        "D. The word direction is a noun, but it does not refer to a person.","4");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("12","Congratulation on your..........application.","A", "successful", "pending", "conditional", "revised",
                "A. Ms.Vali applied for a grant and the letter announces that her organization has been awarded the grant, so her application has been successful. \n" +
                        "B. Pending is incorrect because it means the application has not yet been considered \n" +
                        "C. A conditional application would be one that has not actually been made. \n" +
                        "D. The letter does not say the application had been submitted once and then revised, so (D) is not correct.","4");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("13","We would like to advise employees of arrangements that have been made to clean the company parking garage. All indoor parking areas will be closed during the cleaning. .......... will include permit parking spaces, visitor parking spaces, and service vehicle spaces.","B", "Any", "This", "She", "What",
                "A. Any is incorrect because it generally has a noun after it and the meaning does not fit with all. \n" +
                        "B. The pronoun this is needed to refer back to the information in the previous sentence that all indoor parking areas will be closed. \n" +
                        "C. She refers to a person,but no one person is mentioned here. \n" +
                        "D. What does not refer to something that comes before it.","5");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("14","Vehicle.........to the parking garage will not be allowed after 6:00 P.M. on Friday,but departures will be possible until the garage is empty.","B", "permission", "access", "opening", "inclusion",
                "A. is not acceptable because permission needs to and a verb after it, as in permission to enter. \n" +
                        "B. This sentence tells employees when vehicles will not be allowed to enter the parking garage. Access is correct because it means entry. \n" +
                        "C. Opening are not correct because the noun used here should describe something a vehicle does. \n" +
                        "D. inclusion are not correct because the noun used here should describe something a vehicle does.","5");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("15","During the cleaning,permit holders should use the outdoor parking area located on Sussex Drive West. ........ parking will also be available in Lot B and Lot C. If you have any questions with regard to this notice,please contact Mike Mallone at extension 3888.Thank you.","C", "Consequent", "Replaceable", "Alternative", "Capable",
                "A. Consequent is not used to describe parking. \n" +
                        "B. The parking in Lots B and C replaces the usual parking,so it would not be describe as replaceable,which means can be replaced. \n" +
                        "C. This paragraph is about where parking other than in the garage can be found,so alternative is the best choice. \n" +
                        "D. Capable is used to describe people, not parking","5");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("16","I am an avid biker and a longtime user of Sky Breeze cycling helmets.Two weeks ago,I noticed that my old Sky Breeze helmet........to show signs of wear.In the interest of safety,I immediately went to your company Web site and purchased a new helmet.","B", "starts", "was starting", "will start", "have started",
                "A. Starts is a present tense,so it is incorrect. \n" +
                        "B. A verb tense is needed for an action that was in progress around two weeks ago,so the past continuous was starting is correct. \n" +
                        "C. the future will start cannot be used. \n" +
                        "D. Present perfect have started is not correct with two weeks ago. It also requires a plural subject, and helmet is singular.","6");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("17","The day after I received the new helmet in the mail,I saw a tiny crack on the helmet's exterior.I am sure that this damage occurred prior to purchase.........I have not dropped,thrown, or hit the helmet in any way. According to your warranty policy,Sky Breeze will replace a defective helmet free of charge.","A", "as", "although", "so", "but",
                "A. The conjunction as can be used to introduce the reason why the writer is sure the damage happened prior to purchase, which means before she received the helmet. \n" +
                        "B. Although introduces a contrasting idea,not a reason. \n" +
                        "C. So introduces the result of an action described earlier, not the reason. \n" +
                        "D. But shows contrast, so it is not correct.", "6");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("18","I have enclosed the cracked helmet and the payment receipt.Please send the.........to the address written on the back of the receipt.","A", "replacement", "contract", "prize", "guarantee",
                "A. In the previous sentence, the writer mentions the policy to replace a defective helmet,so here she is asking for a replacement. \n" +
                        "B. She is not asking for a contract. \n" +
                        "C. She is not writing about a prize. \n" +
                        "D. She has already looked at the guarantee, so she is not asking for one to be sent to her.","6");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("19","The.........job opening will be posted externally on October 1. Employees interested in applying","C", "destined", "indicating", "following", "extended",
                "A. Destined does not mean mentioned later. \n" +
                        "B. Indicating cannot describe job opening. \n" +
                        "C. This sentence introduces the topic of the e-mail,which is a job opening.The adjective following can be used with job opening and means the one described later in the e-mail. \n" +
                        "D. A deadline can be extended,but not a job opening.","7");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("20","The Human Resources assistant helps with the........ ,selection,and orientation of new employees.","A", "recruitment", "recruiter", "recruits", "recruit",
                "A. The noun recruitment,which means finding new staff,refers to a task that might be done by somebody working in human resources. \n" +
                        "B. The noun recruiter refer to a person,not a task. \n" +
                        "C. As a noun,recruits means new employees,not a task,so it is incorrect. It could be a verb,which is incorrect here. \n" +
                        "D. Similarly, recruit cloud be a noun or a verb and is incorrect in either case.","7");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestion("21", "In addition,the assistant is responsible for ensuring that employee databases are ............. maintained.", "C", "accurate", "accuracy", "accurately", "more accurate",
                "A.The adjective accurate cannot modify maintained.\n"+
                "B.Accuracy is a noun, so it is incorrect.\n"+
                "C.The adverb accurately should be used to describe how the databases should be maintained.\n"+
                "D.The comparative adjective more accurate is not correct here.", "7");

    }

    private void addValuesReadingComprehensionScript(){

        objReadingPractice.addValuesToReadingScript("1",
                "Theater Digest \n" +
                        "125 LAKE AVE. \n" +
                        "CHICAGO, IL 60616 \n" +
                        "Dear Subscriber, \n" +
                        "Your subscription to theater Digest will end in two months.Please don't let that happen. Take time to renew your subscription today.By doing so,you will continue to receive every month the very latest theater reviews,information on actors and directors,and up-to-date reports on new dramas and musicals for the next year. \n" +
                        "I've attached an invoice for your renewal order.You will receive 12 issues for the special low price of $35. Please send your payment in the reply envelope provided. Make any corrections to your name or address right on the back of the invoice.Then, visit our Web site at www.theaterdigest.com to read about contests for readers.You could win tickets to a great show! \n" +
                        "Matthew Chambers \n" +
                        "Matthew Chambers \n" +
                        "Customer Service Representative");
        objReadingPractice.addValuesToReadingScript("2",
                "TRAVEL TO NACU CONFERENCE \n" +
                        "Airline Arrangements \n" +
                        "Sky High Air and Mountain High Airlines will serve as the official carriers for attendees of the Forty-Fourth Annual NACU Conference. Both carriers have agreed to offer low fares for conference attendees.To obtain information on discount airfares, call Sky High Air at (800) 555-0987 and refer to Convention Number CV786309 or call the Mountain High Airlines Reservation Desk at (800) 555-7382 and refer to Convention Number HJ987. \n" +
                        "Ground Transportation \n" +
                        "The trip from Rushmore Airport to downtown hotel is about 15 miles and takes 45 minutes by shuttle bus or car. \n" +
                        "By Shuttle Bus: Airporter (708) 555-9541 offers a shuttle bus service from the airport to the Fairmont Hotel and the Regency Hotel.Departure time is every 20 minutes from 9:00 A.M. to 8:00 P.M. and every 30 minutes from 8:00 P.M. to 11:30 P.M. The Airporter main desk is located on the lower level, near Exit B. No reservations are required,but tickets must purchased at the Airporter main desk,at the conference registration desk in the convention center, or at the travel agency located in the Regency Hotel.Tickets are not available directly form the shuttle bus drivers. \n" +
                        "Shuttle Bus One-Way Fares, Adult $9.00 , Child $4.00 , Family $17.00 \n" +
                        "Shuttle Bus Round-Trip Fares , Adult $14.00 , Child $6.00 , Family $26.00 \n" +
                        "By Taxi: Taxis are readily available outside Exit C in the main terminal Appropriate fare to downtown hotel is $18.00-$25.00. \n" +
                        "Parking \n" +
                        "For attendees driving to the conference,parking is available at both hotels. The Fairmont Hotel provides parking for $15.00 per day,with in/out privileges. The Regency Hotel offers parking for $10.00 per day Monday to Friday, $8.00 on Saturday, and $6.00 on Sunday.");
        objReadingPractice.addValuesToReadingScript("3",
                "Toppo Travel, Inc. \n" +
                        "Mr. Boyce Adams \n" +
                        "424 Lenox St. \n" +
                        "Orange,Ma 01388 \n" +
                        "Dear Mr.Adams, \n" +
                        "At the end of this year, Toppo Travel will celebrate its twentieth year as a successful operator in the leisure industry. With modern hotels,exciting itineraries,and beautiful locations, our all-inclusive,organized tours have remained the most popular in the industry for the past ten years. We have decided to include our most loyal customers in the celebration of our success. \n" +
                        "Our records indicate that since you became a customer five years ago,you have booked six trip with us.We would therefore like to invite you ta an evening of exotic fare and tropical sounds,which will be held on December 1 in the Grand Ballroom of the Panorama Hotel. \n" +
                        "The buffet will consist of delicacies from the 16 countries that are featured in our catalog for the new season.Bands from Cuba,Mali,and Slovenia will lend an exuberant atmosphere to the event. \n " +
                        "Enclosed please find two complimentary tickets for entry to this exclusive event. \n" +
                        "Sincerely, \n" +
                        "Jim Bull \n" +
                        "Jim Bull \n" +
                        "Director Customer Relations \n" +
                        "Toppo Travel, Inc.");
        objReadingPractice.addValuesToReadingScript("4",
                "NOTICE TO ALDER PARK RESIDENTS: \n" +
                        "New City Recycling Program \n" +
                        "on August 1,a new law will take effect in Alder Park that will require residents to recycle products made of paper,glass, and aluminum.Those recyclables should be placed in green plastic bins provided by the city,which will be delivered during the week of July 15. The city will pick up those recyclables during the first and third weeks of each month on garbage pick-up days. \n" +
                        "Some examples of acceptable recyclables include: \n" +
                        "Type       Examples                            Notes \n" +
                        "Paper      Newspapers,printer and copy paper,  Staples are permitted \n" +
                        "           magazines,envelopes,cardboard       No paper clips or plastic sheets. \n" +
                        "Glass      Bottles,jars                        Must be clean. \n" +
                        "                                               Labels are permitted. \n" +
                        "                                               Non-glass caps must be removed. \n" +
                        "                                               No broken glass. \n" +
                        "Aluminium cans,foils                           Must be clean. \n" +
                        "                                               Crush if possible \n" +
                        "Please follow the guidelines provided above.Garbage pick-up will continue on a weekly basis,according to the regular schedule. \n" +
                        "If you have any question,please contact the city refuse program at 555-1067,extension 27.");
        objReadingPractice.addValuesToReadingScript("5",
                "Lasell, Inc. \n" +
                        "676 Keenan Dr. \n" +
                        "Fort Worth,Texas 76035 \n" +
                        "Dear Customer, \n" +
                        "As you may be aware,Lasell has routinely received commendations for our commitment to product safety.Every item that is sold under the Lasell name is subjected to rigorous product testing. When design flaws are detected,the model is revised to eliminate the problem.In addition,our products are built from the strongest plastics to ensure that you never have a problem with a Lasell product. \n" +
                        "In the unlikely event that products are released with previously undetected flaws,great steps are taken to ensure that every flawed item is removed from the market and replaced for customers. \n" +
                        "Late last week,our exceptional quality assurance team identified a previously undetected minor flaw in the air filters in our 6000x model.Because our records show that you have recently purchased a 6000x vacuum cleaner,we want to inform you of this flaw and of your right to return the product for a new one.Please be assured that there is absolutely no safety risk to you or any operators of the machine. However,to ensure that you are satisfied with the quality of our products,we would like to ask you to contact the Lasell store nearest to you.The store will arrange to pick up your machine at your home at a time that is convenient for you. \n" +
                        "Our customers' satisfaction is foremost on our mind,and we want to make sure that you are not inconvenienced in any manner.A replacement vacuum cleaner will be delivered at the time of the pickup.Furthermore,customers who share in the exchange will receive a free gift in appreciation of their business. \n" +
                        "Sincerely, \n" +
                        "Joe Glidden \n" +
                        "Joe Glidden \n" +
                        "Director \n" +
                        "Customer Satisfaction Department \n" +
                        "Lassell,Inc.");
        objReadingPractice.addValuesToReadingScript("6",
                "BRAND MANAGER \n" +
                        "COMPANY BACKGROUND: The Juneco Company,expected earnings of approximately $40 million,seeks to increase marketing and product innovation efforts to significantly increase revenues within 1-2 years.Headquartered in upstate New York,Juneco manufactures kitchenware products and home security systems under several nationally recognized brand names.In addition to these major brands,Juneco produces similar products under private label programs for home improvement merchants. \n" +
                        "RESPONSIBILITIES: The brand manager's priority is to handle the heightening of product recognition of Juneco's major accounts abroad. The brand manager position was created to build brand recognition in the marketing sector of Juneco's new International Division.The brand manager will spearhead efforts to foster growth in garden tool products. \n " +
                        "Specific responsibilities include the following: \n" +
                        "generate marketing plans and lead the development to new products; \n" +
                        "increase public awareness of and demand for Juneco's product; \n" +
                        "identify and evaluate market requirements and opportunities; \n" +
                        "work closely with other members of the marketing department,as well as sales and sales operations departments to ensure achievement of company goals \n" +
                        "PROFESSIONAL EXPERIENCE AND PERSONAL QUALITIES: the ideal candidate will have the following qualifications: \n" +
                        "3-5 years' experience as brand manager in retail marketing; \n" +
                        "strong experience in analyzing current markets; \n" +
                        "outstanding verbal and written communication skills. \n" +
                        "EDUCATIONAL BACKGROUND: An MA degree in Marketing is required; an MBA is a significant plus. Upon receipt of applications,confirmation letters will be sent to applicants via e-mail. Thereafter,priority applicants will be invited to meet with Juneco's CEO and Chief Marketing Officer." );
        objReadingPractice.addValuesToReadingScript("7",
                "BLIXEN MEMORIAL THEATER \n" +
                        "Annual Fundraising Event \n" +
                        "January 19 \n" +
                        "Dear Friend of Blixen Memorial Theater: \n" +
                        "As a non-profit performing arts organization,we rely on membership and fundraising efforts to support our operating costs.Every year at this time we hold a drawing to help raise funds to meet a portion of our budget. \n" +
                        "This is your chance to help Blixen Memorial Theater.Each individual who donates $20 to the theater at this time will be eligible for a special prize.On may 20,we will select one name at random to receive this year's prize -4 tickets to each of the 10 Blixen Memorial Theater performances for the upcoming year.(The winner need not be present.) That's 40 tickets,a $3,000 value! \n" +
                        "To participate in this year's drawing,simply complete the entry form and mail it your payment. We will send you a confirmation number for each $20 donation upon receipt.Every $20 you donate increases your chances of winning a whole year's worth of exciting performances.And even if your name is not selected,you'll still win by helping Blixen Memorial Theater offer high-quality programs. \n" +
                        "For further information,call the office at(507) 555-8826, ext. 908. \n" +
                        "Sincerely, \n" +
                        "Anna Kessler \n" +
                        "Anna Kessler \n" +
                        "Executive Director \n" +
                        "Name Anton Maldonado \n" +
                        "I have enclosed a check for $........ Address 14 Sunset Drive \n" +
                        "Please charge my credit card $40 City Stockton  State MN Zip 55988 \n" +
                        "Card# 1122334455667788  Phone 507-555-2292 \n" +
                        "Signature Anton Maldonado \n" +
                        "Please return this form with payment to: \n" +
                        "BLIXEN MEMORIAL THEATER \n" +
                        "Attention: Raffle \n" +
                        "480 Sioux St. \n" +
                        "Winona,MN 55987");
        objReadingPractice.addValuesToReadingScript("8",
                "7 July 20_ \n" +
                        "Fax to:       P.Peterman \n" +
                        "Fax number    0101.202.555.1218 \n" +
                        "Dear Mr.Peterman: \n" +
                        "Thank you to your confirmation fax of today. We take great pleasure in confirming your reservation of one superior double room for the evening of 28 though 30 July.The cost of this room will be 135 a night,inclusive of tax,newspaper,and continental breakfast.The total charge of 405 will be made to the credit card number which you previously provided to us. \n" +
                        "Should you require transportation from the airport when you arrive in our city,we can arrange a special airport shuttle for you. Just call the hotel from one of the white courtesy phones located throughout the arrivals terminal. Press 15 to reach the One Devonshire Gardens front desk. \n" +
                        "We look forward to welcoming you at One Devonshire Gardens. Please don't hesitate to contact me should you have any questions regarding your reservations or our accommodations. \n" +
                        "Your sincerely \n" +
                        "Debbit Smith \n" +
                        "Reservations Manager");
        objReadingPractice.addValuesToReadingScript("9",
                "Seeking: Assistant Controller \n" +
                        "- Large downtown law firm is seeking an Assistant Controller for our Accounting Department. \n" +
                        "- Basic responsibilities include control of the accounting systems,supervision of a seven-person team, assisting with the hiring and training of bew employees. \n" +
                        "- Qualified applicant should have eight years of accounting experience,as well as a minimum of two to three years in a supervisory position.Experience working in a law firm is desirable. \n" +
                        "- Education requirements include an undergraduate degree in accounting. CPA is preferred. \n" +
                        "- The successful candidate will have the necessary computer skills and be familiar with the most current automated financial systems. \n" +
                        "To apply for this position, send a resume and three letters of reference \n" +
                        "to: \n" +
                        "Annabelle Smythe \n" +
                        "Forbes, Lawrence,and Ross \n" +
                        "187 Oakland Boulevard \n" +
                        "Detroit, MI 41084 \n" +
                        "Closing date: November 12");
        objReadingPractice.addValuesToReadingScript("10",
                "Dear Customer, \n" +
                        "Congratulation! You have just purchased one of the world's most sophisticated microwave ovens. This appliance has been designed with your convenience in mind. It combined an array of special features with ease of use. State-of-the-art-features include a temperature sensor so that you will never again have an overcooked or undercooked meal; a 24-hour timer so that you can prepare your food when you have time and have it ready to eat when you are;an automated defrosting system so that you can prepare frozen food with no extra waiting time; a programmable chime system to let you know when your food is ready; and an automatic self-cleaning system so that your oven is always fresh and ready for use. \n" +
                        "All of these features and more are available to you at just the push of a button. It is so simple to use. Each feature is completely explained in this manual.Just follow the step-by-step instructions and you will be cooking delicious meals in no time at all! In addition,recipes for various entrees and desserts are included at the back of the manual to get you started on your new adventures in microwave cooking.This product has been designed to give you many years of trouble-free operation as long as the instructions are followed. If for some reason the product should fail,it is completely guaranteed for one year.A manual.Additional instructions and recipes are available on our website. \n" +
                        "Thank you again for becoming a Kitchen Appliances customer. \n" +
                        "Sincerely, \n" +
                        "M.S. Fujimoto \n" +
                        "President \n" +
                        "Kitchen Appliances, Inc.");

        //question
        objReadingPractice.addValuesToReadingQuestion("1","What is the purpose of this letter?","b", "To advertise a new publication","To encourage subscription renewal","To correct a billing error","To request a donation",
                "A. The recipient already has a subscription to Theater Digest,so it is not a new publication. \n" +
                        "B. The letter states that the recipient's magazine subscription ends soon and mentions the advantages of renewing the subscription,so the purpose is to encourage subscription renewal. \n" +
                        "C. No billing error is mentioned. \n" +
                        "D. The letter is not a request for a donation,or gift of money.", "1");
        objReadingPractice.addValuesToReadingQuestion("2","How often is Theater Digest published?","a", "Once a month","Every two months","Twice a year","Once a year",
                "A. The letter states that by renewing the subscription the subscriber will continue to receive Theater Digest every month,so it is published once a month. \n" +
                        "B. The subscription will end in two months,but the magazine is not published every tow months. \n" +
                        "C. Theater Digest is not published twice a year. \n" +
                        "D. Theater Digest is not published once a year.", "1");
        objReadingPractice.addValuesToReadingQuestion("3","What is mentioned about the Web site?","c", "It provides access to other theater-related Web sites.","It offers additional information on stories printed in Theater Digest.","It contains information about competitions for readers.","It can be used for online payment.",
                "A. The letter does not mention access to other theater-related Web site. \n" +
                        "B. The letter only mentions that the Web site has information about contests for readers. \n" +
                        "C. The letter suggests visiting the Web site to read about contests for readers,which are competitions. \n" +
                        "D. There is no mention of marking online payments via the Web site.", "1");
        objReadingPractice.addValuesToReadingQuestion("4","On whose Web site would this information most likely be found?","b", "Sky High Air","NACU","Regency Hotel","Airporter",
                "A. This information is intended for conference attendees,not the general public,so it is unlikely to be found on the Web site of Sky High Air. \n" +
                        "B. The information is about transportation to the NACU Conference for conference attendees,so it would most likely be found on the Web site of NACU. \n" +
                        "C. This information is intended for conference attendees,not the general public,so it is unlikely to be found on the Web site of Regency Hotel. \n" +
                        "D. This information is intended for conference attendees,not the general public,so it is unlikely to be found on the Web site of Airporter.", "2");
        objReadingPractice.addValuesToReadingQuestion("5","The word \"serve\" in paragraph 1,line 1 is closet in meaning to","b", "wait on","operate","obey","give out",
                "A. In the context of food service in a restaurant,serve means wait on,but this information is not about a restaurant. \n" +
                        "B. Is this context,serve is closest in meaning to operate.Particular airlines often agree to operate as the official carriers for attendees to a large conference. \n" +
                        "C. Serve can mean to work for a person and in that context it can mean obey,but that is not the meaning here. \n" +
                        "D. Give out means distribute.That is not the correct meaning here.", "2");
        objReadingPractice.addValuesToReadingQuestion("6","How long does it take to drive to downtown hotels from the airport?","d", "15 minutes","20 minutes","30 minutes","45 minutes",
                "A. The information does not say it will take 15 minutes. \n" +
                        "B. The information does not say it will take 20 minutes. \n" +
                        "C. The information does not say it will take 30 minutes. \n" +
                        "D. In the section on Ground Transportation the information says this journey will take 45 minutes by car.", "2");
        objReadingPractice.addValuesToReadingQuestion("7","To what event has Mr.Adams been invited?","c", "A hotel's grand opening","A retirement dinner","An anniversary celebration","An awards ceremony",
                "A. The event will be held at a hotel,but it is not the grand opening of a hotel. \n" +
                        "B. The event is not a retirement dinner.There is no mention of anybody retiring. \n" +
                        "C. The letter invites Mr.Adams to special evening to celebrate Toppo Travel's twentieth year,in other words an anniversary celebration. \n" +
                        "D. The event is not an awards ceremony.", "3");
        objReadingPractice.addValuesToReadingQuestion("8","For how many years has Mr.Adams been traveling","a", "Five years","Six years","Ten years","Twenty years",
                "A. According to the letter,Mr Adams became a customer of Toppo five years ago,so he has been travelling with them for five years. \n" +
                        "B. He has not been travelling with the company for six year. \n" +
                        "C. He has not been travelling with the company for ten year. \n" +
                        "D. He has not been travelling with the company for twenty year.", "3");
        objReadingPractice.addValuesToReadingQuestion("9","What will the event feature?","c", "A slide show","Ballroom dancing","Exotic food","A noted speaker",
                "A. There is no mention of a slide show. \n" +
                        "B. The event will be in the Grand Ballroom of the hotel,but ballroom dancing is the mentioned. \n" +
                        "C. The event features exotic fare,which means exotic food and the letter describes the buffet,a meal where people serve themselves from a selection of food. \n" +
                        "D. There is no mention of any speaker.", "3");
        objReadingPractice.addValuesToReadingQuestion("10","What is announced in this notice?","c", "A revised schedule for garbage collection","Free for residential garbage pickup","Rules for recycling household items","The opening of a recycling center",
                "A. The notice says garbage pick-up will continue according to the regular schedule,so a revised schedule for garbage collection in sot announced. \n" +
                        "B. Frees for collecting garbage are not mentioned. \n" +
                        "C. The notice gives guidelines,or rules for recycling products and is for residents,so it is about recycling household items. \n" +
                        "D. The notice is about a new recycling program,not a new recycling center.", "4");
        objReadingPractice.addValuesToReadingQuestion("11","Who will supply green containers?","a", "City employees","Bottling company staff","Recycling center volunteers","Alder Park residents",
                "A. The information states that the green plastic bins,or containers,will be provided by the city and delivered in a particular week,so city employees will supply them. \n" +
                        "B. It is the city who will supply the green containers,not bottling company staff. \n" +
                        "C. It is the city who will supply the green containers,not recycling center volunteers. \n" +
                        "D. It is the city who will supply the green containers,not Alder Park residents.", "4");
        objReadingPractice.addValuesToReadingQuestion("12","How many times per month will paper be collected?","b", "1","2","3","4",
                "A. They will not be picked up one time per month. \n" +
                        "B. Papers for recycling should be put in the green plastic bins.They will be picked up during the first and third weeks of each month,which is two times per month. \n" +
                        "C. They will not be picked up three time per month. \n" +
                        "D. They will not be picked up four time per month.", "4");
        objReadingPractice.addValuesToReadingQuestion("13","What is the purpose of the letter?","d", "To introduce a policy change","To invite customers to an in-store event","To respond to a customer complaint","To announce a replacement plan",
                "D. A flaw has been found in Lasell's 6000X model vacuum cleaner.The purpose of the letter is to inform customers that can return the product and receive a new one,which is a replacement plan. \n" +
                        "A. The first two paragraphs outline the company's policy regarding any flaws in their products.The letter does not introduce a policy change. \n" +
                        "B. No invitation to an in-store event is given. \n" +
                        "C. The letter is not a response to a customer complaint.The problem was found by the company itself. \n" +
                        "D. A flaw has been found in Lasell's 6000X model vacuum cleaner.The purpose of the letter is to inform customers that can return the product and receive a new one,which is a replacement plan.", "5");
        objReadingPractice.addValuesToReadingQuestion("14","What should the reader do?","d", "Report problems immediately","Pick up a gift in the office","Submit a copy of the receipt","Call to schedule a pickup",
                "A. The reader should arrange to have the machine picked up.This can be done even if there not actually a problem with it. \n" +
                        "B. A free gift is offered if a customer exchanges their machine,but there is no mention of picking it up in the office. \n" +
                        "C. The customer is not told to send or submit a copy of the receipt \n" +
                        "D. The customer is asked to contact,probably by phone,their nearest Lasell store and the store will then arrange for the machine to be picked up.Thus,the reader should call to schedule a pick-up.", "5");
        objReadingPractice.addValuesToReadingQuestion("15","What is Lasell's business?","a", "Producing appliances","Safety assessment","Packaging materials","Commercial deliveries",
                "A. The mention of Lasell's product testing program and quality assurance team indicate that it is a manufacturer.The product the letter is about is a vacuum cleaner,which is a household appliance. \n" +
                        "B. Lasell wants to ensure the safety of its product,but safety assessment is not its main area of business. \n" +
                        "C. Lasell does not produce packaging materials. \n" +
                        "D. This is not a delivery company making commercial deliveries.", "5");
        objReadingPractice.addValuesToReadingQuestion("16","In which Juneco division will the successful applicant probably work?","a", "International marketing","Human resources","Production","Accounting",
                "A In the responsibilities section it is stated that the person who gets the job of brand manager will be working in the marketing sector of the company's International Division,so will be working in international marketing. \n" +
                        "B. Human resources deals with hiring new employees.The successful applicant will not work in this division. \n" +
                        "C. The successful applicant will market the products,not work in production. \n" +
                        "D. The successful applicant will not be in the accounting division.", "6");
        objReadingPractice.addValuesToReadingQuestion("17","What is NOT a stated job responsibility?","c", "Creating marketing plans","Evaluating market opportunities","Increasing product recognition domestically","Working with sales representatives",
                "A. A stated responsibility is to generate marking plans.Generate means the same as create. \n" +
                        "B. One responsibility is to evaluate market requirements and opportunities. \n" +
                        "C. Increasing product recognition domestically,or nationally,is NOT stated as a job responsibility.The role is to heighten product recognition abroad. \n" +
                        "D. A stated responsibility is to work with sales operations departments,which involves working with sales representatives.", "6");
        objReadingPractice.addValuesToReadingQuestion("18","The word \"foster\" in paragraph 2 line 4 is closest in meaning to","d", "substitute","measure","cherish","encourage",
                "A. Substitute means exchange one thing for another,which is not the meaning of foster. \n" +
                        "B. The brand manager may measure growth in a product,but foster does not mean measure. \n" +
                        "C. Foster can mean cherish in the context of looking after children,but that is not the meaning here. \n" +
                        "D. In a marketing context to foster growth means to encourage growth.The phrases build brand recognition and increase public awareness of products provide clues to the meaning of foster.", "6");
        objReadingPractice.addValuesToReadingQuestion("19","Why did Ms.Kessler write this letter?","d", "To invite Anton Maldonado to an event","To advertise a new show","To explain a new ticketing policy","To announce a fund-raising event",
                "A. The letter is not an invitation to an event. \n" +
                        "B. The letter does not advertise a new show. \n" +
                        "C. The letter was not written to explain a new ticketing policy. \n" +
                        "D. The letter announces a drawing to raise money for the theater.This is a type of fund-raising event.If a person donates $20 to the theater their name will be entered in the drawing once.On May 20 a name will be selected,or drawn,and this is the name of the prize winner.", "7");
        objReadingPractice.addValuesToReadingQuestion("20","What does Ms.Kessler say about the Blixen Memorial Theater?,","c", "It has had to reduce its budget.","Its next season begins on May 20.","It holds an annual drawing.","It is offering discount tickets to people who donate money.",
                "A. Ms.Kessler does not say the theater has had to reduce its budget. \n" +
                        "B. The drawing takes place on May 20.This is not the date of the beginning of the new season. \n" +
                        "C. Ms.Kessler is writing on behalf of the Blixen Memorial Theater and she writes we hold a drawing every year,so the theater holds an annual drawing. \n" +
                        "D. She does not say the theater is offering discount tickets to people who give money.", "7");
        objReadingPractice.addValuesToReadingQuestion("21","What prize is being offered?","b", "A check for $3,000","A year's worth of theater tickets","Front-row seats to four performances","Meetings with performers after the shows",
                "A. This is not a form for purchasing tickets because at the bottom the form says attention:Raffle.Raffle is another word for drawing. \n" +
                        "B. The form Anton Maldonado has completed is the entry form for the drawing mentioned in the letter.He asks for his credit card to be charged $40,so he will contribute $40 to the theater. \n" +
                        "C. He has not completed the section for paying by check. \n" +
                        "D. He is  not requesting a schedule.", "7");
        objReadingPractice.addValuesToReadingQuestion("22","What kind of room was reserved?","c", "A single","A twin","A double","A suit",
                "(c) A superior double room was reserved. Choice(a)confuses a single and one superior double room. Choice(b) confuses twin with double.Choice(d) is not mentioned.", "8");
        objReadingPractice.addValuesToReadingQuestion("23","Which of the following is not included in the price of the room?","d", "Breakfast","Tax","A newspaper","Dinner",
                "(d) Dinner is not included in the price of the room. Choices(a),(b),and (c) are all included in the price of the room.", "8");
        objReadingPractice.addValuesToReadingQuestion("24","How did Mr.Peterman make a reservation?","a", "By fax","Through an agent","By letter","In person",
                "(a) Mr.Peterman faxed his reservation. Choices (b),(c),and (d) are contradicted by thank you for your confirmation fax.", "8");
        objReadingPractice.addValuesToReadingQuestion("25","What kind of firm is hiring?","d", "A computer company","An accounting office","An advertising agency","A law firm",
                "(d) A large law firm is hiring an accountant. Choice(a) confuses a computer company and computer skills. Choice(b) associates accounting office with accountant. Choice(c)confuses advertising agency and job advertisement.", "9");
        objReadingPractice.addValuesToReadingQuestion("26","Which of the following is NOT mentioned as a qualification?","c", "Experience as a supervisor","Familiarity with automated financial systems","A law degree","A degree in accounting",
                "(c) A law degree is not mentioned as a qualification. Choice(a),(b),and (d) are mentioned.", "9");
        objReadingPractice.addValuesToReadingQuestion("27","What kind of applicant would be most attracted to this job?","b", "A lawyer","An accountant","A computer science major","A director of human resources",
                "(b) An accountant would probably be most interested in an accounting position. Choice (a) associates lawyer with law firm. Choice (c) associates a computer science major with computer skills. Choice (d) associates director of human resources with supervisory experience.", "9");
        objReadingPractice.addValuesToReadingQuestion("28","Where would this letter most likely","a", "In a microwave manual","In the mail","In an a advertisement","In a design store",
                "(a) This letter would be found in a microwave manual. Choices (b) and (c) are contradicted by in this manual. Choice (d) confuses design store and product has been designed to give you many years of trouble-free operation.", "10");
        objReadingPractice.addValuesToReadingQuestion("29","The word \"sophisticated\" in line2 is closest in meaning to","b", "popular","advanced","dependable","well-known",
                "(b) Sophisticated means advanced. Choices (a),(c), and (d) could also be used to describe a microwave oven,but they have different meanings.", "10");
        objReadingPractice.addValuesToReadingQuestion("30","What must the user do for trouble-free operation?","b", "Exchange the product","Follow instructions","Purchase another model","Redesign the Kitchen",
                "(b) The user must follow the instructions in the manual. Choices (a) and (c) are not mentioned. Choice (d) confuses redesign the kitchen and this product has been designed.", "10");

    }

    private void AddValuesToPhotosTest() {

        objPhotoDatabase.AddValuesToPhotographsQuestionTest("1","A");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("2","C");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("3","A");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("4","B");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("5","C");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("6","B");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("7","D");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("8","A");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("9","B");
        objPhotoDatabase.AddValuesToPhotographsQuestionTest("10","A");


        objPhotoDatabase.AddValuesToPhotographsChoiceTest("1", "She's wearing protective clothing.", "She's buying a new hat.", "She's storing food in jars.", "She's dressing for a party.", "1");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("2", "The water glass is empty.", "The swimmers are racing.", "The man is cleaning the pool.", "The guest is relaxing by the pool.", "2");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("3", "The man is putting the suitcase into the trunk.", "The woman is walking behind the man.", "The couple is getting out of the car.", "The bags are being weighed.", "3");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("4", "She's closing the notebook.", "She's filling out a form.", "She's checking the bookshelves.", "She's cleaning the table.", "4");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("5", "The cord is being cut.", "The telephone booth is on the corner.", "The woman is on the phone.", "The tourist is studying the map.", "5");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("6", "The panes are in the frames.", "The planes are at their gates.", "The trains are in the station.", "The cranes are on the wharf.", "6");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("7", "He's trying to catch a mouse.", "He's holding a pad of paper.", "He's examining his eyes.", "He's working at his computer.", "7");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("8", "The ferry is crossing the water.", "The passengers are boarding at the pier.", "The sailboat is in the harbor.", "The  tanker is in dry dock.", "8");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("9", "The customers are waiting for a table.", "The people are reading their newspapers.", "The library is open at night.", "The menus are being printed.", "9");
        objPhotoDatabase.AddValuesToPhotographsChoiceTest("10", "He's pouring a cup of coffee.", "He's emptying his pockets.", "He's spilling the liquid.", "He's brewing a pot of coffee.", "10");

    }

    private void AddValuesToQandRTest() {

        objQandR.addValuseToQuestionAndResponseQuestionTest("1", "Do you want some lemon with your tea?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("2", "Whose jacket is this?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("3", "Where can I find the printer cartridges?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("4", "What time does the reception start?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("5", "Do you want to unpack supplies, or would you rather help customers?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("6", "Didn't we go to the same university?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("7", "I'm leaving early again today.", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("8", "Don't you think you might need an umbrella?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("9", "The mail has been delivered already,hasn't it?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("10", "How many employees are expected to attend the staff training?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("11", "It's hard to find a taxi around here.", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("12", "When will you finish editing that report?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("13", "Should we try the new restaurant across the street?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("14", "Why do you think the flight was canceled?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("15", "Will you notify everyone of the change?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("16", "I've just arranged to have my car repaired tomorrow.", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("17", "Do you know where I can get my computer fixed?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("18", "Why don't we buy new chairs for the waiting room?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("19", "Susan's out on vacation, isn't she?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("20", "When was the client billed for the new designs?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("21", "Haven't we received our concert tickets yet?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("22", "Why were you so late to the morning session?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("23", "Who was the director talking to this morning?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("24", "Would you like to review the final draft,or should I send it now?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("25", "What areas will the inspectors be checking tomorrow?", "B");
        objQandR.addValuseToQuestionAndResponseQuestionTest("26", "We should recruit volunteers to help plan the company picnic.", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("27", "Would you care to join us for dinner after the board meeting?", "A");
        objQandR.addValuseToQuestionAndResponseQuestionTest("28", "We aren't going to be done on time, are we?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("29", "How often do you hear from your former colleagues?", "C");
        objQandR.addValuseToQuestionAndResponseQuestionTest("30", "Did you remember to bring your passport?", "A");

        objQandR.addValuseToQuestionAndResponseChoiceTest("1", "Yes, but only a small slice.", "No, we left at three.", "Under the lemon tree.", "1");
        objQandR.addValuseToQuestionAndResponseChoiceTest("2", "Oh, that's Joan's.", "It's not cold outside.", "I guess it is.", "2");
        objQandR.addValuseToQuestionAndResponseChoiceTest("3", "Did you really?", "Ten copies of each.", "In aisle two, on the left.", "3");
        objQandR.addValuseToQuestionAndResponseChoiceTest("4", "Yes, it was a great evening.", "The invitation said 7 P.M.", "I don't think he's coming.", "4");
        objQandR.addValuseToQuestionAndResponseChoiceTest("5", "It's a pack of six.", "Which would you prefer?", "The office supply store.", "5");
        objQandR.addValuseToQuestionAndResponseChoiceTest("6", "Some of them are.", "That's a nice name.", "Yes, I remember you.", "6");
        objQandR.addValuseToQuestionAndResponseChoiceTest("7", "Yes,the leaves are colorful.", "Are you still feeling ill?", "Sometime before three.", "7");
        objQandR.addValuseToQuestionAndResponseChoiceTest("8", "I have one in my bag.", "Correct, last night.", "We'll think of you, too.", "8");
        objQandR.addValuseToQuestionAndResponseChoiceTest("9", "Sorry, your meal isn't ready yet.", "Yes, it came at 2 o'clock.", "He lives in New York City.", "9");
        objQandR.addValuseToQuestionAndResponseChoiceTest("10", "Only thirty have signed up so far.", "We do need to hire more staff.", "He never showed up.", "10");
        objQandR.addValuseToQuestionAndResponseChoiceTest("11", "It's about 1O percent.", "This one feels softer.", "Yes,we should take a bus.", "11");
        objQandR.addValuseToQuestionAndResponseChoiceTest("12", "No,I don't need to add that.", "By the middle of next week.", "Since some time last January.", "12");
        objQandR.addValuseToQuestionAndResponseChoiceTest("13", "Sure, let me go get my wallet.", "It should be,yes.", "We'll order one.", "13");
        objQandR.addValuseToQuestionAndResponseChoiceTest("14", "There was a snowstorm.", "Three flight attendants.", "An hour from here.", "14");
        objQandR.addValuseToQuestionAndResponseChoiceTest("15", "Only some of them are.", "I'm sorry, I don't have any.", "I'll take care of it right away.", "15");
        objQandR.addValuseToQuestionAndResponseChoiceTest("16", "Between about 15 and 20.", "I bought two pair.", "Will you need a ride to work?", "16");
        objQandR.addValuseToQuestionAndResponseChoiceTest("17", "I've never met him.", "Yes, it can be.", "My cousin can do it for you.", "17");
        objQandR.addValuseToQuestionAndResponseChoiceTest("18", "Please have a seat.", "There's not enough money.", "It's about 34 kilograms.", "18");
        objQandR.addValuseToQuestionAndResponseChoiceTest("19", "She already has one.", "It's right outside.", "Her assistant probably knows.", "19");
        objQandR.addValuseToQuestionAndResponseChoiceTest("20", "The building was completed on schedule.", "We sent out an invoice last week.", "That's what the sign says.", "20");
        objQandR.addValuseToQuestionAndResponseChoiceTest("21", "I've never heard of that group.", "No, I need to call the box office again.", "It's an hour away.", "21");
        objQandR.addValuseToQuestionAndResponseChoiceTest("22", "Thanks,but I already had breakfast.", "I was catching up on some work at home.", "I thought it went well, too.", "22");
        objQandR.addValuseToQuestionAndResponseChoiceTest("23", "A former employee.", "No,tomorrow morning.", "In the other direction.", "23");
        objQandR.addValuseToQuestionAndResponseChoiceTest("24", "Please give it to me first.", "Yes, I received your e-mail.", "The view is lovely from up here.", "24");
        objQandR.addValuseToQuestionAndResponseChoiceTest("25", "We can always arrange a tour.", "The cutting and sewing rooms.", "For a semi-annual inspection.", "25");
        objQandR.addValuseToQuestionAndResponseChoiceTest("26", "I'm sure Kathy from accounting will help.", "He leads the overnight crew.", "That's not included in the business plan", "26");
        objQandR.addValuseToQuestionAndResponseChoiceTest("27", "That sounds wonderful , thank you.", "I'll have the chicken, please.", "The meeting was boring, wasn't it?", "27");
        objQandR.addValuseToQuestionAndResponseChoiceTest("28", "I'll watch it for you.", "She's going there now.", "It doesn't look that way.", "28");
        objQandR.addValuseToQuestionAndResponseChoiceTest("29", "I still need one.", "I didn't hear it.", "Every few weeks.", "29");
        objQandR.addValuseToQuestionAndResponseChoiceTest("30", "No, I'll have to go home and get it.", "Enter your password.", "Hong Wei brought some,too.", "30");

    }

    private void AddValuesToShortConTest() {

        objShortCon.addValuesToShortConversationScriptTest("1",
                "W: My plane is at seven, so I'll have to leave the office around five-thirty. That should give me enough time to get to the airport, don't you think?\n \n"+
                        "M: Actually, I think you should leave earlier, Rush hour starts at five. The traffic will be really heavy by five-thirty.\n \n"+
                        "W: Yes, you're right. Maybe I'd better  plan to leave at four.\n \n"+
                        "M: That's a much better idea. And, look, you'll need something to read on that long flight. Take these newspapers with you.I've already read them.");
        objShortCon.addValuesToShortConversationScriptTest("2",
                "M: Do you hav e any cash on you by any chance?\n \n"+
                        "W: Well, yes, I went to the bank this afternoon, but I only have about forty dollars.\n \n"+
                        "M: Great. That's plenty. Could you lend me fifteen until tomorrow? I'm in a rush to get to a meeting, and I need some cash to pay for a taxi.\n \n"+
                        "W: Well, all right, but promise you'll pay me back tomorrow, not next week or the week after.");
        objShortCon.addValuesToShortConversationScriptTest("3",
                "W: I really don't want to see a Western or a murder movie. I'm tired of violent movies. Isn't there a comedy playing? That's something we'd both enjoy.\n \n"+
                        "M: Let's see . . . the next movie starts at 7:30. Oh, but it's a war story. More violence.\n \n"+
                        "W: Then let's just stay home. I'm sure we can find something good on TV to watch.\n \n"+
                        "M: No, I don't want to do that. I'm tired of staying home. I want to go out. Let's have dinner at that place around the corner. We haven't been there in a while.");
        objShortCon.addValuesToShortConversationScriptTest("4",
                "M: Please let everyone know that Wednesday's meeting has been postponed for two days. And please do i t as soon as possible. They should know about this right away.\n \n"+
                        "W: So you want me to tell them that it'll be on Friday instead?\n \n"+
                        "M: Yes. I'm sorry to have to do this at the last minute, but I just found out that Ms.Schmidt is still out sick, and we can't have a budget meeting without our accountant. What would be the point?\n \n"+
                        "W: I guess you're right about that. I'll make sure the conference room will be ready. And I'll call everyone about the meeting to make sure they get the information on time. It's more reliable that way than by e-mail.");
        objShortCon.addValuesToShortConversationScriptTest("5",
                "W: I'm sorry I'm late again. I ran out of gas. Can you believe it?\n \n"+
                        "M: You should take the bus. Then you wouldn't have to worry about gas or flat tires or anything like that. Or, even better, get up earlier and walk. That's what Ido. I walk to work every morning.\n \n"+
                        "W: Well, I tried taking the bus yesterday, and I got here even later than I do when I drive because it has to stop at every bus stop. It takes forever.\n \n"+
                        "M: See? Walking's the fastest way. It only takes me 45 minutes to get to work. And I always arrive feeling relaxed and energized.\n \n"+
                        "W: Well, it would take me a lot longer than it takes you. I'd have to train before I could walk that fast, and anyhow, I live a  lot farther away than you do.");
        objShortCon.addValuesToShortConversationScriptTest("6",
                "M: Oh, no. I don't have my glasses. I think I must have left them on the desk in your office.\n \n"+
                        "W: I'll wait for you here while you go back and look for them. Here, you'll need my keys. My office is locked.\n \n"+
                        "M: OK, thanks. Hold onto my briefcase for me, and don't wait here. I'll catch up with you at the car. It will only take me a minute.\n \n"+
                        "W: All right. I'm parked right across the street, in front of the post office.");
        objShortCon.addValuesToShortConversationScriptTest("7",
                "M: It sure is hot today. You could fry an egg on the sidewalk. I can't remember the last time it was this hot in April.\n \n"+
                        "W: I know. It's usually cooler this time of year.\n \n"+
                        "M: Well, I can't complain. Since I sell ice cream, this heat's good for business. I've been doing really well all week.\n \n"+
                        "W: That's good  for you. And I get to stay cool by working in an air-conditioned office all day, so that's something.");
        objShortCon.addValuesToShortConversationScriptTest("8",
                "W: What a handsome suit. I've never seen you wear it before. Did you buy it somewhere around here?\n \n "+
                        "M: No, I bought it when I was in Hong Kong last spring. The shoes, shirt, and tie I already had. Do you think they look good together?\n \n"+
                        "W: They look very nice. The whole outfit looks great. That color tie really goes well with the suit. Did you get it in Hong Kong, too? It's really unusual looking.\n \n"+
                        "M: No, I don't remember where I got the tie, but I make a point of buying all my suits when I go out of town. The suits at these local department stores are too expensive and, in my opinion, not very attractive."+
                        "W: I know what you mean. I don't much like going shopping around here, either. The choices are not very good.");
        objShortCon.addValuesToShortConversationScriptTest("9",
                "M: I'm going hiking in the mountains for my vacation.\n \n"+
                        "W: Hiking? That's energetic of you. After working as hard as you do, I would think you would want to spend a few weeks just lying on a sunny beach reading a good book.\n \n"+
                        "M: I'm tired of lazy vacations by the sea. I like to be active when I take a vacation. I'm really looking forward to three weeks walking in the mountains.\n \n"+
                        "W: Wow! That's a long time. Are you leaving soon?\n \n"+
                        "M: Yes, on Sunday. Just two days away. I can't wait.");
        objShortCon.addValuesToShortConversationScriptTest("10",
                "W: I think we've got the transportation plan worked out. We have four buses to take people from the hotel to the convention center. That should be enough, don't you think?\n \n"+
                        "M: Yes, I think so. It takes five minutes to load one bus and ten minutes to make the trip, so we could plan to have one bus leave every fifteen minutes.\n \n"+
                        "W: That sounds like a good schedule. Each bus could make the round trip in thirty minutes, and no one would have to wait too long for a ride . Then we won't have to worry about anybody's ruffled feelings.");



        // Question

        objShortCon.addValuesToShortConversationQuestionTest("1", "What time will the woman leave?", "A", "4:00.", "5:00.", "5:30.", "7:00.", "1");
        objShortCon.addValuesToShortConversationQuestionTest("2", "Where will the woman go?", "B", "To the train station.", "To the airport.", "To the office.", "To the bus station.", "1");
        objShortCon.addValuesToShortConversationQuestionTest("3", "What does the man give the woman?", "C", "A toothbrush.", "A book.", "Some newspapers.", "Some writing paper.", "1");
        objShortCon.addValuesToShortConversationQuestionTest("4", "How much money does the man need?", "B", "Fourteen dollars.", "Fifteen dollars.", "Forty dollars.", "Fifty dollars", "2");
        objShortCon.addValuesToShortConversationQuestionTest("5", "What does he need the money for?", "C", "To buy lunch.", "To pay a sales tax.", "To pay his taxi fare.", "To bu y some reading material.", "2");
        objShortCon.addValuesToShortConversationQuestionTest("6", "When does the man say he will pay the money back?", "B", "This afternoon.", "Tomorrow.", "The day after tomorrow", "Next week.", "2");
        objShortCon.addValuesToShortConversationQuestionTest("7", "What kind of movie do both speakers like?", "A", "Comedies.", "War stories.", "Murder mysteries", "Westerns.", "3");
        objShortCon.addValuesToShortConversationQuestionTest("8", "What time does the next movie start?", "C", "4:00.", "7:00.", "7:30.", "10:30.", "3");
        objShortCon.addValuesToShortConversationQuestionTest("9", "What does the man want to do?", "D", "See a war movie.", "Stay home.", "Watch TV.", "Go to a restaurant.", "3");
        objShortCon.addValuesToShortConversationQuestionTest("10", "When will the meeting be held?", "D", "Tuesday.", "Wednesday.", "Thursday.", "Friday.", "4");
        objShortCon.addValuesToShortConversationQuestionTest("11", "Why has the meeting been postponed?", "A", "The accountant is ill.", "The room is not ready.", "Ms.Schmidt is away at a conference.", "The speaker is still reading the budget report.", "4");
        objShortCon.addValuesToShortConversationQuestionTest("12", "How will people be notified about the postponed meeting?", "A", "By phone.", "By e-mail.", "By letter.", "In person.", "4");
        objShortCon.addValuesToShortConversationQuestionTest("13", "Why was the woman late today?", "D", "She slept late.", "She walked slowly.", "She took the bus.", "She ran out of gas.", "5");
        objShortCon.addValuesToShortConversationQuestionTest("14", "How does the man get to work?", "C", "By bus.", "By car.", "On foot.", "By train.", "5");
        objShortCon.addValuesToShortConversationQuestionTest("15", "How long does it take the man to get to work?", "D", "14  minutes.", "35 minutes.", "40 minutes.", "45 minutes.", "5");
        objShortCon.addValuesToShortConversationQuestionTest("16", "Where did the man leave his glasses?", "B", "On his desk.", "In the woman's office.", "In his briefcase.", "In the car.", "6");
        objShortCon.addValuesToShortConversationQuestionTest("17", "What does the woman give the man?", "B", "A kiss.", "Her keys.", "A briefcase.", "A locket.", "6");
        objShortCon.addValuesToShortConversationQuestionTest("18", "Where is the woman's car?", "D", "In a parking lot.", "In front of her office.", "Across the street from the park.", "In front of the post office.", "6");
        objShortCon.addValuesToShortConversationQuestionTest("19", "What are the speakers talking about?", "C", "Eating.", "Doing business.", "The hot weather.", "The city's sidewalks.", "7");
        objShortCon.addValuesToShortConversationQuestionTest("20", "What does the man sell?", "C", "Eggs.", "Coolers.", "Ice cream.", "Air conditioners.", "7");
        objShortCon.addValuesToShortConversationQuestionTest("21", "Where does the woman work?", "B", "At a pool.", "At an office.", "At a school.", "At a restaurant.", "7");
        objShortCon.addValuesToShortConversationQuestionTest("22", "What did the man buy?", "C", "A shirt.", "A he.", "A suit.", "A pair of shoes.", "8");
        objShortCon.addValuesToShortConversationQuestionTest("23", "Where did he buy it?", "A", "In Hong Kong.", "At the mall.", "Downtown.", "At a local department store.", "8");
        objShortCon.addValuesToShortConversationQuestionTest("24", "What is the woman's opinion of the suit?", "C", "It's not very attractive.", "It's too expensive.", "It's nice-looking.", "Its color is great.", "8");
        objShortCon.addValuesToShortConversationQuestionTest("25", "How will the man spend his vacation?", "A", "Hiking.", "Reading.", "Lying in the sun.", "Swimming in the sea.", "9");
        objShortCon.addValuesToShortConversationQuestionTest("26", "How long is his vacation?", "D", "Two days.", "Eight days.", "Two weeks.", "Three weeks.", "9");
        objShortCon.addValuesToShortConversationQuestionTest("27", "When will his vacation begin?", "B", "This afternoon.", "On Sunday.", "On Tuesday.", "In a few weeks.", "9");
        objShortCon.addValuesToShortConversationQuestionTest("28", "How many buses are there?", "D", "One.", "Two.", "Three.", "Four.", "10");
        objShortCon.addValuesToShortConversationQuestionTest("29", "Where will the buses leave from?", "A", "The hotel.", "The convention center.", "The bus station.", "The loading dock.", "10");
        objShortCon.addValuesToShortConversationQuestionTest("30", "How of ten should the buses leave?", "C", "Every five minutes.", "Every ten minutes.", "Every fifteen minutes.", "Every thirty minutes.", "10");

    }

    private void AddValuesToShortTalkTest() {

        objShortTalkScript.AddValuesToShortTalkScriptTest("1","In traffic news,construction work on the new downtown shopping center is causing heavy traffic delays along Dawson Street. To reach the downtown area between the hours of 9 A.M. and 5 P.M., city officials recommend taking either Valley Road or Perry Road. Traffic controllers will be stationed along these roads to help direct drivers until the shopping center is completed. For the most up-to-date traffic information,drivers are advised to call 555-3732.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("2","Hello,Mr.McDuffee.This is Trevor Davis from WGT. I'm calling about the teleconferencing meeting that's scheduled for tomorrow evening.I've e-mailed you the agenda for the discussion of the Kenner contract. If you have anything to add to the agenda,please e-mail us by tomorrow at 4 P.M. The meeting is scheduled for 7 P.M., and you'll be called at your home number. If there are any changes,you can reach me in Boston at 310-555-4364. Thank you.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("3","Before we start the meeting,I have an announcement. As you know,we've been searching for a new public relations director here at Tektron Advertising. Our search is over and i'd like to introduce our newest employee, Ryoko Hashimoto. Before joining our company, Ryoko worked as a manager at a marketing agency for fifteen years.She just started yesterday and she's new to our region,so it would be helpful if you could all assist Ryoko and answer any of her questions about the local area. She'd like to say a few words,so join me in welcoming our new public relations director, Ms. Ryoko Hashimoto.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("4","Attention,passengers waiting to board flight 1425 to Moscow. Your plane has been delayed due to snow in Paris. We do not expect it to arrive here in New York for another hour,so boarding will begin at approximately 1:30 P.M. We apologize for this inconvenience and we'll continue to update you as we receive additional information. Again, passengers waiting for flight 1425 to Moscow.Because of bad weather conditions in Paris,the plane will arrive late,and boarding will not take place until 1:30 P.M.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("5","OK everyone, thanks for coming today to hear our quarterly results. The Sedonar Company has performed well over the last three months,earning a 6 percent rate of return. These high levels of growth are mainly attributed to our acquisition of the Tucker Firm,an investment that is proving to be very profitable.In addition,we were able to keep costs down by streamlining out production plants. The time it takes to produce one of our top-selling sofas was cut by one hour,which is a very positive development.We expect costs to remain static and growth to continue into the next quarter.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("6","Please help us make our ride-sharing program a success. The recent acquisition of five additional vans has made it possible for us to expand this program to three new neighborhoods in the city. This complements the existing groups who've been traveling to our office's West-end location for the last several months. Employees who live in these areas and are interested in joining a car-pooling group should contact the Employee Services office. The vans are provided free to each group,and the company pays for insurance and maintenance. Participating employees contribute to a fund for gas.We hope you'll choose to save time and money and help improve the environment by joining the ride-sharing program.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("7","Hello,Ms.Cho. This is Mary Harding from Essentials Beauty Shop.I'm calling to let you know that Kim won't be able to cut your hair ar 3 o'clock tomorrow.She went home feeling unwell today and scheduled a doctor's appointment for tomorrow afternoon. I would be happy to schedule you for an appointment tomorrow with someone else at the same time as your original appointment,or if you would prefer to come in sometime next week when your regular stylist is feeling better,that to offer you a ten percent discount on any future service at Essentials. Could you please call me sometime today to confirm that you received this message? I'm so sorry for any inconvenience this may have caused you.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("8","Welcome to Horanza Natural History Museum.You are probably familiar with this museum's world-famous dinosaur exhibit.However,today's tour focuses on the history of Native Americans in the western United States from the 1500s to the present.In the first room you'll see displays of tools,handicrafts,costumers,and other artifacts produced by Native Americans who lived throughout the West.In the second room you will have the opportunity to watch movies of Native American celebrations,including demonstrations of traditional food preparation and dances.Our tour will last one hour,and if you have any questions,please be sure to ask.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("9","Waynard Laboratories,one of the would's largest pharmaceutical companies,announced today that for the tenth consecutive year it has been selected as one of the 100 best companies to work at.The magazine At Work recognized the company for its commitment to employee health and fitness.Dan Wright,director of human resources,stated that Waynard Laboratories was proud to be acknowledged by this popular magazine for its commitment to helping employees stay in good physical condition. He believes that people working long hours at a desk or in a laboratory greatly benefit from the company's training center,which includes an on-site fitness room and swimming pool for exercising before and after work hours or at lunchtime.");
        objShortTalkScript.AddValuesToShortTalkScriptTest("10","Good afternoon,everybody,and thanks for agreeing to come in on your weekend.We've got a lot of work to get through,so i'll be brief. I got a phone call on Friday from Julius at GT Telecom,and he was not satisfied with the Web site we set up for them.First,the good news: he liked the overall graphic design of the site,and was happy that the GT logo was prominently displayed throughout the site.There are a few important things missing,though.We need to set up a way for GT customers to manage their accounts through the Web site.That includes letting them pay bills and change their address information. Also,we need to make the Customer Service phone numbers and e-mail address much easier to find.I think we should put them on the home page.Let's get to work.");


        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("31","What is the main topic of this report?","D", "City traffic laws","Online news updates","Downtown entertainment","Alternate driving routes", "31");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("32","Who will provide assistance to the public?","C", "Construction workers","Shop owners","Traffic controllers","Bus drivers", "32");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("33","What type of information is available by telephone?","A", "Traffic updates","Store hours","Weather reports","Sales promotions", "33");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("34","Why did Trevor Davis call?","D","To request a telephone number","To reschedule a meeting","To confirm a fax number","To provide information about a meeting", "34");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("35","What should Mr. McDuffee do before 4 P.M. ?","A", "Send comments about the agenda","Finish writing the contract","E-mail the Kenner company","Call to schedule the meeting", "35");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("36","Where will Mr.McDuffee be when the group meets?","B", "On an airplane","At home","In an office","In a television studio", "36");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("37","What is the main purpose of the talk?","B", "To announce a job opening","To introduce an employee","To describe a department's function","To answer a question about advertising", "37");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("38","What is Ms. Hashimoto's new position?","B", "Development officer","Public relations director","Marketing supervisor","Production manager", "38");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("39","How are other employees asked to assist Ms. Hashimoto?","C", "By providing technical training","By setting up her office","By telling her about the local area","By giving her a tour of the building", "39");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("40","Where does this announcement probably take place?","D", "On a tour bus","On a ship","In a train station","At an airport", "40");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("41","According to the talk,why is there a delay?","A", "Weather conditions are bad.","There are mechanical problems.","Traffic is heavy.","Luggage us still being loaded.", "41");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("42","Where did a delay occur?","A", "In Paris","In New York","In Moscow","In Chicago", "42");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("43","Who is probably speaking","D", "A business news reporter","The president of the Tucker Firm","A performance artist","A representative of the Sedonar Company", "43");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("44","What type of business is Sedonar?","A", "A furniture manufacturer","An investment firm","A film production company","A flower shop", "44");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("45","How has Sedonar saved money?","C", "By selling the Tucker Firm","By closing production plants","By increasing efficiency","By limiting expenses", "45");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("46","What is the talk mainly about?","B","Changes to employee salaries","A service for commuters","Traffic problems is the city","A new community pool", "46");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("47","What is now available for employees?","D", "A new insurance plan","A fitness center","Additional office space","Additional vehicles for commuting", "47");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("48","What are employees responsible for","C", "Equipment maintenance","Insurance fees","Contributions for gas","Regular check-ups", "48");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("49","Where does the speaker work","A", "At a beauty salon","At a doctor's office","At a fitness center","At an auto repair shop", "49");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("50","Why is the appointment being rescheduled?","C", "The shop will be closed.","The doctor is out of town.","The stylist is sick","The mechanic is not available.", "50");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("51","What will Ms.Cho receive","C", "A gift certificate","A note of apology","A discount on a service","A gift basket", "51");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("52","Where is the talk probably being given?","C", "On a tour bus","At a conference center","In a museum","In a university lecture hall", "52");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("53","What will the listeners learn about?","D", "Famous explorers","Asian art","Prehistoric tools","Native American history", "53");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("54","What will the listeners have the opportunity to do?","A", "View movies","Sample food","Meet artists","Try on costumes", "54");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("55","What is the report mainly about?","C", "Construction of a fitness center","The opening of new laboratories","Recognition of a company's health program","The appointment of a new company director", "55");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("56","According to the report,who gave the company special acknowledgment?","D", "A sports association","A government commission","A pharmaceutical organization","A popular magazine", "56");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("57","According to the report,what is a benefit of working at Waynard Laboratories?","B", "It has access to new medical treatments.","It offers opportunities to exercise at work.","It has modern laboratories for research","It pays employees to take work-related classes.", "57");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("58","On what day does this talk likely take place?","D", "Monday","Wednesday","Friday","Saturday", "58");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("59","What did Julius like about the GT Telecom Web site?","B", "It is easy to get from page to page.","The company logo appears frequently.","Customers can pay their bills online.","It contains the company's address.", "59");
        objShortTalkQuestion.AddValuesToShortTalkQuestionTest("60","What will be added to the GT Telecom Web site?","B", "More picture of GT products","Features for managing accounts online","Photos of a celebrity spokesperson","Product reviews from GT customers", "60");

    }

    private void AddValuesToIncompleteSentenceTest() {

        objIncomplete.addValuesToIncompleteQuestionTest("1", "The two companies are now .............the price Luco Ltd. will pay Gnose for the property in Quebec.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("2", ".............having several years of experience in management, Pedro Castillo did not get the promotion for which he applied.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("3", ".............the past twenty years, Premium Telecom has rewarded all of its employees with a generous vacation package.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("4", "Students ............. present a valid identification card can obtain a ten percent discount on tickets to all musical performances.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("5", "The manufacturer recommends machine-drying at low temperatures; high temperatures may result in excessive shrinkage and shorten the life of a .............. .", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("6", "We will be hiring five part-time employees to ............. staff in the operations department.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("7", "Because of rush-hour congestion in Farmington, several large corporations are implementing .............work-hour programs.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("8", "After ............. years of performing for live audiences,stage director Kenneth Ogozi is glad to be working behind the scenes again.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("9", "Dairy exports .............for only five percent of the country's total agricultural sales.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("10", "A local steel manufacturing .............has purchased the riverfront lot in order to increase its production capacity.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("11", "Although the graphics department ............. acquired a color printer, it has already submitted a request for another one.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("12", "This exciting new product is ............. of the new software applications that our developers are working on.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("13", "All Gruner Corporation employees will be invited to the holiday ............. scheduled for next Friday.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("14", "Great Hope is Toshi Raymond's most inventive stage production .............. .", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("15", "The company is confident that its new spokesman will be effective ............. attracting customers within the 18-to-49-year-old demographic.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("16", "When you .............your loan application, please remember to sign and date the last page.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("17", "Customers need not pay for shipping because it is ............. in the total price of the item.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("18", "Both financial advisors recommend investing in pharmaceutical companies, although Paul Laurinen is suggesting a wider ............. of stocks.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("19", "Water service in the building will be temporarily interrupted ............. city maintenance workers repair the fire hydrants.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("20", "When you are finished analyzing the survey data, please give ............. report to Ms. Chin so she can copy it.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("21", "The figures .............in this estimate are approximate costs and are subject to adjustment at the date of final settlement.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("22", "Most of our visitors find the information they need here in the main office, though we do ............. receive requests for records that are housed off-site.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("23", "Many analysts attribute Kramar Industries'............. success to its state-of-the-art research department.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("24", "The auditors' report indicates that the firm should ............. its manufacturing division.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("25", "Since the closing of the community theater, many Corana residents have become strong ............. of public funding for the arts.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("26", "As a security measure, employees of Kramnick Corporation are encouraged to change their computer passwords .............. .", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("27", "Construction on the bridge ............. the two cities has progressed more rapidly than anticipated.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("28", "To attract applicants who ............. might not be interested, Phantom Chemical Laboratories is offering each new hire a relocation allowance.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("29", "Agricomp plans to spend $54 million ............. the next six years to build laboratories near its headquarters.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("30", "Mr.Granger began his speech by thanking Mr.Takase, who has been teaching him Japanese ............. his arrival in Tokyo.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("31", "Mr.Doh ............. clients' phone calls.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("32", "Success depends ............. the efforts of the organization.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("33", "There has been strong competition; ............. , the new company has made great profits.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("34", "Ms.Shirish wi ll resign her position as chief ............. officer.", "D");
        objIncomplete.addValuesToIncompleteQuestionTest("35", "The weather report predicts it will rain ............. become colder.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("36", "The printer ............. paper.", "B");
        objIncomplete.addValuesToIncompleteQuestionTest("37", "The electricity went out ............. we were making coffee.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("38", "............. all the negotiators, Ms.Neas seems the most reliable.", "C");
        objIncomplete.addValuesToIncompleteQuestionTest("39", "The sales division reported a 64 percent drop ............. the last sales period.", "A");
        objIncomplete.addValuesToIncompleteQuestionTest("40", "The company is financially sound; ............. , there is no debt.", "B");


        objIncomplete.addValuesToIncompleteChoiceTest("1", "negotiate", "negotiating", "negotiation", "negotiated", "1");
        objIncomplete.addValuesToIncompleteChoiceTest("2", "In spite of", "Unless", "Regardless", "Even so", "2");
        objIncomplete.addValuesToIncompleteChoiceTest("3", "From", "Before", "For", "After", "3");
        objIncomplete.addValuesToIncompleteChoiceTest("4", "whoever", "whose", "whom", "who", "4");
        objIncomplete.addValuesToIncompleteChoiceTest("5", "garment", "clothing", "fabrication", "fitting", "5");
        objIncomplete.addValuesToIncompleteChoiceTest("6", "assist", "assists", "assisting", "assisted", "6");
        objIncomplete.addValuesToIncompleteChoiceTest("7", "submissive", "inclusive", "tangible", "flexible", "7");
        objIncomplete.addValuesToIncompleteChoiceTest("8", "all", "many", "much", "every", "8");
        objIncomplete.addValuesToIncompleteChoiceTest("9", "assign", "account", "charge", "contribute", "9");
        objIncomplete.addValuesToIncompleteChoiceTest("10", "facilitate", "facilitating", "facility", "facilitation", "10");
        objIncomplete.addValuesToIncompleteChoiceTest("11", "highly", "usually", "recently", "entirely", "11");
        objIncomplete.addValuesToIncompleteChoiceTest("12", "represents", "representative", "representing", "representation", "12");
        objIncomplete.addValuesToIncompleteChoiceTest("13", "management", "attendance", "celebration", "circumstance", "13");
        objIncomplete.addValuesToIncompleteChoiceTest("14", "yet", "only", "once", "when", "14");
        objIncomplete.addValuesToIncompleteChoiceTest("15", "between", "in", "to", "around", "15");
        objIncomplete.addValuesToIncompleteChoiceTest("16", "completed", "had completed", "are completing", "were completing", "16");
        objIncomplete.addValuesToIncompleteChoiceTest("17", "earned", "balanced", "checked", "included", "17");
        objIncomplete.addValuesToIncompleteChoiceTest("18", "select", "selected", "selection", "selective", "18");
        objIncomplete.addValuesToIncompleteChoiceTest("19", "during", "as far as", "now", "while", "19");
        objIncomplete.addValuesToIncompleteChoiceTest("20", "you", "your", "yourself", "yours", "20");
        objIncomplete.addValuesToIncompleteChoiceTest("21", "disposed", "provided", "solved", "handed", "21");
        objIncomplete.addValuesToIncompleteChoiceTest("22", "period", "periodical", "periodically", "periodic", "22");
        objIncomplete.addValuesToIncompleteChoiceTest("23", "phenomenon", "phenomena", "phenomenally", "phenomenal", "23");
        objIncomplete.addValuesToIncompleteChoiceTest("24", "expand", "discover", "excel", "devise", "24");
        objIncomplete.addValuesToIncompleteChoiceTest("25", "supportive", "supporters", "supporting", "support", "25");
        objIncomplete.addValuesToIncompleteChoiceTest("26", "frequently", "incidentally", "honestly", "relatively", "26");
        objIncomplete.addValuesToIncompleteChoiceTest("27", "was to link", "linking", "linked", "will be linked", "27");
        objIncomplete.addValuesToIncompleteChoiceTest("28", "otherwise", "except", "whether", "besides", "28");
        objIncomplete.addValuesToIncompleteChoiceTest("29", "over", "down", "along", "about", "29");
        objIncomplete.addValuesToIncompleteChoiceTest("30", "since", "at", "to", "when", "30");
        objIncomplete.addValuesToIncompleteChoiceTest("31", "rarely returns", "returns rarely", "has returned rarely", "rarely had returned", "31");
        objIncomplete.addValuesToIncompleteChoiceTest("32", "from", "in", "on", "of", "32");
        objIncomplete.addValuesToIncompleteChoiceTest("33", "instead", "nonetheless", "then", "despite", "33");
        objIncomplete.addValuesToIncompleteChoiceTest("34", "operator", "operational", "operation", "operating", "34");
        objIncomplete.addValuesToIncompleteChoiceTest("35", "neither", "nor", "and", "either", "35");
        objIncomplete.addValuesToIncompleteChoiceTest("36", "ran into", "ran out of", "ran without", "ran over", "36");
        objIncomplete.addValuesToIncompleteChoiceTest("37", "so", "because of", "while", "gor", "37");
        objIncomplete.addValuesToIncompleteChoiceTest("38", "From", "As", "Of", "But", "38");
        objIncomplete.addValuesToIncompleteChoiceTest("39", "during", "with", "at", "to", "39");
        objIncomplete.addValuesToIncompleteChoiceTest("40", "in spite of", "for example", "on the other hand", "nevertheless", "40");

    }

    private void AddValuesToTextCompletionTest() {

        objTextCompletionScript.AddValuesToTextCompletionScriptTest("1","ALBERTA BANK \n" +
                "Dear Steve Martin");
        objTextCompletionScript.AddValuesToTextCompletionScriptTest("2","");
        objTextCompletionScript.AddValuesToTextCompletionScriptTest("3","From:    PatriciaMarchmont <pmarchmont@cityparkvillage.com> \n" +
                "To:      rtsilva@gmail.com \n" +
                "Date:    24 April \n" +
                "Subject: Reservation \n" +
                "Dear Mr.Silva,");
        objTextCompletionScript.AddValuesToTextCompletionScriptTest("4","Office Work \n" +
                "544 Hudson Street \n" +
                "Boston, MA 34602 \n" +
                "Tel:(617)555-7664 Fax:(617)555-7670 \n" +
                "May 10,20__ \n" +
                "Mary Briddock \n" +
                "Banqueting Director \n" +
                "Wynd's Garden Hotel \n" +
                "219 Center Circle \n" +
                "Boston, MA 03299 \n" +
                "Dear Ms.Briddock: \n" +
                "Our company,Office Works,is seeking a place to host a banquet. We will honor our top employees at this event, which will include dinner followed by speeches and the presentation of awards.");


        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("1","In response to your request,we are switching your current Express account to our most popular........account FullAccess.","c", "check","checked","checking","checker", "1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("2","As a valued customer,we want to make this operation as simple...........possible for you.","d", "than","more","also","as", "1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("3","As of March 1st,we will automatically transfer your funds to your new account.Please note your existing account number and bank card number will remain the same. \n" +
                "For more information on the benefits of your new FullAccess account,please visit our website at www.alberta-bank.ca or visit your neighborhood branch. \n" +
                "We would like to remind you that Alberta Bank also offers a wide range of saving accounts that are ideal for customers sho wish to earn...........from their savings.","a", "interest","expenditure","investment","insurance" ,"1");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("4","Do you know that,last year alone,over 20 million cell phone users in this country.........to switch to newer models?","d", "are deciding","have decided","decide","decided", "2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("5","And what did they do with their old ones,you may ask? Well nothing - and that means that ............phones have have now been added to the growing mountain","a", "even more","more than","much more","as many as", "2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("6","of disused telephones which is currently at something like 66 million.So, if you are planning on changing yours sometime soon,then............of simply discarding it,make a gesture for the environment.","d", "as a result","because","in spite","instead", "2");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("7","Thank you for your letter enquiring about the availability of rental accommodation at our City Park Village I am sorry to have to inform you that the village is currently fully booked during the month of August,which is always the..........time of year for us.","d", "busy","busier","busily","busiest", "3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("8","We do,...........,have two apartments at our Highland Gates residence,","a", "however","while","although","yet", "3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("9","which are both vacant during the first two week of the month.These are both prestige apartments and feature the same standard of high quality furnishings.The Highland Gates residence is located on the outskirts of the city and is..........easy reach of the city center and of all the major festival venues.","b", "inside","within","into","along", "3");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("10","At the same time we plan to celebrate an...........to our","a", "addition","additive","addend","addendum", "4");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("11","company. We recently purchased the Office Supply Store, and we are now the region's largest seller of office supplies. \n" +
                "We expect approximately 100 guests. We would like to have our event on Saturday, August 15th. If no room is available for that date, we could consider............it on the following Saturday, August 22nd.","c", "have","to have","having","will have", "4");
        objTextCompletionQuestion.AddvaluesToTextCompletionQuestionTest("12","Cloud you please mail me your latest price list, descriptive brochure, and menus? I am interested in learning more about............facilities and services.","c", "my","our","your","their", "4");



    }

    private void AddValuesToReadingComprehensionTest(){

        //Reading Script
        objReading.addValuesToReadingScriptTest("1",
                "From:      Maria Jacobsen \n " +
                        "To:        Rina Evans\n "+
                        "Sent:      June 5\n "+
                        "Subject:   Automobile Insurance Policy #35629\n \n \n"+
                        "Hello, Rina,\n "+
                        "Mr.Williams sent me his personal information, which I have pasted into this e-mail."+
                        "Would you make a copy and put it in the customer's file, please?\n"+
                        "Thanks,\n "+
                        "Maria\n \n \n"+
                        "Hello, Ms.Jacobsen,\n \n"+
                        "Thanks for getting back to me so quickly. In answer to your questions, I purchased the"+
                        "used car last month on May 7 from the original owner, John Weldon.The car is a four-year-"+
                        "old Festo 645, and it still has the original black paint. I have registered the car and installed"+
                        "the license plates, 28 ON77. I've read through your company's list of policies, and I think I'd "+
                        "like to sign up for the comprehensive insurance. My home phone number is 343-555-3792."+
                        "I'll have a copy of my driving record faxed to you by next week. In the meantime, please let "+
                        "me know if I need to provide any other information.\n \n "+
                        "Thanks for your help, \n "+
                        "David Williams"); //3
        objReading.addValuesToReadingScriptTest("2",
                "                                                                             April 29\n \n"+
                        "                                                                       Martha Simmons\n "+
                        "                                                               425 East River Parkway\n "+
                        "                                                                   St. Paul, MN 55112\n "+
                        "Selvac Appliances\n"+
                        "Attn.: Customer Service\n "+
                        "8642 Lower Pine Drive \n "+
                        "Scranton, PA  18502\n \n "+
                        "Dear Customer Service,\n \n "+
                        "On April  15 I ordered a Selvac vacuum cleaner (model 12) from your product catalog. "+
                        "I had seen the product advertised on television and had high expectations. However, I "+
                        "have noticed that the vacuum cleaner barely absorbs dust, leaving the carpet unclean. "+
                        "In fact, the product is worse than my old vacuum cleaner, which I bought a couple of "+
                        "years ago for under $50.\n \n "+
                        "I am very disappointed with your product. Please contact me as soon as possible and "+
                        "let me know how to return the product and obtain a full refund. You can reach me by "+
                        "phone at 651-555-6323 or at the address above.\n \n "+
                        "Enclosed is a copy of the invoice, which shows how much I paid for the vacuum "+
                        "cleaner.I look forward to hearing from you soon.\n \n"+
                        "Sincerely,\n \n "+
                        "Martha Simmons"); //3
        objReading.addValuesToReadingScriptTest("3",
                "Garinello Studio\n \n"+
                        "Instruction in Photography\n \n "+
                        "Garinello Studio is pleased to offer instruction in photography in three different areas as outlined "+
                        "below. Serious amateurs are invited to join studio owner Steve Garinello for comprehensive, fun "+
                        "lessons in photography. Though class sizes vary, only ONE photographer at a time shoots pictures "+
                        "during each session. Classes are offered Monday through Friday except where noted otherwise.\n \n"+
                        "Portrait Session\n "+
                        "Participants will work with several different styles of lighting and learn how to achieve the best "+
                        "exposures. Instruction will be given in how best to pose the sitter, as well as how to integrate "+
                        "different props and backgrounds. We usually work through four different settings in a session, and "+
                        "sessions are limited to ten photographers. 7:30 to 10:00 P.M.;£25 per participant.\n \n "+
                        "Fashion Session\n  "+
                        "In addition to the key areas of lighting, poses, props, and backgrounds, participants will be given "+
                        "instruction in how to direct a model and what techniques can be used to bring out "+
                        "the model's best shots. Again, we typically work through four different scenes, and sessions "+
                        "are limited to ten participants. 7:30 to 10:30 P.M.; £28 per participant.\n \n"+
                        "Advertising and Still Life Session \n "+
                        "This is an introduction to the principles of advertising design. Special attention will be paid to the "+
                        "positioning and presentation of the product, as well as any models required in the shot. Some shots "+
                        "will be based on actual advertisements created by Steve Garinello, while others will be created to "+
                        "illustrate a specific technique. Sessions are limited to six photographers. 7:00 to 11:00 P.M.; £30 per "+
                        "participant.\n \n "+
                        "All-Day Sessions\n "+
                        "On Saturdays we offer full-day instruction covering Portrait and Fashion photography. Sessions are "+
                        "limited  to eight photographers . 9:30 A.M. to 3:00 P.M.;£60 (includes lunch and refreshments).");//4
        objReading.addValuesToReadingScriptTest("4",
                "Town Meeting to discuss Tunnel Project\n \n"+
                        "THE CLARION CITY Council will hold a meeting this week to discuss the proposal to build a tunnel under the Central River. The tunnel would not replace the existing bridge but "+
                        "would relieve traffic congestion on the roads that link downtown Clarion to suburbs on the south side of the river. Yanco Builders, seen as the firm likely "+
                        "to be given the building contract, has been working with council members ever since preliminary studies of the tunnel project began.A spokesperson for Yanco, Donald Young, stated, "+
                        "\"This tunnel will have a tremendous impact on the people of Clarion. Traffic conditions will improve; noise pollution will be reduced, and the struggling downtown shops and "+
                        "restaurants will see a growth in business.If people in the suburbs can get downtown more easily, they will go there for their shopping and entertainment.\"\n"+
                        "Some opponents of the tunnel project question whether the tunnel will actually reduce traffic jams. A citizens group calling itself Responsible Traffic "+
                        "Solutions (RTS) has been pushing for a temporary delay in the project so that further studies can be conducted. RTS, which has requested a meeting "+
                        "with  city council members, believes that the city's shortage of public transportation might be a more important issue to address when considering how "+
                        "to reduce traffic on the bridge. The group has also expressed concern over the loss of green space along the river. Says an RTS spokesperson, \"To take "+
                        "away the little green space the city has would be harmful to plant and animal species that live along the southern edge of the city and would take away "+
                        "from the natural charm of the area.\""); // 4
        objReading.addValuesToReadingScriptTest("5",
                "Classy Shopping Bags \n"+
                        "Winning Customers\n"+
                        "BY KATARINA WIESE\n \n"+
                        "Consumers' excessive use of hard-to-recycle plastic bags is harmful to the environment. Supermarkets have used many tactics to encourage their customers not "+
                        "to use these plastic bags.Some try charging extra for each bag used or offering paper bags instead, but these methods are far from perfect. "+
                        "Charging for a plastic bag can have the effect of driving customers to a competing supermarket. Paper bags are expensive to make, so they are "+
                        "less viable than even low-quality plastic bags.Many supermarkets offer a small discount in return for not using a plastic bag, and still others "+
                        "sell reusable cloth sacks that customers can bring back every time they come shopping. But the availability of cloth bags has done little to "+
                        "reduce the use of plastic disposables. Marketing experts at Ipanerra Supermarkets Company believe cloth bags would be more successful if they were more fashionable.\n"+
                        "According to them, it is hard to blame customers for not buying a durable cloth carryall, even a cheap one, if it doesn't have a catchy design, and if all it "+
                        "features is a company logo.\n"+
                        "To address this problem, Ipanerra hired a team of designers to make its cloth bags more colorful than those available at other supermarkets. Rather "+
                        "than sporting company logos, Ipanerra bags feature pictures of animals, drawings of famous buildings, and cartoon figures commissioned for this purpose. "+
                        "The result? Not only is there considerable demand for the bags but they also attract attention in the street, which means free advertising for the "+
                        "supermarket. The bags are easily associated with the supermarket chain even without the logos, which Ipanerra removed from both its cloth and plastic "+
                        "bags. The success has prompted the company to consider designing smaller sacks suited for younger customers, a logical next step for a supermarket "+
                        "chain targeting children."); // 5
        objReading.addValuesToReadingScriptTest("6",
                "Obtaining  Certified Vital Records\n \n"+
                        "You must apply in person or by mail for certified copies of vital records, such as birth certificates, marriage licenses,or health records. Our office does not"+
                        "accept applications by telephone , fax , or through our Web site.You must provide the following information with your request:\n \n"+
                        "- Name\n"+
                        "- Address\n"+
                        "- Type of record requested\n"+
                        "- Passport, government ID card, or other photo identification (Photocopies of these documents are acceptable)\n \n"+
                        "The charge is $15.00 per copy issued. We accept checks, money orders, and credit cards\n \n"+
                        "In Person:\n"+
                        "You can apply in person at 5 East Amber Street, Room 115. The Vital Records Office is open 8:00 A .M. to 4:30 P.M. Monday to Wednesday ; 8:00 A.M. to 5:30 P.M.Thursday ; and "+
                        "8:00 A.M. to 4:00 P.M. on Friday.\n \n"+
                        "By Mail:\n"+
                        "Applications for copies of vital records should be mailed to the following address: Vital Records, P.O. Box 349, Madison, WI 53702. Please include an addressed, stamped, "+
                        "business-size envelope with your request. Copies are normally mailed within fourteen business days. If you require faster service, you may request our expedited service. "+
                        "Expedited requests are normally processed within 24 hours and require an additional charge of $10 per request, for a total of $25 per copy.\n"+
                        "Requests for expedited service should be mailed to the following address: Vital Records-Rush Service, P.O. Box 567, Madison, WI 53702.\n \n \n \n"+
                        "2235 Meyer Way\n"+
                        "Appleton, Wisconsin 52206 \n \n"+
                        "Dear Sir or Madam: \n \n"+
                        "This letter is to request a certified copy of a marriage license issued last month in Milwaukee County to John Allen Heinrich and Elizabeth Ann Miller. I have enclosed a check for $25, "+
                        "as well as a copy of my passport.Please mail the certificate to John Heinrich, 2235 Meyer Way, Appleton, WI 52206.\n \n"+
                        "Thank you.\n \n"+
                        "Yours sincerely,\n"+
                        "John Heinrich"); // 5
        objReading.addValuesToReadingScriptTest("7",
                "Cezanta's Move\n \n"+
                        "Atlanta (United News Service)-Atlanta-based Cezanta Air, the nation's number four airline, announced today that it will cut its domestic airfares significantly. The company is hoping "+
                        "that by offering its new promotional fare plan, the carrier will be able to increase its customer base and win a greater share of the market. The company promises to charge no more than "+
                        "$300 for a one-way economy ticket or $500 for a one-way first-class ticket. The new plan also includes the reduction of various ticketing fees and the elimination of several less-traveled "+
                        "routes at the end of this year. Cezanta hopes that these changes will raise revenue over the long term. The company is also planning to update its airplanes' interiors and redesign "+
                        "flight attendants' uniforms.\n \n \n \n"+
                        "Buford Valley Daily News\n"+
                        "WILL NEW AIRLINE \n"+
                        "STRATEGY FLY? \n"+
                        "by Ken Daly\n \n"+
                        "Faced with weak revenues, rising labor costs, aggressive pricing from small, discount competitors, and most notably high fuel costs, several major air carriers have struggled mightily "+
                        "in the past three years.They have experimented with a wide variety of marketing strategies without much success.Recently,Cezanta cut its ticket prices by nearly 50 percent. However, "+
                        "it is unlikely that this strategy will result in a major increase in profits for the airline, as most of the other large airlines will probably cut their prices to follow suit. Some analysts are "+
                        "actually expecting Cezanta's revenues to fall in the upcoming year.With fuel prices expected to continue rising and with the cost of fuel accounting for nearly 40 percent of all carriers' "+
                        "operating expenses, any increase in passenger traffic is unlikely to offset the decrease in ticket prices.The one sure thing is that, after December, consumers flying out of Buford Valley "+
                        "will no longer be able to take advantage of Cezanta's price cuts."); // 5
        objReading.addValuesToReadingScriptTest("8",
                "Customer Service Department \n "+
                        "Mid-City Savings Bank \n "+
                        "115 Main Street \n "+
                        "San Diego, California  92122 \n \n"+
                        "April 14 \n \n"+
                        "Dear Sir or Madam: \n \n"+
                        "I am writing to ask why my bank account was charged a $30 service fee on April 1. In accordance with the terms of my account, I always maintain a balance of at least"+
                        "$500. This is the first time that I have been charged a fee in the five years that I have had the account. Could you please explain the additional charges to me? \n \n"+
                        "Sincerely,\n \n"+
                        "Susan Young\n \n \n"+
                        "Mid-City Savings Bank \n "+
                        "115 Main Street\n "+
                        "San Diego, California 92122\n \n"+
                        "April 15 \n \n"+
                        "Dear Ms.Young:\n \n"+
                        "Thank you for your inquiry. I will be happy to explain the reason for the service charge. As of April 1, the minimum balance requirement was changed to $1000. All accounts with balances "+
                        "below $1000 were automatically charged a service fee.We announced this change in a letter to all our customers in early February and again in early March.\n \n"+
                        "Because you are a long-time customer of Mid-City Savings Bank, I am going to waive the fee on your account this month and issue a credit for the amount you were charged. In order to "+
                        "avoid future charges, though, please remember to maintain the required minimum balance each month.\n \n"+
                        "Please let me know if you require further assistance.\n \n"+
                        "Sincerely,\n \n"+
                        "Mark Aubrey\n"+
                        "Assistant Vice President \n"+
                        "Customer  Relations");
        objReading.addValuesToReadingScriptTest("9",
                "Seeking: Assistant Controller\n \n"+
                        "- Large downtown law firm is seeking an Assistant Controller for our Accounting Department.\n"+
                        "- Basic responsibilities include control of the accounting systems, supervision of a seven-person team, and assisting with the hiring and training of new employees.\n"+
                        "- Qualified applicant should have eight years of accounting experience, as well as a minimum of two to three years in a supervisory position. Experience working in a law firm is desirable.\n"+
                        "- Education requirements  include an undergraduate degree in accounting. CPA is preferred.\n"+
                        "- The successful candidate will have the necessary computer skills and be familiar with the most current automated financial systems.\n"+
                        "  To apply for this position, send a resume and three letters of reference to:\n"+
                        "  Annabelle  Smythe\n"+
                        "  Forbes, Lawrence, and Ross \n"+
                        "  187 Oakland Boulevard\n"+
                        "  Detroit, Ml 41084\n"+
                        "  Closing date: November 12");
        objReading.addValuesToReadingScriptTest("10",
                "Dear Customer,\n \n"+
                        "Congratulations! You have just purchased one of the world's most sophisticated microwave ovens. This appliance has been designed with your convenience in mind. It combines an array of special features with "+
                        "ease of use. State-of-the-art features include a temperature sensor so that you will never again have an overcooked or undercooked meal; a 24-hour timer so that you can prepare your food when you have time "+
                        "and have it ready to eat when you are; an automated defrosting system so that you can prepare frozen food with no extra waiting time; a programmable chime system to let you know when your food is ready; "+
                        "and an automatic self-cleaning system so tha t your oven is always fresh and ready for use.\n \n"+
                        "All of these features and more are available to you at just the push of a button. It is so simple to use. Each feature is completely explained in this manual. Just follow the step-by-step instructions and you will be "+
                        "cooking delicious meals in no time at all! In addition, recipes for various entrees and desserts are included at the back of the manual to get you started on your new adventures in microwave cooking. This "+
                        "product has been designed to give you many years of trouble-free operation as long as the instructions are followed. If for some reason the product should fail, it is completely guaranteed for one year. A "+
                        "complete explanation of the warranty is included  on page 15 of the manual. Additional instructions and recipes are available on our website.\n \n"+
                        "Thank you again for becoming a Kitchen Appliances customer.\n \n"+
                        "Sincerely,\n \n"+
                        "M.S. Fujimoto\n"+
                        "President\n"+
                        "Kitchen Appliances, Inc.");
        objReading.addValuesToReadingScriptTest("11",
                "More than 50,000 electronics retailers and distributors are expected at the McCormick Convention Center in Chicago starting next Saturday. Some l,300 manufacturers from more than 35 countries will exhibit 1·heir latest high-technology "+
                        "equipment, including industrial equipment, office machines, and household appliances. The new products won't appear on retailers'shelves until next fall, but show attendees will be able to purchase them during the show at special prices.\n \n"+
                        "Highlights of the show include the following: \n \n"+
                        "- Demonstrations of robots designed for household use. Watch robots perform everyday household chores. Each day, models from a different group of manufacturers will be shown. Hall of Industry, 3:00-5:00 P.M. daily.\n"+
                        "- Talks by product developers representing companies from various countries on topics such as The Impact of Electronic Technology on Business, Future Developments in Technology, How Electronic Technology Will Solve Our Transportation Problems, and "+
                        "more. Call the Convention Center or visit our website for the speaker list. Wilson Auditorium, 7 :00 P.M. nightly.\n"+
                        "- Musical equipment demonstrations. Show attendees will be able to try out the latest synthesizers, guitars, and other electronic musical equipment. Exhibit Hall A, ongoing.\n"+
                        "- Inventors of Tomorrow, a special hands-on workshop for children ages l0-13. Free with the price of admission to the show, but due to space limitations, pre-registration is required. Call the Convention Center or visit our website to register. Saturday and "+
                        "Sunday, 2:00 P.M.\n \n"+
                        "Visit the Convention Center website for a complete schedule of demonstrations, workshops, and special events going on throughout the show.\n \n"+
                        "Tickets are available by calling the Convention Center or through the Convention Center website. Special prices ore available for multi-day passes.\n \n"+
                        "Contact us by phone: 800-555-0913 or on the web \n \n"+
                        "The Summer Consumer Electronics Show will continue through June 5.");
        objReading.addValuesToReadingScriptTest("12",
                "Programming for Sunday, March 26\n \n"+
                        "           11:30 A.M.\n"+
                        "Ch 4       Business Review\n"+
                        "               A review of this week's business news. This week's special guest is international business analyst Marilyn Kim of the McGuire Institute.\n \n"+
                        "           1:00 P.M.\n"+
                        "Ch 9, 11   Company Profiles\n"+
                        "               An in-depth look at significant companies around the world. Featured this week are Limnex, Inc., and Asian Global Industries, two newcomers to the international finance scene.\n \n"+
                        "           1:30 P.M.\n"+
                        "Ch 4       Up Front with Politics and Economics\n"+
                        "               Discussion of the latest political decisions affecting business and finance. Host Richard Lee interviews political analysts and finance experts.\n \n"+
                        "           2:00 P.M.\n"+
                        "Ch 7, 13   Business Today\n"+
                        "               Recent innovations in business. This week we visit with Tina and Luis Gomez, who will share how they built their small family clothing business into an international company.\n \n"+
                        "           3:00 P.M.\n"+
                        "Ch 4       World View of Business\n"+
                        "               News on business around the world, with commentaries by Masafumi Sachimoto and Jacques Deleon.\n \n"+
                        "           4:00 P.M.\n"+
                        "Ch 20      Making Money\n"+
                        "               Successful personal investing. This week's topic: \"How to Take Advantage of the Real Estate Market.\" Plus, tips for financing your child's college education.");
        objReading.addValuesToReadingScriptTest("13",
                "International Films, Ltd.\n"+
                        "124 West Houston St., New York, NY 10012\n \n"+
                        "July 30, 20\n \n"+
                        "E. Denikos, Inc. \n"+
                        "Earns 42\n"+
                        "Aghia Paraskevi 15342\n"+
                        "Athens, Greece\n \n"+
                        "Dear Mr. Denikos:\n \n"+
                        "I am writing to you at the request of Ms. Evangelia Makestos, who is applying for a position as an assistant in your company.\n \n"+
                        "Ms. Makestos worked for me as an assistant during her summer vacations for the past three years. My colleagues and I found her to be a very competent and reliable employee. Her duties consisted of typing and copying documents, maintaining files, "+
                        "organizing appointment schedules, assisting visitors to the office, and other office tasks as they  arose. She was able to handle multiple tasks and to work independently. She "+
                        "always assisted our clients in a knowledgeable, professional, and patient manner. In addition, she developed a high level of ability in the English language during the time "+
                        "she worked and studied in this country. We had hoped to rehire her at our company in a permanent position when she finished her business course here in New York. However, "+
                        "she has decided to go through with her original plan of returning to Greece.\n \n"+
                        "We will miss Ms. Makestos here at International Films, but I am happy to recommend her as a valuable addition to your company staff. Please feel free to contact me at the "+
                        "above address if you have any questions or need further information.\n \n"+
                        "Sincerely,\n \n"+
                        "Elizabeth Hogan, Director \n"+
                        "International Films, Ltd.");
        objReading.addValuesToReadingScriptTest("14",
                "TWO TYPES OF TRAINING\n \n"+
                        "There are two common forms of employee training-on-the-job training and off-the-job training. On-the-job training is the most widely used and least expensive form of training. It consists of an employee "+
                        "learning from a supervisor or co-worker how to do the job. On-the-job training could be described as an apprenticeship. It is efficient because it is done at the workplace while the employee is fulfilling work duties. "+
                        "As time goes by, the employee becomes more and more skilled at the job and eventually can train other employees in turn.\n \n"+
                        "Off-the-job training is the most expensive form of training. It consists of an employee being sent away from the workplace to a training program where training is provided. It is less efficient because it requires the "+
                        "employee to take time away from work duties. In addition, depending on where the training site is located, travel and accommodation expenses may be incurred. And of course, fees must be paid to the "+
                        "person or organization providing the training.\n \n"+
                        "When deciding which form of training to provide, an employer must consider such things as the availability of staff with necessary skills and time to provide on-the-job training and the types of off-site training "+
                        "available, in addition to the expense. It may well be decided that off-the-job training is worth the cost. While the requirements are different for on-the-job training as compared to off-the-job training, the purpose "+
                        "of both types is the same-to improve employee efficiency and productivity.");
        objReading.addValuesToReadingScriptTest("15",
                "From:      Hussein Gitai\n"+
                        "To:        Olga Montgomery\n"+
                        "Subject:   Errands for Monday\n\n"+
                        "Olga,\n \n"+
                        "I am out sick today, so there are several errands I'11 need you to do for me. I have outlined them below. Please call me at home if this message is not clear.\n \n"+
                        "There are several things to deliver to other floors in the library. All of these items are on my desk, and they must be delivered today. Take the DVDs to Marjorie. At the same time, you can take the black umbrella to the Lost and "+
                        "Found since it's on the same floor. Also, there are some books in Arabic. They go to Level 2. Deliver the biography on Anwar Sadat to Level 3.\n \n"+
                        "I was scheduled to give two presentations today. The first one is a workshop in Room C. Please put a sign on the door saying, \"Today 's workshop is canceled.\" I am also scheduled to read a book to the children. Please go to "+
                        "Children's Services and let Adishree know that I can 't do it.\n \n"+
                        "Because you are a new employee, I have attached a copy of the library directory to help you find your way around. Thank you again. I hope to recover quickly and see you at work tomorrow.\n \n"+
                        "Hussein\n \n \n"+
                        "Directory\n \n"+
                        "Audiovisual (DVDs, Videos)           Level 1\n"+
                        "Biography                            Level 3\n"+
                        "Children's Services                  Level 5\n"+
                        "Fiction                              Level 3\n"+
                        "Information Desk                     Level 1\n"+
                        "International Languages              Level 3\n"+
                        "Lost and Found Items                 Level 1\n"+
                        "Music Research Collections           Level 6\n"+
                        "Political Science                    Level 2\n"+
                        "Research Collection, A-M             Level 6\n"+
                        "Research Collection, N-Z             Level 6\n"+
                        "Security Desk                        Level 1\n"+
                        "Telephones                           Level 1\n"+
                        "Workshop Rooms                       Level 4");
        objReading.addValuesToReadingScriptTest("16",
                "From:      Christina van Dijk\n"+
                        "To:        Heinz Niebaum\n"+
                        "Subject:   Meeting next week\n \n"+
                        "Dear Heinz,\n \n"+
                        "I am coming to Germany next Wednesday at 10:00 A.M. I will be at our office in Berlin. Iam free on Thursday at 9:00 A.M. to go to Potsdam and meet with you. Are you available then?\n \n"+
                        "I'd like to talk about ordering computer systems from your company. Our offices in Utrecht and Tillburg are expanding and need to upgrade their technology. I'd also like to meet with a trainer "+
                        "from your company so that we can work out a training package. Also, could you bring a training manual with you?\n \n"+
                        "Let me know if you need me to bring anything. Also let me know if 9:00 isn't good for you. Maybe we can work something out later in the day.\n \n"+
                        "Christina\n \n \n"+
                        "From:      Heinz Niebaum\n"+
                        "To:        Christina van Dijk\n"+
                        "Subject:   Re: Meeting next week\n \n"+
                        "Christina,\n \n"+
                        "I'm delighted that you're coming to Germany. It will be very convenient for us to meet because I will actually be in Potsdam from Tuesday through Friday, so we'll be able to meet at our office there "+
                        "on the day you suggested. The time you suggested is a bit early for me as I will be meeting with our CEO all morning. Are you available to meet at noon? Perhaps we could meet during lunch.\n \n"+
                        "I will bring the materials that you requested. Theodor Eckert, our training supervisor, will join us. It would be helpful if you could bring some of your company's brochures and a copy of the annual "+
                        "report for us. Thank you.\n \n"+
                        "Heinz");

        // Question
        objReading.addValuesToReadingQuestionTest("1", "What does Ms.Jacobsen ask Ms.Evans to do?", "A", "File information about a customer", "Provide a customer with a price quote", "Contact a customer about a new policy", "Compose an e-mail message to a customer", "1");
        objReading.addValuesToReadingQuestionTest("2", "What information about the car is NOT given?", "C", "Its color", "Its license plate number", "Its price", "Its purchase date", "1");
        objReading.addValuesToReadingQuestionTest("3", "Who was the first owner of the car?", "D", "Rina Evans", "Maria Jacobsen", "David Williams", "John Weldon", "1");
        objReading.addValuesToReadingQuestionTest("4", "What is the purpose of the letter?", "A", "To make a complaint", "To inquire about a discount", "To request repair services", "To place an order", "2");
        objReading.addValuesToReadingQuestionTest("5", "What can NOT be inferred from the letter?", "C", "Selvac model 12 is a poor-quality product.", "Martha Simmons expects a quick response.", "Martha Simmons used to work for Selvac.", "Selvac advertises its products on television.", "2");
        objReading.addValuesToReadingQuestionTest("6", "What is enclosed with the letter?", "D", "A product order form", "A copy of the warranty", "A return envelope", "A proof of purchase", "2");
        objReading.addValuesToReadingQuestionTest("7", "For whom is the instruction intended?", "C", "People who want to have their photographs taken", "Professional photographers who are preparing exhibits", "People who want to learn about photography", "Models who are trying to expand their portfolios", "3");
        objReading.addValuesToReadingQuestionTest("8", "How many scenes are included in a fashion session?", "B", "1", "4", "6", "10", "3");
        objReading.addValuesToReadingQuestionTest("9", "What is NOT mentioned as a topic in the advertising session?", "D", "Product presentation", "Design principles", "Model placement", "Camera selection", "3");
        objReading.addValuesToReadingQuestionTest("10", "What is true about the city council?", "B", "It has voted to approve the tunnel project.", "It has been working with Yanco Builders.", "It plans to build more shops and restaurants.", "It has met with the leaders of RTS.", "4");
        objReading.addValuesToReadingQuestionTest("11", "What did Mr.Young say about the tunnel project?", "D", "It will be completed later than expected.", "It will create more jobs in the suburbs.", "It will replace the existing bridge.", "It will be good for the downtown economy.", "4");
        objReading.addValuesToReadingQuestionTest("12", "Why has RTS asked that the project be delayed?", "C", "It wants citizens to vote on the project.", "It wants to extend the tunnel to a neighboring town.", "It wants more studies to be done.", "It wants a different company to build the tunnel.", "4");
        objReading.addValuesToReadingQuestionTest("13", "What method of reducing the use of plastic bags is NOT mentioned?", "D", "Introducing extra charges for plastic bags", "Providing customers with other kinds of bags", "Offering a discount for not using plastic bags", "Publicizing the harmful effects of using plastic bags", "5");
        objReading.addValuesToReadingQuestionTest("14", "According to the article, why are paper bags not a good alternative?", "C", "It is difficult to recycle them.", "They contain harmful substances.", "It costs a lot to produce them.", "(D)\tThey are generally of low quality.", "5");
        objReading.addValuesToReadingQuestionTest("15", "What is the disadvantage of many supermarkets'cloth bags?", "B", "Their durability", "Their appearance", "Their cost", "Their size", "5");
        objReading.addValuesToReadingQuestionTest("16", "What is the main purpose of the notice?", "C", "To announce a recent price increase", "To provide directions to an office", "To explain how to obtain certain official records", "To describe a new government facility", "6");
        objReading.addValuesToReadingQuestionTest("17", "What information must accompany each request?", "A", "Identification that includes a photograph", "Two copies of recent tax returns", "The applicant's telephone number", "A letter written on business stationery", "6");
        objReading.addValuesToReadingQuestionTest("18", "On what day does the Vital Records Office close at 5:30?", "D", "Monday", "Tuesday", "Wednesday", "Thursday", "6");
        objReading.addValuesToReadingQuestionTest("19", "What is suggested about the airline industry?", "B", "Operating costs have recently gone down.", "Smaller airlines often offer very low  prices.", "Ticketing fees will be eliminated.", "Airlines have increased the amount spent on advertising.", "7");
        objReading.addValuesToReadingQuestionTest("20", "What does Cezanta Air plan to do?", "D", "Reduce the number of its international flights", "Expand its service on less-traveled routes", "Eliminate business-class and first-class seating", "Modernize the inside of its aircraft", "7");
        objReading.addValuesToReadingQuestionTest("21", "What does Mr. Daly suggest?", "B", "Other airlines will try to match Cezanta's prices.", "Smaller airlines will be bought by their larger competitors.", "Cezanta's revenues will increase right away.", "Passengers' opinions influence aircraft design.", "7");
        objReading.addValuesToReadingQuestionTest("22", "Why did Ms.Young write to the bank?", "C", "To inquire about an unexplained deposit", "To close an account", "To ask about a fee", "To transfer $500 into an account", "8");
        objReading.addValuesToReadingQuestionTest("23", "In the first letter, the word \"terms\" in paragraph 1, line 2, is closest in meaning to", "A", "conditions", "expressions", "agreements", "periods", "8");
        objReading.addValuesToReadingQuestionTest("24", "How much will be credited to Ms. Young's account?", "C", "$1000", "$500", "$30", "$15", "8");
        objReading.addValuesToReadingQuestionTest("25", "What kind of firm is hiring?", "D", "A computer company", "An accounting office", "An advertising agency", "A law firm", "9");
        objReading.addValuesToReadingQuestionTest("26", "Which of the following is NOT mentioned as a qualification?", "C", "Experience as a supervisor", "Familiarity with automated financial systems", "A law degree", "A degree in accounting", "9");
        objReading.addValuesToReadingQuestionTest("27", "What kind of applicant would be most attracted to this job?", "B", "A lawyer", "An accountant", "A computer science major", "A director of human resources ", "9");
        objReading.addValuesToReadingQuestionTest("28", "Where would this Jetter most likely be found?", "A", "In a microwave manual", "In the mail", "In an advertisement", "In a design store", "10");
        objReading.addValuesToReadingQuestionTest("29", "The word \"sophisticated\" in line 2 is closest in meaning to", "B", "popular", "advanced", "dependable", "well-known", "10");
        objReading.addValuesToReadingQuestionTest("30", "What must the user do for trouble-free operation?", "B", "Exchange the product", "Follow instructions", "Purchase another model", "Redesign the kitchen", "10");
        objReading.addValuesToReadingQuestionTest("31", "What is the main topic of the press release?", "D", "The McCormick Convention Center", "Chicago's convention centers", "Electronics retailers", "The Summer Consumer Electronics Show", "11");
        objReading.addValuesToReadingQuestionTest("32", "How many manufacturers are expected?", "A", "1,300", "5,000", "13,000", "50,000", "11");
        objReading.addValuesToReadingQuestionTest("33", "What is on display at the Convention Center?", "A", "High-technology products", "Distribution of networks", "Retail outlets", "Shelving samples", "11");
        objReading.addValuesToReadingQuestionTest("34", "What do these TV listings feature?", "B", "Concerts", "Business programs", "Travelogues", "Sports events", "12");
        objReading.addValuesToReadingQuestionTest("35", "What begins on TV at 2:00 P.M.?", "A", "Business Today", "Company Profiles", "Making Money", "Business Review", "12");
        objReading.addValuesToReadingQuestionTest("36", "Which station would someone who has money to invest watch?", "D", "Ch 4", "Ch 7", "Ch 11", "Ch 20", "12");
        objReading.addValuesToReadingQuestionTest("37", "What is Ms. Makestos probably doing?", "A", "Job hunting", "Quitting her job", "Moving to New York", "Applying to school", "13");
        objReading.addValuesToReadingQuestionTest("38", "The word \"competent\" in paragraph 2, line 2, is closest in meaning to", "C", "responsible", "friendly", "skilled", "useful", "13");
        objReading.addValuesToReadingQuestionTest("39", "How long did Ms. Makestos work at International Films?", "B", "One summer", "Three summers", "One year", "Three years", "13");
        objReading.addValuesToReadingQuestionTest("40", "Which of the following best describes on-the-job training?", "C", "Expensive", "Ineffective", "Common", "Quick", "14");
        objReading.addValuesToReadingQuestionTest("41", "What is on-the-job training similar to?", "A", "An apprenticeship", "Off-the-job training", "A supervisory position", "A company benefit", "14");
        objReading.addValuesToReadingQuestionTest("42", "According to the passage, what is the purpose of training?", "A", "To improve employee efficiency", "To spend excess capital", "To satisfy government requirements", "To please a supervisor", "14");
        objReading.addValuesToReadingQuestionTest("43", "Where does Marjorie work?", "A", "Level 1", "Level 2", "Level 3", "Level 4", "15");
        objReading.addValuesToReadingQuestionTest("44", "Why should Olga put a sign on a door?", "C", "To help children learn to read", "To tell library users that a workshop location has changed", "To inform people that Hussein can't give a presentation", "To let Adishree know that Hussein is out of the office today", "15");
        objReading.addValuesToReadingQuestionTest("45", "What does Hussein plan to do tomorrow?", "C", "Stay home", "Show Olga around the library", "Return to work", "Read a story to children", "15");
        objReading.addValuesToReadingQuestionTest("46", "What does Christina want to discuss at the meeting with Heinz?", "B", "Finding train schedules", "Buying computers", "Mailing packages", "Expanding office space", "16");
        objReading.addValuesToReadingQuestionTest("47", "What day will they meet?", "C", "Tuesday", "Wednesday", "Thursday", "Friday", "16");
        objReading.addValuesToReadingQuestionTest("48", "What will Heinz bring to the meeting with Christina?", "B", "Some computers", "A training manual", "Some brochures", "An annual report", "16");


    }


}
