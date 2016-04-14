package com.megan.exquisitecorpse.capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

public class SheetOfPaper extends AppCompatActivity {

    private boolean lastFlag;
    private TextView beholdText;
    private ImageView headHolder;
    private ImageView upperTorsoHolder;
    private ImageView torsoHolder;
    private ImageView legHolder;
    private int numPlayers;
    private FloatingActionButton fab;
    private boolean saveFlag = false;
    private boolean deleteFlag = false;
    private Picture picture;
    private byte[] bytePicture;
    private Bitmap headBitmap;
    private Bitmap upperTorsoBitmap;
    private Bitmap torsoBitmap;
    private Bitmap legsBitmap;
    private Bitmap[] parts;
    private Bitmap fullBitmap;
    private byte[] fullByteArray;
    private GalleryPicture fullPicture;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.paper_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(deleteFlag == false && saveFlag == false) {
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                }
                else{
                    Intent homeIntent = new Intent(this, OpeningScreen.class);
                    startActivity(homeIntent);
                }
                return true;
            case R.id.action_save:
                if(deleteFlag == false && saveFlag == false) {
                    fullBitmap = combineParts(parts);
                    fullByteArray = Utility.getBytes(fullBitmap);
                    fullPicture = new GalleryPicture();
                    fullPicture.full_drawing = fullByteArray;
                    fullPicture.save();
                    Toast.makeText(this, "Your corpse was saved to the gallery", Toast.LENGTH_LONG).show();
                    saveFlag = true;
                    new Delete().from(Picture.class).execute();
                }
                else if(deleteFlag == true) {
                    Toast.makeText(this, "Sorry, your corpse was deleted!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_delete:
                if(deleteFlag == false && saveFlag == false) {
                    Toast.makeText(this, "Your corpse was deleted", Toast.LENGTH_LONG).show();
                    headHolder.setVisibility(View.GONE);
                    upperTorsoHolder.setVisibility(View.GONE);
                    torsoHolder.setVisibility(View.GONE);
                    legHolder.setVisibility(View.GONE);
                    new Delete().from(Picture.class).execute();
                    deleteFlag = true;
                }
                return true;
            case R.id.action_gallery:
                if (saveFlag == false && deleteFlag == false) {
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("gallery");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                } else {
                    Intent intent = new Intent(this, Gallery.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_settings:
                if (saveFlag == false && deleteFlag == false) {
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("settings");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                } else {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        numPlayers = preferences.getInt("numPlayers", 3);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paper);
        getSupportActionBar().hide();

        headHolder = (ImageView)findViewById(R.id.head);
        upperTorsoHolder = (ImageView)findViewById(R.id.uppertorso);
        torsoHolder = (ImageView)findViewById(R.id.torso);
        legHolder = (ImageView)findViewById(R.id.legs);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.hide();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 135, getResources().getDisplayMetrics());

        Intent intent = getIntent();
        lastFlag = intent.getBooleanExtra("lastFlag", false);

        if(!lastFlag) {
            //Creating the passing prompt, if the game is still going
            PassingPrompt prompt = new PassingPrompt();
            prompt.show(getSupportFragmentManager(), "HI");
        } else {
           // beholdText.setText("Behold your creation!");
            getSupportActionBar().show();
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle("Your corpse");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (saveFlag == false && deleteFlag == false) {
                        GameInterrupt gameInterrupt = GameInterrupt.newInstance("new");
                        gameInterrupt.show(getSupportFragmentManager(), "HI");
                    } else {
                        NoPlayerPicker playerPicker = new NoPlayerPicker();
                        playerPicker.show(getSupportFragmentManager(), "HI");
                    }
                }
            });


            if(numPlayers == 2 || numPlayers == 4) {
                headBitmap = getPic("head");
                headHolder.setImageBitmap(headBitmap);
                headHolder.getLayoutParams().height = height;
                upperTorsoBitmap = getPic("upper torso");
                upperTorsoHolder.setImageBitmap(upperTorsoBitmap);
                upperTorsoHolder.getLayoutParams().height = height;
                torsoBitmap = getPic("lower torso");
                torsoHolder.setImageBitmap(torsoBitmap);
                torsoHolder.getLayoutParams().height = height;
                legsBitmap = getPic("legs");
                legHolder.setImageBitmap(legsBitmap);
                legHolder.getLayoutParams().height = height;
                parts = new Bitmap[4];
                parts[0] = headBitmap;
                parts[1] = upperTorsoBitmap;
                parts[2] = torsoBitmap;
                parts[3] = legsBitmap;
            }

            else if(numPlayers == 3){
                headBitmap = getPic("head");
                headHolder.setImageBitmap(headBitmap);
                torsoBitmap = getPic("torso");
                torsoHolder.setImageBitmap(torsoBitmap);
                legsBitmap = getPic("legs");
                legHolder.setImageBitmap(legsBitmap);
                parts = new Bitmap[3];
                parts[0] = headBitmap;
                parts[1] = torsoBitmap;
                parts[2] = legsBitmap;
            }

            else if(numPlayers == 1){
                headBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.samplehead);
                headHolder.setImageBitmap(headBitmap);
                torsoBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.sampletorso);
                torsoHolder.setImageBitmap(torsoBitmap);
                legsBitmap = getPic("legs");
                legHolder.setImageBitmap(legsBitmap);
                parts = new Bitmap[3];
                parts[0] = headBitmap;
                parts[1] = torsoBitmap;
                parts[2] = legsBitmap;
            }
        }
    }

    public Bitmap getPic(String segment){
        picture = new Select()
                .from(Picture.class)
                .where("Segment = ?", segment)
                .orderBy("ID ASC")
                .executeSingle();
        bytePicture = picture.drawing;
        Bitmap bitmapPicture = Utility.getImage(bytePicture);
        return bitmapPicture;
    }

    @Override
    public void onBackPressed() {
        if (saveFlag == false && deleteFlag == false) {
            GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
            gameInterrupt.show(getSupportFragmentManager(), "HI");
        }
        else{
            Intent intent = new Intent(this, OpeningScreen.class);
            startActivity(intent);
        }
        return;
    }

    public Bitmap combineParts(Bitmap[] parts){
        int numPlayers = parts.length;
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth(), parts[0].getHeight() * parts.length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        /*for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        } */

        if(numPlayers == 3){
            canvas.drawBitmap(parts[0], 0, 0, null);
            canvas.drawBitmap(parts[1], 0, parts[0].getHeight(), null);
            canvas.drawBitmap(parts[2], 0, parts[1].getHeight()*2, null);
        }

        else{
            canvas.drawBitmap(parts[0], 0, 0, null);
            canvas.drawBitmap(parts[1], 0, parts[0].getHeight(), null);
            canvas.drawBitmap(parts[2], 0, parts[1].getHeight()*2, null);
            canvas.drawBitmap(parts[3], 0, parts[2].getHeight()*3, null);
        }

        return result;
    }

}