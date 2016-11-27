package com.samuel.notepad;

import android.app.Application;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Application class for the Notepad project I am working
 * on. Here is the persistence engine for all of the app's
 * working data.
 *
 * Created by samuel on 11/23/16.
 */

public class NotepadApplication extends Application {

    private String text;
    private List<File> files;

    @Override
    public void onCreate() {
        this.text = "";
        this.files = Arrays.asList(getApplicationContext().getFilesDir().listFiles());
    }

    public int getSize() {return this.files.size();}

    public String getText() {return text;}

    public void setText(String text) {
        this.text = text;
    }

    public List<File> getFiles() {return files;}
}
