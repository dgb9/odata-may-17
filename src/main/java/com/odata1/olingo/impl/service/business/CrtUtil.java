package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.data.CrtData;
import com.odata1.olingo.impl.service.ODataConst;
import com.odata1.olingo.impl.service.provider.CrtConst;
import com.odata1.olingo.impl.tools.EdmIdHolder;
import com.odata1.olingo.impl.tools.EdmUtil;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import java.util.List;

public class CrtUtil {
    public static Entity convert(CrtData crt) {
        Entity e = new Entity();
        e.setType(ODataConst.ET_CRT_FQN.getFullQualifiedNameAsString());

        List<Property> props = e.getProperties();
        props.add(new Property(null, CrtConst.ID, ValueType.PRIMITIVE, crt.getId()));
        props.add(new Property(null, CrtConst.NAME, ValueType.PRIMITIVE, crt.getName()));
        props.add(new Property(null, CrtConst.INFO1, ValueType.PRIMITIVE, crt.getInfo1()));
        props.add(new Property(null, CrtConst.VAL1, ValueType.PRIMITIVE, crt.getVal1()));
        props.add(new Property(null, CrtConst.VAL2, ValueType.PRIMITIVE, crt.getVal2()));
        props.add(new Property(null, CrtConst.VAL3, ValueType.PRIMITIVE, crt.getVal3()));

        e.setId(EdmUtil.createId(ODataConst.ET_CRT, new EdmIdHolder(CrtConst.ID, crt.getId(), true)));

        return e;
    }
}
