package com.samuel.notepad.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.samuel.notepad.R;

/**
 * Class representing a dialog box
 * which asks for a file's name.
 *
 * Created by samuel on 12/5/16.
 */

public class InputDialogFragment extends DialogFragment {

    public static final String TEXT_EXT = ".txt";

    public interface InputDialogListener {
        void onInputPositiveButtonClick(DialogFragment dialog);
    }

    protected InputDialogListener idl;

    public static InputDialogFragment newInstance(int arg) {
        InputDialogFragment f = new InputDialogFragment();

        Bundle args = new Bundle();
        args.putInt("type", arg);
        f.setArguments(args);

        return f;
    }

    @Override
    @TargetApi(21)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_input);
        int res = R.string.dialog_name_message;

        builder.setMessage(res);
        builder.setPositiveButton(
            R.string.dialog_okay_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    idl.onInputPositiveButtonClick(InputDialogFragment.this);
                }
            });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a = null;
        if (context instanceof Activity) {
            a = (Activity) context;
        }
        try {
            this.idl = (InputDialogListener) a;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
    }
}
