package com.megan.exquisitecorpse.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetOfPaper extends AppCompatActivity {

    boolean firstFlag;
    List<String> playerNames = new ArrayList<>();
    String playerNamesString;
    String segmentsString;
    List<String> segments = new ArrayList<>();
    StringBuilder nameBuilder = new StringBuilder();
    StringBuilder segmentBuilder = new StringBuilder();
    String newNamesString;
    String newSegmentsString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.paper);
        getSupportActionBar().hide();

        //Figure out if this class is being called the first time or a subsequent time
        //If it's the first time, the current player name and segment are correct
        //Otherwise, we should remove the first element of both
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getPreferences(MODE_PRIVATE);
        Intent intent = getIntent();
        firstFlag = intent.getBooleanExtra("firstFlag", false);

        //Creating the passing prompt
        PassingPrompt prompt = new PassingPrompt();
        prompt.show(getSupportFragmentManager(), "HI");
    }

}
