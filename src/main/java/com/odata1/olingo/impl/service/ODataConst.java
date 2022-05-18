package com.odata1.olingo.impl.service;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

public class ODataConst {

    // Service Namespace
    public static final String NAMESPACE = "com.crt.detail";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    public static final String CT_PAIR_VALUE = "PairValue";
    public static final FullQualifiedName CT_PAIR_VALUE_FQN = new FullQualifiedName(NAMESPACE, CT_PAIR_VALUE);

    public static final String CT_COMPLETE_DATA = "CompleteData";
    public static final FullQualifiedName CT_COMPLETE_DATA_FQN = new FullQualifiedName(NAMESPACE, CT_COMPLETE_DATA);

    public static final int MAXIMUM_TOP_FETCH = 1000;

    // Action
    public static final String ACTION_GET_BIG_VALUE = "GetBigValue";
    public static final FullQualifiedName ACTION_GET_BIG_VALUE_FQN = new FullQualifiedName(NAMESPACE, ACTION_GET_BIG_VALUE);

    public static final String PARAMETER_COMBINED = "Combined";
}
