package com.samuel.notepad;

import android.app.Application;
import android.app.DialogFragment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Application class for the Notepad project I am working
 * on. Here is the persistence engine for all of the app's
 * working data.
 *
 * Created by samuel on 11/23/16.
 */

public class NotepadApplication extends Application {

    private String text;
    private ArrayList<File> files;
    private String currentFileName;

    @Override
    public void onCreate() {
        this.text = "";
        this.files = new ArrayList<>();
        for(File f:getApplicationContext().getFilesDir().listFiles()) {
            this.files.add(f);
        }
        this.currentFileName = "";
    }
    @Deprecated
    public int getSize() {return this.files.size();}

    public String getText() {return text;}

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<File> getFiles() {return files;}

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public String getCurrentFileName() {return currentFileName;}

    public void setCurrentFileName(String currentFile) {
        this.currentFileName = currentFile;
    }

    public void saveCurrent() {
        int i = this.getFiles().size();
        File f;
        if (this.getFiles().size() == 1) {
            f = new File(this.getFilesDir(), generateFileName(1));
        } else if (this.getCurrentFileName().equals("")) {
            f = new File(this.getFilesDir(), generateFileName(Integer.parseInt(this
                    .getFiles().get(i - 1).getName().substring(4)) + 1));
        } else {
            f = new File(this.getFilesDir(), this.getCurrentFileName());
        }

        try {
            FileWriter w = new FileWriter(f);
            w.write(this.getText());
            w.close();
        } catch(IOException o) {
            //TODO implement
        }
        this.files.add(f);
    }

    private String generateFileName(int i) {
        String f = "FILE";
        String z = (i < 10) ? "0":"";
        return f+z+this.files.size();
    }
}
