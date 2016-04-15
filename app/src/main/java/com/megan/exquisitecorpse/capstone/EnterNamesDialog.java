package com.megan.exquisitecorpse.capstone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.widget.ArrayAdapter;

import com.activeandroid.query.Select;

public class EnterNamesDialog extends DialogFragment {
    int numPlayers;
    StringBuilder playerNames = new StringBuilder();
    String playerNamesString;
    String sectionsString;
    ArrayList<String> artistsArrayList;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        numPlayers = preferences.getInt("numPlayers", 3);
        String[] artistNameArray;
        artistsArrayList = new ArrayList<>();
        List artistList = new Select()
                .from(Artists.class)
                .orderBy("ID DESC")
                .execute();
        for(int i = 0; i < artistList.size(); i++) {
            Artists artist = (Artists)artistList.get(i);
            artistsArrayList.add(artist.artistName);
        }
        artistNameArray = artistsArrayList.toArray(new String[artistsArrayList.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (LinearLayout) inflater.inflate(R.layout.enter_names_dialog, null);

        final AutoCompleteTextView player1 = (AutoCompleteTextView) convertView.findViewById(R.id.name1);
        player1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, artistNameArray));
        final AutoCompleteTextView player2 = (AutoCompleteTextView) convertView.findViewById(R.id.name2);
        player2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, artistNameArray));
        final AutoCompleteTextView player3 = (AutoCompleteTextView) convertView.findViewById(R.id.name3);
        player3.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, artistNameArray));
        final AutoCompleteTextView player4 = (AutoCompleteTextView) convertView.findViewById(R.id.name4);
        player4.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, artistNameArray));
        builder.setView(convertView)
                .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(numPlayers == 1){
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                                addArtist(player1.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                            }
                        }
                        else if(numPlayers == 2){
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                                playerNames.append(",");
                                addArtist(player1.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player2)) {
                                playerNames.append(player2.getText().toString());
                                playerNames.append(",");
                                addArtist(player2.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                                playerNames.append(",");
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player2)) {
                                playerNames.append(player2.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                            }
                        }
                        else if(numPlayers == 3){
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                                playerNames.append(",");
                                addArtist(player1.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player2)) {
                                playerNames.append(player2.getText().toString());
                                playerNames.append(",");
                                addArtist(player2.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player3)) {
                                playerNames.append(player3.getText().toString());
                                addArtist(player3.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player3));
                            }
                        }
                        else if(numPlayers == 4){
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                                playerNames.append(",");
                                addArtist(player1.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player2)) {
                                playerNames.append(player2.getText().toString());
                                playerNames.append(",");
                                addArtist(player2.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player3)) {
                                playerNames.append(player3.getText().toString());
                                playerNames.append(",");
                                addArtist(player3.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player3));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player4)) {
                                playerNames.append(player4.getText().toString());
                                addArtist(player4.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player4));
                            }
                        }

                        //Add the player names to the shared preferences
                        playerNamesString = playerNames.toString();
                        editor.putString("playerNames", playerNamesString);
                        editor.commit();

                        //Add the player names to the shared preferences to associate with the completed
                        //drawing at the game's end
                        editor.putString("playerNameAssociations", playerNamesString);
                        editor.commit();

                        //Add an intent extra to start the game
                        Intent intent = new Intent(getContext(), SheetOfPaper.class);
                        intent.putExtra("lastFlag", false);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.skip_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(numPlayers == 1){
                            playerNames.append(getContext().getString(R.string.player1));
                            playerNames.append(",");
                        }
                        else if(numPlayers == 2){
                            playerNames.append(getContext().getString(R.string.player1));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player2));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player1));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player2));
                        }
                        else if(numPlayers == 3){
                            playerNames.append(getContext().getString(R.string.player1));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player2));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player3));
                        }
                        else if(numPlayers == 4){
                            playerNames.append(getContext().getString(R.string.player1));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player2));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player3));
                            playerNames.append(",");
                            playerNames.append(getContext().getString(R.string.player4));
                        }

                        //Add the player names to the shared preferences to keep track of current player
                        playerNamesString = playerNames.toString();
                        editor.putString("playerNames", playerNamesString);
                        editor.commit();

                        //Add an intent extra to start the game
                        Intent intent = new Intent(getContext(), SheetOfPaper.class);
                        intent.putExtra("lastFlag", false);
                        startActivity(intent);

                    }
                });

        if(numPlayers == 1) {
            //Set the title
            builder.setTitle(R.string.enter_name);

            //Set how many edittexts are available
            player2.setVisibility(View.GONE);
            player3.setVisibility(View.GONE);
            player4.setVisibility(View.GONE);

            //Set up sections
            sectionsString = "legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else if(numPlayers == 2) {
            builder.setTitle(R.string.enter_names);
            player3.setVisibility(View.GONE);
            player4.setVisibility(View.GONE);

            //Set up sections
            sectionsString = "head,upper torso,lower torso,legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else if(numPlayers == 3){
            builder.setTitle(R.string.enter_names);
            player4.setVisibility(View.GONE);

            //Set up sections
            sectionsString = "head,torso,legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else{
            builder.setTitle(R.string.enter_names);

            //Set up sections
            sectionsString = "head,upper torso,lower torso,legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        return builder.create();
    }

    boolean editTextEmpty(EditText e){
        return e.getText().toString().trim().length() == 0;
    }

    public void addArtist(String artistName){
        if(!artistsArrayList.contains(artistName)) {
            Artists artist = new Artists();
            artist.artistName = artistName;
            artist.save();
        }
    }
}
