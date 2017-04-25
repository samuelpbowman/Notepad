package com.samuel.notepad;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private char called;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (EditText) findViewById(R.id.editText);
        this.count = (TextView) findViewById(R.id.charCount);
        this.called = '\0';

        if(!(((NotepadApplication)getApplication()).getFile().exists())) {
            this.text.clearComposingText();
        } else {
            String string = ((NotepadApplication)getApplication()).openFile();
            text.setText(string, BufferType.EDITABLE);
        }
        savedSinceLastEdit = true;

        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
        RelativeLayout filler = (RelativeLayout) findViewById(R.id.filler);

        float delta = MainActivity.this.calculateDelta();

        MainActivity.this.text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .1f + delta));
        buttons.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .1f));
        filler.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .8f - delta));

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
            public void afterTextChanged(Editable editable) {
                LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
                RelativeLayout filler = (RelativeLayout) findViewById(R.id.filler);

                float delta = MainActivity.this.calculateDelta();

                MainActivity.this.text.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0, .1f + delta));
                buttons.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0, .1f));
                filler.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0, .8f - delta));
            }
        });

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.called = 'n';
                if (!savedSinceLastEdit && MainActivity.this.text.getText().toString().isEmpty()) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragmentNew");
                } else {
                    MainActivity.this.text.setText("");
                    ((NotepadApplication)getApplication()).setFile(new File(""));
                }
            }
        });

        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!savedSinceLastEdit) {
                    MainActivity.this.called = 'o';
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
                MainActivity.this.called = 's';
                if(!savedSinceLastEdit) {
                    DialogFragment fragment = new SaveDialogFragment();
                    fragment.show(getFragmentManager(), "SaveDialogFragmentSave");
                }
            }
        });
        int charCount = this.text.getText().toString().length();
        this.count.setText(String.valueOf(charCount));
    }

    @Override
    public void onSaveDialogPositiveClick(DialogFragment dialog) {
        if(((NotepadApplication)getApplication()).getFile().getName().isEmpty()) {
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
        if(called == 'o')
            startActivity(new Intent(this, ListActivity.class));
        else //if(called == 'n')
            this.text.clearComposingText();
    }

    @Override
    public void onNamePositiveButtonClick(DialogFragment dialog) {
        EditText edit = (EditText)dialog.getDialog().findViewById(R.id.name_field);
        String name = edit.getText().toString();

        File file = new File(getApplicationContext().getFilesDir(), name + ".txt");
        ((NotepadApplication)getApplication()).setFile(file);
        ((NotepadApplication)getApplication()).saveFile(this.text.getText().toString());

        if(called == 'o')
            startActivity(new Intent(this, ListActivity.class));
    }

    @TargetApi(16)
    private float calculateDelta() {
        int lines = this.text.getText().length() / 52;
        float t;
        if(lines == 0)
            t = 0.1f;
        else
            t = 0.1f * lines;
        float r = 0.9f - t;
        float delta = t - (8/9f)*r + 0.6f;
        return (Math.abs(delta) < 0.8f) ? Math.abs(delta) : 0.8f;
    }
}
