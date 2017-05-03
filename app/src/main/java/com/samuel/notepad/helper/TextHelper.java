package com.samuel.notepad.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Static helper methods for reading the text in and writing text to
 * the main text field in a MainActivity.
 *
 * Created by samuel on 12/5/16.
 */

public class TextHelper {

    public static String readTextFromFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] text = new byte[(int)file.length()];
        stream.read(text);
        stream.close();
        return new String(text);
    }


    public static void writeTextToFile(String text, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(text.getBytes());
        stream.close();
    }

}