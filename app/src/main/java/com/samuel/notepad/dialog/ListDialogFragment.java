package com.samuel.notepad.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samuel.notepad.R;

import java.util.ArrayList;

/**
 * Class representing a popup menu for the user
 * to share his/her text to social media
 *
 * Created by samuel on 5/10/17.
 */

public class ListDialogFragment extends DialogFragment {

    /*public interface ListDialogListener {
        void onItemSelected(DialogFragment dialog);
    }

    private ListDialogListener listener;*/

    @Override
    @TargetApi(21)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.share_message);
        //RelativeLayout rel = (RelativeLayout)getResources().getLayout(R.layout.dialog_list);
        builder.setView(R.layout.dialog_list);

        //ListView lv = (ListView)rel.findViewById(R.id.outlet_list);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(
                getActivity(), R.layout.simple_list_item);//, getResources().getStringArray(R.array.social_media));
        String vals[] = getResources().getStringArray(R.array.social_media);
        adapt.addAll(vals);
        builder.setAdapter(adapt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO implement
            }
        });
        return builder.create();
    }
}
