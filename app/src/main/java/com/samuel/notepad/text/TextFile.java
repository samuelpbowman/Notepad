package com.samuel.notepad.text;

import java.io.File;

/**
 * Created by samuel on 12/5/16.
 */

public class TextFile extends File {

    private String name;

    public TextFile(String path, String name) {
        super(path);
        this.name = name;
    }

    @Override
    public boolean isDirectory() { return false; }

    @Override
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }



}
