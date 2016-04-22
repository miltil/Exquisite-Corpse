package com.megan.exquisitecorpse.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class DrawingInterface extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private TextView timerView;
    private Button undoButton;
    private Button doneButton;
    private ImageButton black, red, pink, orange, yellow, green, teal, blue, light_blue,
            purple, lavender, brown, gray, white;
    private TextView titleText;
    private String currentSegment = "next segment!";
    private String currentPlayer;
    private SeekBar lineWidthAdjust;
    private DrawableView drawableView;
    private DrawableViewConfig config = new DrawableViewConfig();
    private ImageView lineThickness;
    private boolean lastFlag = false;
    private CountDownTimer timer;
    private boolean zoomable = true;
    private String timeLimitString;
    private int timeLimit;
    private boolean timerFlag = true;
    private Bitmap bitmapImage;
    private byte[] byteArrayImage;
    private Picture picture;
    private int rawTime;
    private int pausedTime;
    private String polishedTime;
    static final String TIMER_REMAINING = "timerRemaining";
    static final String CURRENT_COLOR = "currentColor";
    static final String CURRENT_WIDTH = "currentWidth";
    private int timerRemaining = 30000;
    private int currentColor;
    private float currentWidth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.draw_interface_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!timerFlag) {
            menu.removeItem(R.id.action_pause);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pause:
                if(timerFlag) {
                    pausedTime = rawTime;
                    timer.cancel();
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                }
                return true;
            case R.id.action_stop:
                if(timerFlag) {
                    timer.cancel();
                }
                Intent intent = new Intent(this, OpeningScreen.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                timer.cancel();
                GameInterrupt interrupt = GameInterrupt.newInstance("settings");
                interrupt.show(getSupportFragmentManager(), "HI");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawing_interface);

        Intent intent = getIntent();
        currentSegment = intent.getStringExtra("currentSegment");
        currentPlayer = intent.getStringExtra("currentPlayer");
        lastFlag = intent.getBooleanExtra("lastFlag", false);
        String headingText = this.getString(R.string.draw_the) + " " + currentSegment + "!";
        getSupportActionBar().setTitle(headingText);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        zoomable = preferences.getBoolean("zoom", true);
        timeLimitString = preferences.getString("timer", "30");
        timeLimit = Integer.parseInt(timeLimitString) * 1000;

        configureDrawView();
        setColorButtons();

        undoButton = (Button)findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawableView.undo();
            }
        });

        doneButton = (Button)findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerFlag) {
                    timer.cancel();
                }
                bitmapImage = drawableView.obtainBitmap();
                byteArrayImage = Utility.getBytes(bitmapImage);
                picture = new Picture();
                picture.artist = currentPlayer;
                picture.segment = currentSegment;
                picture.drawing = byteArrayImage;
                picture.save();

                Intent intent = new Intent(DrawingInterface.this, SheetOfPaper.class);
                intent.putExtra("lastFlag", lastFlag);
                startActivity(intent);
            }
        });

        lineThickness = (ImageView) findViewById(R.id.lineThickness);

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

        if (timeLimit != 0 && savedInstanceState == null) {
            setTimer(timeLimit);
        }

        else if(timeLimit != 0 && savedInstanceState != null){
            timerFlag = true;
        }

        else{
            timerView.setVisibility(View.GONE);
            timerFlag = false;
        }


    }

    @Override
    public void onBackPressed() {
        if(timerFlag) {
            pausedTime = rawTime;
            timer.cancel();
        }
        GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
        gameInterrupt.show(getSupportFragmentManager(), "HI");
        return;
    }

    public void setTimer(int timeLimit){
        timer = new CountDownTimer(timeLimit, 1000) {
            public void onTick(long millisUntilFinished) {
                rawTime = (int)millisUntilFinished;
                if(rawTime >= 60000) {
                    polishedTime = String.format("%d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(rawTime),
                            TimeUnit.MILLISECONDS.toSeconds(rawTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(rawTime))
                    );
                }
                else {
                    polishedTime = String.format(":%02d",
                            TimeUnit.MILLISECONDS.toSeconds(rawTime)
                    );
                }
                if(rawTime > 6000){
                    timerView.setText(polishedTime);
                }
                else{
                    timerView.setTextColor(getResources().getColor(R.color.red));
                    timerView.setText(polishedTime);
                }
            }

            public void onFinish() {
                timerView.setText("Time's up!");

                bitmapImage = drawableView.obtainBitmap();
                byteArrayImage = Utility.getBytes(bitmapImage);
                picture = new Picture();
                picture.artist = currentPlayer;
                picture.segment = currentSegment;
                picture.drawing = byteArrayImage;
                picture.save();

                Intent intent = new Intent(DrawingInterface.this, SheetOfPaper.class);
                intent.putExtra("lastFlag", lastFlag);
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

        lavender = (ImageButton)findViewById(R.id.lavender);
        lavender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStrokeColor(Color.argb(255, 209, 26, 255));
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
                config.setStrokeColor(Color.argb(255, 192, 192, 192));
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
        config.setCanvasHeight(675);
        config.setCanvasWidth(900);

        if(zoomable) {
            config.setMaxZoom(3.0f);
        }
        else {
            config.setMaxZoom(1.0f);
        }

        drawableView.setConfig(config);

        drawableView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        if(timerFlag) {
            setTimer(pausedTime);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        if(timerFlag) {
            timerRemaining = rawTime;
            timer.cancel();
            savedInstanceState.putInt(TIMER_REMAINING, timerRemaining);
        }
        currentColor = config.getStrokeColor();
        savedInstanceState.putInt(CURRENT_COLOR, currentColor);
        currentWidth = config.getStrokeWidth();
        savedInstanceState.putFloat(CURRENT_WIDTH, currentWidth);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(timerFlag) {
            timerRemaining = savedInstanceState.getInt(TIMER_REMAINING);
            setTimer(timerRemaining);
        }
        currentColor = savedInstanceState.getInt(CURRENT_COLOR);
        config.setStrokeColor(currentColor);
        currentWidth = savedInstanceState.getFloat(CURRENT_WIDTH);
        config.setStrokeWidth(currentWidth);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
