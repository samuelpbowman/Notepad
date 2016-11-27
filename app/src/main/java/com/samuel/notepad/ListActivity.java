package com.samuel.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((NotepadApplication)getApplication()).setCurrentFileName("");
        List<String> l = new ArrayList<>(((NotepadApplication)getApplication()).getSize());
        for(File f: ((NotepadApplication)getApplication()).getFiles()) {
            l.add(f.getPath());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, l);
        ListView lv = (ListView)findViewById(R.id.documents);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                ((NotepadApplication)getApplication()).setText("");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapt, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        File file = ((NotepadApplication)getApplication()).getFiles().get(position);
        try {
            BufferedReader b = new BufferedReader(new FileReader(file));
            StringBuilder s = new StringBuilder();
            String line = b.readLine();
            while(line != null) {
                s.append(line);
                line = b.readLine();
            }
            ((NotepadApplication)getApplication()).setText(s.toString());
            b.close();
        } catch(IOException i) {
            //TODO implement
        }
        ((NotepadApplication)getApplication()).setCurrentFileName(file.getName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return ((NotepadApplication)getApplication()).getFiles().get(i).delete();

    }
}
