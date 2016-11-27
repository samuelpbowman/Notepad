package com.samuel.notepad;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.editText);
        text.setText(((NotepadApplication)getApplication()).getText());

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add Dialog Functionality
                ((NotepadApplication)getApplication()).setText("");
                ((TextView)findViewById(R.id.editText)).setText("".toCharArray(), 0, 0);
                Snackbar.make(view, "All Set!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add Dialog Functionality
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.saveCurrent(view);
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private String generateFileName(int i) {
        String f = "FILE";
        String z = (i < 10) ? "0":"";
        return f+z+((NotepadApplication)getApplication()).getSize();
    }

    private void saveCurrent(View view) {
        ((NotepadApplication)getApplication())
                .setText(String.valueOf(((TextView)findViewById
                        (R.id.editText)).getText()));
        try {
            int i = ((NotepadApplication)getApplication()).getFiles().size();
            File f = new File((getApplication())
                    .getFilesDir(), generateFileName(Integer.parseInt(
                    ((NotepadApplication)getApplication()).getFiles().get(i-1).getName()
                            .substring(4))));
            FileWriter w = new FileWriter(f);
            w.write(((NotepadApplication)getApplication()).getText());
            w.close();
        } catch(IOException i) {
            Snackbar.make(view, "ERROR", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
