package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samuel.notepad.dialog.NameDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener,
        NameDialogFragment.NameDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.editText);

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TODO implement
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NameDialogFragment();
                dialog.show(getFragmentManager(), "NameDialogFragment");
            }
        });
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {

    }
}
