package com.odata1.olingo.impl.data;

import java.util.Objects;

public class PairValueData {
    private String name;
    private String strValue;

    public PairValueData() {
    }

    public PairValueData(String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairValueData that = (PairValueData) o;
        return Objects.equals(name, that.name) && Objects.equals(strValue, that.strValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strValue);
    }
}
