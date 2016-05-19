package com.megan.exquisitecorpse.capstone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PassingPrompt extends DialogFragment {
    String playerNamesString;
    String segmentsString;
    List<String> playerNames = new ArrayList<>();
    List<String> segments = new ArrayList<>();
    String currentPlayer = "next player!";
    String currentSegment = "";
    String newNamesString;
    String newSegmentsString;
    String passingText;
    boolean lastFlag = false;
    int numPlayers;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setCancelable(false);

        //What we need to get here is the name of the current player (to display on the "pass to")
        //and the part of the body they will be drawing to first display and then put in an intent extra
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        numPlayers = preferences.getInt("numPlayers", 3);
        playerNamesString = preferences.getString("playerNames", "next player");
        if(playerNamesString != null){
            playerNames = new LinkedList<>(Arrays.asList(playerNamesString.split(",")));
        }
        segmentsString = preferences.getString("segments", "next part");
        if(segmentsString != null){
            segments = new LinkedList<>(Arrays.asList(segmentsString.split(",")));
        }

        currentPlayer = playerNames.get(0);
        currentSegment = segments.get(0);

        playerNames.remove(0);
        segments.remove(0);

        if(playerNames.size() == 0){
            lastFlag = true;
        }

        newNamesString = android.text.TextUtils.join(",", playerNames);
        newSegmentsString = android.text.TextUtils.join(",", segments);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("playerNames", newNamesString);
        editor.putString("segments", newSegmentsString);
        editor.commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = inflater.inflate(R.layout.passing_prompt_dialog, null);
        builder.setView(convertView)
                .setPositiveButton(R.string.ready_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), DrawingInterface.class);
                        intent.putExtra("currentSegment", currentSegment);
                        intent.putExtra("currentPlayer", currentPlayer);
                        intent.putExtra("lastFlag", lastFlag);
                        startActivity(intent);
                    }
                });

        TextView passToText = (TextView) convertView.findViewById(R.id.passTo);

        if(numPlayers > 1) {
            passingText = getContext().getString(R.string.passing_prompt) + " " + currentPlayer +
                    " " + getContext().getString(R.string.to_draw) + " " + currentSegment;
        }
        else{
            passingText = currentPlayer + ", " + "are you ready" +
                    " " + getContext().getString(R.string.to_draw) + " " + currentSegment + "?";
        }
        passToText.setText(passingText);

        return builder.create();
    }
}
