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

    }

    public class Configuration {
        private int level;
        private int[] operators;
        private boolean highScore;
        private boolean howTo;
        private int skip;

        public Configuration(int level, int[] operators, boolean highScore, boolean howTo, int skip) {
            level = level;
            operators = operators;
            highScore = highScore;
            howTo = howTo;
            skip = skip;
        }

        public Configuration() {
            level = 1;
            operators = new int[]{0, 1};
            highScore = true;
            howTo = true;
            skip = 1;
        }

        public int getLevel() {
            return level;
        }

        public int[] getOperators() {
            return operators;
        }

        public boolean getHighScore() {
            return highScore;
        }

        public boolean getHowTo() {
            return howTo;
        }

        public int getSkip() {
            return skip;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_button) {
            String username = usernameInput.getText().toString();
            boolean participates = participate_yes.isChecked();
            if(participates) {
                //getConfigurations
                Configuration configuration = getConfiguration(username);
                startHome(configuration);

            } else {
                startHome(new Configuration());
            }
        }
    }

    private void startHome(Configuration configuration) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("level", configuration.getLevel());
        homeIntent.putExtra("operators", configuration.getOperators());
        homeIntent.putExtra("highScore", configuration.getHighScore());
        homeIntent.putExtra("howTo", configuration.getHowTo());
        homeIntent.putExtra("skip", configuration.getSkip());
        this.startActivity(homeIntent);
    }

    private Configuration getConfiguration(String username) {
        int level;
        int[] operators;
        boolean highScore;
        boolean howTo;
        int skip;

        String url  = "http://127.0.0.1:6543/configurations";




        return new Configuration();
        //return new Configuration(level, operators, highScore, howTo, skip);
    }


}
