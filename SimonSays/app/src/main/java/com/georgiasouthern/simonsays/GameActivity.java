package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    int currentRound;
    int playerIndex;
    int blue;
    int red;
    int green;
    boolean running;
    boolean playerTurn;
    TextView roundDisplay;
    TextView message;
    Button[] gameButtons;
    Drawable[] backgrounds;
    MediaPlayer[] sounds;
    ArrayList<Integer> simonSequence = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        currentRound = 1;
        playerIndex = 0;
        blue = getResources().getColor(R.color.theme_blue);
        red = getResources().getColor(R.color.theme_red);
        green = getResources().getColor(R.color.theme_green);
        running = true;
        playerTurn = false;
        roundDisplay = findViewById(R.id.roundDisplay);
        message = findViewById(R.id.turnDisplay);
        gameButtons = new Button[]{
                findViewById(R.id.butOne), findViewById(R.id.butTwo), findViewById(R.id.butThree),
                findViewById(R.id.butFour), findViewById(R.id.butFive), findViewById(R.id.butSix),
                findViewById(R.id.butSeven), findViewById(R.id.butEight), findViewById(R.id.butNine)};
        backgrounds = new Drawable[]{
                getResources().getDrawable(R.drawable.game_bg),  getResources().getDrawable(R.drawable.game_over_bg),
                getResources().getDrawable(R.drawable.round_complete_bg)};
        sounds = new MediaPlayer[]{
                MediaPlayer.create(this, R.raw.button_one), MediaPlayer.create(this, R.raw.button_two),
                MediaPlayer.create(this, R.raw.button_three), MediaPlayer.create(this, R.raw.button_four),
                MediaPlayer.create(this, R.raw.button_five), MediaPlayer.create(this, R.raw.button_six),
                MediaPlayer.create(this, R.raw.button_seven), MediaPlayer.create(this, R.raw.button_eight),
                MediaPlayer.create(this, R.raw.button_nine), MediaPlayer.create(this, R.raw.game_over)};
        playGame();
    }

    @Override
    public void onBackPressed() {

    }

    public void toggleButtons() {
        if (playerTurn) {
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
            roundDisplay.setText("Round " + currentRound);
            new DisplaySequence().execute();
        } else if (playerTurn) {
            if (playerIndex == currentRound) {
                playerTurn = false;
                toggleButtons();
                playerIndex = 0;
                currentRound++;
                message.setText("Correct!");
                new RoundComplete().execute();
            }
        }
    }

    public void gameButtonClick(View view) {
        Button buttonClicked = findViewById(view.getId());
        if (gameButtons[simonSequence.get(playerIndex)] == buttonClicked) {
            sounds[simonSequence.get(playerIndex)].start();
            playerIndex++;
        } else {
            sounds[9].start();
            running = false;
        }
        playGame();
    }

    public void gameOver() {
        playerTurn = false;
        for (int i = 0; i < 9; i++) {
            gameButtons[i].setBackgroundColor(red);
        }
        message.setVisibility(View.INVISIBLE);
        roundDisplay.setVisibility(View.INVISIBLE);
        toggleButtons();
        setActivityBackground(backgrounds[1]);
    }

    public void setActivityBackground(Drawable background) {
        findViewById(R.id.game).setBackground(background);
    }

    public void addToSequence() {
        Random rand = new Random();
        int newInt = rand.nextInt(9);
        simonSequence.add(newInt);
    }

    public class RoundComplete extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected Double doInBackground(Integer... integers) {
            publishProgress(2, green);
            SystemClock.sleep(1000);
            publishProgress(0, blue);
            SystemClock.sleep(500);
            return null;
        }

        protected void onProgressUpdate(Integer...params) {
            for (int i = 0; i < 9; i++) {
                setActivityBackground(backgrounds[params[0]]);
                gameButtons[i].setBackgroundColor(params[1]);
            }
        }

        protected void onPostExecute(Double result) {
            message.setText("Simon's turn");
            playGame();
        }
    }

    public class DisplaySequence extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected Double doInBackground(Integer... integers) {
            addToSequence();
            for (int i = 0; i < simonSequence.size(); i++) {
                SystemClock.sleep(500);
                publishProgress(i, Color.WHITE);
                SystemClock.sleep(500);
                publishProgress(i, blue);
            }
            return null;
        }

        protected void onProgressUpdate(Integer...params) {
            sounds[simonSequence.get(params[0])].start();
            gameButtons[simonSequence.get(params[0])].setBackgroundColor(params[1]);
        }

        protected void onPostExecute(Double result) {
            playerTurn = true;
            message.setText("Your turn");
            toggleButtons();
            playGame();
        }
    }
}