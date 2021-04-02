package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    int currentRound;
    int playerIndex;
    int blueColor;
    int redColor;
    boolean running;
    boolean playerTurn;
    Button[] gameButtons;
    Drawable[] backgrounds;
    ArrayList<Integer> simonSequence = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        currentRound = 1;
        playerIndex = 0;
        blueColor = getResources().getColor(R.color.theme_blue);
        redColor = getResources().getColor(R.color.theme_red);
        running = true;
        playerTurn = false;
        gameButtons = new Button[]{
                findViewById(R.id.butOne), findViewById(R.id.butTwo), findViewById(R.id.butThree),
                findViewById(R.id.butFour), findViewById(R.id.butFive), findViewById(R.id.butSix),
                findViewById(R.id.butSeven), findViewById(R.id.butEight), findViewById(R.id.butNine)};
        backgrounds = new Drawable[]{
                getResources().getDrawable(R.drawable.title_bg),  getResources().getDrawable(R.drawable.game_bg),
                getResources().getDrawable(R.drawable.game_over_bg)};
        playGame();
    }

    @Override
    public void onBackPressed() {

    }

    public void toggleButtons() {
        if (playerTurn){
            for (int i = 0; i < 9; i++) {
                gameButtons[i].setEnabled(true);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                gameButtons[i].setEnabled(false);
            }
        }

    }

    public void playGame() {
        if (!running) {
            gameOver();
        } else if (!playerTurn) {
            addToSequence();
            new DisplaySequence().execute();
        } else if (playerTurn) {
            if (playerIndex == currentRound) {
                currentRound++;
                playerIndex = 0;
                playerTurn = false;
                toggleButtons();
                playGame();
            }
        }
    }

    public void gameButtonClick(View view) {
        Button buttonClicked = findViewById(view.getId());
        if (gameButtons[simonSequence.get(playerIndex)] == buttonClicked) {
            playerIndex++;
        } else {
            running = false;
        }
        playGame();
    }

    public void gameOver() {
        playerTurn = false;
        for (int i = 0; i < 9; i++) {
            gameButtons[i].setBackgroundColor(redColor);
        }
        toggleButtons();
        setActivityBackground(backgrounds[2]);
    }

    public void setActivityBackground(Drawable background) {
        findViewById(R.id.game).setBackground(background);
    }

    public void addToSequence() {
        Random rand = new Random();
        int newInt = rand.nextInt(8);
        simonSequence.add(newInt);
    }

    public class DisplaySequence extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected Double doInBackground(Integer... integers) {
            for (int i = 0; i < simonSequence.size(); i++) {
                SystemClock.sleep(500);
                publishProgress(i, Color.WHITE);
                SystemClock.sleep(500);
                publishProgress(i, blueColor);
            }
            return null;
        }

        protected void onProgressUpdate(Integer...params) {
            gameButtons[simonSequence.get(params[0])].setBackgroundColor(params[1]);
        }

        protected void onPostExecute(Double result) {
            playerTurn = true;
            toggleButtons();
            playGame();
        }
    }
}