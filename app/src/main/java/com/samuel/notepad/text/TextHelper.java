package com.samuel.notepad.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
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

    private static File getTextFileOfSameName(File file, File[] files) {
        for(File f:files) {
            if(f.getName().equals(file.getName())) {
                return f;
            }
        }
        return null;
    }
}