package com.example.janikaka.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by janikaka on 2.8.2016.
 */
public class PlayGameActivity extends Activity implements OnClickListener {
    private Random random;
    private TextView questionText, answerText, scoreText, cheatText;
    
    private ImageView response;
    private int level;
    private int[] operators;
    private int skip;
    private boolean highScore;
    private boolean howTo;
    private Dataitem gameplayDataitem;

    private String username;

    private int leftToSkip;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, enterBtn, clearBtn, quitBtn, skipBtn;

    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2, DIVIDE_OPERATOR = 3,
            MODULUS_OPERATOR = 4, POWER_OPERATOR = 5;
    private int operator = 0, operand1 = 0, operand2 = 0;
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        random = new Random();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            level = extras.getInt("level");
            operators = extras.getIntArray("operators");
            skip = extras.getInt("skip");
            highScore = extras.getBoolean("highScore");
            howTo = extras.getBoolean("howTo");
            username = extras.getString("username");
        }
        questionText = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);
        scoreText = (TextView) findViewById(R.id.score);
        cheatText = (TextView) findViewById(R.id.cheat);
        response = (ImageView) findViewById(R.id.response);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);
        quitBtn = (Button) findViewById(R.id.quit);
        skipBtn = (Button) findViewById(R.id.skip);

        if(skip < 0) {
            skipBtn.setVisibility(View.INVISIBLE);
        } else {
            skipBtn.setOnClickListener(this);
            leftToSkip = skip;
        }

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        setNewGameplay();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.quit) {
            setHighScore();
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.putExtra("level", level); //1 2 3 4 5
            homeIntent.putExtra("operators", operators); //+ - * / % ^
            homeIntent.putExtra("highScore", highScore); //true false
            homeIntent.putExtra("howTo", howTo); //true false
            homeIntent.putExtra("skip", skip); //-1, 0, 1, ...
            this.startActivity(homeIntent);
        }else if (view.getId() == R.id.skip && leftToSkip <= 0) {
            skipQuestion();
        }else if(view.getId()==R.id.enter){
            boolean answerCorrect = checkAnswer();
            if (answerCorrect) {
                response.setImageResource(R.drawable.tick);
                int oldScore = getScore();
                scoreText.setText("" + (oldScore + 1));
                setQuestion();
                leftToSkip--;
                if(leftToSkip == 0 && !skipBtn.isClickable()) {
                    skipBtn.setClickable(true);
                    skipBtn.setBackgroundResource(R.drawable.skip_on);
                }
                answerText.setText("?");
            } else {
                sendGameplayDataitem();
                setNewGameplay();
                response.setImageResource(R.drawable.cross);
                answerText.setText("?");
                setHighScore();
            }
        } else if(view.getId() == R.id.clear) {
            answerText.setText("?");
        } else {
            int enteredNum = Integer.parseInt(view.getTag().toString());
            if(answerText.getText().toString().endsWith("?")) {
                answerText.setText("" + enteredNum);
            } else {
                answerText.append("" + enteredNum);
            }
        }
    }

    private void skipQuestion() {
        leftToSkip = skip;
        skipBtn.setClickable(false);
        skipBtn.setBackgroundResource(R.drawable.skip_off);
        setQuestion();
    }

    private void setQuestion() {
        operator = operators[random.nextInt(operators.length)];
        char operatorMark = '+';
        int modifiedLevel = level;
        if (operator == 2 || operator == 3 || operator == 4) {
            modifiedLevel--;
            if(modifiedLevel == 0) {
                modifiedLevel = 1;
            }
        }
        operand1 = getOperand(modifiedLevel);
        operand2 = getOperand(modifiedLevel);
        if (operator == 0) {
            operatorMark = '+';
        } else if(operator == 1) {
            operatorMark = '-';
            while(operand1 < operand2) {
                operand1 = getOperand(modifiedLevel);
            }
        } else if(operator == 2) {
            operatorMark = '*';
        } else if(operator == 3) {
            operatorMark = '/';
            while(((double)operand1/operand2)%1 != 0) {
                operand1++;
            }
        } else if(operator == 4) {
            operatorMark = '%';
        } else if(operator == 5) {
            operatorMark = '^';
            operand2 = 2;
        }
        questionText.setText(operand1 + " " + operatorMark + " " + operand2 + " = ");
        cheatText.setText("" + getRealAnswer());
    }

    private int getOperand(int level) {
        int operand = 0;
        if (level == 1) {
            operand = random.nextInt(10) + 1;
        } else if (level == 2) {
            operand = random.nextInt(100) + 1;
        } else if (level == 3) {
            operand = random.nextInt(91) + 10;
        } else if (level == 4) {
            operand = random.nextInt(1000) + 1;
        } else if (level == 5) {
            operand = random.nextInt(901) + 100;
        }
        return operand;
    }

    private boolean checkAnswer() {
        int realAnswer = getRealAnswer();
        return realAnswer == Integer.parseInt(answerText.getText().toString());
    }

    private void setNewGameplay() {
        gameplayDataitem = new Dataitem("gameplay", username);
        gameplayDataitem.setValue(1);
        scoreText.setText("Score: 0");
        setQuestion();
    }

    private int getRealAnswer() {
        if (operator == 0) {
            return operand1 + operand2;
        } else if (operator == 1) {
            return operand1 - operand2;
        } else if(operator == 2) {
            return operand1 * operand2;
        } else if(operator == 3) {
            return operand1 / operand2;
        } else if(operator == 4) {
            return operand1 % operand2;
        } else if(operator == 5) {
            return operand1 * operand1;
        }
        return 0;
    }

    private int getScore(){
        String scoreStr = scoreText.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    private void setHighScore(){
        int exScore = getScore();
        if(exScore>0){
            //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            //get existing scores
            String scores = gamePrefs.getString("highScores", "");
            //check for scores
            if(scores.length()>0){
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();
                //split scores
                String[] exScores = scores.split("\\|");
                //add score object for each
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                //new score
                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);
                //sort
                Collections.sort(scoreStrings);
                //get top ten
                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;
                    if(s>0) scoreBuild.append("|");
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();

            }
            else{
                //no existing scores
                scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
                scoreEdit.commit();
            }
        }
    }

    //set high score if activity destroyed
    protected void onDestroy(){
        setHighScore();
        super.onDestroy();
    }

    //save instance state
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save score and level
        int exScore = getScore();
        savedInstanceState.putInt("score", exScore);
        savedInstanceState.putInt("level", level);
        //superclass method
        super.onSaveInstanceState(savedInstanceState);
    }

    private void sendGameplayDataitem() {
        gameplayDataitem.setEndDatetime();
        Dataitem dt = gameplayDataitem; //is this necessary because of async?
        String key = dt.getKey();
        int value = dt.getValue();
        String startDatetime = dt.getStartDatetime();
        String endDatetime = dt.getEndDatetime();
        String username = dt.getUsername();
        String url = "http://10.0.2.2:6543/events";

        String json = "{\"key\": \"" + key + "\", \"value\": " + value + ", \"startDatetime\": \"" + startDatetime + "\", \"endDatetime\": \"" + endDatetime + "\"}";
        Log.v("JSON", json);
        OkHttpClient client = new OkHttpClient();
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .header("username", username)
                .post(jsonBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                String data = response.body().string();
                Log.v("Dataitem response", data);
            }
        });

    }
}