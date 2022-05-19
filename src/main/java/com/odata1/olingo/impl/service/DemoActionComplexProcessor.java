package com.odata1.olingo.impl.service;

import com.odata1.olingo.impl.service.processor.GetBigValueProcessor;
import com.odata1.olingo.impl.service.processor.IComplexValueProcessor;
import com.odata1.olingo.impl.service.provider.ComplexTypeValue;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmAction;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.ActionComplexProcessor;
import org.apache.olingo.server.api.serializer.ComplexSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceAction;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoActionComplexProcessor implements ActionComplexProcessor, EdmProvider {
    private static Map<String, IComplexValueProcessor> processorMap;

    private OData oData;
    private ServiceMetadata serviceMetadata;

    public DemoActionComplexProcessor() {
    }

    @Override
    public void init(OData oData, ServiceMetadata serviceMetadata) {
        this.oData = oData;
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public void processActionComplex(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataLibraryException {

        List<UriResource> parts = uriInfo.asUriInfoResource().getUriResourceParts();
        int partsSize = parts.size();

        UriResourceAction resourceAction = (UriResourceAction) parts.get(partsSize - 1);
        final EdmAction edmAction = resourceAction.getAction();
        final String actionName = edmAction.getName();
        final ODataDeserializer deserializer = oData.createDeserializer(requestFormat);

        DeserializerResult actionParams = deserializer.actionParameters(oDataRequest.getBody(), edmAction);
        Map<String, Parameter> actionParameter = actionParams.getActionParameters();

        ComplexTypeValue result = processPayloadData(actionName, actionParameter);
        EdmComplexType resultType = result.getType();
        ComplexValue resultValue = result.getValue();

        // basic processing
        // prepare the serializer and everything and then proceed with the info
        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();
        ContextURL contextUrl = ContextURL.with().build();
        ComplexSerializerOptions opts = ComplexSerializerOptions.with().contextURL(contextUrl).select(selectOption).expand(expandOption).build();

        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(responseFormat);
        SerializerResult serializedContent = serializer.complex(serviceMetadata, resultType, new Property(null, "anyNameWorks", ValueType.COMPLEX, resultValue), opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    private ComplexTypeValue processPayloadData(String actionName, Map<String, Parameter> actionParameter) {

        ComplexTypeValue res = null;

        if (processorMap.containsKey(actionName)) {
            IComplexValueProcessor processor = processorMap.get(actionName);

            res = processor.processPayloadData(this, actionName, actionParameter);
        } else {
            throw new RuntimeException("Cannot find processor for the following action: " + actionName);
        }

        return res;

    }

    static {
        processorMap = new HashMap<>();

        processorMap.put(ODataConst.ACTION_GET_BIG_VALUE, new GetBigValueProcessor());
    }

    @Override
    public Edm getEdm() {
        return serviceMetadata.getEdm();
    }
}
