package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.samuel.notepad.dialog.DeleteDialogFragment;
import com.samuel.notepad.dialog.InputDialogFragment;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        InputDialogFragment.InputDialogListener, DeleteDialogFragment.DeleteDialogListener {

    private ListView view;
    private File selected;
    private boolean rename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rename = false;

        view = (ListView) findViewById(R.id.documents);
        this.resetList();
        view.setOnItemClickListener(this);
        view.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the dialog fragment and show it
                DialogFragment dialog = new InputDialogFragment();
                dialog.show(getFragmentManager(), "InputDialogFragment");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        File f = new File(getFilesDir().getAbsolutePath() + "/" + adapterView.getItemAtPosition(position) + ".txt");
        ((NotepadApplication)getApplication()).setFile(f);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selected = new File(getApplicationContext().getFilesDir(), "/" + adapterView.getItemAtPosition(position) + ".txt");
        DialogFragment fragment = new DeleteDialogFragment();
        fragment.show(getFragmentManager(), "DeleteDialogFragment");
        return true;
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText t = (EditText) dialog.getDialog().findViewById(R.id.input_field);
        String str = t.getText().toString();
        if (str.equals("")) {
            DialogFragment dialog2 = new InputDialogFragment();
            dialog2.show(getFragmentManager(), "NameDialogFragment2");
        } else if(rename) {
            if(selected.renameTo(new File(this.getFilesDir(), str + ".txt"))) {
                this.resetList();
            } else {
                InputDialogFragment fragment = new InputDialogFragment();
                fragment.show(getFragmentManager(), "NameDialogFragment2");
            }
        } else {
            File file = new File(getApplicationContext().getFilesDir(), str + ".txt");
            ((NotepadApplication) getApplication()).setFile(file);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onDeleteDialogPositiveButtonClick(DialogFragment dialog) {
        if (selected.delete())
            Snackbar.make(view, "Deleted", Snackbar.LENGTH_SHORT).show();
        this.resetList();
    }

    @Override
    public void onDeleteDialogNegativeButtonClick(DialogFragment dialog) {
        //do nothing
    }

    @Override
    public void onDeleteDialogNeutralButtonClick(DialogFragment dialog) {
        rename = true;
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.show(getFragmentManager(), "InputDialogFragment");
    }


    private void resetList() {
        ArrayList<String> files = new ArrayList<>();
        for(File f : ListActivity.this.getFilesDir().listFiles()) {
            if(f.getName().contains("instant-run")) continue;
            files.add(f.getName().substring(0, f.getName().length() - 4));
        }

        ArrayAdapter<String> adapt = new ArrayAdapter<>(
                ListActivity.this, R.layout.simple_list_item, files.toArray(new String[files.toArray().length]));
        view.setAdapter(adapt);
        rename = false;
    }
}
