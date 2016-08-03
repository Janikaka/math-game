package com.example.janikaka.mathgame;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by janikaka on 2.8.2016.
 */
public class HomeActivity extends Activity implements OnClickListener {
    private Button playBtn, howToBtn, highBtn;
    private int level;
    private int[] operators;
    private int skip;
    private boolean highScore;
    private boolean howTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        playBtn = (Button)findViewById(R.id.play_btn);
        howToBtn = (Button)findViewById(R.id.help_btn);
        highBtn = (Button)findViewById(R.id.high_btn);

        playBtn.setOnClickListener(this);



        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            level = extras.getInt("level");
            operators = extras.getIntArray("operators");
            skip = extras.getInt("skip");
            highScore = extras.getBoolean("highScore");
            howTo = extras.getBoolean("howTo");
        }
        if(highScore) {
            highBtn.setOnClickListener(this);
        } else {
            highBtn.setVisibility(View.INVISIBLE);
        }
        if(howTo) {
            howToBtn.setOnClickListener(this);
        } else {
            howToBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.play_btn){
            startGame();
        }
        else if(view.getId()==R.id.help_btn){
            showHowToPlay();
        }
        else if(view.getId()==R.id.high_btn){
            showHighScores();
        }
    }

    private void startGame() {
        Intent playIntent = new Intent(this, PlayGameActivity.class);
        playIntent.putExtra("level", level);
        playIntent.putExtra("operators", operators);
        playIntent.putExtra("skip", skip);
        this.startActivity(playIntent);
    }

    private void showHighScores() {
        Intent highScoresIntent = new Intent(this, HighScoresActivity.class);
        this.startActivity(highScoresIntent);
    }

    private void showHowToPlay() {
        Intent howToIntent = new Intent(this, HowToPlayActivity.class);
        this.startActivity(howToIntent);
    }
}









