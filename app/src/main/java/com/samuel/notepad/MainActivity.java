package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.samuel.notepad.dialog.NameDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener, NameDialogFragment.NameDialogListener {

    private EditText text;
    private TextView count;
    private boolean savedSinceLastEdit;
    private boolean needsNameDialog;
    private boolean needsListActivity;
    private boolean needsTextClear;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);
        this.count = (TextView) findViewById(R.id.charCount);

        if((((NotepadApplication)getApplication()).getFile().exists())) {
            String string = ((NotepadApplication)getApplication()).openFile();
            text.setText(string, BufferType.EDITABLE);
        }
        String name = ((NotepadApplication)getApplication()).getFile().getName();
        this.savedSinceLastEdit = true;
        this.needsNameDialog = false;
        this.needsListActivity = false;
        this.needsTextClear = false;

        this.text.addTextChangedListener(new TextWatcher() {
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

        Spinner spinner = (Spinner) findViewById(R.id.options);
        ArrayList<String> options = new ArrayList<>();
        options.add((((NotepadApplication)getApplication()).getFile().getName().isEmpty() ? "Untitled" : name));
        options.addAll(Arrays.asList(getResources().getStringArray(R.array.options_array)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView view) {}
            public void onItemSelected(AdapterView view, View v, int i, long l) {
                switch(i) {
                    case 1:
                        view.setSelection(0);
                        MainActivity.this.needsTextClear = true;
                        if(!((NotepadApplication)getApplication()).getFile().exists())
                            MainActivity.this.needsNameDialog = true;
                        if(savedSinceLastEdit) {
                            MainActivity.this.text.setText("");
                            ((NotepadApplication)getApplication()).setFile(new File(""));
                        } else {
                            SaveDialogFragment fragment = new SaveDialogFragment();
                            fragment.show(getFragmentManager(), "SaveDialogFragment");
                        }
                        break;
                    case 2:
                        view.setSelection(0);
                        MainActivity.this.needsListActivity = true;
                        if(!((NotepadApplication)getApplication()).getFile().exists())
                            MainActivity.this.needsNameDialog = true;
                        if(savedSinceLastEdit) {
                            startActivity(new Intent(MainActivity.this, ListActivity.class));
                        } else {
                            SaveDialogFragment fragment = new SaveDialogFragment();
                            fragment.show(getFragmentManager(), "SaveDialogFragment");
                        }
                        break;
                    case 3:
                        view.setSelection(0);
                        if(!((NotepadApplication)getApplication()).getFile().exists())
                            MainActivity.this.needsNameDialog = true;
                        if(!savedSinceLastEdit) {
                            SaveDialogFragment fragment = new SaveDialogFragment();
                            fragment.show(getFragmentManager(), "SaveDialogFragment");
                        }
                        break;
                    case 4:
                        view.setSelection(0);
                        Snackbar.make(findViewById(R.id.container), R.string.coming_soon, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        if(this.needsNameDialog) {
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
        if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
        else if(needsTextClear)
            this.text.setText("");
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
        else if(needsTextClear)
            this.text.setText("");
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText)dialog.getDialog().findViewById(R.id.name_field);
        String name = edit.getText().toString();

        File file = new File(getApplicationContext().getFilesDir(), name + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
        else if(needsTextClear)
            this.text.setText("");
    }

}
