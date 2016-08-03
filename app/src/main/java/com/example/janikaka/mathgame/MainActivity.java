package com.example.janikaka.mathgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.content.Intent;
import android.widget.EditText;
import android.widget.RadioButton;


public class MainActivity extends Activity implements OnClickListener {
    private EditText usernameInput;
    private RadioButton participate_yes;
    //private RadioButton participate_no;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.username);
        participate_yes = (RadioButton) findViewById(R.id.participate_yes);
        //participate_no = (RadioButton) findViewById(R.id.participate_no);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);



        //RadioButton participateYes = (RadioButton) findViewById(R.id.participate_yes);
        //participates = participateYes.isChecked();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_button) {
            String username = usernameInput.getText().toString();
            boolean participates = participate_yes.isChecked();
            if(participates) {
                //getConfigurations
                String key = "level";
                int value = 2;
                startHome(key, value);
            } else {
                startHome("level", 1);
            }
            //startHome(username, participates);
        }
    }

    private void startHome(String key, int value) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("level", value); //1 2 3 4 5
        homeIntent.putExtra("operators", new int[]{3}); //+ - * / % ^
        homeIntent.putExtra("highScore", true); //true false
        homeIntent.putExtra("howTo", true); //true false
        homeIntent.putExtra("skip", 1); //-1, 0, 1, ...
        this.startActivity(homeIntent);
    }




}
