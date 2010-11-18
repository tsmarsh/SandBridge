package com.tailoredshapes.sandbridge;

import netscape.javascript.JSObject;

import java.applet.Applet;
import java.io.*;

public class SandBridge extends Applet {

    public void open(String filename, String callback) throws IOException {
        FileWriter writer = new FileWriter(filename);
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        StringBuilder bob = new StringBuilder();
        String line = reader.readLine();

        while(line != null){
            bob.append(line);
            line = reader.readLine();
        }

        reader.close();

        JSObject window = JSObject.getWindow(this);
        String javascript = String.format("%s('%s');", callback, bob.toString());
        window.eval(javascript);
    }

    public void delete(String filename, String callback) throws IOException {
        File file = new File(filename);
        file.delete();

        JSObject window = JSObject.getWindow(this);
        window.eval(String.format("%s();", callback));
    }

    public void save(String filename, String contents, String callback) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(contents);
        writer.close();
        JSObject window = JSObject.getWindow(this);
        window.eval(String.format("%s();", callback));
    }
}
