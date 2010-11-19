package com.tailoredshapes.sandbridge;

import netscape.javascript.JSObject;

import java.applet.Applet;
import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class SandBridge extends Applet {

    private class FileOpener implements PrivilegedAction<String> {

        private String filename;

        public FileOpener(String filename) {
            this.filename = filename;
        }

        public String run() {
            BufferedReader reader = null;
            StringBuilder bob = new StringBuilder();

            try {
                reader = new BufferedReader(new FileReader(filename));


                String line = reader.readLine();

                while (line != null) {
                    bob.append(line);
                    line = reader.readLine();
                }
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            return bob.toString();    
        }
    }

    private class FileSaver implements PrivilegedAction<Object> {

        private String filename;
        private String contents;

        public FileSaver( String filename, String contents) {
            this.contents = contents;
            this.filename = filename;
        }

        public Object run() {
            FileWriter writer = null;

            try{
                writer = new FileWriter(filename);
                writer.write(contents);
            } catch (IOException e){
                throw new RuntimeException(e);  
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
    }

    private class FileDeleter implements PrivilegedAction<Boolean> {

        private String filename;

        public FileDeleter(String filename) {
            this.filename = filename;
        }

        public Boolean run() {
            try{
                File file = new File(filename);
                return file.delete();
            }catch(SecurityException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void open(String filename, String callback) {
        String fileContents = AccessController.doPrivileged(new FileOpener(filename));
        String javascript = String.format("%s('%s');", callback, fileContents);

        callback(javascript);
    }

    public void delete(String filename, String callback){
        Boolean deleted = AccessController.doPrivileged(new FileDeleter(filename));

        callback(String.format("%s(%s);", callback, deleted));
    }

    public void save(String filename, String contents, String callback) {
        System.out.println("Saving");
        AccessController.doPrivileged(new FileSaver(filename, contents));

        callback(String.format("%s();", callback));
    }

    private void callback(String javascript) {
        JSObject window = JSObject.getWindow(this);
        window.eval(javascript);
    }
}
