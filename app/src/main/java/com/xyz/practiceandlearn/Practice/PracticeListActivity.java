package com.xyz.practiceandlearn.Practice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xyz.practiceandlearn.Main.MainActivity;
import com.xyz.practiceandlearn.R;

public class PracticeListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    Intent intent = new Intent(PracticeListActivity.this, PhotosPracticeActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(PracticeListActivity.this, QandrPracticeActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(PracticeListActivity.this, ShortConversationActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(PracticeListActivity.this, ShortTalkActivity.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(PracticeListActivity.this, IncompleteSentenceActivity.class);
                    startActivity(intent);
                }
                if (position == 5) {
                    Intent intent = new Intent(PracticeListActivity.this, TextCompletionActivity.class);
                    startActivity(intent);
                }
                if (position == 6) {
                    Intent intent = new Intent(PracticeListActivity.this, ReadingComprehensionActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView list1 = (ListView) findViewById(R.id.listPractice);
        list1.setOnItemClickListener(itemClickListener);
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Stop the practice?")
                .setMessage("Are you sure you want to quit to practice?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PracticeListActivity.this, MainActivity.class);
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

