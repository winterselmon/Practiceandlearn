package com.xyz.practiceandlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PracticeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    Intent intent = new Intent(PracticeListActivity.this, PhotosPracticeActivity.class);
                    startActivity(intent);
                }
                if (position == 1){
                    Intent intent = new Intent(PracticeListActivity.this, QandrPracticeActivity.class);
                    startActivity(intent);
                }
                if (position == 2){
                    Intent intent = new Intent(PracticeListActivity.this, ShortConversationActivity.class);
                    startActivity(intent);
                }
                if (position == 3){
                    Intent intent = new Intent(PracticeListActivity.this, ShortTalkActivity.class);
                    startActivity(intent);
                }
                if (position == 4){
                    Intent intent = new Intent(PracticeListActivity.this, IncompleteSentenceActivity.class);
                    startActivity(intent);
                }
                if (position == 5){
                    Intent intent = new Intent(PracticeListActivity.this, TextCompletionActivity.class);
                    startActivity(intent);
                }
                if (position == 6){
                    Intent intent = new Intent(PracticeListActivity.this, ReadingComprehensionActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView list1 = (ListView) findViewById(R.id.listPractice);
        list1.setOnItemClickListener(itemClickListener);

    }
}
