package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.samuel.notepad.dialog.NameDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener, NameDialogFragment.NameDialogListener {

    private EditText text;
    private TextView count;
    private boolean savedSinceLastEdit;
    private String called;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);
        this.count = (TextView) findViewById(R.id.charCount);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                savedSinceLastEdit = false;
                int counter = MainActivity.this.text.getText().toString().length();
                MainActivity.this.count.setText(String.valueOf(counter));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.called = "New";
                if (!(savedSinceLastEdit || MainActivity.this.text.getText().toString().trim().equals(""))) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragmentNew");
                } else if(MainActivity.this.text.getText().toString().trim().equals("")) {
                    MainActivity.this.text.setText("");
                    ((NotepadApplication)getApplication()).setFile(new File(""));
                }
            }
        });

        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!savedSinceLastEdit) {
                    MainActivity.this.called = "Open";
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragmentOpen");
                } else {
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            }
        });

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.called = "Save";
                if(!savedSinceLastEdit) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragmentSave");
            }
            }
        });

        if(!(((NotepadApplication)getApplication()).getFile().exists())) {
            text.setText("");
        } else {
            String string = ((NotepadApplication)getApplication()).openFile();
            text.setText(string, BufferType.EDITABLE);
        }
        savedSinceLastEdit = true;
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        if(((NotepadApplication)getApplication()).getFile().getName().equals("")) {
            DialogFragment fragment = new NameDialogFragment();
            String tag;
            if(dialog.getTag().equals("SaveDialogFragmentOpen")) {
                tag = "NameDialogFragmentTransition";
            } else {
                tag = "NameDialogFragment";
            }
            fragment.show(getFragmentManager(), tag);
        } else {
            String string = text.getText().toString();
            ((NotepadApplication) getApplication()).saveFile(string);
        }
        savedSinceLastEdit = true;
        int counter = text.getText().toString().length();
        this.count.setText(String.valueOf(counter));
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        if(called.equals("Open"))
            startActivity(new Intent(this, ListActivity.class));
        else if(called.equals("New"))
            this.text.setText("", BufferType.EDITABLE);
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText)dialog.getDialog().findViewById(R.id.name_field);
        String name = edit.getText().toString();

        File file = new File(getApplicationContext().getFilesDir(), name + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        if(called.equals("Open"))
            startActivity(new Intent(this, ListActivity.class));
    }
}
