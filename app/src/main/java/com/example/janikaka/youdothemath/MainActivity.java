package com.example.janikaka.youdothemath;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {
    private Button playBtn, helpBtn, highBtn;
    private String[] levelNames = {"Easy", "Medium", "Hard"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playBtn = (Button) findViewById(R.id.play_btn);
        helpBtn = (Button) findViewById(R.id.help_btn);
        highBtn = (Button) findViewById(R.id.high_btn);

        playBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        highBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.play_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose a level")
                    .setSingleChoiceItems(levelNames, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startPlay(which);
                        }
                    });
            AlertDialog ad = builder.create();
            ad.show();
        } else if(view.getId()==R.id.help_btn) {

        } else if(view.getId()==R.id.high_btn) {

        }
    }

    private void startPlay(int chosenLevel) {
        Intent playIntent = new Intent(this, PlayGame.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }
}
