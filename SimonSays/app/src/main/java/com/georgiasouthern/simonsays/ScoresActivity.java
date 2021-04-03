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
        setContentView(R.layout.activity_scores);

        TextView highScoreDisplay = findViewById(R.id.highScoreDisplay);
        SQLiteDatabase scores = helper.getReadableDatabase();
        Cursor highScoreCursor = scores.rawQuery("select * from scorestable where id=?", new String[]{"highscore"});
        if (highScoreCursor.moveToFirst()) {
            highScoreDisplay.setText(highScoreCursor.getString(1));
        }

    }
}