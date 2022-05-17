package com.odata1.olingo.impl.tools;

import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Locale;

public class Parameter {
    private final String name;
    private final Object value;

    public Parameter(String name, String strValue, DbFieldType dbFieldType) throws ODataApplicationException {
        this.name = name;

        if (strValue == null) {
            this.value = null;
        } else {
            try {
                switch (dbFieldType) {
                    case Boolean:
                        value = Boolean.valueOf(strValue);
                        break;
                    case Long:
                        value = Long.valueOf(strValue);
                        break;
                    case Timestamp:
                        value = parseTimestamp(strValue);
                        break;
                    case Double:
                        value = Double.valueOf(strValue);
                        break;
                    case Integer:
                        value = Integer.valueOf(strValue);
                        break;
                    case Date:
                        value = new SimpleDateFormat("yyyy-MM-dd").parse(strValue);
                        break;
                    default:
                        this.value = strValue; // String is included here as well in this default processing
                        break;
                }
            } catch (ODataApplicationException e) {
                throw e;
            } catch (Exception e) {
                throw new ODataApplicationException(e.getMessage(), HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        }
    }

    private Date parseTimestamp(String strValue) throws ODataApplicationException {
        Date val;

        try {
            OffsetDateTime offset = OffsetDateTime.parse(strValue);
            val = Date.from(offset.toInstant());
        } catch (Exception e) {
            throw new ODataApplicationException(e.getMessage(), HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        return val;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Parameter{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
