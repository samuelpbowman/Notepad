package com.samuel.notepad;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements SaveDialogFragment.SaveDialogListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.editText);
        text.setText(((NotepadApplication)getApplication()).getText());

        Button newFile = (Button) findViewById(R.id.newBut);
        Button openFile = (Button) findViewById(R.id.openBut);
        Button saveFile = (Button) findViewById(R.id.saveBut);

        newFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                /*new Thread(new Runnable() {
                    public synchronized void run() {
                        if (((NotepadApplication) getApplication()).getText() != "")
                            showSaveDialog();

                    }
                });
                try {
                    this.wait();
                } catch(Exception e) {}*/
                ((NotepadApplication)getApplication()).setText("");
                ((TextView)findViewById(R.id.editText)).setText("".toCharArray(), 0, 0);
                Snackbar.make(view, "All Set!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                /*new Thread(new Runnable() {
                    public synchronized void run() {
                        if(((NotepadApplication)getApplication()).getText() != "") showSaveDialog();
                    }});
                try {
                    this.wait();
                } catch(Exception e) {}
                if(((NotepadApplication)getApplication()).getText() != "")
                    showSaveDialog();*/
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NotepadApplication)getApplication()).saveCurrent();
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void showSaveDialog() {
        DialogFragment dialog = new SaveDialogFragment();
        dialog.show(getFragmentManager(), "SaveDialogFragment");
    }


    @Override
    public synchronized void onSaveDialogYesClick(DialogFragment dialog) {
        ((NotepadApplication)getApplication()).saveCurrent();
    }

    @Override
    public synchronized void onSaveDialogNoClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
