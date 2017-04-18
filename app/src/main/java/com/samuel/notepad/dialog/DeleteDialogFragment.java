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
 * Created by samuel on 4/17/17.
 */

public class DeleteDialogFragment extends DialogFragment {

    public interface DeleteDialogListener {
        void onDeleteDialogPositiveButtonClick(DialogFragment dialog);
        void onDeleteDialogNegativeButtonClick(DialogFragment dialog);
    }

    private DeleteDialogListener dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_message);
        builder.setPositiveButton(R.string.dialog_yes_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.onDeleteDialogPositiveButtonClick(DeleteDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.dialog_no_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.onDeleteDialogNegativeButtonClick(DeleteDialogFragment.this);
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
            this.dialog = (DeleteDialogFragment.DeleteDialogListener)a;
        } catch(ClassCastException c) {
            c.printStackTrace();
        }
    }
}
