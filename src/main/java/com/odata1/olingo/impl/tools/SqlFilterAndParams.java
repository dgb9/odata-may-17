package com.odata1.olingo.impl.tools;

public class SqlFilterAndParams {
    private final String sql;
    private final ParametersHolder holder;

    public SqlFilterAndParams(String sql, ParametersHolder holder) {
        this.sql = sql;
        this.holder = holder;
    }

    public String getSql() {
        return sql;
    }

    public ParametersHolder getHolder() {
        return holder;
    }
}
