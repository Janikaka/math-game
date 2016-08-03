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
import java.util.Random;

/**
 * Created by janikaka on 2.8.2016.
 */
public class PlayGameActivity extends Activity implements OnClickListener {
    private Random random;
    private TextView questionText;
    private TextView answerText;
    private TextView scoreText;
    
    private ImageView response;
    private int level;
    private int[] operators;
    private int skip;
    private int leftToSkip;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, enterBtn, clearBtn, quitBtn, skipBtn;

    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2, DIVIDE_OPERATOR = 3,
            MODULUS_OPERATOR = 4, POWER_OPERATOR = 5;
    private int operator = 0, operand1 = 0, operand2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        random = new Random();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            level = extras.getInt("level");
            operators = extras.getIntArray("operators");
            skip = extras.getInt("skip");
        }
        questionText = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);
        scoreText = (TextView) findViewById(R.id.score);
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

        setQuestion();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.quit) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.putExtra("level", level); //1 2 3 4 5
            homeIntent.putExtra("operators", operators); //+ - * / % ^
            homeIntent.putExtra("highScore", true); //true false
            homeIntent.putExtra("howTo", true); //true false
            homeIntent.putExtra("skip", skip); //-1, 0, 1, ...
            this.startActivity(homeIntent);
        }else if (view.getId() == R.id.skip && leftToSkip <= 0) {
            skipQuestion();
        }else if(view.getId()==R.id.enter){
            boolean answerCorrect = checkAnswer();
            if (answerCorrect) {
                response.setImageResource(R.drawable.tick);
                String scoreStr = scoreText.getText().toString();
                int oldScore = Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
                scoreText.setText("" + (oldScore + 1));
                setQuestion();
                leftToSkip--;
                if(leftToSkip == 0 && !skipBtn.isClickable()) {
                    skipBtn.setClickable(true);
                    skipBtn.setBackgroundResource(R.drawable.skip_on);
                }
                answerText.setText("?");
            } else {
                response.setImageResource(R.drawable.cross);
                answerText.setText("?");
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
        operand1 = getOperand(level);
        operand2 = getOperand(level);
        if (operator == 0) {
            operatorMark = '+';
        } else if(operator == 1) {
            operatorMark = '-';
            while(operand1 < operand2) {
                operand1 = getOperand(level);
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
        String givenAnswer = answerText.getText().toString();
        if (operator == 0) {
            int realAnswer = operand1 + operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if (operator == 1) {
            int realAnswer = operand1 - operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if(operator == 2) {
            int realAnswer = operand1 * operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if(operator == 3) {
            int realAnswer = operand1 / operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if(operator == 4) {
            int realAnswer = operand1 % operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if(operator == 5) {
            int realAnswer = operand1 * operand1;
            return Integer.parseInt(givenAnswer) == realAnswer;
        }
        return true;
    }
}
