package com.odata1.olingo.impl.service.processor;

import com.odata1.olingo.impl.service.EdmProvider;
import com.odata1.olingo.impl.service.provider.ComplexTypeValue;
import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.server.api.ServiceMetadata;

import java.util.Map;

public interface IComplexValueProcessor {

    // gets the list of parameters from the action request, action name (to dispatch) and returns a complex type value
    // which holds the namespace of the type being returned together with the actual payload data
    ComplexTypeValue processPayloadData(EdmProvider edmProvider, String actionName, Map<String, Parameter> actionParameter);

}

