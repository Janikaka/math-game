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
    private Button playBtn, helpBtn, highBtn;
    private int level;
    private int[] operators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        playBtn = (Button)findViewById(R.id.play_btn);
        helpBtn = (Button)findViewById(R.id.help_btn);
        highBtn = (Button)findViewById(R.id.high_btn);

        playBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        highBtn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            level = extras.getInt("level");
            operators = extras.getIntArray("operators");
        }


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.play_btn){
            startGame();
        }
        else if(view.getId()==R.id.help_btn){
            //how to play button
        }
        else if(view.getId()==R.id.high_btn){
            //high scores button
        }
    }

    private void startGame() {
        Intent playIntent = new Intent(this, PlayGameActivity.class);
        playIntent.putExtra("level", level);
        playIntent.putExtra("operators", operators);
        this.startActivity(playIntent);
    }
}









