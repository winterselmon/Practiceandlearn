package com.xyz.practiceandlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        sumScore();

        Button btnhome = (Button) findViewById(R.id.btnhome);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sumScore(){
        int score = 0;
        for ( int i = 0; i < 200; i++){
            if (Global.collect[i])
                score++;
        }
        String strScore = Integer.toString(score);
        TextView txtScore = (TextView) findViewById(R.id.txtshowScore);
        txtScore.setText(strScore);

    }
}
