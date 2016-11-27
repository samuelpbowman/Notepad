package com.samuel.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> l = new ArrayList<>(((NotepadApplication)getApplication()).getSize());
        for(File f: ((NotepadApplication)getApplication()).getFiles()) {
            l.add(f.getPath());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, l);
        ListView lv = (ListView)findViewById(R.id.documents);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

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

    public void onItemClick(AdapterView<?> adapt, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        //.getName();
        try {
            File file = ((NotepadApplication)getApplication()).getFiles().get(position);
            FileReader r = new FileReader(file);
            ((NotepadApplication)getApplication()).setText(r.toString());
        } catch(IOException i) {
            Log.e("Problem", i.getStackTrace().toString());
        }
        startActivity(intent);
    }
}
