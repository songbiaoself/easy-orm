package com.coderevolt.sql.core.sub;

import com.coderevolt.doc.NotNull;
import com.coderevolt.sql.SubSelectBuilder;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.sql.core.symbol.InterfaceSqlOp;
import com.coderevolt.sql.core.symbol.SqlLink;
import com.coderevolt.sql.core.symbol.SqlOp;
import com.coderevolt.util.SFuncUtil;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubCompareChain extends AbstractSubChain {

    private final List<AbstractSub> compareList;

    public SubCompareChain() {
        this.compareList = new ArrayList<>();
    }

    public <T, R>SubCompareChain between(SFunction<T, ?> column, Serializable start, Serializable end) {
        compareList.add(new AbstractSub() {
            @Override
            public String apply(SqlChainContext ctx) {
                ctx.addParamValue(start);
                ctx.addParamValue(end);
                sqlBuf.append(SFuncUtil.getColumn(column)).append(" ").append(SqlOp.M.BETWEEN).append(" ? AND ?");
                if (link != null) {
                    this.sqlBuf.append(" ").append(link);
                }
                return toSql();
            }
        });
        return this;
    }

    public SubCompareChain wrap(AbstractSub abstractSub) {
        compareList.add(new AbstractSub() {
            @Override
            public String apply(SqlChainContext ctx) {
                sqlBuf.append("(").append(abstractSub.apply(ctx)).append(")");
                if (link != null) {
                    this.sqlBuf.append(" ").append(link);
                }
                return toSql();
            }
        });
        return this;
    }

    public <T, R>SubCompareChain eq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.EQ, subSelect);
    }

    public <T, R>SubCompareChain ne(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.NE, subSelect);
    }

    public <T, R>SubCompareChain lt(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.LT, subSelect);
    }

    public <T, R>SubCompareChain lq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.LQ, subSelect);
    }

    public <T, R>SubCompareChain gt(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.RT, subSelect);
    }

    public <T, R>SubCompareChain gq(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.RT, subSelect);
    }

    public <T, R>SubCompareChain in(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.IN, subSelect);
    }

    public <T, R>SubCompareChain notIn(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.NOT_IN, subSelect);
    }

    public <T, R>SubCompareChain eq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.EQ, parentColumn);
    }

    public <T, R>SubCompareChain ne(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.NE, parentColumn);
    }

    public <T, R>SubCompareChain lt(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.LT, parentColumn);
    }

    public <T, R>SubCompareChain lq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.LQ, parentColumn);
    }

    public <T, R>SubCompareChain gt(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.RT, parentColumn);
    }

    public <T, R>SubCompareChain gq(SFunction<T, ?> column, SFunction<R, ?> parentColumn) {
        return compare(column, SqlOp.M.RT, parentColumn);
    }

    public <T, R>SubCompareChain eq(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.EQ, data);
    }

    public <T, R>SubCompareChain ne(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.NE, data);
    }

    public <T, R>SubCompareChain lt(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.LT, data);
    }

    public <T, R>SubCompareChain lq(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.LQ, data);
    }

    public <T, R>SubCompareChain gt(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.RT, data);
    }

    public <T, R>SubCompareChain gq(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.RT, data);
    }

    public <T, R>SubCompareChain in(SFunction<T, ?> column, List<Serializable> data) {
        return compare(column, SqlOp.M.IN, data);
    }

    public <T, R>SubCompareChain notIn(SFunction<T, ?> column, List<Serializable> data) {
        return compare(column, SqlOp.M.NOT_IN, data);
    }

    public <T, R>SubCompareChain exist(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(SqlOp.L.EXISTS, subSelect);
    }

    public <T, R>SubCompareChain notExist(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(SqlOp.L.NOT_EXISTS, subSelect);
    }

    public <T, R>SubCompareChain isNull(SFunction<T, ?> column) {
        return compare(column, SqlOp.R.IS_NULL);
    }

    public <T, R>SubCompareChain isNotNull(SFunction<T, ?> column) {
        return compare(column, SqlOp.R.IS_NOT_NULL);
    }

    public <T, R>SubCompareChain like(SFunction<T, ?> column, Serializable data) {
        return compare(column, SqlOp.M.LIKE, (SelectFunction<SqlChainContext, SubSelectSqlGenerator>) ctx -> {
            ctx.addParamValue(data);
            return SubSelectBuilder.create().sql("?");
        });
    }

    public <T, R>SubCompareChain like(SFunction<T, ?> column, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        return compare(column, SqlOp.M.LIKE, subSelect);
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, @NotNull InterfaceSqlOp op, SFunction<R, ?> parentColumn, List<Serializable> data, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        compareList.add(new SubCompare<>(column, op, parentColumn, data, subSelect));
        return this;
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, SqlOp.M op, Serializable data) {
        compare(column, op, null, Collections.singletonList(data), null);
        return this;
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, SqlOp.M op, List<Serializable> data) {
        compare(column, op, null, data, null);
        return this;
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, SqlOp.M op, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        compare(column, op, null, null, subSelect);
        return this;
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, SqlOp.M op, SFunction<R, ?> parentColumn) {
        compare(column, op, parentColumn, null, null);
        return this;
    }

    private <T, R>SubCompareChain compare(SFunction<T, ?> column, SqlOp.R op) {
        compare(column, op, null, null, null);
        return this;
    }

    private <T, R>SubCompareChain compare(SqlOp.L op, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect) {
        compare(null, op, null, null, subSelect);
        return this;
    }

    @Override
    public SubCompareChain or() {
        if (!compareList.isEmpty()) {
            compareList.get(compareList.size() - 1).setLink(SqlLink.OR);
        }
        return this;
    }

    @Override
    public SubCompareChain and() {
        if (!compareList.isEmpty()) {
            compareList.get(compareList.size() - 1).setLink(SqlLink.AND);
        }
        return this;
    }

    @Override
    public String apply(SqlChainContext ctx) {
        sqlBuf.append(compareList.stream().map(s -> s.apply(ctx)).collect(Collectors.joining(" ")));
        return toSql();
    }

    @Override
    public SubCompareChain sql(String sql) {
        compareList.add(new AbstractSub() {

            @Override
            public String apply(SqlChainContext ctx) {
                this.sqlBuf.append(sql);
                if (link != null) {
                    this.sqlBuf.append(" ").append(link);
                }
                return toSql();
            }
        });
        return this;
    }
}
