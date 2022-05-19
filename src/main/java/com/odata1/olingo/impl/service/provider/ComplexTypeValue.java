package com.odata1.olingo.impl.service.provider;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.edm.EdmComplexType;

import java.util.Objects;

public class ComplexTypeValue {
    private EdmComplexType type;
    private ComplexValue value;

    public ComplexTypeValue(EdmComplexType type, ComplexValue value) {
        this.type = type;
        this.value = value;
    }

    public EdmComplexType getType() {
        return type;
    }

    public ComplexValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComplexTypeValue{");
        sb.append("type=").append(type);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexTypeValue that = (ComplexTypeValue) o;
        return Objects.equals(type, that.type) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
