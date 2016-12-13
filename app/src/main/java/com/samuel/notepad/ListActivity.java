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

import com.samuel.notepad.dialog.NameDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        NameDialogFragment.NameDialogListener {

    private ArrayAdapter adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.documents);
        ArrayList<String> files = new ArrayList<>();
        for(File f : getApplicationContext().getFilesDir().listFiles()) {
            if(f.getName().equals("instant-run")) continue;
            files.add(f.getAbsolutePath());
        }

        adapt = new ArrayAdapter(
                getApplicationContext(), R.layout.simple_list_item, files.toArray());
        lv.setAdapter(adapt);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the dialog fragment and show it
                DialogFragment dialog = new NameDialogFragment();
                dialog.show(getFragmentManager(), "NameDialogFragment");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapt, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        File f = new File((String)adapt.getItemAtPosition(position));
        ((NotepadApplication)getApplication()).setFile(f);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((NotepadApplication)getApplication()).getFile().delete();
        startActivity(new Intent(this, ListActivity.class));
        return true;
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText t = (EditText)dialog.getDialog().findViewById(R.id.name_field);
        String s = t.getText().toString();
        if(s.equals("") || s == null) {
            DialogFragment dialog2 = new NameDialogFragment();
            dialog2.show(getFragmentManager(), "NameDialogFragment2");
        } else {
            File f = new File(getApplicationContext().getFilesDir(), s+".txt");
            try {
                f.createNewFile();
            } catch(IOException i) {
                i.printStackTrace();
            }
            ((NotepadApplication)getApplication()).setFile(f);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
