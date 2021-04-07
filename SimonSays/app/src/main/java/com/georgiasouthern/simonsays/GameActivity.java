package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


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
    ImageButton playAgain;
    ImageButton mainMenu;
    Button[] gameButtons;
    Drawable[] backgrounds;
    MediaPlayer[] sounds;
    ArrayList<Integer> simonSequence = new ArrayList<>();
    DBHelper helper = new DBHelper(this);

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
        playAgain = findViewById(R.id.playAgainButton);
        mainMenu = findViewById(R.id.mainMenuButton);
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
                MediaPlayer.create(this, R.raw.button_nine), MediaPlayer.create(this, R.raw.round_complete),
                MediaPlayer.create(this, R.raw.game_over)};
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
            sounds[10].start();
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
        roundDisplay.setGravity(Gravity.CENTER);
        roundDisplay.setText("Score: " + currentRound);
        playAgain.setVisibility(ImageButton.VISIBLE);
        mainMenu.setVisibility(ImageButton.VISIBLE);
        updateScoresDatabase();
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

    public void playClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void menuClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateHighScore() {
        SQLiteDatabase dbWrite = helper.getWritableDatabase();
        SQLiteDatabase dbRead = helper.getReadableDatabase();
        ContentValues highScoreRow = new ContentValues();
        highScoreRow.put("id", "highscore");
        highScoreRow.put("val", Integer.toString(currentRound));
        Cursor highScoreCursor = dbRead.rawQuery("select * from scorestable where id=?", new String[]{"highscore"});
        if (highScoreCursor.moveToFirst()) {
            if (Integer.parseInt(highScoreCursor.getString(1)) < currentRound) {
                dbWrite.update("scorestable", highScoreRow, "id=?", new String[]{"highscore"});
            }
        } else {
            dbWrite.insert("scorestable", null, highScoreRow);
        }
    }

    public void updateAverageScore() {
        System.out.println("trying average");
        int totalScore = currentRound;
        int totalRounds = 1;
        SQLiteDatabase dbWrite = helper.getWritableDatabase();
        SQLiteDatabase dbRead = helper.getReadableDatabase();
        ContentValues averageScoreRow = new ContentValues();
        averageScoreRow.put("id", "averagescore");
        Cursor averageScoreCursor = dbRead.rawQuery("select * from scorestable where id=?", new String[]{"averagescore"});
        if (averageScoreCursor.moveToFirst()) {
            totalScore += Integer.parseInt(averageScoreCursor.getString(1));
            totalRounds += Integer.parseInt(averageScoreCursor.getString(2));
            averageScoreRow.put("val", Integer.toString(totalScore));
            averageScoreRow.put("val2", Integer.toString(totalRounds));
            dbWrite.update("scorestable", averageScoreRow, "id=?", new String[]{"averagescore"});
            System.out.println("updated");
        } else {
            averageScoreRow.put("val", Integer.toString(totalScore));
            averageScoreRow.put("val2", Integer.toString(totalRounds));
            dbWrite.insert("scorestable", null, averageScoreRow);
            System.out.println("inserted");
        }
    }

    public void updateRecentScore() {
        SQLiteDatabase dbWrite = helper.getWritableDatabase();
        SQLiteDatabase dbRead = helper.getReadableDatabase();
        ContentValues lastScoreRow = new ContentValues();
        lastScoreRow.put("id", "lastscore");
        lastScoreRow.put("val", Integer.toString(currentRound));
        Cursor lastScoreCursor = dbRead.rawQuery("select * from scorestable where id=?", new String[]{"lastscore"});
        if (lastScoreCursor.moveToFirst()) {
            dbWrite.update("scorestable", lastScoreRow, "id=?", new String[]{"lastscore"});
        } else {
            dbWrite.insert("scorestable", null, lastScoreRow);
        }
    }

    public void updateScoresDatabase() {
//        SQLiteDatabase dbWrite = helper.getWritableDatabase();
//        SQLiteDatabase dbRead = helper.getReadableDatabase();
//        ContentValues highScoreRow = new ContentValues();
//        highScoreRow.put("id", "highscore");
//        highScoreRow.put("val", Integer.toString(currentRound));
//        ContentValues lastScoreRow = new ContentValues();
//        lastScoreRow.put("id", "lastscore");
//        lastScoreRow.put("val", Integer.toString(currentRound));
//        Cursor highScoreCursor = dbRead.rawQuery("select * from scorestable where id=?", new String[]{"highscore"});
//        Cursor lastScoreCursor = dbRead.rawQuery("select * from scorestable where id=?", new String[]{"lastscore"});
//        if (highScoreCursor.moveToFirst()) {
//            if (Integer.parseInt(highScoreCursor.getString(1)) < currentRound) {
//                dbWrite.update("scorestable", highScoreRow, "id=?", new String[]{"highscore"});
//            }
//        } else {
//            dbWrite.insert("scorestable", null, highScoreRow);
//        }
//        if (lastScoreCursor.moveToFirst()) {
//            dbWrite.update("scorestable", lastScoreRow, "id=?", new String[]{"lastscore"});
//        } else {
//            dbWrite.insert("scorestable", null, lastScoreRow);
//        }
        updateHighScore();
        updateRecentScore();
        updateAverageScore();
    }

    public class RoundComplete extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected Double doInBackground(Integer... integers) {
            SystemClock.sleep(200);
            sounds[9].start();
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