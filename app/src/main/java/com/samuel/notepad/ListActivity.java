package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
        ArrayList<File> files = new ArrayList<>();
        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir+"/files";
        } catch(PackageManager.NameNotFoundException n) {
            n.printStackTrace();
        }

        for(File f:new File(s).listFiles()) {
            if(f.getName().equals("instant-run")) continue;
            files.add(f);
        }
        adapt = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item, files.toArray());
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
        File f = (File)adapt.getItemAtPosition(position);
        ((NotepadApplication)getApplication()).setFile(f);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //DialogFragment dialog = new DeleteDialogFragment();
        //dialog.show(getFragmentManager(), "DeleteDialogFragment");
        //adapt.get
        return true;
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText e = (EditText) findViewById(R.id.name_field);
        String s = e.getText().toString();
        ((NotepadApplication)getApplication()).getFile().renameTo(new File(s));
        startActivity(new Intent(this, MainActivity.class));
    }

}
