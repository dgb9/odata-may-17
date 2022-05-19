package com.odata1.olingo.impl.service.processor;

import com.odata1.olingo.impl.data.CompleteData;
import com.odata1.olingo.impl.data.PairValueData;
import com.odata1.olingo.impl.service.EdmProvider;
import com.odata1.olingo.impl.service.ODataConst;
import com.odata1.olingo.impl.service.business.CompleteUtil;
import com.odata1.olingo.impl.service.provider.ComplexTypeValue;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.commons.api.edm.EdmComplexType;

import java.util.Map;

public class GetBigValueProcessor implements IComplexValueProcessor {

    @Override
    public ComplexTypeValue processPayloadData(EdmProvider edmProvider, String actionName, Map<String, Parameter> actionParameter) {
        Parameter param = actionParameter.get(ODataConst.PARAMETER_COMBINED);
        ComplexValue val = (ComplexValue) param.getValue();

        CompleteData data = CompleteUtil.readCompleteData(val);

        // apply whatever changes we need to data, go to database and all the needed processing
        data.setLastName(data.getLastName() + ", annotated");
        data.setFirstName(data.getFirstName() + ", annotated as well");

        PairValueData firstItem = data.getItems().get(0);
        firstItem.setName(firstItem.getName() + ", another string added");
        firstItem.setStrValue(firstItem.getStrValue() + ", another string added here");

        // because this function returns the same type of object, prepare the return complex type and proceed
        ComplexValue resultValue = CompleteUtil.convert(data);

        // we know the complex type is the complete data
        EdmComplexType complexType = edmProvider.getEdm().getComplexType(ODataConst.CT_COMPLETE_DATA_FQN);
        return new ComplexTypeValue(complexType, resultValue);
    }
}
