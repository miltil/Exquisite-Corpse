package com.megan.exquisitecorpse.capstone;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

public class Gallery extends AppCompatActivity {

    private FloatingActionButton fab;
    private GridView gridView;
    private GalleryAdapter galleryAdapter;
    private TextView errorMessage;
    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);


      /*  CursorAdapter adapter = new CursorAdapter(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                columns,
                columnTextId,
                0); */


        String sql = new Select()
                .from(Artists.class)
                .toSql();
        String[] params = null;
        Cursor cursor = Cache.openDatabase().rawQuery(sql, params);

        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"ArtistName"},
                new int[]{android.R.id.text1}
        );
        searchView.setSuggestionsAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                //String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Intent i = new Intent(Gallery.this, Searchable.class);

                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String artistName = cursor.getString(1);

                i.putExtra("query", artistName);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });

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

    private class FillGalleryTask extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground (Void... params){

            return true;
        }

        protected void onPostExecute() {
            super.onPostExecute(true);
        }
    }

}
