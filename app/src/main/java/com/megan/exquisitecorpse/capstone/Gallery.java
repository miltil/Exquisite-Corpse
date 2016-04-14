package com.megan.exquisitecorpse.capstone;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

import com.activeandroid.query.Select;

public class Gallery extends AppCompatActivity {

    private FloatingActionButton fab;
    private GridView gridView;
    private GalleryAdapter galleryAdapter;

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
        getSupportActionBar().setTitle(R.string.gallery_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NoPlayerPicker playerPicker = new NoPlayerPicker();
                playerPicker.show(getSupportFragmentManager(), "HI");
            }
        });

        galleryAdapter = new GalleryAdapter(this, new ArrayList<GalleryPicture>());
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(galleryAdapter);

        GalleryPicture galleryPicture = new Select()
                .from(GalleryPicture.class)
                .orderBy("ID DESC")
                .executeSingle();

        galleryAdapter.add(galleryPicture);

       /* ImageView fullImageTest = (ImageView)findViewById(R.id.fullImageTest);

        fullImageTest.setImageBitmap(bitmapPicture); */
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OpeningScreen.class);
        startActivity(intent);
        return;
    }

}
