package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.jpa.data.DetailData;
import com.odata1.olingo.impl.service.ODataConst;
import com.odata1.olingo.impl.service.provider.DetailConst;
import com.odata1.olingo.impl.tools.EdmIdHolder;
import com.odata1.olingo.impl.tools.EdmUtil;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.List;

public class DetailUtil {
    public static Entity convert(DetailData detail) {
        Entity e = new Entity();
        e.setType(ODataConst.ET_DETAIL_FQN.getFullQualifiedNameAsString());

        List<Property> props = e.getProperties();
        props.add(new Property(null, DetailConst.ID, ValueType.PRIMITIVE, detail.getId()));
        props.add(new Property(null, DetailConst.NAME1, ValueType.PRIMITIVE, detail.getName()));
        props.add(new Property(null, DetailConst.ANOTHER, ValueType.PRIMITIVE, detail.getAnotherName()));
        props.add(new Property(null, DetailConst.CRT_ID, ValueType.PRIMITIVE, detail.getCrtId()));
        props.add(new Property(null, DetailConst.VAL_DETAIL1, ValueType.PRIMITIVE, detail.getVal1()));
        props.add(new Property(null, DetailConst.VAL_DETAIL2, ValueType.PRIMITIVE, detail.getVal2()));

        e.setId(EdmUtil.createId(ODataConst.ET_DETAIL, new EdmIdHolder(DetailConst.ID, detail.getId(), true)));

        return e;
    }
}
