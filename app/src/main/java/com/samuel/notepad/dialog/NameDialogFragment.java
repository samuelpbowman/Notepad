package com.samuel.notepad.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.samuel.notepad.R;

/**
 * Class representing a dialog box
 * which asks for a file's name.
 *
 * Created by samuel on 12/5/16.
 */

public class NameDialogFragment extends DialogFragment {

    public interface NameDialogListener {
        void onNamePositiveButtonClick(DialogFragment dialog);
    }

    NameDialogListener ndl;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflater.inflate(R.layout.dialog_name, null));
        builder.setMessage(R.string.dialog_name_message);
        builder.setPositiveButton(
                R.string.dialog_okay_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ndl.onNamePositiveButtonClick(NameDialogFragment.this);
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
            this.ndl = (NameDialogListener)a;
        } catch(ClassCastException c) {
            c.printStackTrace();
        }
    }
}
