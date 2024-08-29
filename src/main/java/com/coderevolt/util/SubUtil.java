package com.coderevolt.util;

import com.coderevolt.sql.SubSelectBuilder;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubCompareChain;
import com.coderevolt.sql.core.sub.SubOrder;
import com.coderevolt.sql.core.sub.SubSet;
import com.coderevolt.sql.core.symbol.SqlSort;

import java.io.Serializable;
import java.util.List;

public class SubUtil {

    public static <T> SubCompareChain between(SFunction<T, ?> column, Serializable start, Serializable end) {
        return new SubCompareChain().between(column, start, end);
    }

    public static <T, R> SubCompareChain eq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().eq(column, parentColumn);
    }

    public static <T, R> SubCompareChain ne(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().ne(column, parentColumn);
    }

    public static <T, R> SubCompareChain lt(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().lt(column, parentColumn);
    }

    public static <T, R> SubCompareChain lq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().lq(column, parentColumn);
    }

    public static <T, R> SubCompareChain gt(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().gt(column, parentColumn);
    }

    public static <T, R> SubCompareChain gq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return new SubCompareChain().gq(column, parentColumn);
    }

    public static <T> SubCompareChain eq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().eq(column, subSelect);
    }

    public static <T> SubCompareChain ne(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().ne(column, subSelect);
    }

    public static <T> SubCompareChain lt(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().lt(column, subSelect);
    }

    public static <T> SubCompareChain lq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().lq(column, subSelect);
    }

    public static <T> SubCompareChain gt(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().gt(column, subSelect);
    }

    public static <T> SubCompareChain gq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().gq(column, subSelect);
    }

    public static <T> SubCompareChain in(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().in(column, subSelect);
    }

    public static <T> SubCompareChain notIn(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().notIn(column, subSelect);
    }

    public static <T> SubCompareChain eq(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().eq(column, data);
    }

    public static <T> SubCompareChain ne(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().ne(column, data);
    }

    public static <T> SubCompareChain lt(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().lt(column, data);
    }

    public static <T> SubCompareChain lq(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().lq(column, data);
    }

    public static <T> SubCompareChain gt(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().gt(column, data);
    }

    public static <T> SubCompareChain gq(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().gq(column, data);
    }

    public static <T> SubCompareChain in(SFunction<T, ?> column, List<Serializable> data) {
        return new SubCompareChain().in(column, data);
    }

    public static <T> SubCompareChain notIn(SFunction<T, ?> column, List<Serializable> data) {
        return new SubCompareChain().notIn(column, data);
    }

    public static <T> SubCompareChain exist(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().exist(subSelect);
    }

    public static <T> SubCompareChain notExist(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubCompareChain().notExist(subSelect);
    }

    public static <T> SubCompareChain isNull(SFunction<T, ?> column) {
        return new SubCompareChain().isNull(column);
    }

    public static <T> SubCompareChain isNotNull(SFunction<T, ?> column) {
        return new SubCompareChain().isNotNull(column);
    }

    public static <T> SubCompareChain like(SFunction<T, ?> column, Serializable data) {
        return new SubCompareChain().like(column, data);
    }

    public static <T> SubSet<T> set(SFunction<T, ?> column, Serializable data) {
        return new SubSet<>(column, data, null);
    }

    public static <T> SubSet<T> setNull(SFunction<T, ?> column) {
        return new SubSet<>(column, null, null);
    }

    public static <T> SubSet<T> set(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return new SubSet<>(column, null, subSelect);
    }

    public static <T> SubOrder<T> order(SFunction<T, ?> column, SqlSort sort) {
        return SubOrder.order(column, sort);
    }

    public static SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSql(String sql) {
        return ctx -> SubSelectBuilder.create().sql(sql);
    }

    public static SubSelectSqlGenerator subSelect() {
        return SubSelectBuilder.create();
    }

    public static SubSelectSqlGenerator subSelect(SqlOption option) {
        return SubSelectBuilder.create(option);
    }

    public static SubCompareChain wrap(AbstractSub subs) {
        return new SubCompareChain().wrap(subs);
    }

    public static SubCompareChain sql(String sql) {
        return new SubCompareChain().sql(sql);
    }

}
