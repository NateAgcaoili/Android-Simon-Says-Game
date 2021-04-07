package com.georgiasouthern.simonsays;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class ScoresActivity extends AppCompatActivity {

    DBHelper helper = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_scores);

        TextView highScoreDisplay = findViewById(R.id.highScoreDisplay);
        TextView averageScoreDisplay = findViewById(R.id.averageScoreDisplay);
        TextView recentScoreDisplay = findViewById(R.id.recentScoreDisplay);
        SQLiteDatabase scores = helper.getReadableDatabase();
        Cursor highScoreCursor = scores.rawQuery("select * from scorestable where id=?", new String[]{"highscore"});
        Cursor averageScoreCursor = scores.rawQuery("select * from scorestable where id=?", new String[]{"averagescore"});
        Cursor recentScoreCursor = scores.rawQuery("select * from scorestable where id=?", new String[]{"lastscore"});
        if (highScoreCursor.moveToFirst()) {
            highScoreDisplay.setText(highScoreCursor.getString(1));
        } else {
            highScoreDisplay.setText("N/A");
        }
        if (averageScoreCursor.moveToFirst()) {
            averageScoreDisplay.setText(Integer.toString(
                    Integer.parseInt(averageScoreCursor.getString(1)) /
                    Integer.parseInt(averageScoreCursor.getString(2))));
        } else {
            averageScoreDisplay.setText("N/A");
        }
        if (recentScoreCursor.moveToFirst()) {
            recentScoreDisplay.setText(recentScoreCursor.getString(1));
        } else {
            recentScoreDisplay.setText("N/A");
        }

    }
}