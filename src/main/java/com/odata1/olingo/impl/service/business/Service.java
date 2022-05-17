package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.jpa.service.IDetailRepo;
import com.odata1.olingo.impl.service.ODataConst;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class Service {

    @Autowired
    private CrtService crtService;

    @Autowired
    private DetailService detailService;

    @Autowired
    private IDetailRepo detailRepo;

    public EntityCollection fetchCollectionData(String edmSetName, UriInfo uriInfo) throws ODataApplicationException {
        EntityCollection res = null;

        if (ODataConst.ES_CRT.equals(edmSetName)) {
            res = crtService.fetchCollectionData(uriInfo);
        }
        else if (ODataConst.ES_DETAIL.equals(edmSetName)) {
            res = detailService.fetchCollectionData(uriInfo);
        }
        else {
            String message = String.format("cannot resolve the edm set: %s", edmSetName);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        return res;
    }

    public EntityCollection getAllDetails() throws ODataApplicationException {
        List<Entity> entities = detailRepo.findAll().stream().map(DetailUtil::convert).collect(Collectors.toList());

        EntityCollection collection = new EntityCollection();
        collection.setId(entities.get(0).getId());
        collection.getEntities().addAll(entities);

        return collection;
    }

    public ComplexValue getBunchOfWhatever() throws ODataApplicationException {
        List<ComplexValue> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String first = "first name " + i;
            String last = "last name " + i;

            ComplexValue e = new ComplexValue();
            List<Property> properties = e.getValue();

            properties.add(new Property(null, "FirstName", ValueType.PRIMITIVE, first));
            properties.add(new Property(null, "LastName", ValueType.PRIMITIVE, last));

            list.add(e);
        }

        ComplexValue returnValue = new ComplexValue();
        List<Property> values = returnValue.getValue();
        values.add(new Property(null, "Description", ValueType.PRIMITIVE, "this is the description here"));
        values.add(new Property(null, "Another", ValueType.PRIMITIVE, "the second elements"));
        values.add(new Property(null, "Here", ValueType.COLLECTION_COMPLEX, list));

        return returnValue;
    }
}
