package com.coderevolt.sql.core.dql;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.doc.NotNull;
import com.coderevolt.doc.Nullable;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.AbstractSqlGenerator;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubOrder;
import com.coderevolt.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class DQLSqlGenerator extends AbstractSqlGenerator {

    private static final Logger log = LoggerFactory.getLogger(DQLSqlGenerator.class);

    public DQLSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    protected <T> DQLSqlGenerator select(@Nullable SFunction<T, ?>... columns) {
        SqlChainContext sqlChainContext = getSqlChainContext();
        if (!sqlChainContext.isExistSelect()) {
            sqlBuf.append("SELECT ");
            sqlChainContext.setExistSelect(true);
        } else if (columns != null && columns.length > 0) {
            sqlBuf.append(",");
        }
        if (columns == null || columns.length == 0) {
            if (!sqlChainContext.isSelectAll()) {
                sqlBuf.append("*");
                sqlChainContext.setSelectAll(true);
            }
        } else {
            sqlBuf.append(Arrays.stream(columns)
                    .map(SFuncUtil::getColumn)
                    .collect(Collectors.joining(",")));
        }
        return this;
    }

    protected <T> DQLSqlGenerator select(@NotNull SFunction<T, ?> column, String alias) {
        Assert.isTrue(column != null, "column must not be null");
        SqlChainContext sqlChainContext = getSqlChainContext();
        if (!sqlChainContext.isExistSelect()) {
            sqlBuf.append("SELECT ");
            sqlChainContext.setExistSelect(true);
        } else {
            sqlBuf.append(",");
        }
        sqlBuf.append(SFuncUtil.getColumn(column));
        if (alias != null) {
            sqlBuf.append(" AS ").append(alias);
        }
        return this;
    }

    protected DQLSqlGenerator select(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect, @NotNull String alias) {
        Assert.isTrue(subSelect != null && alias != null, "subSelect and alias must not be null");
        SqlChainContext sqlChainContext = getSqlChainContext();
        if (!sqlChainContext.isExistSelect()) {
            sqlBuf.append("SELECT ");
            sqlChainContext.setExistSelect(true);
        } else {
            sqlBuf.append(",");
        }
        SubSelectSqlGenerator select = subSelect.apply(sqlChainContext);
        sqlBuf.append("(").append(select.toSql()).append(") AS ").append(alias);
        return this;
    }

    protected DQLSqlGenerator from(@NotNull Class<?> tableEntity, String alias) {
        connectTable(" FROM ", tableEntity, alias);
        return this;
    }


    protected DQLSqlGenerator from(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        SqlChainContext sqlCtx = getSqlChainContext();
        SubSelectSqlGenerator subSelectSqlGenerator = subSelectFunc.apply(sqlCtx);
        subSelectSqlGenerator.getSqlChainContext().getParamValueList().forEach(sqlCtx::addParamValue);
        sqlBuf.append(" FROM ").append("(")
                .append(subSelectSqlGenerator.toSql())
                .append(") ")
                // 别名
                .append(alias);
        return this;
    }

    private void join(String prefix, String alias, Class<?> tableEntity, AbstractSub subOn) {
        connectTable(prefix, tableEntity, alias);
        if (subOn != null) {
            sqlBuf.append(" ON ").append(subOn.apply(getSqlChainContext()));
        }
    }

    private void join(String prefix, String alias, SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, AbstractSub subOn) {
        connectTable(prefix, subSelectFunc, alias);
        if (subOn != null) {
            sqlBuf.append(" ON ").append(subOn.apply(getSqlChainContext()));
        }
    }

    protected DQLSqlGenerator leftJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        join(" LEFT JOIN ", alias, tableEntity, subOn);
        return this;
    }

    protected DQLSqlGenerator rightJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        join(" RIGHT JOIN ", alias, tableEntity, subOn);
        return this;
    }

    protected DQLSqlGenerator innerJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        join(" INNER JOIN ", alias, tableEntity, subOn);
        return this;
    }

    protected DQLSqlGenerator leftJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        join(" LEFT JOIN ", alias, subSelectFunc, subOn);
        return this;
    }

    protected DQLSqlGenerator rightJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        join(" RIGHT JOIN ", alias, subSelectFunc, subOn);
        return this;
    }

    protected DQLSqlGenerator innerJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        join(" INNER JOIN ", alias, subSelectFunc, subOn);
        return this;
    }

    protected <T> DQLSqlGenerator groupBy(@NotNull SFunction<T, ?>... field) {
        if (!getSqlChainContext().isExistGroupBy()) {
            sqlBuf.append(" GROUP BY ");
            getSqlChainContext().setExistGroupBy(true);
        } else {
            sqlBuf.append(",");
        }
        sqlBuf.append(Arrays.stream(field).map(SFuncUtil::getColumn).collect(Collectors.joining(",")));
        return this;
    }

    protected DQLSqlGenerator having(@NotNull AbstractSub subHaving) {
        if (!getSqlChainContext().isExistHaving()) {
            sqlBuf.append(" HAVING ");
            getSqlChainContext().setExistHaving(true);
        } else {
            sqlBuf.append(" ");
        }
        sqlBuf.append(subHaving.apply(getSqlChainContext()));
        return this;
    }

    protected DQLSqlGenerator order(@NotNull SubOrder... subOrder) {
        if (!getSqlChainContext().isExistOrderBy()) {
            sqlBuf.append(" ORDER BY ");
            getSqlChainContext().setExistOrderBy(true);
        } else {
            sqlBuf.append(",");
        }
        sqlBuf.append(Arrays.stream(subOrder).map(w -> w.apply(getSqlChainContext())).collect(Collectors.joining(",")));
        return this;
    }

    protected DQLSqlGenerator limit(@Nullable Integer start, Integer count) {
        sqlBuf.append(" LIMIT ");
        if (start != null) sqlBuf.append(start).append(", ");
        sqlBuf.append(count);
        return this;
    }

    @Override
    protected DQLSqlGenerator where(AbstractSub sqlWhere) {
        return (DQLSqlGenerator) super.where(sqlWhere);
    }

    protected List<Map<String, Object>> execute() throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        SqlChainContext sqlChainContext = getSqlChainContext();
        SqlOption sqlOption = sqlChainContext.getSqlOption();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            // 从连接池获取连接
            connection = SqlConnectFactory.getConnection(sqlOption.getSourceName());

            if (sqlOption.getTransactionIsolation() != null) {
                connection.setTransactionIsolation(sqlOption.getTransactionIsolation().getValue());
            }

            // 执行SQL查询
            String sql = toSql();
            preparedStatement = connection.prepareStatement(sql);

            // 占位符赋值
            List<Object> paramValueList = getSqlChainContext().getParamValueList();
            for (int i = 0; i < paramValueList.size(); i++) {
                Object data = paramValueList.get(i);
                preparedStatement.setObject((i + 1), data);
            }

            if (sqlOption.getSqlExecuteHook() != null) {
                sqlOption.getSqlExecuteHook().beforeRun(connection, sqlChainContext);
            }

            log.debug("==> execute sql: {}", sql);
            if (log.isDebugEnabled() && !paramValueList.isEmpty()) {
                AtomicInteger count = new AtomicInteger(0);
                String paramInfo = paramValueList.stream().map(item -> String.format("param%s=%s", count.addAndGet(1), item))
                        .collect(Collectors.joining(", "));
                log.debug("==> {}", paramInfo);
            }

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columns.add(metaData.getColumnLabel(i));
            }

            // 处理查询结果
            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                for (String col : columns) {
                    map.put(FieldUtil.camelCase(col), resultSet.getObject(col));
                }
                result.add(map);
            }
            log.debug("<== total: {}", result.size());
            if (log.isTraceEnabled()) {
                log.trace("<== data: {}", result);
            }

            if (sqlOption.getSqlExecuteHook() != null) {
                sqlOption.getSqlExecuteHook().afterRun(connection, sqlChainContext, result);
            }

            return result;
        } finally {
            try {
                // 关闭资源
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error("connection close error", e);
            }
        }
    }

}
