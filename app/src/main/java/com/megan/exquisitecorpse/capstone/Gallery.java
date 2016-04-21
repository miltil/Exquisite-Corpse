package com.megan.exquisitecorpse.capstone;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.activeandroid.query.Select;

public class Gallery extends AppCompatActivity {

    private FloatingActionButton fab;
    private GridView gridView;
    private GalleryAdapter galleryAdapter;
    private TextView errorMessage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
       // searchView.setSuggestionsAdapter();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, OpeningScreen.class);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        getSupportActionBar().setTitle(R.string.gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NoPlayerPicker playerPicker = new NoPlayerPicker();
                playerPicker.show(getSupportFragmentManager(), "HI");
            }
        });

        errorMessage = (TextView)findViewById(R.id.error_message);

        galleryAdapter = new GalleryAdapter(this, new ArrayList<GalleryPicture>());
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(galleryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryPicture galleryPicture = galleryAdapter.getItem(position);
                long picID = galleryPicture.getId();
                Intent i = new Intent(Gallery.this, PictureViewer.class);
                i.putExtra("picID", picID);
                startActivity(i);
            }
        });

        List galleryPictures = new Select()
                .from(GalleryPicture.class)
                .orderBy("ID DESC")
                .execute();

        for(int i = 0; i < galleryPictures.size(); i++) {
            galleryAdapter.add((GalleryPicture)galleryPictures.get(i));
        }

        if(galleryPictures.size() == 0){
            errorMessage.setText("You haven't created any drawings yet.");
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OpeningScreen.class);
        startActivity(intent);
        return;
    }

}
