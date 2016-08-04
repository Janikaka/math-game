package com.example.janikaka.mathgame;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.content.Intent;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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


    String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }
    public class Configuration {
        private int level;
        private ArrayList<Integer> operators;
        private boolean highScore;
        private boolean howTo;
        private int skip;

        public Configuration(int level, ArrayList<Integer> operators, boolean highScore, boolean howTo, int skip) {
            this.level = level;
            this.operators = operators;
            this.highScore = highScore;
            this.howTo = howTo;
            this.skip = skip;
        }

        public Configuration() {
            level = 1;
            operators = new ArrayList<Integer>();
            operators.add(0);
            highScore = true;
            howTo = true;
            skip = 1;
        }

        public int getLevel() {
            return level;
        }

        public ArrayList<Integer> getOperators() {
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
        public int[] getOperatorsAsIntArray() {
            int[] asArray = new int[operators.size()];
            for(int i = 0; i < operators.size(); i++) {
                asArray[i] = operators.get(i);
            }
            return asArray;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_button) {
            String username = usernameInput.getText().toString();
            boolean participates = participate_yes.isChecked();
            if(participates) {
                getConfiguration(username);
            } else {
                startHome(new Configuration());
            }
        }
    }

    private void startHome(Configuration configuration) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("level", configuration.getLevel());
        homeIntent.putExtra("operators", configuration.getOperatorsAsIntArray());
        homeIntent.putExtra("highScore", configuration.getHighScore());
        homeIntent.putExtra("howTo", configuration.getHowTo());
        homeIntent.putExtra("skip", configuration.getSkip());
        this.startActivity(homeIntent);
    }

    private void getConfiguration(String username) {
        int level;
        int[] operators;
        boolean highScore;
        boolean howTo;
        int skip;
        String url = "https://raw.githubusercontent.com/Janikaka/TestServer/master/README.md";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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
                try {
                    JSONObject Jobject = new JSONObject(data);
                    JSONArray JArray = Jobject.getJSONArray("data");
                    int level = 1;
                    ArrayList<Integer> operators = new ArrayList<Integer>();
                    boolean highScore = true;
                    boolean howTo = true;
                    int skip = 1;
                    for(int i = 0; i < JArray.length(); i++) {
                        JSONObject obj = JArray.getJSONObject(i);
                        String key = obj.getString("key");
                        if(key.matches("level")) {
                            level = obj.getInt("value");
                        } else if(key.matches("operators")) {
                            operators.add(obj.getInt("value"));
                        } else if(key.matches("highScore")) {
                            highScore = obj.getBoolean("value");
                        } else if(key.matches("howTo")) {
                            howTo = obj.getBoolean("value");
                        } else if(key.matches("skip")) {
                            skip = obj.getInt("value");
                        }
                    }
                    Configuration configuration = new Configuration(level, operators, highScore, howTo, skip);
                    startHome(configuration);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
