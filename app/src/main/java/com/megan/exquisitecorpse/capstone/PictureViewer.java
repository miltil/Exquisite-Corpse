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
    TextView artistHolder;
    List nameAssociationList;
    long picID;
    ArrayList<String> artistList;
    String artistListString;
    NameAssociation nameAssociation;
    private String artistString;

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
                deleteNameAssociations(galleryPicture.getId());
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
        getSupportActionBar().setTitle(R.string.gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.picture_viewer);

        corpseHolder = (ImageView)findViewById(R.id.corpse_holder);
        artistHolder = (TextView) findViewById(R.id.artists_holder);

        artistListString = getResources().getString(R.string.by) + " ";

        picID = getIntent().getLongExtra("picID", 1);
        galleryPicture = (new Select()
                .from(GalleryPicture.class)
                .where("Id = ?", picID)
                .executeSingle());
        byte[] bytePicture = galleryPicture.full_drawing;
        Bitmap bitmapPicture = Utility.getImage(bytePicture);

        corpseHolder.setImageBitmap(bitmapPicture);

        getArtists();

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
                artistString = (nameAssociation.artistName).substring(0,1) +
                        (nameAssociation.artistName).substring(1).toLowerCase();
                artistListString = artistListString.concat(artistString + ", ");
            }
            nameAssociation = (NameAssociation) nameAssociationList.get(nameAssociationList.size() - 1);
            artistString = (nameAssociation.artistName).substring(0,1) +
                    (nameAssociation.artistName).substring(1).toLowerCase();
            artistListString = artistListString.concat("and " + artistString);
            artistHolder.setText(artistListString);
        }
        else if(nameAssociationList.size() == 1){
            nameAssociation = (NameAssociation) nameAssociationList.get(0);
            artistString = (nameAssociation.artistName).substring(0,1) +
                    (nameAssociation.artistName).substring(1).toLowerCase();
            artistListString = artistListString.concat(artistString);
            artistHolder.setText(artistListString);
        }
    }

    private void deleteNameAssociations(long deleteID){
        List nameAssociationsToDelete = new Select()
                .from(NameAssociation.class)
                .where("DrawingId = ?", deleteID)
                .orderBy("ID ASC")
                .execute();
        if(nameAssociationsToDelete != null && !nameAssociationsToDelete.isEmpty()) {
            for (int i = 0; i < nameAssociationsToDelete.size(); i++) {
                NameAssociation itemToDelete = (NameAssociation) nameAssociationsToDelete.get(i);
                itemToDelete.delete();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
        return;
    }

}
