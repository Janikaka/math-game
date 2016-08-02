package com.example.janikaka.mathgame;

import android.os.Bundle;
import android.app.Activity;
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
    private TextView question;
    private TextView answerText;
    private ImageView response;
    private int level;
    private int[] operators;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, enterBtn, clearBtn, quitBtn;

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
        }
        question = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);
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
        if(view.getId()==R.id.quit) {

        }else if(view.getId()==R.id.enter){
            boolean answerCorrect = checkAnswer();
            if (answerCorrect) {
                response.setImageResource(R.drawable.tick);
            } else {
                response.setImageResource(R.drawable.cross);
            }

        } else if(view.getId() == R.id.clear) {
            answerText.setText("?");
        } else {
            int enteredNum = Integer.parseInt(view.getTag().toString());
            if(answerText.getText().toString().endsWith("?")) {
                answerText.setText(enteredNum);
            } else {
                answerText.append("" + enteredNum);
            }
        }
    }

    private void setQuestion() {
        operator = operators[random.nextInt(operators.length)];
        char operatorMark = '+';
        if (operator == 0) {
            operatorMark = '+';
        } else if(operator == 1) {
            operatorMark = '-';
        }
        if(level == 1) {
            operand1 = random.nextInt(10) + 1;
            operand2 = random.nextInt(10) + 1;
        } else if (level == 2) {
            operand1 = random.nextInt(100) + 1;
            operand2 = random.nextInt(100) + 1;
        }
        question.setText(operand1 + " " + operatorMark + " " + operand2 + " = ");
    }

    private boolean checkAnswer() {
        String givenAnswer = answerText.getText().toString();
        if (operator == 0) {
            int realAnswer = operand1 + operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        } else if (operator == 1) {
            int realAnswer = operand1 - operand2;
            return Integer.parseInt(givenAnswer) == realAnswer;
        }
        return false;
    }
}
