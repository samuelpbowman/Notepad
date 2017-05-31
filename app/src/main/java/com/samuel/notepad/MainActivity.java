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

public class MainActivity extends AppCompatActivity implements
        SaveDialogFragment.SaveDialogListener, InputDialogFragment.InputDialogListener {

    private EditText text;
    private boolean savedSinceLastEdit;
    private boolean needsNameDialog;
    private boolean needsListActivity;
    private boolean needsTextClear;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
                needsTextClear = true;
                if(!((NotepadApplication)getApplication()).getFile().exists())
                    needsNameDialog = true;
                if(!savedSinceLastEdit) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragment");
                }
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                needsListActivity = true;
                if(!((NotepadApplication)getApplication()).getFile().exists())
                    needsNameDialog = true;
                if(savedSinceLastEdit) {
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                } else {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragment");
                }
                //Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
                if(!savedSinceLastEdit) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragment");
                }
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);
        String name = ((NotepadApplication)getApplication()).getFile().getName();

        //error-checked opening of text file and setting of character count
        if((((NotepadApplication)getApplication()).getFile().exists())) {
            String string = ((NotepadApplication)getApplication()).openFile();
            text.setText(string, BufferType.EDITABLE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(name.isEmpty() ? "Untitled" : name.substring(0, name.length()-4));
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle();

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
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        if(this.needsNameDialog) {
            DialogFragment fragment = InputDialogFragment.newInstance(InputDialogFragment.NAME_DIALOG);
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
        EditText field = (EditText)dialog.getDialog().findViewById(R.id.input_field);
        String text = field.getText().toString();

        File file = new File(getFilesDir(), text + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        if(needsTextClear)
            this.text.setText("");
        else if(needsListActivity)
            startActivity(new Intent(this, ListActivity.class));
    }
}
