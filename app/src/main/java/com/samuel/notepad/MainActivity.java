package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.samuel.notepad.dialog.NameDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;
import com.samuel.notepad.text.TextHelper;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener,
        NameDialogFragment.NameDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText text = (EditText) findViewById(R.id.editText);

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        try {
            text.setText(TextHelper.readTextFromFile(
                    ((NotepadApplication) getApplication()).getFile()));
        } catch(IOException i) {
            text.setText("ERROR: File not readable", TextView.BufferType.EDITABLE);
        }

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().equals("")) {
                    return;
                }
                text.setText("", TextView.BufferType.EDITABLE);
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new SaveDialogFragment();
                dialog.show(getFragmentManager(), "SaveDialogFragment");
            }
        });
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((NotepadApplication)getApplication()).getFile().getName() == "") {
                    DialogFragment name = new NameDialogFragment();
                    name.show(getFragmentManager(), "NameDialogFragment");
                }
                String s = ((NotepadApplication)getApplication()).getFile().getName();
                ((NotepadApplication)getApplication()).getFile().delete();
                try {
                    TextHelper.writeTextToFile(text.getText().toString(), new File(s));
                } catch(IOException i) {
                    Log.e("Error", "Error");
                }
            }
        });
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        DialogFragment fragment = new NameDialogFragment();
        fragment.show(getFragmentManager(), "NameDialogFragment");
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        dialog.dismiss();
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }
}
