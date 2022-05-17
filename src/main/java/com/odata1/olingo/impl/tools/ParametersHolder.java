package com.odata1.olingo.impl.tools;

import org.apache.olingo.server.api.ODataApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ParametersHolder {
    private final List<Parameter> parameters;
    int index;

    public ParametersHolder() {
        this.parameters = new ArrayList<>();
        index = 0;
    }

    public void addParameter(String name, String strValue, DbFieldType dbFieldType) throws ODataApplicationException {
        Parameter param = new Parameter(name, strValue, dbFieldType);
        parameters.add(param);
    }

    public Stream<Parameter> getParameters() {
        return parameters.stream();
    }

    public String getNextName() {
        index ++;
        String name = "param" + index;

        return name;
    }

    public String getPrependedCurrentName() {
        return ":param" + index;
    }
}
