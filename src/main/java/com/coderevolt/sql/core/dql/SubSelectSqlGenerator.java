package com.coderevolt.sql.core.dql;

import com.coderevolt.doc.Nullable;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubOrder;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

public class SubSelectSqlGenerator extends DQLSqlGenerator {

    public SubSelectSqlGenerator(SqlChainContext chainContext) {
        super(chainContext);
    }

    @SafeVarargs
    @Override
    public final <T> SubSelectSqlGenerator select(@Nullable SFunction<T, ?>... columns) {
        return (SubSelectSqlGenerator) super.select(columns);
    }

    @Override
    public SubSelectSqlGenerator select(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect, String alias) {
        return (SubSelectSqlGenerator) super.select(subSelect, alias);
    }

    @Override
    public <T> SubSelectSqlGenerator select(SFunction<T, ?> column, String alias) {
        return (SubSelectSqlGenerator) super.select(column, alias);
    }

    @Override
    public SubSelectSqlGenerator from(Class<?> tableEntity, String alias) {
        return (SubSelectSqlGenerator) super.from(tableEntity, alias);
    }

    public SubSelectSqlGenerator from(Class<?> tableEntity) {
        return from(tableEntity, null);
    }

    @Override
    public SubSelectSqlGenerator from(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        return (SubSelectSqlGenerator) super.from(subSelectFunc, alias);
    }

    @Override
    public SubSelectSqlGenerator leftJoin(Class<?> tableEntity, String alias, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.leftJoin(tableEntity, alias, subOn);
    }

    public SubSelectSqlGenerator leftJoin(Class<?> tableEntity, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.leftJoin(tableEntity, null, subOn);
    }

    public SubSelectSqlGenerator leftJoin(Class<?> tableEntity) {
        return (SubSelectSqlGenerator) super.leftJoin(tableEntity, null, null);
    }

    public SubSelectSqlGenerator leftJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.leftJoin(subSelectFunc, alias, subOn);
    }

    public SubSelectSqlGenerator leftJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        return (SubSelectSqlGenerator) super.leftJoin(subSelectFunc, alias, null);
    }

    @Override
    public SubSelectSqlGenerator rightJoin(Class<?> tableEntity, String alias, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.rightJoin(tableEntity, alias, subOn);
    }

    public SubSelectSqlGenerator rightJoin(Class<?> tableEntity, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.rightJoin(tableEntity, null, subOn);
    }

    public SubSelectSqlGenerator rightJoin(Class<?> tableEntity) {
        return (SubSelectSqlGenerator) super.rightJoin(tableEntity, null, null);
    }

    public SubSelectSqlGenerator rightJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.rightJoin(subSelectFunc, alias, subOn);
    }

    public SubSelectSqlGenerator rightJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        return (SubSelectSqlGenerator) super.rightJoin(subSelectFunc, alias, null);
    }

    @Override
    public SubSelectSqlGenerator innerJoin(Class<?> tableEntity, String alias, @Nullable AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.innerJoin(tableEntity, alias, subOn);
    }

    public SubSelectSqlGenerator innerJoin(Class<?> tableEntity, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.innerJoin(tableEntity, null, subOn);
    }

    public SubSelectSqlGenerator innerJoin(Class<?> tableEntity) {
        return (SubSelectSqlGenerator) super.innerJoin(tableEntity, null, null);
    }

    public SubSelectSqlGenerator innerJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias, AbstractSub subOn) {
        return (SubSelectSqlGenerator) super.innerJoin(subSelectFunc, alias, subOn);
    }

    public SubSelectSqlGenerator innerJoin(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        return (SubSelectSqlGenerator) super.innerJoin(subSelectFunc, alias, null);
    }

    @SafeVarargs
    @Override
    public final <T> SubSelectSqlGenerator groupBy(SFunction<T, ?>... field) {
        return (SubSelectSqlGenerator) super.groupBy(field);
    }

    @Override
    public SubSelectSqlGenerator where(AbstractSub sqlWhere) {
        return (SubSelectSqlGenerator) super.where(sqlWhere);
    }

    @Override
    public SubSelectSqlGenerator having(AbstractSub subHaving) {
        return (SubSelectSqlGenerator) super.having(subHaving);
    }

    @Override
    public SubSelectSqlGenerator order(SubOrder... subOrder) {
        return (SubSelectSqlGenerator) super.order(subOrder);
    }

    @Override
    public SubSelectSqlGenerator limit(Integer start, Integer count) {
        return (SubSelectSqlGenerator) super.limit(start, count);
    }

    public SubSelectSqlGenerator limit(Integer count) {
        return limit(null, count);
    }

    @Override
    public SubSelectSqlGenerator sql(String sql) {
        return (SubSelectSqlGenerator) super.sql(sql);
    }


}
