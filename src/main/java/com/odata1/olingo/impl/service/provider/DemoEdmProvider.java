package com.odata1.olingo.impl.service.provider;

import com.odata1.olingo.impl.service.ODataConst;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {

    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

        // this method is called for each EntityType that are configured in the Schema
        CsdlEntityType entityType = null;

        if (entityTypeName.equals(ODataConst.ET_CRT_FQN)) {
            // create EntityType properties
            CsdlProperty id = new CsdlProperty().setName(CrtConst.ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName(CrtConst.NAME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty info1 = new CsdlProperty().setName(CrtConst.INFO1).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty val1 = new CsdlProperty().setName(CrtConst.VAL1).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty val2 = new CsdlProperty().setName(CrtConst.VAL2).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty val3 = new CsdlProperty().setName(CrtConst.VAL3).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());

            // create PropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName(CrtConst.ID);

            CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                    .setName(ODataConst.NAV_DET)
                    .setType(ODataConst.ET_DETAIL_FQN)
                    .setNullable(false)
                    .setCollection(true);

            List<CsdlNavigationProperty> navs = Collections.singletonList(navProp);

            // configure EntityType
            entityType = new CsdlEntityType();
            entityType.setName(ODataConst.ET_CRT);
            entityType.setProperties(Arrays.asList(id, name, info1, val1, val2, val3));
            entityType.setKey(Collections.singletonList(propertyRef));
            entityType.setNavigationProperties(navs);
        } else if (entityTypeName.equals(ODataConst.ET_DETAIL_FQN)) {
            // create EntityType properties
            CsdlProperty id = new CsdlProperty().setName(DetailConst.ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty crtId = new CsdlProperty().setName(DetailConst.CRT_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty name1 = new CsdlProperty().setName(DetailConst.NAME1).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty another = new CsdlProperty().setName(DetailConst.ANOTHER).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty valDetail1 = new CsdlProperty().setName(DetailConst.VAL_DETAIL1).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty valDetail2 = new CsdlProperty().setName(DetailConst.VAL_DETAIL2).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create PropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName(DetailConst.ID);

            entityType = new CsdlEntityType();
            entityType.setName(ODataConst.ET_DETAIL);
            entityType.setProperties(Arrays.asList(id, crtId, name1, another, valDetail1, valDetail2));
            entityType.setKey(Collections.singletonList(propertyRef));
        }

        return entityType;
    }

    @Override
    public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
        CsdlComplexType type = null;

        if (ODataConst.CT_WHATEVER_FQN.equals(complexTypeName)) {
            CsdlProperty firstName = new CsdlProperty().setName("FirstName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty lastName = new CsdlProperty().setName("LastName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            type = new CsdlComplexType();
            type.setProperties(Arrays.asList(firstName, lastName));
            type.setName(ODataConst.CT_WHATEVER);
        }

        return type;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        CsdlEntitySet entitySet = null;

        if (entityContainer.equals(ODataConst.CONTAINER)) {

            if (entitySetName.equals(ODataConst.ES_CRT)) {
                CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setPath(ODataConst.NAV_DET); // the path from entity type to navigation property
                navPropBinding.setTarget(ODataConst.ES_DETAIL); //target entitySet, where the nav prop points to

                List<CsdlNavigationPropertyBinding> navPropBindingList = Collections.singletonList(navPropBinding);

                entitySet = new CsdlEntitySet();

                entitySet.setName(ODataConst.ES_CRT);
                entitySet.setType(ODataConst.ET_CRT_FQN);
                entitySet.setNavigationPropertyBindings(navPropBindingList);
            } else if (entitySetName.equals(ODataConst.ES_DETAIL)) {
                entitySet = new CsdlEntitySet();

                entitySet.setName(ODataConst.ES_DETAIL);
                entitySet.setType(ODataConst.ET_DETAIL_FQN);
            }
        }

        return entitySet;

    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {

        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(ODataConst.NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
        entityTypes.add(getEntityType(ODataConst.ET_CRT_FQN));
        entityTypes.add(getEntityType(ODataConst.ET_DETAIL_FQN));
        schema.setEntityTypes(entityTypes);

        // add complex types
        List<CsdlComplexType> complexTypes = Collections.singletonList(getComplexType(ODataConst.CT_WHATEVER_FQN));
        schema.setComplexTypes(complexTypes);

        // add functions
        List<CsdlFunction> functions = getFunctions(ODataConst.FUNCTION_COUNT_CATEGORIES_FQN);
        schema.setFunctions(functions);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
        schemas.add(schema);

        return schemas;
    }

    public CsdlEntityContainer getEntityContainer() throws ODataException {
        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
        entitySets.add(getEntitySet(ODataConst.CONTAINER, ODataConst.ES_CRT));
        entitySets.add(getEntitySet(ODataConst.CONTAINER, ODataConst.ES_DETAIL));

        // the functions
        List<CsdlFunctionImport> functionImports = Collections.singletonList(
                getFunctionImport(ODataConst.CONTAINER, ODataConst.FUNCTION_COUNT_CATEGORIES)
        );

        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(ODataConst.CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);
        entityContainer.setFunctionImports(functionImports);

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
    public List<CsdlFunction> getFunctions(FullQualifiedName functionName) throws ODataException {
        List<CsdlFunction> res = null;

        // check for each function
        if (ODataConst.FUNCTION_COUNT_CATEGORIES_FQN.equals(functionName)) {
            CsdlParameter paramCountFirst = new CsdlParameter();
            paramCountFirst.setName(ODataConst.PARAMETER_COUNT_FIRST);
            paramCountFirst.setNullable(false);
            paramCountFirst.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlParameter paramCountSecond = new CsdlParameter();
            paramCountSecond.setName(ODataConst.PARAMETER_COUNT_SECOND);
            paramCountSecond.setNullable(false);
            paramCountSecond.setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());

            List<CsdlParameter> parameters = Arrays.asList(paramCountFirst, paramCountSecond);

            CsdlReturnType returnType = new CsdlReturnType();
            returnType.setCollection(true);
            returnType.setType(ODataConst.CT_WHATEVER_FQN);

            CsdlFunction function = new CsdlFunction();
            function.setName(ODataConst.FUNCTION_COUNT_CATEGORIES_FQN.getName());
            function.setParameters(parameters);
            function.setReturnType(returnType);

            res = Collections.singletonList(function);
        }

        return res == null ? new ArrayList<>() : res;
    }

    @Override
    public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer, String functionImportName) throws ODataException {
        CsdlFunctionImport res = null;
        if (entityContainer.equals(ODataConst.CONTAINER)) {
            if (functionImportName.equals(ODataConst.FUNCTION_COUNT_CATEGORIES_FQN.getName())) {
                res = new CsdlFunctionImport()
                        .setName(functionImportName)
                        .setFunction(ODataConst.FUNCTION_COUNT_CATEGORIES_FQN)
                        .setEntitySet(ODataConst.ES_DETAIL)
                        .setIncludeInServiceDocument(true);
            }
        }

        return res;
    }
}
