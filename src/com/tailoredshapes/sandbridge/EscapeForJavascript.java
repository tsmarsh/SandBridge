package com.tailoredshapes.sandbridge;

import java.util.Locale;

class EscapeForJavascript {

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    //Stolen mercilessly from Apache Commons.  Thank You!
    public static String escapeJavaStyleString(String str) {

        if (str == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {

            // handle unicode
            if (ch > 0xfff) {
                builder.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                builder.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                builder.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                handleSpecialCharacters(builder, ch);
            } else {
                handleEscapedChars(builder, ch);
            }
        }
        return builder.toString();
    }

    private static void handleEscapedChars(StringBuilder builder, char ch) {
        switch (ch) {
            case '\'':

                builder.append("\\'");
                break;
            case '"':
                builder.append('\\').append('"');
                break;
            case '\\':
                builder.append("\\\\");
                break;
            case '/':

                builder.append("\\/");
                break;
            default:
                builder.append(ch);
                break;
        }
    }

    private static void handleSpecialCharacters(StringBuilder builder, char ch) {
        switch (ch) {
            case '\b':
                builder.append("\\b");
                break;
            case '\n':
                builder.append("\\n");
                break;
            case '\t':
                builder.append("\\t");
                break;
            case '\f':
                builder.append("\\f");
                break;
            case '\r':
                builder.append("\\r");
                break;
            default:
                if (ch > 0xf) {
                    builder.append("\\u00").append(hex(ch));
                } else {
                    builder.append("\\u000").append(hex(ch));
                }
                break;
        }
    }
}