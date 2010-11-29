package com.tailoredshapes.sandbridge;

import java.io.File;
import java.security.PrivilegedAction;

class FileDeleter implements PrivilegedAction<Boolean> {

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
