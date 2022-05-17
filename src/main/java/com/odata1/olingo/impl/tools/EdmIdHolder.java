package com.odata1.olingo.impl.tools;

import org.apache.olingo.server.api.ODataApplicationException;

public class EdmIdHolder {
    private String name;
    private Object value;
    private boolean hasQuotes;

    public EdmIdHolder(String name, Object value, boolean hasQuotes) {
        this.name = name;
        this.value = value;
        this.hasQuotes = hasQuotes;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean isHasQuotes() {
        return hasQuotes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EdmIdHolder{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", hasQuotes=").append(hasQuotes);
        sb.append('}');
        return sb.toString();
    }

    public String toText(boolean addName) throws ODataApplicationException {
        StringBuilder b = new StringBuilder();

        if (addName) {
            b.append(getName());
            b.append("=");
        }

        if (isHasQuotes()) {
            b.append("'");
        }

        b.append(getValue());

        if (isHasQuotes()) {
            b.append("'");
        }

        return b.toString();
    }
}
