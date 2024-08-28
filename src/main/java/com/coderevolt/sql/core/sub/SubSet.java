package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.util.SFuncUtil;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

import java.io.Serializable;

public class SubSet<T> extends AbstractSub {

    private final SFunction<T, ?> column;

    private final Serializable data;

    private final SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunction;

    public SubSet(SFunction<T, ?> column, Serializable data, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunction) {
        this.column = column;
        this.data = data;
        this.subSelectFunction = subSelectFunction;
    }

    @Override
    public String apply(SqlChainContext ctx) {
        this.sqlBuf.append(SFuncUtil.getColumn(column)).append(" = ");
        if (subSelectFunction != null) {
            // 将当前占位符参数传递给父级上下文
            SubSelectSqlGenerator subSelectSqlGenerator = subSelectFunction.apply(ctx);
            SqlChainContext subCtx = subSelectSqlGenerator.getSqlChainContext();
            subCtx.getParamValueList().forEach(ctx::addParamValue);

            this.sqlBuf.append("(").append(subSelectSqlGenerator.toSql()).append(")");
        } else {
            ctx.addParamValue(data);
            this.sqlBuf.append("?");
        }
        return toSql();
    }
}
