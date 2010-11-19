package com.tailoredshapes.sandbridge;

import netscape.javascript.JSObject;

import java.applet.Applet;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;

public class ActionRunner implements Runnable {

    private final Applet applet;
    private final PrivilegedAction action;
    private final String callback;

    public ActionRunner(Applet applet, PrivilegedAction action, String callback) {
        this.applet = applet;
        this.action = action;
        this.callback = callback;
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    //Stolen mercilessly from Apache Commons.  Thank You!
    private String escapeJavaStyleString(String str) {
        boolean escapeSingleQuote = true;
        boolean escapeForwardSlash = true;

        if (str == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                builder.append("\\u" + hex(ch));
            } else if (ch > 0xff) {
                builder.append("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                builder.append("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        builder.append('\\');
                        builder.append('b');
                        break;
                    case '\n':
                        builder.append('\\');
                        builder.append('n');
                        break;
                    case '\t':
                        builder.append('\\');
                        builder.append('t');
                        break;
                    case '\f':
                        builder.append('\\');
                        builder.append('f');
                        break;
                    case '\r':
                        builder.append('\\');
                        builder.append('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            builder.append("\\u00" + hex(ch));
                        } else {
                            builder.append("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':

                        builder.append('\\');

                        builder.append('\'');
                        break;
                    case '"':
                        builder.append('\\');
                        builder.append('"');
                        break;
                    case '\\':
                        builder.append('\\');
                        builder.append('\\');
                        break;
                    case '/':

                        builder.append('\\');

                        builder.append('/');
                        break;
                    default:
                        builder.append(ch);
                        break;
                }
            }
        }
        return builder.toString();
    }


    public void run() {
        callback(String.format("%s('%s');", callback, escapeJavaStyleString((AccessController.doPrivileged(action)).toString())));
    }

    private void callback(String javascript) {
        JSObject window = JSObject.getWindow(applet);
        window.eval(javascript);
    }
}
