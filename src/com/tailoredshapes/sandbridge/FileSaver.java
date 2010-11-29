package com.tailoredshapes.sandbridge;

import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivilegedAction;

class FileSaver implements PrivilegedAction<Boolean> {

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
                    return false;
                }
            }
        }
        return true;
    }
}