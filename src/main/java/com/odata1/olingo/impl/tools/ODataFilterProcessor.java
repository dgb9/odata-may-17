package com.odata1.olingo.impl.tools;

import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Binary;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Enumeration;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.Method;
import org.apache.olingo.server.api.uri.queryoption.expression.TypeLiteral;
import org.apache.olingo.server.api.uri.queryoption.expression.Unary;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ODataFilterProcessor {

    private static final String OPEN = " (";
    private static final String CLOSE = ") ";
    private static final String COMMA_DELIMITER = ", ";

    private static Map<String, String> binaryOperators;

    /**
     * When processing a binary like whateverField in (val1, val2, val3) the val1 - 3 must be produced of the
     * type associated with the type of the field whateverField. So if the field is of type bit (boolean), we
     * cannot map a String on the top of it, it has to be a boolean. Because of that, we maintain the
     * latest database field type and all the values that come associated with this field type are being
     * converted to this value
     */
    private DbFieldType currentFieldTypeOperated = null;

    public ODataFilterProcessor() {

    }

    public SqlFilterAndParams processFilterOption(FilterOption option, IFieldMapper mapper, boolean addWhere) throws ODataApplicationException {
        String sql = "";
        ParametersHolder holder = new ParametersHolder();

        if (option != null) {
            Expression expression = option.getExpression();

            sql = getSql(expression, mapper, holder);
        }

        if (addWhere && sql.length() > 0) {
            sql = " where " + sql;
        }

        return new SqlFilterAndParams(sql, holder);
    }

    /**
     *
     * @param expression expression to be rendered
     * @param mapper the mapper - to determine the mapping between odata and database fields
     * @return
     * @throws ODataApplicationException
     */
    private  String getSql(Expression expression, IFieldMapper mapper, ParametersHolder holder) throws ODataApplicationException {
        StringBuilder b = new StringBuilder();

        if (expression instanceof Binary) {
            Binary binary = (Binary) expression;
            processBinary(binary, mapper, b, holder);
        } else if (expression instanceof Member) {
            Member member = (Member) expression;

            // process the memeber - get the name and process it through the database mapper
            String name = member.toString();
            name = mapper.getMappedDbField(name, true);
            currentFieldTypeOperated = mapper.getDbFieldType(name);

            b.append(name);
        } else if (expression instanceof TypeLiteral) {
            TypeLiteral literal = (TypeLiteral) expression;
            processTypeLiteral(b, literal, holder);
        } else if (expression instanceof Unary) {
            Unary unary = (Unary) expression;
            processUnary(mapper, b, unary, holder);
        } else if (expression instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) expression;
            processEnumeration(b, enumeration, holder);
        } else if (expression instanceof Literal) {
            Literal literal = (Literal) expression;
            processLiteral(b, literal, holder);
        } else if (expression instanceof Method) {
            Method method = (Method) expression;
            processMethod(method, mapper, b, holder);
        }

        return b.toString();
    }

    private void processLiteral(StringBuilder b, Literal literal, ParametersHolder holder) throws ODataApplicationException {
        String text = literal.getText();
        if (text != null) {
            ModifiedStringHolder strHolder = PersistUtil.removeQuotes(text);
            text = strHolder.getValue();
        }

        String name = holder.getNextName();
        String jpaName = holder.getPrependedCurrentName();

        holder.addParameter(name, text, currentFieldTypeOperated);
        b.append(jpaName);
    }

    private void processTypeLiteral(StringBuilder b, TypeLiteral literal, ParametersHolder holder) {
        EdmType type = literal.getType();
        FullQualifiedName fullQualifiedName = type.getFullQualifiedName();

        b.append(fullQualifiedName);
    }

    private void processEnumeration(StringBuilder b, Enumeration enumeration, ParametersHolder holder) throws ODataApplicationException {
        List<String> values = enumeration.getValues();
        List<String> jpaParamNames = new ArrayList<>();

        for (String value : values) {
            String name = holder.getNextName();
            String jpaName = holder.getPrependedCurrentName();

            jpaParamNames.add(jpaName);
            holder.addParameter(name, value, currentFieldTypeOperated);
        }

        b.append(OPEN);
        b.append(String.join(COMMA_DELIMITER, jpaParamNames));
        b.append(CLOSE);
    }

    private void processUnary(IFieldMapper mapper, StringBuilder b, Unary unary, ParametersHolder holder) throws ODataApplicationException {
        UnaryOperatorKind operator = unary.getOperator();
        Expression operand = unary.getOperand();

        b.append(OPEN);
        b.append(getUnarySqlOperator(operator));
        b.append(getSql(operand, mapper, holder));
        b.append(CLOSE);
    }

    private void processBinary(Binary expression, IFieldMapper mapper, StringBuilder b, ParametersHolder holder) throws ODataApplicationException {
        Binary binary = expression;

        Expression left = binary.getLeftOperand();
        Expression right = binary.getRightOperand();
        BinaryOperatorKind operator = binary.getOperator();

        //region Description
        b.append(OPEN);
        //endregion
        b.append(getSql(left, mapper, holder));
        String strOperator = getSqlOperator(operator);
        b.append(strOperator);

        if (right != null) {
            b.append(getSql(right, mapper, holder));
        } else {
            // get the expressions, if it is an array, it has to be serialized and then wrapped in brackets
            if (binary.getExpressions() != null && binary.getExpressions().size() > 0) {
                b.append(OPEN); // typically this is an "in" clause, wrapping in brackets

                List<Expression> expressions = binary.getExpressions();
                List<String> list = new ArrayList<>();

                // no lambdas as I need to process the checked exception
                for (Expression expr : expressions) {
                    String mapped = getSql(expr, mapper, holder);
                    list.add(mapped);
                }

                String strList = String.join(COMMA_DELIMITER, list);
                b.append(strList);

                b.append(CLOSE);
            }
        }

        b.append(CLOSE);
    }

    private void processMethod(Method method, IFieldMapper mapper, StringBuilder b, ParametersHolder holder) throws ODataApplicationException {
        String name = method.getMethod().name();
        if ("startswith".equalsIgnoreCase(name)) {
            processLikeMethod(method, "", "%", b, mapper, holder);
        } else if ("endswith".equalsIgnoreCase(name)) {
            processLikeMethod(method, "%", "", b, mapper, holder);
        } else if ("contains".equalsIgnoreCase(name)) {
            processLikeMethod(method, "%", "%", b, mapper, holder);
        } else {
            String message = String.format("Method: %s not implemented", name);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    private void processLikeMethod(Method method, String prefix, String suffix, StringBuilder b, IFieldMapper mapper, ParametersHolder holder) throws ODataApplicationException {
        // the method must have exactly two parameters
        List<Expression> expressions = method.getParameters();
        String name = method.getMethod().name();

        if (expressions.size() != 2) {
            String message = String.format("for filtering method %s expecting exactly two parameters, one field and one literal value", name);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        // the first parameter must be a member variable
        // the second parameter must be a literal
        Expression member = expressions.get(0);
        if (! (member instanceof Member)) {
            String message = String.format("for filtering method %s first parameter must be a valid field name", name);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        Expression literal = expressions.get(1);
        if (!(literal instanceof Literal)) {
            String message = String.format("for filtering method %s the second parameter must be a valid literal", name);
            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        b.append(getSql(member, mapper, holder));
        b.append(" like ");

        String strLiteral = ((Literal) literal).getText();
        ModifiedStringHolder quoteProcess = PersistUtil.removeQuotes(strLiteral);
        strLiteral = String.join("", prefix, quoteProcess.getValue(), suffix);

        String paramName = holder.getNextName();
        String jpaParamName = holder.getPrependedCurrentName();
        holder.addParameter(paramName, strLiteral, currentFieldTypeOperated);

        b.append(jpaParamName);
    }

    private String getSqlOperator(BinaryOperatorKind operator) throws ODataApplicationException {
        String name = operator.name().toLowerCase();
        if (!binaryOperators.containsKey(name)) {
            String message = String.format("Cannot process the \"%s\" binary operator", name);

            throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        return binaryOperators.get(name);
    }

    private String getUnarySqlOperator(UnaryOperatorKind operator) {
        // not processed further, the only values are minus (the sign) and "not"
        return " " + operator.name() + " ";
    }

    static {
        binaryOperators = new HashMap<>();

        binaryOperators.put("in", " in ");
        binaryOperators.put("gt", " > ");
        binaryOperators.put("ge", " >= ");
        binaryOperators.put("lt", " < ");
        binaryOperators.put("le", " <= ");
        binaryOperators.put("eq", " = ");
        binaryOperators.put("ne", " <> ");
        binaryOperators.put("and", " and ");
        binaryOperators.put("or", " or ");
    }
}
