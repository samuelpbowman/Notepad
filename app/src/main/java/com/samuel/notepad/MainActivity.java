package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
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

import com.samuel.notepad.dialog.InputDialogFragment;
import com.samuel.notepad.dialog.ListDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        SaveDialogFragment.SaveDialogListener, InputDialogFragment.InputDialogListener {

    private EditText text;
    private TextView count;
    private Spinner spinner;
    private ArrayList<String> options;
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

        //error-checked opening of text file and setting of character count
        if((((NotepadApplication)getApplication()).getFile().exists())) {
            String string = ((NotepadApplication)getApplication()).openFile();
            text.setText(string, BufferType.EDITABLE);
        }
        int counter = MainActivity.this.text.getText().toString().length();
        this.count.setText(String.valueOf(counter));

        String name = ((NotepadApplication)getApplication()).getFile().getName();
        this.savedSinceLastEdit = true;
        this.needsNameDialog = false;
        this.needsListActivity = false;
        this.needsTextClear = false;

        //listener for when the text changes, so that both the
        //character counter and saved variable can be updated
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

        spinner = (Spinner) findViewById(R.id.options);
        options = new ArrayList<>();
        options.add((((NotepadApplication)getApplication()).getFile().getName().isEmpty() ?
                "Untitled" : name.substring(0, name.length() - 4)));
        options.addAll(Arrays.asList(getResources().getStringArray(R.array.options_array)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //listeners for the dropdown menu
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
                            options.remove(0);
                            options.add(0, "Untitled");
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    MainActivity.this, R.layout.simple_list_item, options);
                            spinner.setAdapter(adapter);
                        } else {
                            SaveDialogFragment fragment = new SaveDialogFragment();
                            fragment.show(getFragmentManager(), "SaveDialogFragment");
                        }
                        if(!MainActivity.this.needsNameDialog) {
                            MainActivity.this.text.setText("");
                            ((NotepadApplication)getApplication()).setFile(new File(""));
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
                        DialogFragment fragment = new ListDialogFragment();
                        fragment.show(getFragmentManager(), "");
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        if(this.needsNameDialog) {
            DialogFragment fragment = InputDialogFragment.newInstance(false);
            String tag;
            if(dialog.getTag().equals("SaveDialogFragmentOpen")) {
                tag = "InputDialogFragmentTransition";
            } else {
                tag = "InputDialogFragment";
            }
            fragment.show(getFragmentManager(), tag);
        } else {
            String string = text.getText().toString();
            ((NotepadApplication) getApplication()).saveFile(string);
        }
        savedSinceLastEdit = true;
        int counter = text.getText().toString().length();
        this.count.setText(String.valueOf(counter));
        if(needsTextClear)
            this.text.setText("");
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
        else if(needsTextClear)
            this.text.setText("");
        this.needsTextClear = false;
    }

    @Override
    public void onInputPositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText)dialog.getDialog().findViewById(R.id.input_field);
        String name = edit.getText().toString();

        File file = new File(getApplicationContext().getFilesDir(), name + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        //change the dropdown to reflect the newly created filename
        //changing the name of a file from inside the editor is
        //not currently supported
        options.remove(0);
        options.add(0, ((NotepadApplication)getApplication()).getFile().getName().isEmpty() ?
                "Untitled" : name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, options);
        spinner.setAdapter(adapter);

        if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
        else if(needsTextClear)
            this.text.setText("");
        this.needsTextClear = false;
    }
}
