package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.samuel.notepad.dialog.InputDialogFragment;
import com.samuel.notepad.dialog.SaveDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        //toolbar.setTitleTextColor(getResources().getColor());
        setSupportActionBar(toolbar);
        //getSupportActionBar().setIcon(R.drawable.notepad_launcher_rounded_web);
        getSupportActionBar().setTitle(name.isEmpty() ? "Untitled":name);

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
