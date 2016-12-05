package com.samuel.notepad.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.samuel.notepad.R;

/**
 * Created by samuel on 12/5/16.
 */

public class SaveDialogFragment extends DialogFragment {

    public interface SaveDialogListener {
        public void onSaveDialogPositiveClick(DialogFragment dialog);
        public void onSaveDialogNegativeClick(DialogFragment dialog);
    }

    private SaveDialogListener sdl;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_save_message);
        builder.setPositiveButton(R.string.dialog_yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sdl.onSaveDialogPositiveClick(SaveDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.dialog_no_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sdl.onSaveDialogNegativeClick(SaveDialogFragment.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a = null;
        if(context instanceof Activity) {
            a = (Activity)context;
        }
        try {
            this.sdl = (SaveDialogListener)a;
        } catch(ClassCastException c) {
            c.printStackTrace();
        }
    }



}
