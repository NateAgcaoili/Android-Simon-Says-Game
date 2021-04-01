package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    int currentRound;
    boolean running;
    boolean playerTurn;
    Button[] buttons;
    ArrayList<Integer> simonSequence = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        currentRound = 1;
        running = true;
        playerTurn = false;
        buttons = new Button[]{
                findViewById(R.id.butOne), findViewById(R.id.butTwo), findViewById(R.id.butThree),
                findViewById(R.id.butFour), findViewById(R.id.butFive), findViewById(R.id.butSix),
                findViewById(R.id.butSeven), findViewById(R.id.butEight), findViewById(R.id.butNine)};
    }

    @Override
    protected void onStart() {
        super.onStart();
        addToSequence();
        showButtons();
    }

    @Override
    public void onBackPressed() {

    }

    public void addToSequence() {
        Random rand = new Random();
        int newInt = rand.nextInt(8);
        simonSequence.add(newInt);
    }

    public void displaySequence() throws InterruptedException {
        for (int i = 0; i < simonSequence.size(); i++) {
            buttons[simonSequence.get(i)].setBackgroundColor(Color.GREEN);
            buttons[simonSequence.get(i)].setBackgroundColor(Color.BLUE);
        }
    }

    public void playGame(boolean isPlayersTurn) {
        if (!running) {

        }
    }

    public void showButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setVisibility(View.VISIBLE);
        }
    }
}