package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.service.provider.CrtConst;
import com.odata1.olingo.impl.service.provider.DetailConst;
import com.odata1.olingo.impl.tools.DbFieldType;
import com.odata1.olingo.impl.tools.FieldMapper;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailFieldMapper extends FieldMapper {
    final private Map<String, DbFieldType> fieldTypeMap;

    private static final String[] ODATA_FIELDS = new String[]{DetailConst.ID, DetailConst.CRT_ID, DetailConst.NAME1, DetailConst.ANOTHER, DetailConst.VAL_DETAIL1, DetailConst.VAL_DETAIL2};

    private static final String[] DB_FIELDS = new String[]{"id", "crtId", "name", "anotherName", "val1", "val2"};

    public DetailFieldMapper() {
        addMapping(DB_FIELDS, ODATA_FIELDS);

        fieldTypeMap = new HashMap<>();

        fieldTypeMap.put("id", DbFieldType.String);
        fieldTypeMap.put("crtId", DbFieldType.String);
        fieldTypeMap.put("name", DbFieldType.String);
        fieldTypeMap.put("anotherName", DbFieldType.String);
        fieldTypeMap.put("val1", DbFieldType.Integer);
        fieldTypeMap.put("val2", DbFieldType.Integer);
    }

    @Override
    public String getODataIdField() {
        return DetailConst.ID;
    }

    @Override
    public DbFieldType getDbFieldType(String dbField) throws ODataApplicationException {
        if (!fieldTypeMap.containsKey(dbField)) {
            String message = String.format("cannot find the field: %s among database field types", dbField);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        return fieldTypeMap.get(dbField);
    }
}
