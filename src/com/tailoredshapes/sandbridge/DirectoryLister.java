package com.tailoredshapes.sandbridge;

import java.io.File;
import java.security.PrivilegedAction;

class DirectoryLister implements PrivilegedAction<String> {

    private final String filename;

    public DirectoryLister(String filename) {
        this.filename = filename;
    }

    public String run() {
        File dir = new File(filename);
        StringBuilder builder = new StringBuilder("[");
        if (dir.isDirectory()) {
            String[] contents = dir.list();

            for (int i = 0; i < contents.length; i++) {
                builder.append(String.format("\"%s\"", contents[i]));
                if (i != contents.length - 1) {
                    builder.append(",");

                }
            }
        }
        builder.append("]");
        return builder.toString();
    }
}