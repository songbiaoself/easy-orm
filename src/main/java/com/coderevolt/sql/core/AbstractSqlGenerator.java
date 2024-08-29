package com.coderevolt.sql.core;


import com.coderevolt.doc.NotNull;
import com.coderevolt.sql.attr.Table;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.symbol.VariablePlaceholder;
import com.coderevolt.util.Assert;
import com.coderevolt.util.SelectFunction;

import java.beans.Introspector;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractSqlGenerator {

    protected final StringBuilder sqlBuf = new StringBuilder();
    private final SqlChainContext sqlChainContext;

    public AbstractSqlGenerator(SqlChainContext sqlChainContext) {
        this.sqlChainContext = sqlChainContext;
    }

    protected AbstractSqlGenerator where(@NotNull AbstractSub sqlWhere) {
        if (!sqlChainContext.isExistWhere()) {
            sqlBuf.append(" WHERE ");
            sqlChainContext.setExistWhere(true);
        } else {
            sqlBuf.append(" ");
        }
        sqlBuf.append(sqlWhere.apply(sqlChainContext));
        return this;
    }

    protected AbstractSqlGenerator sql(@NotNull String sql) {
        sqlBuf.append(sql);
        return this;
    }

    protected void connectTable(@NotNull String prefix, @NotNull Class<?> tableEntity, String alias) {
        Table tableAnnotation = tableEntity.getAnnotation(Table.class);
        Assert.isTrue(tableAnnotation != null && tableAnnotation.value() != null, tableEntity.getName() + "not use @Table annotation defined a table name");
        sqlBuf.append(prefix).append(tableAnnotation.value());
        String tablePlaceHolder = getTablePlaceHolder(tableEntity);
        if (alias != null) {
            // 别名
            sqlBuf.append(" ").append(alias);
            sqlChainContext.putPlaceHolder(tablePlaceHolder, alias + ".");
        } else {
            // 别名
            String t = Introspector.decapitalize(tableEntity.getSimpleName());
            sqlBuf.append(" ").append(t);
            sqlChainContext.putPlaceHolder(tablePlaceHolder, t + ".");
        }
    }

    protected void connectTable(@NotNull String prefix, @NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias) {
        Assert.isTrue(prefix != null && subSelectFunc != null && alias != null, "prefix and subSelectFunc and alias are required");
        sqlBuf.append(prefix).append(" (").append(subSelectFunc.apply(getSqlChainContext()).toSql()).append(") ");
        // 别名
        sqlBuf.append(alias);
        sqlChainContext.putPlaceHolder(String.format(VariablePlaceholder.TABLE_NAME_DOT, alias), alias + ".");
    }

    private String getTablePlaceHolder(Class<?> tableEntity) {
        return String.format(VariablePlaceholder.TABLE_NAME_DOT, Introspector.decapitalize(tableEntity.getSimpleName()));
    }

    public SqlChainContext getSqlChainContext() {
        return sqlChainContext;
    }

    public String toSql() {
        String sql = sqlBuf.toString();
        Map<String, String> placeHolderMap = sqlChainContext.getPlaceHolderMap();
        for (String placeholder : placeHolderMap.keySet()) {
            sql = sql.replace(placeholder, placeHolderMap.getOrDefault(placeholder, ""));
        }
        return sql;
    }
}
