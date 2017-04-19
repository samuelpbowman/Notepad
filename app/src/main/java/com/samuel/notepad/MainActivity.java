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
import com.samuel.notepad.text.TextHelper;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener, NameDialogFragment.NameDialogListener {

    private EditText text;
    private boolean savedSinceLastEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                savedSinceLastEdit = false;
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
                savedSinceLastEdit = true;
                if (text.getText().toString().equals("")) return;
                text.setText("", TextView.BufferType.EDITABLE);
                ((NotepadApplication)getApplication()).setFile(new File(""));
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savedSinceLastEdit) return;
                DialogFragment saveFragment = new SaveDialogFragment();
                saveFragment.show(getFragmentManager(), "SaveDialogFragment");
            }
        });

        if(!(((NotepadApplication)getApplication()).getFile().exists())) {
            text.setText("");
        } else {
            try {
                text.setText(TextHelper.readTextFromFile(
                        ((NotepadApplication)getApplication()).getFile()), BufferType.EDITABLE);
            } catch(IOException i) {
                i.printStackTrace();
            }
        }
        savedSinceLastEdit = this.text.getText().toString().equals("");
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        savedSinceLastEdit = true;
        if(((NotepadApplication)getApplication()).getFile().getName().equals("")) {
            NameDialogFragment fragment = new NameDialogFragment();
            fragment.show(getFragmentManager(), "NameDialogFragment");
        } else {
            try {
                TextHelper.writeTextToFile(text.getText().toString(), ((NotepadApplication) getApplication()).getFile());
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveDialogNegativeClick(DialogFragment dialog) {
        //do nothing
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText) dialog.getDialog().findViewById(R.id.name_field);
        String str = edit.getText().toString();
        File file = new File(getApplicationContext().getFilesDir(), str + ".txt" );
        if(str.equals("")) {
            NameDialogFragment fragment = new NameDialogFragment();
            fragment.show(getFragmentManager(), "NameDialogFragment");
        } else {
            try {
                TextHelper.writeTextToFile(text.getText().toString(), file);
                ((NotepadApplication)getApplication()).setFile(file);
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            } catch (IOException i) {
                i.printStackTrace();
            }
        }

    }
}
