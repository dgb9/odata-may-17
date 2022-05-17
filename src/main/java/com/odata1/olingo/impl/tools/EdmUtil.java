package com.odata1.olingo.impl.tools;

import com.odata1.olingo.impl.service.ODataConst;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class EdmUtil {
    public static CsdlProperty createStringProperty(String name) {
        FullQualifiedName stringFqn = EdmPrimitiveTypeKind.String.getFullQualifiedName();
        return new CsdlProperty().setName(name).setType(stringFqn);
    }

    public static CsdlProperty createBooleanProperty(String name) {
        FullQualifiedName booleanFqn = EdmPrimitiveTypeKind.Boolean.getFullQualifiedName();
        return new CsdlProperty().setName(name).setType(booleanFqn);
    }

    public static CsdlProperty createDateProperty(String name) {
        FullQualifiedName dateFqn = EdmPrimitiveTypeKind.Date.getFullQualifiedName();
        return new CsdlProperty().setName(name).setType(dateFqn);
    }

    public static CsdlProperty createTimestampProperty(String name) {
        FullQualifiedName dateFqn = EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName();
        return new CsdlProperty().setName(name).setType(dateFqn);
    }

    public static CsdlProperty createDoubleProperty(String name) {
        FullQualifiedName doubleFqn = EdmPrimitiveTypeKind.Double.getFullQualifiedName();
        return new CsdlProperty().setName(name).setType(doubleFqn);
    }

    public static CsdlPropertyRef createPropertyRef(String name) {
        CsdlPropertyRef pref = new CsdlPropertyRef();
        pref.setName(name);

        return pref;
    }


    public static CsdlEntityType createEntityType(String entityTypeName, List<CsdlProperty> properties, List<CsdlPropertyRef> propertyRefs, List<CsdlNavigationProperty> navigationProperties) {
        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(entityTypeName);
        entityType.setProperties(properties);
        entityType.setKey(propertyRefs);

        // setting the navigation properties if passed
        if (navigationProperties != null) {
            entityType.setNavigationProperties(navigationProperties);
        }

        return entityType;
    }

    /**
     * Odata does not accept ids of type string without quotes. Then the value
     * is passed with quotes. So the quotes must be removed
     * @param id
     * @return
     */
    public static String removeSingleQuotes(String id) {
        String res = null;

        if (id != null) {
            if (id.startsWith("'")) {
                id = id.substring(1);
            }

            if (id.endsWith("'")) {
                id = id.substring(0, id.length() - 1);
            }

            res = id;
        }

        return res;
    }

    public static String processOrderBy(String prefix, OrderByOption orderByOption, IFieldMapper fieldMapper) {
        StringBuilder b = new StringBuilder();

        if (orderByOption != null) {
            List<String> orders = orderByOption.getOrders().stream().map((info) -> {
                // get the field
                String field = info.getExpression().toString();

                // with the field, resolve the database field
                String dbField = prefix + fieldMapper.getMappedDbField(field, true);

                // get desc status
                boolean desc = info.isDescending();
                String strDesc = desc ? " desc " : "";

                // bundle them in the full expression
                return dbField + strDesc;
            }).collect(Collectors.toList());

            // produce the order by clause and attach it to the resulting sql
            String orderClause = String.join(", ", orders);
            if (orders.size() > 0) {
                b.append(" order by ");
                b.append(orderClause);
            }
        }

        return b.toString();
    }

    public static int getSkip(UriInfo uriInfo) {
        SkipOption opt = uriInfo.getSkipOption();

        return opt == null ? 0 : opt.getValue();
    }

    /**
     * Get the top value for the resultset. Most of the time the option is not passed in the request, in which case
     * the maximum top value as defined in OdataConst and subsequently externalized is being returned
     * @param uriInfo
     * @return
     */
    public static int getTop(UriInfo uriInfo) {
        TopOption opt = uriInfo.getTopOption();

        return opt == null ? ODataConst.MAXIMUM_TOP_FETCH : opt.getValue();
    }

    public static URI createId(String entitySetName, EdmIdHolder... idInformation)  {
        URI res = null;
        StringBuilder b = new StringBuilder();

        try {
            b.append(entitySetName);
            b.append("(");

            // if idInformation has only one item, then qualification is not needed, otherwise it is needed
            if (idInformation.length == 1) {
                b.append(idInformation[0].toText(false));
            } else {
                boolean first = true;

                for (EdmIdHolder idInfo : idInformation) {
                    String text = idInfo.toText(true);
                    if (first) {
                        first = false;
                    } else {
                        b.append(",");
                    }

                    b.append(text);
                }
            }

            b.append(")");

            String strUri = b.toString();
            strUri = strUri.replaceAll("\\s*", "");

            res = new URI(strUri);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }

}
