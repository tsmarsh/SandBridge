package com.tailoredshapes.sandbridge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivilegedAction;

class FileOpener implements PrivilegedAction<String> {

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
                    return e.getMessage();
                }
            }

        }

        return bob.toString();
    }
}
