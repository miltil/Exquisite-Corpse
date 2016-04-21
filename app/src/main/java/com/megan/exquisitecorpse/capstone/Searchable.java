package com.megan.exquisitecorpse.capstone;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Searchable extends AppCompatActivity {

    private List nameAssociationList;
    private ArrayList idList = new ArrayList();
    private GalleryPicture searchedDrawings;
    private ArrayList searchedDrawingsArrayList = new ArrayList();
    private GalleryAdapter galleryAdapter;
    private GridView gridView;
    private long id;
    private String emptyMessage = "";
    private TextView errorMessage;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        getSupportActionBar().setTitle(R.string.results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        galleryAdapter = new GalleryAdapter(this, new ArrayList<GalleryPicture>());
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(galleryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryPicture galleryPicture = galleryAdapter.getItem(position);
                long picID = galleryPicture.getId();
                Intent i = new Intent(Searchable.this, PictureViewer.class);
                i.putExtra("picID", picID);
                startActivity(i);
            }
        });

        errorMessage = (TextView)findViewById(R.id.error_message);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NoPlayerPicker playerPicker = new NoPlayerPicker();
                playerPicker.show(getSupportFragmentManager(), "HI");
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            query = query.toUpperCase();

            nameAssociationList = new Select()
                    .from(NameAssociation.class)
                    .where("ArtistName = ?", query)
                    .orderBy("ID DESC")
                    .execute();

            if(nameAssociationList == null){
                emptyMessage = "No name association list---";
            }
            else {
                for (int i = 0; i < nameAssociationList.size(); i++) {
                    NameAssociation nameAssociation = (NameAssociation) nameAssociationList.get(i);
                    idList.add(nameAssociation.drawingId);
                }
            }

            if(idList == null){
                emptyMessage = emptyMessage.concat("No ID list----");
            }
            else {
                for (int i = 0; i < idList.size(); i++) {
                    id = (long) idList.get(i);
                    searchedDrawings = (new Select()
                            .from(GalleryPicture.class)
                            .where("Id = ?", id)
                            .executeSingle());
                    searchedDrawingsArrayList.add(searchedDrawings);
                }
            }

            if(searchedDrawingsArrayList == null){
                emptyMessage = emptyMessage.concat("No Searched Drawings List---");
            }
            else if(searchedDrawingsArrayList.isEmpty()){
                errorMessage.setText("Sorry, no items match your search.");
            }
            else {
                for (int i = 0; i < searchedDrawingsArrayList.size(); i++) {
                    galleryAdapter.add((GalleryPicture) searchedDrawingsArrayList.get(i));
                }
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
