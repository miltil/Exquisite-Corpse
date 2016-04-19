package com.megan.exquisitecorpse.capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class PictureViewer extends AppCompatActivity {

    ImageView corpseHolder;
    GalleryPicture galleryPicture;
    FloatingActionButton fab;
    TextView artistHolder;
    List nameAssociationList;
    long picID;
    ArrayList<String> artistList;
    String artistListString = "This corpse brought to you by ";
    NameAssociation nameAssociation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picture_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                corpseHolder.setVisibility(View.INVISIBLE);
                artistHolder.setVisibility(View.INVISIBLE);
                galleryPicture.delete();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, Gallery.class);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.gallery_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.picture_viewer);

        corpseHolder = (ImageView)findViewById(R.id.corpse_holder);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        artistHolder = (TextView) findViewById(R.id.artists_holder);


        picID = getIntent().getLongExtra("picID", 1);
        galleryPicture = (new Select()
                .from(GalleryPicture.class)
                .where("Id = ?", picID)
                .executeSingle());
        byte[] bytePicture = galleryPicture.full_drawing;
        Bitmap bitmapPicture = Utility.getImage(bytePicture);

        corpseHolder.setImageBitmap(bitmapPicture);

        getArtists();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NoPlayerPicker playerPicker = new NoPlayerPicker();
                playerPicker.show(getSupportFragmentManager(), "HI");
            }
        });
    }

    public void getArtists(){
        nameAssociationList = new Select()
                .from(NameAssociation.class)
                .where("DrawingId = ?", picID)
                .orderBy("ID ASC")
                .execute();
        if(nameAssociationList != null && nameAssociationList.size() > 1){
            for(int i = 0; i < nameAssociationList.size() - 1; i++) {
                nameAssociation = (NameAssociation) nameAssociationList.get(i);
                artistListString = artistListString.concat(nameAssociation.artistName + ", ");
            }
            nameAssociation = (NameAssociation) nameAssociationList.get(nameAssociationList.size() - 1);
            artistListString = artistListString.concat("and " + nameAssociation.artistName);
            artistHolder.setText(artistListString);
        }
        else if(nameAssociationList.size() == 1){
            nameAssociation = (NameAssociation) nameAssociationList.get(0);
            artistListString = artistListString.concat(nameAssociation.artistName);
            artistHolder.setText(artistListString);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
        return;
    }

}
