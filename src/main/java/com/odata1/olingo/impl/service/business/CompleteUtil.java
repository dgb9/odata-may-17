package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.data.CompleteData;
import com.odata1.olingo.impl.data.PairValueData;
import com.odata1.olingo.impl.service.ODataConst;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;
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
        CompleteData res = null;

        if (value != null) {
            Map<String, Property> propertyMap = new HashMap<>();
            value.getValue().forEach((property) -> {
                String name = property.getName();

                propertyMap.put(name, property);
            });

            // get each property and proceed
            res = new CompleteData();
            res.setFirstName(getStringProperty(propertyMap, DataConst.FIELD_FIRST_NAME));
            res.setLastName(getStringProperty(propertyMap, DataConst.FIELD_LAST_NAME));

            // process the items
            Object valItems = propertyMap.get(DataConst.FIELD_ITEMS).getValue();
            if (valItems instanceof List) {
                List<ComplexValue> list = (List<ComplexValue>) valItems;
                List<PairValueData> resItems = res.getItems();

                for (ComplexValue item : list) {
                    PairValueData pvData = readPairValueData(item);
                    resItems.add(pvData);
                }
            }

        }



        return res;
    }

    private static String getStringProperty(Map<String, Property> propertyMap, String fieldName) {
        String res = null;

        if (propertyMap.containsKey(fieldName)) {
            Property prop = propertyMap.get(fieldName);
            Object val = prop.getValue();

            res = (String) val;
        }

        return res;
    }

    private static Double getDoubleProperty(Map<String, Property> propertyMap, String fieldName) {
        Double res = null;

        if (propertyMap.containsKey(fieldName)) {
            Property prop = propertyMap.get(fieldName);
            Object val = prop.getValue();

            res = (Double) val;
        }

        return res;
    }

    public static PairValueData readPairValueData(ComplexValue value) {
        PairValueData res = null;

        if (value != null) {
            Map<String, Property> propertyMap = new HashMap<>();
            value.getValue().forEach((property) -> {
                String name = property.getName();

                propertyMap.put(name, property);
            });

            res = new PairValueData();

            res.setName(getStringProperty(propertyMap, DataConst.FIELD_NAME));
            res.setStrValue(getStringProperty(propertyMap, DataConst.FIELD_STR_VALUE));
        }

        return res;
    }
}
