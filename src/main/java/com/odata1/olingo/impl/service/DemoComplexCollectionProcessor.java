package com.odata1.olingo.impl.service;

import com.odata1.olingo.impl.service.business.Service;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmReturnType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.ComplexCollectionProcessor;
import org.apache.olingo.server.api.serializer.ComplexSerializerOptions;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.core.uri.UriResourceFunctionImpl;

import java.util.List;
import java.util.Locale;

public class DemoComplexCollectionProcessor implements ComplexCollectionProcessor {
    private final Service service;
    private OData oData;
    private ServiceMetadata serviceMetadata;

    @Override
    public void init(OData oData, ServiceMetadata serviceMetadata) {
        this.oData = oData;
        this.serviceMetadata = serviceMetadata;
    }

    public DemoComplexCollectionProcessor(Service service) {
        this.service = service;
    }

    @Override
    public void readComplexCollection(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType) throws ODataApplicationException, ODataLibraryException {
        EdmComplexType complexType = serviceMetadata.getEdm().getComplexType(ODataConst.CT_WHATEVER_FQN);


        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(contentType);

        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();
        String selectList = oData.createUriHelper().buildContextURLSelectList(complexType, expandOption, selectOption);

        ContextURL contextUrl = ContextURL.with().selectList(selectList).build();

        ComplexSerializerOptions opts = ComplexSerializerOptions.with().contextURL(contextUrl).select(selectOption).expand(expandOption).build();
        List<ComplexValue> collection = service.getBunchOfWhatever();

        SerializerResult serializedContent = serializer.complexCollection(serviceMetadata, complexType, new Property(null, "whatever", ValueType.COLLECTION_COMPLEX, collection), opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, contentType.toContentTypeString());
    }

    private EntityCollection fetchDatabaseData(String name, UriInfo uriInfo) {
        return null;
    }

    @Override
    public void updateComplexCollection(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType, ContentType contentType1) throws ODataApplicationException, ODataLibraryException {
        throw new ODataApplicationException("not implemented", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
    }

    @Override
    public void deleteComplexCollection(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo) throws ODataApplicationException, ODataLibraryException {
        throw new ODataApplicationException("not implemented", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
    }

}

