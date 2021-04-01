package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button[] gameButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        gameButtons = new Button[]{
                findViewById(R.id.butOne), findViewById(R.id.butTwo), findViewById(R.id.butThree),
                findViewById(R.id.butFour), findViewById(R.id.butFive), findViewById(R.id.butSix),
                findViewById(R.id.butSeven), findViewById(R.id.butEight), findViewById(R.id.butNine)};
    }

    public void playClick(View view) {
//        Intent intent = new Intent(this, GameActivity.class);
//        startActivity(intent);
        startGame();
    }
    public void showButtons() {
        for (int i = 0; i < 9; i++) {
            gameButtons[i].setVisibility(View.VISIBLE);
        }
    }

    public void startGame() {
        showButtons();
        findViewById(R.id.playButton).setVisibility((View.GONE));
        findViewById(R.id.scoresButton).setVisibility((View.GONE));
        findViewById(R.id.aboutButton).setVisibility((View.GONE));
        setActivityBackground(getResources().getDrawable(R.drawable.game_bg));
    }

    public void setActivityBackground(Drawable background) {
        findViewById(R.id.constraint).setBackground(background);
    }
}