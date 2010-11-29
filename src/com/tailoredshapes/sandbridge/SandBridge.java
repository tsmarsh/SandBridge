package com.tailoredshapes.sandbridge;

import java.applet.Applet;
import java.security.PrivilegedAction;

public class SandBridge extends Applet {

    private void go(String callback, PrivilegedAction<?> opener) {
        new Thread(
                new ActionRunner(
                        this,
                        opener,
                        callback)
        ).start();
    }

    public void open(String filename, String callback) {
        FileOpener opener = new FileOpener(filename);
        go(callback, opener);
    }

    public void delete(String filename, String callback) {
        FileDeleter deleter = new FileDeleter(filename);
        go(callback, deleter);
    }

    public void list(String filename, String callback) {
        DirectoryLister lister = new DirectoryLister(filename);
        go(callback, lister);
    }

    public void save(String filename, String contents, String callback) {
        FileSaver saver = new FileSaver(filename, contents);
        go(callback, saver);
    }
}
