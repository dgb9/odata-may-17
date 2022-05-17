package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.service.provider.CrtConst;
import com.odata1.olingo.impl.tools.DbFieldType;
import com.odata1.olingo.impl.tools.FieldMapper;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrtFieldMapper extends FieldMapper {
    final private Map<String, DbFieldType> fieldTypeMap;

    private static final String[] ODATA_FIELDS = new String[]{CrtConst.ID, CrtConst.INFO1, CrtConst.NAME, CrtConst.VAL1, CrtConst.VAL2, CrtConst.VAL3};

    private static final String[] DB_FIELDS = new String[]{"id", "info1", "name", "val1", "val2", "val3"};

    public CrtFieldMapper() {
        addMapping(DB_FIELDS, ODATA_FIELDS);

        fieldTypeMap = new HashMap<>();

        fieldTypeMap.put("id", DbFieldType.String);
        fieldTypeMap.put("info1", DbFieldType.String);
        fieldTypeMap.put("name", DbFieldType.String);
        fieldTypeMap.put("val1", DbFieldType.Double);
        fieldTypeMap.put("val2", DbFieldType.Double);
        fieldTypeMap.put("val3", DbFieldType.Double);
    }

    @Override
    public String getODataIdField() {
        return CrtConst.ID;
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
