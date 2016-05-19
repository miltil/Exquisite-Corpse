package com.megan.exquisitecorpse.capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SheetOfPaper extends AppCompatActivity {

    private ImageView headHolder;
    private ImageView upperTorsoHolder;
    private ImageView torsoHolder;
    private ImageView legHolder;
    private boolean saveFlag = false;
    private boolean deleteFlag = false;
    private Bitmap headBitmap;
    private Bitmap upperTorsoBitmap;
    private Bitmap torsoBitmap;
    private Bitmap legsBitmap;
    private Bitmap[] parts;
    private List<String> playerNames = null;

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
                if(!deleteFlag && !saveFlag) {
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                }
                else{
                    Intent homeIntent = new Intent(this, OpeningScreen.class);
                    startActivity(homeIntent);
                }
                return true;
            case R.id.action_save:
                if(!deleteFlag && !saveFlag) {
                    SaveTask saveTask = new SaveTask();
                    saveTask.execute();
                    Toast.makeText(SheetOfPaper.this, "Your corpse was saved to the gallery", Toast.LENGTH_LONG).show();
                }
                else if(deleteFlag) {
                    Toast.makeText(this, "Sorry, your corpse was deleted!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_delete:
                if(!deleteFlag && !saveFlag) {
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
                if (!saveFlag && !deleteFlag) {
                    GameInterrupt gameInterrupt = GameInterrupt.newInstance("gallery");
                    gameInterrupt.show(getSupportFragmentManager(), "HI");
                } else {
                    Intent intent = new Intent(this, Gallery.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_settings:
                if (!saveFlag && !deleteFlag) {
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
        int numPlayers = preferences.getInt("numPlayers", 3);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paper);
        getSupportActionBar().hide();

        headHolder = (ImageView)findViewById(R.id.head);
        upperTorsoHolder = (ImageView)findViewById(R.id.uppertorso);
        torsoHolder = (ImageView)findViewById(R.id.torso);
        legHolder = (ImageView)findViewById(R.id.legs);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());

        Intent intent = getIntent();
        boolean lastFlag = intent.getBooleanExtra("lastFlag", false);

        if(!lastFlag) {
            //Creating the passing prompt, if the game is still going
            PassingPrompt prompt = new PassingPrompt();
            prompt.show(getSupportFragmentManager(), "HI");
        } else {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Your corpse");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!saveFlag && !deleteFlag) {
                        GameInterrupt gameInterrupt = GameInterrupt.newInstance("new");
                        gameInterrupt.show(getSupportFragmentManager(), "HI");
                    } else {
                        NoPlayerPicker playerPicker = new NoPlayerPicker();
                        playerPicker.show(getSupportFragmentManager(), "HI");
                    }
                }
            });

            String playerNamesString = preferences.getString("playerNameAssociations", "");
            if(!playerNamesString.equals("")){
                playerNames = new LinkedList<>(Arrays.asList(playerNamesString.split(",")));
            }

            if(numPlayers == 2 || numPlayers == 4) {
                headBitmap = getPic("head");
                headHolder.setImageBitmap(headBitmap);
                headHolder.getLayoutParams().height = height;
                headHolder.getLayoutParams().width = width;
                upperTorsoBitmap = getPic("upper torso");
                upperTorsoHolder.setImageBitmap(upperTorsoBitmap);
                upperTorsoHolder.getLayoutParams().height = height;
                upperTorsoHolder.getLayoutParams().width = width;
                torsoBitmap = getPic("lower torso");
                torsoHolder.setImageBitmap(torsoBitmap);
                torsoHolder.getLayoutParams().height = height;
                torsoHolder.getLayoutParams().width = width;
                legsBitmap = getPic("legs");
                legHolder.setImageBitmap(legsBitmap);
                legHolder.getLayoutParams().height = height;
                legHolder.getLayoutParams().width = width;
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
                String onePlayer = intent.getStringExtra("onePlayer");
                final Random rndBody = new Random();

                if(onePlayer != null && onePlayer.equals("legs")) {
                    final String headFile = "head_" + rndBody.nextInt(4);
                    int headId = getResourceId(headFile, "drawable", getPackageName());
                    headBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            headId);
                    headHolder.setImageBitmap(headBitmap);

                    final String torsoFile = "torso_" + rndBody.nextInt(4);
                    int torsoId = getResourceId(torsoFile, "drawable", getPackageName());
                    torsoBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            torsoId);
                    torsoHolder.setImageBitmap(torsoBitmap);

                    legsBitmap = getPic("legs");
                    legHolder.setImageBitmap(legsBitmap);
                } else if(onePlayer != null && onePlayer.equals("torso")){
                    final String headFile = "head_" + rndBody.nextInt(4);
                    int headId = getResourceId(headFile, "drawable", getPackageName());
                    headBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            headId);
                    headHolder.setImageBitmap(headBitmap);

                    torsoBitmap = getPic("torso");
                    torsoHolder.setImageBitmap(torsoBitmap);

                    final String legFile = "legs_" + rndBody.nextInt(4);
                    int legId = getResourceId(legFile, "drawable", getPackageName());
                    legsBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            legId);
                    legHolder.setImageBitmap(legsBitmap);
                } else if(onePlayer != null && onePlayer.equals("head")){
                    headBitmap = getPic("head");
                    headHolder.setImageBitmap(headBitmap);

                    final String torsoFile = "torso_" + rndBody.nextInt(4);
                    int torsoId = getResourceId(torsoFile, "drawable", getPackageName());
                    torsoBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            torsoId);
                    torsoHolder.setImageBitmap(torsoBitmap);

                    final String legFile = "legs_" + rndBody.nextInt(4);
                    int legId = getResourceId(legFile, "drawable", getPackageName());
                    legsBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            legId);
                    legHolder.setImageBitmap(legsBitmap);
                } else {
                    final String headFile = "head_" + rndBody.nextInt(4);
                    int headId = getResourceId(headFile, "drawable", getPackageName());
                    headBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            headId);
                    headHolder.setImageBitmap(headBitmap);

                    final String torsoFile = "torso_" + rndBody.nextInt(4);
                    int torsoId = getResourceId(torsoFile, "drawable", getPackageName());
                    torsoBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            torsoId);
                    torsoHolder.setImageBitmap(torsoBitmap);

                    final String legFile = "legs_" + rndBody.nextInt(4);
                    int legId = getResourceId(legFile, "drawable", getPackageName());
                    legsBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            legId);
                    legHolder.setImageBitmap(legsBitmap);
                }

                parts = new Bitmap[3];
                parts[0] = headBitmap;
                parts[1] = torsoBitmap;
                parts[2] = legsBitmap;
            }
        }
    }

    public Bitmap getPic(String segment){
        Picture picture = new Select()
                .from(Picture.class)
                .where("Segment = ?", segment)
                .orderBy("ID DESC")
                .executeSingle();
        byte[] bytePicture = picture.drawing;
        return Utility.getImage(bytePicture);
    }

    @Override
    public void onBackPressed() {
        if (!saveFlag && !deleteFlag) {
            GameInterrupt gameInterrupt = GameInterrupt.newInstance("home");
            gameInterrupt.show(getSupportFragmentManager(), "HI");
        }
        else{
            Intent intent = new Intent(this, OpeningScreen.class);
            startActivity(intent);
        }
    }

    public Bitmap combineParts(Bitmap[] parts){
        int numPlayers = parts.length;
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth(), parts[0].getHeight() * parts.length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        if(numPlayers == 3){
            canvas.drawBitmap(parts[0], 0, 0, null);
            canvas.drawBitmap(parts[1], 0, parts[0].getHeight(), null);
            canvas.drawBitmap(parts[2], 0, parts[1].getHeight() * 2, null);
        }

        else{
            canvas.drawBitmap(parts[0], 0, 0, null);
            canvas.drawBitmap(parts[1], 0, parts[0].getHeight(), null);
            canvas.drawBitmap(parts[2], 0, parts[1].getHeight()*2, null);
            canvas.drawBitmap(parts[3], 0, parts[2].getHeight() * 3, null);
        }

        return result;
    }

    public void associateNames(long pictureId){
        if(playerNames != null) {
            for (int i = 0; i < playerNames.size(); i++) {
                NameAssociation nameAssociation = new NameAssociation();
                nameAssociation.artistName = playerNames.get(i);
                nameAssociation.drawingId = pictureId;
                nameAssociation.save();
            }
        }
    }

    private class SaveTask extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground (Void... params){
            Bitmap fullBitmap = combineParts(parts);
            byte[] fullByteArray = Utility.getBytes(fullBitmap);
            GalleryPicture fullPicture = new GalleryPicture();
            fullPicture.full_drawing = fullByteArray;
            fullPicture.save();
            long pictureId = fullPicture.getId();
            associateNames(pictureId);
            saveFlag = true;
            new Delete().from(Picture.class).execute();
            return true;
        }

    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}