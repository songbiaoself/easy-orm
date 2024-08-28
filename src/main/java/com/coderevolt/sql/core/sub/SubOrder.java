package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.symbol.SqlSort;
import com.coderevolt.util.SFuncUtil;
import com.coderevolt.util.SFunction;

public class SubOrder<T> extends AbstractSub {

    private final SFunction<T, ?> column;

    private final SqlSort sqlSort;

    SubOrder(SFunction<T, ?> column, SqlSort sqlSort) {
        this.column = column;
        this.sqlSort = sqlSort;
    }

    public static <T> SubOrder<T> order(SFunction<T, ?> column, SqlSort sort) {
        return new SubOrder<>(column, sort);
    }

    @Override
    public String apply(SqlChainContext ctx) {
        this.sqlBuf.append(SFuncUtil.getColumn(column));
        if (sqlSort != null) {
            this.sqlBuf.append(" ").append(sqlSort.name());
        }
        return toSql();
    }
}
