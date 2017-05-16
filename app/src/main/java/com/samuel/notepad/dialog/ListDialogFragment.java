package com.samuel.notepad.dialog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.samuel.notepad.R;
import com.samuel.notepad.helper.SMSHelper;

/**
 * Class representing a popup menu for the user
 * to share his/her text to social media
 *
 * Created by samuel on 5/10/17.
 */

public class ListDialogFragment extends DialogFragment implements InputDialogFragment.InputDialogListener {

    @Override
    @TargetApi(21)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.share_message);
        builder.setView(R.layout.dialog_list);

        EditText field = (EditText)getActivity().findViewById(R.id.editText);
        final String export = field.getText().toString();

        ArrayAdapter<String> adapt = new ArrayAdapter<>(
                getActivity(), R.layout.simple_list_item);
        String vals[] = getResources().getStringArray(R.array.social_media);
        adapt.addAll(vals);
        builder.setAdapter(adapt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        DialogFragment fragment = InputDialogFragment.newInstance(true);
                        fragment.show(getFragmentManager(), "");
                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText)dialog.getDialog().findViewById(R.id.input_field);
        String dump = edit.getText().toString();
        String numbers[] = dump.split(",");

        EditText source = (EditText)getActivity().findViewById(R.id.editText);
        String outbound = source.getText().toString();

        SMSHelper.sendSMS(outbound, numbers);
    }

}
