package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.samuel.notepad.dialog.NameDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

public class ListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        SaveDialogFragment.SaveDialogListener, NameDialogFragment.NameDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.documents);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the dialog fragment and show it
                DialogFragment dialog = new SaveDialogFragment();
                dialog.show(getFragmentManager(), "SaveDialogFragment");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapt, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this, ListActivity.class));
        return true;
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
