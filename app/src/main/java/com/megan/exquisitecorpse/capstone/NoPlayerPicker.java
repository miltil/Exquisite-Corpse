package com.megan.exquisitecorpse.capstone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class NoPlayerPicker extends DialogFragment {
    int currentSelection;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        currentSelection = preferences.getInt("numPlayers", 3);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.num_players_title)
                .setSingleChoiceItems(R.array.num_players_options, (currentSelection - 1), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int item) {
                        currentSelection = item + 1;
                    }
                })
                .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("numPlayers", currentSelection);
                        editor.commit();

                        EnterNamesDialog namesDialog = new EnterNamesDialog();
                        namesDialog.show(getFragmentManager(), "HI");
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}