package com.odata1.olingo.impl.tools;

import org.apache.olingo.server.api.ODataApplicationException;

import java.util.List;

/**
 * maps the fields in odata with the ones in the database
 */
public interface IFieldMapper {
    String getODataIdField();

    List<String> getDatabaseFields(String prefix, boolean addAlias, String aliasPrefix);

    List<String> getODataFields();

    String getMappedODataField(String dbField);

    String getMappedDbField(String odataField, boolean removeBracketsFirst);

    DbFieldType getDbFieldType(String dbField) throws ODataApplicationException;
}
