package com.odata1.olingo.impl.service;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

public class ODataConst {

    // Service Namespace
    public static final String NAMESPACE = "com.crt.detail";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_CRT = "Crt";
    public static final FullQualifiedName ET_CRT_FQN = new FullQualifiedName(NAMESPACE, ET_CRT);

    public static final String ET_DETAIL = "Detail";
    public static final FullQualifiedName ET_DETAIL_FQN = new FullQualifiedName(NAMESPACE, ET_DETAIL);

    public static final String CT_WHATEVER = "Whatever";
    public static final FullQualifiedName CT_WHATEVER_FQN = new FullQualifiedName(NAMESPACE, CT_WHATEVER);

    public static final String CT_COMPLICATED = "Complicated";
    public static final FullQualifiedName CT_COMPLICATED_FQN = new FullQualifiedName(NAMESPACE, CT_COMPLICATED);

    public static final String CT_PAIR_VALUE = "PairValue";
    public static final FullQualifiedName CT_PAIR_VALUE_FQN = new FullQualifiedName(NAMESPACE, CT_PAIR_VALUE);

    public static final String CT_COMPLETE_DATA = "CompleteData";
    public static final FullQualifiedName CT_COMPLETE_DATA_FQN = new FullQualifiedName(NAMESPACE, CT_COMPLETE_DATA);

    public static final String ES_CRT = "Crts";
    public static final String ES_DETAIL = "Details";

    public static final int MAXIMUM_TOP_FETCH = 1000;

    public static final String NAV_DET = "Details";

    // Action
    public static final String ACTION_RESET = "ActionReset";
    public static final FullQualifiedName ACTION_RESET_FQN = new FullQualifiedName(NAMESPACE, ACTION_RESET);

    // Function
    public static final String FUNCTION_COUNT_CATEGORIES = "CountCategories";
    public static final FullQualifiedName FUNCTION_COUNT_CATEGORIES_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_COUNT_CATEGORIES);

    // Action
    public static final String ACTION_GET_BIG_VALUE = "GetBigValue";
    public static final FullQualifiedName ACTION_GET_BIG_VALUE_FQN = new FullQualifiedName(NAMESPACE, ACTION_GET_BIG_VALUE);

    // Function/Action Parameters
    public static final String PARAMETER_RESET_ONE = "ResetOne";
    public static final String PARAMETER_RESET_TWO = "ResetTwo";

    public static final String PARAMETER_COUNT_FIRST = "CountFirst";
    public static final String PARAMETER_COUNT_SECOND = "CountSecond";

    public static final String PARAMETER_COMBINED = "Combined";
}
