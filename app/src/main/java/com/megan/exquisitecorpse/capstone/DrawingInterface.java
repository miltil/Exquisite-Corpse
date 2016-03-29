package com.megan.exquisitecorpse.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class DrawingInterface extends AppCompatActivity {

    private TextView timerView;
    private ImageButton undoButton;
    private ImageButton black, red, pink, orange, yellow, green, teal, blue, light_blue,
            purple, brown, gray, white;
    private TextView titleText;
    private String currentSegment = "next segment!";
    private SeekBar lineWidthAdjust;
    private DrawableView drawableView;
    private DrawableViewConfig config = new DrawableViewConfig();
    private ImageView lineThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.drawing_interface);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        currentSegment = intent.getStringExtra("currentSegment");
        titleText = (TextView)findViewById(R.id.title_text);
        String headingText = this.getString(R.string.draw_the) + " " + currentSegment + "!";
        titleText.setText(headingText);

        configureDrawView();
        setColorButtons();

        undoButton = (ImageButton)findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawableView.undo();
            }
        });

        lineThickness = (ImageView) findViewById(R.id.lineThickness);
        lineThickness.setImageResource(R.drawable.linethickness);
        lineThickness.setScaleType(ImageView.ScaleType.FIT_XY);

        lineWidthAdjust = (SeekBar)findViewById(R.id.lineWidthAdjust);
        lineWidthAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 1;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                config.setStrokeWidth(progress);
            }
        });


        timerView = (TextView)findViewById(R.id.timer_view);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                int rawTime = (int)millisUntilFinished / 1000;
                if(rawTime >= 10){
                    timerView.setText(":" + rawTime);
                }
                else{
                    timerView.setText(":0" + rawTime);
                }
            }

            public void onFinish() {
                timerView.setText("Time's up!");
                Intent intent = new Intent(DrawingInterface.this, SheetOfPaper.class);
                intent.putExtra("firstFlag", false);
                startActivity(intent);
            }
        }.start();

    }

    public void setColorButtons(){

        black = (ImageButton)findViewById(R.id.black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 0, 0, 0));
            }
        });

        red = (ImageButton)findViewById(R.id.red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 255, 0, 0));
            }
        });

        pink = (ImageButton)findViewById(R.id.pink);
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 255, 153, 153));
            }
        });

        orange = (ImageButton)findViewById(R.id.orange);
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 255, 102, 0));
            }
        });

        yellow = (ImageButton)findViewById(R.id.yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 255, 214, 51));
            }
        });

        green = (ImageButton)findViewById(R.id.green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 0, 102, 0));
            }
        });

        teal = (ImageButton)findViewById(R.id.teal);
        teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 0, 204, 102));
            }
        });

        blue = (ImageButton)findViewById(R.id.blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 0, 51, 128));
            }
        });

        light_blue = (ImageButton)findViewById(R.id.light_blue);
        light_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 77, 136, 255));
            }
        });

        purple = (ImageButton)findViewById(R.id.purple);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 153, 51, 153));
            }
        });

        brown = (ImageButton)findViewById(R.id.brown);
        brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 153, 77, 0));
            }
        });

        gray = (ImageButton)findViewById(R.id.gray);
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 194, 194, 163));
            }
        });

        white = (ImageButton)findViewById(R.id.white);
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 255, 255, 255));
            }
        });


    }

    public void configureDrawView(){
        drawableView = (DrawableView)findViewById(R.id.paintView);
        config.setStrokeColor(Color.argb(255, 0, 0, 0));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(675);
        config.setCanvasWidth(900);
        drawableView.setConfig(config);

        drawableView.setBackgroundColor(getResources().getColor(R.color.white));
    }

}
