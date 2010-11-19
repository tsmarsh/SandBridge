package com.tailoredshapes.sandbridge;

import java.applet.Applet;
import java.io.*;
import java.security.PrivilegedAction;

public class SandBridge extends Applet {

    private class FileOpener implements PrivilegedAction<String> {

        private final String filename;

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

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //Not much we can do now.
                    }
                }

            }

            return bob.toString();
        }
    }

    private class FileSaver implements PrivilegedAction<Boolean> {

        private final String filename;
        private final String contents;

        public FileSaver(String filename, String contents) {
            this.contents = contents;
            this.filename = filename;
        }

        public Boolean run() {
            FileWriter writer = null;

            try {
                writer = new FileWriter(filename);
                writer.write(contents);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        //Not much we can do now.
                    }
                }
            }
            return true;
        }
    }

    private class FileDeleter implements PrivilegedAction<Boolean> {

        private final String filename;

        public FileDeleter(String filename) {
            this.filename = filename;
        }

        public Boolean run() {
            try {
                File file = new File(filename);
                return file.delete();
            } catch (SecurityException e) {
                return false;
            }
        }
    }

    public void open(String filename, String callback) {
        new Thread(
                new ActionRunner(
                        this,
                        new FileOpener(filename),
                        callback)
        ).start();
    }

    public void delete(String filename, String callback) {
        new Thread(
                new ActionRunner(
                        this,
                        new FileDeleter(filename),
                        callback)
        ).start();
    }

    public void save(String filename, String contents, String callback) {
        new Thread(
                new ActionRunner(
                        this,
                        new FileSaver(filename, contents),
                        callback)
        ).start();
    }
}
