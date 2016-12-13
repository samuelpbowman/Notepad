package com.samuel.notepad;

import android.app.Application;

import java.io.File;

/**
 * Application class for the Notepad project I am working
 * on. Here is the persistence engine for all of the app's
 * working data.
 *
 * Created by samuel on 11/23/16.
 */

public class NotepadApplication extends Application {

    private File file;
    private String name;

    @Override
    public void onCreate() {
        this.file = null;
        this.name = "";
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

}