package com.samuel.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by samuel on 11/27/16.
 */

public class SaveDialogFragment extends DialogFragment {

    public interface SaveDialogListener {
        public void onSaveDialogYesClick(DialogFragment dialog);
        public void onSaveDialogNoClick(DialogFragment dialog);
    }

    SaveDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_save_message)
                .setPositiveButton(R.string.dialog_yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((NotepadApplication)getActivity().getApplication()).saveCurrent();
                    }
                }).setNegativeButton(R.string.dialog_no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof Activity) super.onAttach(context);
        try {
            listener = (SaveDialogListener) context;
        } catch(ClassCastException c) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
