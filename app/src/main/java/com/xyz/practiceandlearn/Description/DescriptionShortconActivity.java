package com.xyz.practiceandlearn.Description;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xyz.practiceandlearn.Main.Global;
import com.xyz.practiceandlearn.Main.MainActivity;
import com.xyz.practiceandlearn.R;
import com.xyz.practiceandlearn.Test.ShortConTestActivity;

public class DescriptionShortconActivity extends AppCompatActivity {

    TextView txttime;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long time = 7200000;
            long millis = time-(System.currentTimeMillis() - Global.startTime) ;
            int seconds = (int) (millis / 1000);
            int minutes = (seconds % 3600) / 60;
            int hour = seconds / 3600;
            seconds = seconds % 60;

            txttime.setText(String.format("%d:%02d:%02d", hour, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_shortcon);

        Button btnnextpart3 = (Button) findViewById(R.id.btnNextpart3);
        btnnextpart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescriptionShortconActivity.this, ShortConTestActivity.class);
                startActivity(intent);
            }
        });

        txttime = (TextView) findViewById(R.id.txttimedesShortcon);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the test?")
                .setMessage("Are you sure you want to quit to test?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DescriptionShortconActivity.this, MainActivity.class);
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
}
