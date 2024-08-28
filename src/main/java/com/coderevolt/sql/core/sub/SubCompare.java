package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.sql.core.symbol.InterfaceSqlOp;
import com.coderevolt.util.SFuncUtil;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SubCompare<T, R> extends AbstractSubComparator {

    private final SFunction<T, ?> column;

    private final SFunction<R, ?> parentColumn;

    private final InterfaceSqlOp sqlOp;

    private final List<Serializable> data;

    private final SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunction;

    public SubCompare(SFunction<T, ?> column, InterfaceSqlOp sqlOp, SFunction<R, ?> parentColumn, List<Serializable> data, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunction) {
        this.column = column;
        this.parentColumn = parentColumn;
        this.sqlOp = sqlOp;
        this.data = data;
        this.subSelectFunction = subSelectFunction;
    }

    @Override
    public String apply(SqlChainContext ctx) {
        if (column != null) {
            this.sqlBuf.append(SFuncUtil.getColumn(column)).append(" ");
        }
        this.sqlBuf.append(sqlOp.getValue()).append(" ");
        if (subSelectFunction != null) {
            // 将当前占位符参数传递给父级上下文
            SubSelectSqlGenerator subSelectSqlGenerator = subSelectFunction.apply(ctx);
            SqlChainContext subCtx = subSelectSqlGenerator.getSqlChainContext();
            subCtx.getParamValueList().forEach(ctx::addParamValue);

            this.sqlBuf.append("(").append(subSelectSqlGenerator.toSql()).append(")");
        } else if (data != null) {
            this.sqlBuf.append("(").append(data.stream().map(item -> {
                ctx.addParamValue(item);
                return "?";
            }).collect(Collectors.joining(","))).append(")");
        } else if (parentColumn != null) {
            this.sqlBuf.append(SFuncUtil.getColumn(parentColumn));
        }
        if (link != null) {
            this.sqlBuf.append(" ").append(link);
        }
        return toSql();
    }

}
