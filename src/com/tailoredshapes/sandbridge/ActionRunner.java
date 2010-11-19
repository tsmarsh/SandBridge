package com.tailoredshapes.sandbridge;

import netscape.javascript.JSObject;

import java.applet.Applet;
import java.security.AccessController;
import java.security.PrivilegedAction;

import static com.tailoredshapes.sandbridge.EscapeForJavascript.escapeJavaStyleString;

class ActionRunner implements Runnable {

    private final Applet applet;
    private final PrivilegedAction<?> action;
    private final String callback;

    public ActionRunner(Applet applet, PrivilegedAction action, String callback) {
        this.applet = applet;
        this.action = action;
        this.callback = callback;
    }

    public void run() {
        callback(String.format("%s('%s');", callback, escapeJavaStyleString((AccessController.doPrivileged(action)).toString())));
    }

    private void callback(String javascript) {
        JSObject window = JSObject.getWindow(applet);
        window.eval(javascript);
    }
}
