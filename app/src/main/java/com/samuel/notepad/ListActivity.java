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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.samuel.notepad.dialog.DeleteDialogFragment;
import com.samuel.notepad.dialog.InputDialogFragment;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        InputDialogFragment.InputDialogListener, DeleteDialogFragment.DeleteDialogListener {

    private ListView view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        toolbar.setTitle("My Files");
        setSupportActionBar(toolbar);

        view = (ListView) findViewById(R.id.documents);
        this.resetList();
        view.setOnItemClickListener(this);
        view.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the dialog fragment and show it
                ((NotepadApplication)getApplication()).setFile(null);
                ListActivity.startActivity(new Intent(ListActivity.this, EditorActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, EditorActivity.class);
        File f = new File(getFilesDir().getAbsolutePath() + "/" + adapterView.getItemAtPosition(position) + InputDialogFragment.TEXT_EXT);
        ((NotepadApplication)getApplication()).setFile(f);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        File file = new File(getApplicationContext().getFilesDir(), "/" + adapterView.getItemAtPosition(position) + InputDialogFragment.TEXT_EXT);
        ((NotepadApplication)getApplication()).setFile(file);
        DialogFragment fragment = new DeleteDialogFragment();
        fragment.show(getFragmentManager(), "DeleteDialogFragment");
        return true;
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText t = (EditText) dialog.getDialog().findViewById(R.id.input_field);
        String str = t.getText().toString();
        ((NotepadApplication)getApplication()).getFile().setName(str.trim() + InputDialogFragment.TEXT_EXT);
    }

    @Override
    public void onDeleteDialogPositiveButtonClick(DialogFragment dialog) {
        if(((NotepadApplication)getApplication()).getFile().delete())
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        this.resetList();
    }

    @Override
    public void onDeleteDialogNegativeButtonClick(DialogFragment dialog) {
        //do nothing
    }

    @Override
    public void onDeleteDialogNeutralButtonClick(DialogFragment dialog) {
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.show(getFragmentManager(), "InputDialogFragment");
    }

    /**
     * Algorithm for populating the list of files.
     */
    private void resetList() {
        ArrayList<String> files = new ArrayList<>();
        for(File f : ListActivity.this.getFilesDir().listFiles()) {
            if(f.getName().contains("instant-run")) continue;
            files.add(f.getName().substring(0, f.getName().length() - 4));
        }

        ArrayAdapter<String> adapt = new ArrayAdapter<>(
                this, R.layout.simple_list_item, files.toArray(new String[files.toArray().length]));
        view.setAdapter(adapt);
        rename = false;
    }
}
