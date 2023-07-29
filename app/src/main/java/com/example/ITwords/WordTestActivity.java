package com.example.ITwords;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WordTestActivity extends AppCompatActivity {
    private TextView oneText, twoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);
        getSupportActionBar().hide();

    }

}
