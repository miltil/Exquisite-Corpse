package com.megan.exquisitecorpse.capstone;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.activeandroid.query.Delete;

public class GameInterrupt extends DialogFragment {
    private String destination = "home";

    static GameInterrupt newInstance(String destination) {
        GameInterrupt f = new GameInterrupt();

        Bundle args = new Bundle();
        args.putString("destination", destination);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        destination = getArguments().getString("destination");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.delete_warning)
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        new Delete().from(Picture.class).execute();

                        if(destination == "settings") {
                            Intent intent = new Intent(getContext(), SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if(destination == "gallery"){
                            Intent intent = new Intent(getContext(), Gallery.class);
                            startActivity(intent);
                        }
                        else if(destination == "new"){
                            NoPlayerPicker playerPicker = new NoPlayerPicker();
                            playerPicker.show(getFragmentManager(), "HI");
                        }
                        else {
                            Intent intent = new Intent(getContext(), OpeningScreen.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onCancel(dialog);
                    }
                });


        return builder.create();
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        super.onCancel(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnCancelListener) {
            ((DialogInterface.OnCancelListener) activity).onCancel(dialog);
        }
    }
}
