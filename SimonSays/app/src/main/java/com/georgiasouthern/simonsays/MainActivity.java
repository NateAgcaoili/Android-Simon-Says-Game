package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int currentRound;
    boolean running;
    boolean playerTurn;
    Button[] gameButtons;
    ArrayList<Integer> simonSequence = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        currentRound = 1;
        running = true;
        playerTurn = false;
        gameButtons = new Button[]{
                findViewById(R.id.butOne), findViewById(R.id.butTwo), findViewById(R.id.butThree),
                findViewById(R.id.butFour), findViewById(R.id.butFive), findViewById(R.id.butSix),
                findViewById(R.id.butSeven), findViewById(R.id.butEight), findViewById(R.id.butNine)};
    }

    public void playClick(View view) {
//        Intent intent = new Intent(this, GameActivity.class);
//        startActivity(intent);
        openGameScreen();
    }

    public void showButtons() {
        for (int i = 0; i < 9; i++) {
            gameButtons[i].setVisibility(View.VISIBLE);
        }
    }

    public void openGameScreen() {
        showButtons();
        findViewById(R.id.playButton).setVisibility((View.GONE));
        findViewById(R.id.scoresButton).setVisibility((View.GONE));
        findViewById(R.id.aboutButton).setVisibility((View.GONE));
        setActivityBackground(getResources().getDrawable(R.drawable.game_bg));
        running = true;
    }

    public void playGame() {
        if (!running) {

        }
    }

    public void gameOver() {
        
    }

    public void setActivityBackground(Drawable background) {
        findViewById(R.id.constraint).setBackground(background);
    }

    public void addToSequence() {
        Random rand = new Random();
        int newInt = rand.nextInt(8);
        simonSequence.add(newInt);
    }

    public void displaySequence() throws InterruptedException {

    }

    public class GameAsyncTask extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected Double doInBackground(Integer... integers) {
            for (int i = 0; i < simonSequence.size(); i++) {
                publishProgress(i, Color.GREEN);
                SystemClock.sleep(5000);
                publishProgress(i, Color.BLUE);
            }

            return null;
        }

        protected void onProgressUpdate(Integer...params) {
            gameButtons[simonSequence.get(params[0])].setBackgroundColor(params[1]);
        }

        protected void onPostExecute(Double result) {

        }
    }
}