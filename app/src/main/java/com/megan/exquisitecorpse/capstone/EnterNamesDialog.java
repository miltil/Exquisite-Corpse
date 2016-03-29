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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnterNamesDialog extends DialogFragment {
    int numPlayers;
    StringBuilder playerNames = new StringBuilder();
    String playerNamesString;
    String sectionsString;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        numPlayers = preferences.getInt("numPlayers", 3);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (LinearLayout) inflater.inflate(R.layout.enter_names_dialog, null);
        final EditText player1 = (EditText) convertView.findViewById(R.id.name1);
        final EditText player2 = (EditText) convertView.findViewById(R.id.name2);
        final EditText player3 = (EditText) convertView.findViewById(R.id.name3);
        final EditText player4 = (EditText) convertView.findViewById(R.id.name4);
        builder.setView(convertView)
                .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(numPlayers == 1){
                            if(!editTextEmpty(player1)) {
                                playerNames.append(player1.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                            }
                        }
                        else if(numPlayers == 2){
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
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player1));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player2)) {
                                playerNames.append(player2.getText().toString());
                                playerNames.append(",");
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player3)) {
                                playerNames.append(player3.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player3));
                            }
                        }
                        else if(numPlayers == 4){
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
                                playerNames.append(",");
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player2));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player3)) {
                                playerNames.append(player3.getText().toString());
                                playerNames.append(",");
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player3));
                                playerNames.append(",");
                            }
                            if(!editTextEmpty(player4)) {
                                playerNames.append(player4.getText().toString());
                            }
                            else{
                                playerNames.append(getContext().getString(R.string.player4));
                            }
                        }

                        //Add the player names to the shared preferences
                        playerNamesString = playerNames.toString();
                        editor.putString("playerNames", playerNamesString);
                        editor.commit();

                        //Add an intent extra to start the game
                        Intent intent = new Intent(getContext(), SheetOfPaper.class);
                        intent.putExtra("firstFlag", true);
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

                        //Add the player names to the shared preferences
                        playerNamesString = playerNames.toString();
                        editor.putString("playerNames", playerNamesString);
                        editor.commit();

                        //Add an intent extra to start the game
                        Intent intent = new Intent(getContext(), SheetOfPaper.class);
                        intent.putExtra("firstFlag", true);
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
            sectionsString = "Legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else if(numPlayers == 2) {
            builder.setTitle(R.string.enter_names);
            player3.setVisibility(View.GONE);
            player4.setVisibility(View.GONE);

            //Set up sections
            sectionsString = "the head,the upper torso,the lower torso,the legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else if(numPlayers == 3){
            builder.setTitle(R.string.enter_names);
            player4.setVisibility(View.GONE);

            //Set up sections
            sectionsString = "the head,the torso,the legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        else{
            builder.setTitle(R.string.enter_names);

            //Set up sections
            sectionsString = "the head,the upper torso,the lower torso,the legs";
            editor.putString("segments", sectionsString);
            editor.commit();
        }

        return builder.create();
    }

    boolean editTextEmpty(EditText e){
        return e.getText().toString().trim().length() == 0;
    }
}