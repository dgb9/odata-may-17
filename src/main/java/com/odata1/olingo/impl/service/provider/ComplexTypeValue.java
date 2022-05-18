package com.odata1.olingo.impl.service.provider;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.edm.EdmComplexType;

import java.util.List;

public class ComplexTypeValue {
    private EdmComplexType type;
    private ComplexValue value;
    private List<ComplexValue> items;

    private boolean collection;

    public ComplexTypeValue(EdmComplexType type, ComplexValue value, List<ComplexValue> items, boolean collection) {
        this.type = type;
        this.value = value;
        this.items = items;
        this.collection = collection;
    }

    public EdmComplexType getType() {
        return type;
    }

    public ComplexValue getValue() {
        return value;
    }

    public List<ComplexValue> getItems() {
        return items;
    }

    public boolean isCollection() {
        return collection;
    }
}
