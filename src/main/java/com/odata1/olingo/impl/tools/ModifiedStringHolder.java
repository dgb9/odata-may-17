package com.odata1.olingo.impl.tools;

public class ModifiedStringHolder {
    private final boolean quoteRemoved;
    private final String value;

    public ModifiedStringHolder(boolean quoteRemoved, String value) {
        this.quoteRemoved = quoteRemoved;
        this.value = value;
    }

    public boolean isQuoteRemoved() {
        return quoteRemoved;
    }

    public String getValue() {
        return value;
    }
}
