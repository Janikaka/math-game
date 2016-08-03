package com.example.janikaka.mathgame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by janikaka on 3.8.2016.
 */
public class HighScoresActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high);

        //get text view
        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        //get shared prefs
        SharedPreferences scorePrefs = getSharedPreferences(PlayGameActivity.GAME_PREFS, 0);
        //get scores
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        //build string
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        //display scores
        scoreView.setText(scoreBuild.toString());
    }
}
