package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.samuel.notepad.dialog.InputDialogFragment;

import java.io.File;

public class EditorActivity extends AppCompatActivity implements InputDialogFragment.InputDialogListener {
    
    private NotepadApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        this.application = ((NotepadApplication)getApplication());

        EditText text = (EditText) findViewById(R.id.editText);
        File file = this.application.getFile();
        String name = (file == null) ? "Untitled.txt" : file.getName();

        String string = (file == null) ? "" : this.application.openFile();
        text.setText(string, BufferType.EDITABLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(name.substring(0, name.length()-4));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Autosave coming soon!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText field = (EditText)dialog.getDialog().findViewById(R.id.input_field);
        String text = field.getText().toString();
        startActivity(new Intent(this, ListActivity.class));
    }
}
