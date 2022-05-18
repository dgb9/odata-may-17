package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.data.CompleteData;
import com.odata1.olingo.impl.data.PairValueData;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompleteUtil {
    public static ComplexValue convert(PairValueData data) {
        Property name = new Property(null, DataConst.FIELD_NAME, ValueType.PRIMITIVE, data.getName());
        Property strValue = new Property(null, DataConst.FIELD_STR_VALUE, ValueType.PRIMITIVE, data.getStrValue());

        ComplexValue value = new ComplexValue();
        List<Property> propertyList = Arrays.asList(name, strValue);
        value.getValue().addAll(propertyList);

        return value;
    }

    public static ComplexValue convert(CompleteData data) {
        Property firstName = new Property(null, DataConst.FIELD_FIRST_NAME, ValueType.PRIMITIVE, data.getFirstName());
        Property lastName = new Property(null, DataConst.FIELD_LAST_NAME, ValueType.PRIMITIVE, data.getLastName());

        List<ComplexValue> itemList = data.getItems()
                .stream()
                .map(CompleteUtil::convert)
                .collect(Collectors.toList());

        Property items = new Property(null, DataConst.FIELD_ITEMS, ValueType.COLLECTION_COMPLEX, itemList);

        ComplexValue value = new ComplexValue();
        List<Property> propertyList = Arrays.asList(firstName, lastName, items);
        value.getValue().addAll(propertyList);

        return value;
    }

    public static CompleteData readCompleteData(ComplexValue value) {
        return null;
    }

    public static PairValueData readPairValueData(ComplexValue value) {
        return null;
    }
}
