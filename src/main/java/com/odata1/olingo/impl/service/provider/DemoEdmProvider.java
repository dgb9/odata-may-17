package com.odata1.olingo.impl.service.provider;

import com.odata1.olingo.impl.service.ODataConst;
import com.odata1.olingo.impl.service.business.DataConst;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {

    @Override
    public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
        CsdlComplexType type = null;

        if (ODataConst.CT_PAIR_VALUE_FQN.equals(complexTypeName)) {
            CsdlProperty name = new CsdlProperty().setName(DataConst.FIELD_NAME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty strValue = new CsdlProperty().setName(DataConst.FIELD_STR_VALUE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            type = new CsdlComplexType();
            type.setProperties(Arrays.asList(name, strValue));
            type.setName(ODataConst.CT_PAIR_VALUE);
        }
        else if (ODataConst.CT_COMPLETE_DATA_FQN.equals(complexTypeName)) {
            CsdlProperty firstName = new CsdlProperty().setName(DataConst.FIELD_FIRST_NAME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty lastName = new CsdlProperty().setName(DataConst.FIELD_LAST_NAME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty items = new CsdlProperty().setName(DataConst.FIELD_ITEMS).setType(ODataConst.CT_PAIR_VALUE_FQN).setCollection(true);

            type = new CsdlComplexType();
            type.setProperties(Arrays.asList(firstName, lastName, items));
            type.setName(ODataConst.CT_COMPLETE_DATA);
        }

        return type;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {
        return null;
    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {

        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(ODataConst.NAMESPACE);

        // add complex types
        List<CsdlComplexType> complexTypes = Arrays.asList(
                getComplexType(ODataConst.CT_PAIR_VALUE_FQN),
                getComplexType(ODataConst.CT_COMPLETE_DATA_FQN)
        );

        schema.setComplexTypes(complexTypes);

        // the actions
        List<CsdlAction> actions = getActions(ODataConst.ACTION_GET_BIG_VALUE_FQN);
        schema.setActions(actions);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
        schemas.add(schema);

        return schemas;
    }

    public CsdlEntityContainer getEntityContainer() throws ODataException {
        CsdlActionImport actionImports = getActionImport(ODataConst.CONTAINER, ODataConst.ACTION_GET_BIG_VALUE);

        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(ODataConst.CONTAINER_NAME);
        entityContainer.setActionImports(Collections.singletonList(actionImports));

        return entityContainer;

    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

        if (entityContainerName == null || entityContainerName.equals(ODataConst.CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(ODataConst.CONTAINER);

            return entityContainerInfo;
        }

        return null;
    }

    @Override
    public List<CsdlAction> getActions(FullQualifiedName actionName) throws ODataException {
        List<CsdlAction> res = new ArrayList<>();

        if (ODataConst.ACTION_GET_BIG_VALUE_FQN.equals(actionName)) {
            CsdlParameter param = new CsdlParameter();
            param.setName(ODataConst.PARAMETER_COMBINED);
            param.setType(ODataConst.CT_COMPLETE_DATA_FQN);
            param.setNullable(false);
            param.setCollection(false);

            List<CsdlParameter> paramList = Collections.singletonList(param);
            CsdlReturnType returnType = new CsdlReturnType().setType(ODataConst.CT_COMPLETE_DATA_FQN);

            CsdlAction action = new CsdlAction();

            action.setName(ODataConst.ACTION_GET_BIG_VALUE);
            action.setParameters(paramList);
            action.setReturnType(returnType);

            res.add(action);
        }

        return res;
    }

    @Override
    public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName) throws ODataException {
        CsdlActionImport res = null;
        if (entityContainer.equals(ODataConst.CONTAINER)) {
            if (actionImportName.equals(ODataConst.ACTION_GET_BIG_VALUE_FQN.getName())) {
                res = new CsdlActionImport()
                        .setName(actionImportName)
                        .setAction(ODataConst.ACTION_GET_BIG_VALUE_FQN);
            }
        }

        return res;
    }
}
