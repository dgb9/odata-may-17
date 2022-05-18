package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.data.CrtData;
import com.odata1.olingo.impl.data.DetailData;
import com.odata1.olingo.impl.service.ODataConst;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Service {

    public EntityCollection fetchCollectionData(String edmSetName, UriInfo uriInfo) throws ODataApplicationException, URISyntaxException {
        EntityCollection res = new EntityCollection();

        if (ODataConst.ES_DETAIL.equals(edmSetName)) {
            getDetailCollection(res);

        } else if (ODataConst.ES_CRT.equals(edmSetName)) {
            getCrtCollection(res);
        }

        return res;
    }

    private void getCrtCollection(EntityCollection res) throws URISyntaxException {
        List<Entity> entities = getCrtDataStream()
                .map((crt) -> {
                    Entity entity = CrtUtil.convert(crt);
                    String crtId = crt.getId();

                    // get the details for the crt
                    List<Entity> details = getDetailDataStream()
                            .filter(detail -> detail.getCrtId().equals(crtId))
                            .map(DetailUtil::convert).collect(Collectors.toList());

                    EntityCollection detailCollection = new EntityCollection();
                    try {
                        detailCollection.setId(new URI("something" + crtId));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    detailCollection.getEntities().addAll(details);

                    // add the link to the crt
                    Link link = new Link();
                    link.setInlineEntitySet(detailCollection);
                    link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
                    link.setTitle(ODataConst.NAV_DET);
                    link.setRel(Constants.NS_ASSOCIATION_LINK_REL + ODataConst.NAV_DET);
                    link.setHref(detailCollection.getId().toASCIIString());

                    entity.getNavigationLinks().add(link);

                    return entity;
                })
                .collect(Collectors.toList());

        res.setId(new URI("id2"));
        res.getEntities().addAll(entities);
    }

    private void getDetailCollection(EntityCollection res) throws URISyntaxException {
        List<Entity> entities = getDetailDataStream()
                .map(DetailUtil::convert)
                .collect(Collectors.toList());

        res.setId(new URI("id1"));
        res.getEntities().addAll(entities);
    }

    private Stream<CrtData> getCrtDataStream() {
        return Stream.of(
                new CrtData("1", "name for crt1", "info for crt 1", 12.2, 2.2, 2.36),
                new CrtData("2", "name for crt2", "info for crt 2", 12.2, 2.2, 2.36)
        );
    }

    private Stream<DetailData> getDetailDataStream() {
        return Stream.of(
                new DetailData("id1", "1", "name for 1", "another 1 name", 12, 23),
                new DetailData("id2", "1", "name for 2", "another 2 name", 12, 23),
                new DetailData("id3", "2", "name for 3", "another 3 name", 12, 23),
                new DetailData("id4", "2", "name for 4", "another 4 name", 12, 23),
                new DetailData("id5", "2", "name for 5", "another 5 name", 12, 23));
    }

    public EntityCollection getAllDetails() throws ODataApplicationException, URISyntaxException {
        return fetchCollectionData(ODataConst.ES_DETAIL, null);
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
