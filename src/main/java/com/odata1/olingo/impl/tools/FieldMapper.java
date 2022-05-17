package com.odata1.olingo.impl.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FieldMapper implements IFieldMapper {

    private final HashMap<String, String> dbOdata;
    private final HashMap<String, String> odataDb;

    public FieldMapper() {
        dbOdata = new HashMap<>();
        odataDb = new HashMap<>();
    }

    public void addMapping(String[] dbFields, String[] odataFields) {
        // the two fields need to have same length
        if (dbFields.length != odataFields.length) {
            throw new RuntimeException("field names must be of the same length");
        }

        int length = dbFields.length;
        for (int i = 0; i < length; i++) {
            String dbField = dbFields[i];
            String odataField = odataFields[i];

            addMapping(dbField, odataField);
        }
    }

    public void addMapping(String dbField, String odataField) {
        if (dbOdata.containsKey(dbField)) {
            throw new RuntimeException(String.join(" ", "db field:", dbField, "already provided"));
        }

        if (odataDb.containsKey(odataField)) {
            throw new RuntimeException(String.join(" ", "odata field:", odataField, "already provided"));
        }

        dbOdata.put(dbField, odataField);
        odataDb.put(odataField, dbField);
    }

    @Override
    public List<String> getDatabaseFields(String prefix, boolean addAlias, String aliasPrefix) {
        return dbOdata
                .keySet()
                .stream()
                .map((field) -> prefix + field) // add the prefix
                .map((field) -> addAlias ? " " + aliasPrefix + field : field) // add alias if needed
                .collect(Collectors.toList()); // collect in resulting list
    }

    @Override
    public List<String> getODataFields() {
        return new ArrayList<>(odataDb.keySet());
    }

    @Override
    public String getMappedODataField(String dbField) {
        String odataField = dbOdata.get(dbField);

        if (odataField == null) {
            throw new RuntimeException(String.join(" ", "database field:", dbField, "not found"));
        }

        return odataField;
    }

    @Override
    public String getMappedDbField(String odataField, boolean removeBracketsFirst) {
        String strField = odataField;
        if (removeBracketsFirst) {
            int endIndex = strField.length() - 1;
            strField = strField.substring(1, endIndex);
        }

        String dbField = odataDb.get(strField);

        if (dbField == null) {
            throw new RuntimeException(String.join("", "odata field: ", odataField, "not found"));
        }

        return dbField;
    }
}
