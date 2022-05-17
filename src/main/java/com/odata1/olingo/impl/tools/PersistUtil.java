package com.odata1.olingo.impl.tools;


public class PersistUtil {
    private static final String QUOTE = "'";

    public static ModifiedStringHolder removeQuotes(String strLiteral) {
        boolean res = false;

        String strResult = strLiteral;
        if (strResult.startsWith(QUOTE) && strResult.endsWith(QUOTE)) {
            res = true;
            int originalLength = strResult.length();

            strResult = strResult.substring(1, originalLength - 1);
        }

        return new ModifiedStringHolder(res, strResult);
    }
}
