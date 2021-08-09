package br.com.sankhya.commercial.analisegiro.util;

public class StringUtils {

    public static String getNullAsEmpty(Object arg0) {
        if (arg0 == null) {
            return "";
        }

        return arg0.toString();
    }

    public static String replaceString(String source, String stringToFind, String stringToReplace) {
        StringBuffer buf = new StringBuffer(source);
        int startSearch = 0;
        int foundIndex;
        int targetSize = stringToFind.length();

        while ((foundIndex = buf.toString().indexOf(stringToFind, startSearch)) != -1) {
            buf.delete(foundIndex, foundIndex + targetSize);
            buf.insert(foundIndex, stringToReplace);
        }

        return buf.toString();
    }

    public static void replaceString(String search, String replace, StringBuffer buf) {
        replaceString(search, replace, buf, true);
    }


    public static void replaceString(String search, String replace, StringBuffer buf, boolean replaceAll) {
        int startIndex = 0;

        if (replace == null) {
            replace = "";
        }

        boolean replaceConstainsSearch = replace.indexOf(search) > -1;

        while ((startIndex = buf.indexOf(search, startIndex)) > -1) {
            buf.replace(startIndex, startIndex + search.length(), replace);

            if (replaceConstainsSearch) { // evita loop infinito
                startIndex += replace.length();
            }

            if (!replaceAll) {
                break;
            }
        }
    }
    public static String getEmptyAsNull(Object arg0) {
        return getEmptyAsNull(getNullAsEmpty(arg0));
    }

    public static String getEmptyAsNull(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        return  s;
    }

    public static String getNullAsEmpty(String arg0) {
        if (arg0 == null) {
            return "";
        }

        return arg0;
    }


}
