package com.odata1.olingo.impl.service;

import com.odata1.olingo.impl.service.business.Service;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.core.uri.UriResourceFunctionImpl;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class DemoEntityCollectionProcessor implements EntityCollectionProcessor {


    private OData oData;
    private ServiceMetadata serviceMetadata;

    private Service service;

    public DemoEntityCollectionProcessor(Service service) {
        this.service = service;
    }

    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.oData = odata;
        this.serviceMetadata = serviceMetadata;
    }

    public void readEntityCollection(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType) throws ODataApplicationException, SerializerException  {
        if (isFunctionProcessing(uriInfo)) {
            try {
                processFunction(oDataRequest, oDataResponse, uriInfo, contentType);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        else if (isEntitySet(uriInfo)) {
            try {
                processEntitySet(oDataRequest, oDataResponse, uriInfo, contentType);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new ODataApplicationException(
                    "The request must be either entity set processing or function implementation invocation: ",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    private void processFunction(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType) throws SerializerException, ODataApplicationException, URISyntaxException {
        // get the function name, the parameters and then dispatch the calls
        UriResourceFunctionImpl functionData = (UriResourceFunctionImpl) uriInfo.getUriResourceParts().get(0);
        EdmFunction edmFunction = functionData.getFunction();
        List<UriParameter> params = functionData.getParameters();
        EdmEntitySet edmEntitySet =functionData.getFunctionImport().getReturnedEntitySet();

        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(contentType);

        // let's just put some entity collection of the Detail type that will fit the function
        EntityCollection entitySet = service.getAllDetails();

        // 4th: Now serialize the content: transform from the EntitySet object to InputStream
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();
        String selectList = oData.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption, selectOption);


        ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList).build();

        final String id = oDataRequest.getRawBaseUri() + "/" + edmEntitySet.getName();

        EntityCollectionSerializerOptions opts =
                EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl).select(selectOption).expand(expandOption).build();
        SerializerResult serializedContent = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet, opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, contentType.toContentTypeString());
    }

    private boolean isFunctionProcessing(UriInfo uriInfo) {
        boolean res = false;
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();

        // we only use one entry
        if (resourcePaths.size() > 0) {
            UriResource firstResourcePath = resourcePaths.get(0);

            res = firstResourcePath instanceof UriResourceFunctionImpl;
        }

        return res;
    }

    private boolean isEntitySet(UriInfo uriInfo) {
        boolean res = false;
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();

        // we only use one entry
        if (resourcePaths.size() > 0) {
            UriResource firstResourcePath = resourcePaths.get(0);

            res = firstResourcePath instanceof UriResourceEntitySet;
        }

        return res;
    }

    private void processEntitySet(ODataRequest oDataRequest, ODataResponse oDataResponse, UriInfo uriInfo, ContentType contentType) throws ODataApplicationException, SerializerException, URISyntaxException {
        EdmEntitySet edmEntitySet = getEdmEntitySet(uriInfo);

        // 2nd: fetch the data from backend for this requested EntitySetName // it has to be delivered as EntitySet object
        EntityCollection entitySet = fetchDatabaseData(edmEntitySet.getName(), uriInfo);

        // 3rd: create a serializer based on the requested format (json)
        ODataSerializer serializer = oData.createSerializer(contentType);

        // 4th: Now serialize the content: transform from the EntitySet object to InputStream
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // select option
        SelectOption selectOption = uriInfo.getSelectOption();
        ExpandOption expandOption = uriInfo.getExpandOption();
        String selectList = oData.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption, selectOption);


        ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList).build();

        final String id = oDataRequest.getRawBaseUri() + "/" + edmEntitySet.getName();

        EntityCollectionSerializerOptions opts =
                EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl).select(selectOption).expand(expandOption).build();
        SerializerResult serializedContent = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet, opts);

        // Finally: configure the response object: set the body, headers and status code
        oDataResponse.setContent(serializedContent.getContent());
        oDataResponse.setStatusCode(HttpStatusCode.OK.getStatusCode());
        oDataResponse.setHeader(HttpHeader.CONTENT_TYPE, contentType.toContentTypeString());
    }

    private EdmEntitySet getEdmEntitySet(UriInfo uriInfo) {
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0); // in our example, the first segment is the EntitySet
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

        return edmEntitySet;
    }

    private EntityCollection fetchDatabaseData(String edmSetName, UriInfo uriInfo) throws ODataApplicationException, URISyntaxException {
        return service.fetchCollectionData(edmSetName, uriInfo);
    }

}
