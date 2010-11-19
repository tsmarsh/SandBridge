package com.tailoredshapes.sandbridge;

import netscape.javascript.JSObject;

import java.applet.Applet;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ActionRunner implements Runnable{

    private final Applet applet;
    private final PrivilegedAction action;
    private final String callback;

    public ActionRunner(Applet applet, PrivilegedAction action, String javascript) {
        this.applet = applet;
        this.action = action;
        this.callback = javascript;
    }

    public void run() {
        callback(String.format("%s(\"%s\");", callback, AccessController.doPrivileged(action)));
    }

    private void callback(String javascript) {
        JSObject window = JSObject.getWindow(applet);
        window.eval(javascript);
    }
}
