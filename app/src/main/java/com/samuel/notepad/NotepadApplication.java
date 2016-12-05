package com.samuel.notepad;

import android.app.Application;

import com.samuel.notepad.text.TextFile;

/**
 * Application class for the Notepad project I am working
 * on. Here is the persistence engine for all of the app's
 * working data.
 *
 * Created by samuel on 11/23/16.
 */

public class NotepadApplication extends Application {

    private TextFile file;

    @Override
    public void onCreate() {

    }

    public TextFile getFile() { return file; }

    public void setFile(TextFile file) {
        this.file = file;
    }
}