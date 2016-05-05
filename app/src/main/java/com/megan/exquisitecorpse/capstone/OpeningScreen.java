package com.megan.exquisitecorpse.capstone;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.DialogFragment;
import android.widget.ImageButton;

public class OpeningScreen extends AppCompatActivity {

    private Button startGameButton;
    private ImageButton settingsButton;
    private ImageButton galleryButton;
    private ImageButton aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opening_screen);
        getSupportActionBar().hide();

  //      ((MyApplication) getApplication()).startTracking();

        startGameButton = (Button)findViewById(R.id.newgame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoPlayerPicker playerPicker = new NoPlayerPicker();
                playerPicker.show(getSupportFragmentManager(), "HI");
            }
        });

        settingsButton = (ImageButton)findViewById(R.id.settingsbutton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpeningScreen.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        aboutButton = (ImageButton)findViewById(R.id.aboutbutton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpeningScreen.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        galleryButton = (ImageButton)findViewById(R.id.gallerybutton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpeningScreen.this, Gallery.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        return;
    }
}
