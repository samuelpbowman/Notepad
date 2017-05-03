package com.samuel.notepad;

import android.app.Application;

import com.samuel.notepad.helper.TextHelper;

import java.io.File;
import java.io.IOException;

/**
 * Application class for the Notepad project I am working
 * on. Here is the persistence engine for all of the app's
 * working data.
 *
 * Created by samuel on 11/23/16.
 */

public class NotepadApplication extends Application {

    private File file;

    @Override
    public void onCreate() {
        super.onCreate();
        this.file = new File("");
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void saveFile(String string) {
        try {
            TextHelper.writeTextToFile(string, this.file);
        } catch(IOException i) {
            i.printStackTrace();
        }
    }

    public String openFile() {
        String string = "";
        try {
            string = TextHelper.readTextFromFile(this.file);
        } catch(IOException i) {
            i.printStackTrace();
        }
        return string;
    }
}