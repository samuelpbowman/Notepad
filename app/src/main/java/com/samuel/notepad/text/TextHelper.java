package com.samuel.notepad.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by samuel on 12/5/16.
 */

public class TextHelper {

    public static String readTextFromFile(File file) throws IOException {
        /*StringBuilder text = new StringBuilder();
        FileReader read = new FileReader(file);
        BufferedReader buf = new BufferedReader(read);
        String s;
        while ((s = buf.readLine()) != null) {
            text.append(s);
            text.append("\n");
        }
        buf.close();
        read.close();
        return text.toString();*/
        FileInputStream stream = new FileInputStream(file);
        byte[] text = new byte[(int)file.length()];
        stream.read(text);
        stream.close();
        return new String(text);
    }


    public static void writeTextToFile(String text, File file) throws IOException {
        /*FileWriter write = new FileWriter(file);
        BufferedWriter buf = new BufferedWriter(write);
        if(getTextFileOfSameName(file, file.getParentFile().listFiles()) != null) {
            getTextFileOfSameName(file, file.getParentFile().listFiles()).delete();
        }
        buf.write(text);
        buf.close();
        write.close();*/
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