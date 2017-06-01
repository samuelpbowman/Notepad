package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.samuel.notepad.dialog.InputDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

import java.io.File;

public class EditorActivity extends AppCompatActivity implements InputDialogFragment.InputDialogListener {

    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);
        String name = ((NotepadApplication)getApplication()).getFile().getName();

        //error-checked opening of text file and setting of character count
        //if((((NotepadApplication)getApplication()).getFile().exists())) {
        String string = ((NotepadApplication)getApplication()).openFile();
        text.setText(string, BufferType.EDITABLE);
        //}

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(name.isEmpty() ? "Untitled" : name.substring(0, name.length()-4));
        toolbar.setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        //listener for when the text changes, so that both the
        //character counter and saved variable can be updated
        this.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                savedSinceLastEdit = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Autosave coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText field = (EditText)dialog.getDialog().findViewById(R.id.input_field);
        String text = field.getText().toString();

        File file = new File(getFilesDir(), text + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        
    }
}
