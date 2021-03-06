package com.odata1.olingo.impl.service;

import com.odata1.olingo.impl.service.business.Service;
import com.odata1.olingo.impl.service.provider.ComplexTypeValue;
import org.apache.olingo.commons.api.data.*;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.commons.core.edm.EdmFunctionImportImpl;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.ComplexCollectionProcessor;
import org.apache.olingo.server.api.processor.ComplexProcessor;
import org.apache.olingo.server.api.serializer.ComplexSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.core.uri.UriResourceFunctionImpl;

import java.util.List;
import java.util.Locale;

public class DemoComplexProcessor implements ComplexProcessor {
    private final Service service;
    private OData oData;
    private ServiceMetadata serviceMetadata;

    @Override
    public void init(OData oData, ServiceMetadata serviceMetadata) {
        this.oData = oData;
        this.serviceMetadata = serviceMetadata;
    }

    public DemoComplexProcessor(Service service) {
        this.service = service;
    }

    @Override
    public void readComplex(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType) throws ODataApplicationException, ODataLibraryException {

        ComplexTypeValue complexTypeValue = getComplexTypeValue(uriInfo);
        EdmComplexType complexType = complexTypeValue.getType();
        Object returnValue = null;

        if (complexTypeValue.isCollection()) {
            returnValue = complexTypeValue.getItems();
        } else {
            returnValue = complexTypeValue.getValue();
        }


        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();

        // basic url
        ContextURL contextUrl = ContextURL.with().build();

        ComplexSerializerOptions opts = ComplexSerializerOptions.with().contextURL(contextUrl).select(selectOption).expand(expandOption).build();

        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(contentType);
        SerializerResult serializedContent = serializer.complex(serviceMetadata, complexType, new Property(null, "info11", ValueType.COMPLEX, returnValue), opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, contentType.toContentTypeString());
    }

    private ComplexTypeValue getComplexTypeValue(UriInfo uriInfo) throws ODataApplicationException {
        // based on uri info we retrieve the function name and the parameters
        // get the latest part of the path part list
        ComplexTypeValue res = null;
        List<UriResource> parts = uriInfo.getUriResourceParts();
        UriResource lastPart = parts.get(parts.size() - 1);

        // we expect a function
        UriResourceFunctionImpl functionImport = (UriResourceFunctionImpl) lastPart;
        FullQualifiedName functionFqn = functionImport.getFunctionImport().getFunctionFqn();
        if (ODataConst.FUNCTION_COUNT_CATEGORIES_FQN.equals(functionFqn)) {
            // this is function Fqn, process as needed
            res = getCountCategoriesFunctionResult();
        }

        // ComplexValue returnValue = service.getBunchOfWhatever();
        return res;
    }

    private ComplexTypeValue getCountCategoriesFunctionResult() throws ODataApplicationException {
        ComplexTypeValue res;
        ComplexValue value = service.getBunchOfWhatever();
        EdmComplexType cType = serviceMetadata.getEdm().getComplexType(ODataConst.CT_COMPLICATED_FQN);

        res = new ComplexTypeValue(cType, value, null, false);
        return res;
    }

    @Override
    public void updateComplex(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType, ContentType contentType1) throws ODataApplicationException, ODataLibraryException {
        throw new ODataApplicationException("update not implemented", HttpStatusCode.BAD_GATEWAY.getStatusCode(), Locale.ENGLISH);
    }

    @Override
    public void deleteComplex(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo) throws ODataApplicationException, ODataLibraryException {
        throw new ODataApplicationException("delete not implemented", HttpStatusCode.BAD_GATEWAY.getStatusCode(), Locale.ENGLISH);
    }

}

