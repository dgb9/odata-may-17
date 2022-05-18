package com.odata1.olingo.impl.service;

import com.odata1.olingo.impl.service.business.Service;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmAction;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.ActionComplexProcessor;
import org.apache.olingo.server.api.serializer.ComplexSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResourceAction;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.util.Map;

public class DemoActionComplexProcessor implements ActionComplexProcessor {
    private final Service service;

    private OData oData;
    private ServiceMetadata serviceMetadata;

    public DemoActionComplexProcessor(Service service) {
        this.service = service;
    }

    @Override
    public void init(OData oData, ServiceMetadata serviceMetadata) {
        this.oData = oData;
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public void processActionComplex(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

        final EdmAction edmAction = ((UriResourceAction) uriInfo.asUriInfoResource().getUriResourceParts()
                .get(0)).getAction();
        final ODataDeserializer deserializer = oData.createDeserializer(requestFormat);
        final Map<String, Parameter> actionParameter = deserializer.actionParameters(oDataRequest.getBody(), edmAction)
                .getActionParameters();
        Parameter param = actionParameter.get("Combined");
        ComplexValue val = (ComplexValue) param.getValue();
        val.getValue().stream().map((vl) -> {
            String name = vl.getName();
            Object val1 = vl.getValue();

            return String.format("the name: %s and the value: %s", name, val1);
        }).forEach(System.out::println);

        // prepare the serializer and everything and then proceed with the info
        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();

        // basic url
        ContextURL contextUrl = ContextURL.with().build();

        ComplexSerializerOptions opts = ComplexSerializerOptions.with().contextURL(contextUrl).select(selectOption).expand(expandOption).build();

        // we know the complex type is the complete data
        EdmComplexType complexType = serviceMetadata.getEdm().getComplexType(ODataConst.CT_COMPLETE_DATA_FQN);

        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(responseFormat);
        SerializerResult serializedContent = serializer.complex(serviceMetadata, complexType, new Property(null, "info11", ValueType.COMPLEX, val), opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }
}
