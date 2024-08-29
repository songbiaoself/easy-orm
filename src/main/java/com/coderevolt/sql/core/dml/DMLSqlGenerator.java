package com.coderevolt.sql.core.dml;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.doc.NotNull;
import com.coderevolt.doc.Nullable;
import com.coderevolt.sql.attr.Column;
import com.coderevolt.sql.attr.Table;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.AbstractSqlGenerator;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubSet;
import com.coderevolt.util.Assert;
import com.coderevolt.util.FieldUtil;
import com.coderevolt.util.SFuncUtil;
import com.coderevolt.util.SFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DMLSqlGenerator extends AbstractSqlGenerator {

    private static final Logger log = LoggerFactory.getLogger(DMLSqlGenerator.class);

    public DMLSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    protected <T> DMLSqlGenerator insertInto(@NotNull Class<T> tableEntity, @Nullable SFunction<T, ?>... columns) {
        SqlChainContext sqlChainContext = getSqlChainContext();
        Assert.isTrue(sqlChainContext.getInsertColumns().isEmpty(), "\"INSERT INTO\" func has already call");
        Table tableAnnotation = tableEntity.getAnnotation(Table.class);
        Assert.isTrue(tableAnnotation != null && tableAnnotation.value() != null, tableEntity.getName() + "not use @Table annotation defined a table name");
        sqlBuf.append("INSERT INTO ").append(tableAnnotation.value());
        Stream<Field> fieldStream = null;
        if (columns != null && columns.length > 0) {
            fieldStream = Arrays.stream(columns).map(SFuncUtil::getField);
        } else {
            fieldStream = Arrays.stream(tableEntity.getDeclaredFields());
        }
        sqlBuf.append("(").append(fieldStream.map(field -> {
            Column column = field.getAnnotation(Column.class);
            sqlChainContext.addInsertColumn(field.getName());
            if (column == null) {
                return FieldUtil.underline(field.getName());
            }
            return "@".equals(column.name()) ? FieldUtil.underline(field.getName()) : column.name();
        }).collect(Collectors.joining(","))).append(")");
        return this;
    }

    protected DMLSqlGenerator values(@NotNull Object data) {
        SqlChainContext sqlChainContext = getSqlChainContext();
        if (!sqlChainContext.isExistValues()) {
            sqlBuf.append(" VALUES ");
            sqlChainContext.setExistValues(true);
        }
        List<String> columns = sqlChainContext.getInsertColumns();
        Assert.isNotEmpty(columns, "columns is empty, please call insert into before used");
        Field[] declaredFields = data.getClass().getDeclaredFields();
        Assert.isTrue(declaredFields.length > 0, "system error, " + data.getClass() + " declaredFields is empty");
        Map<String, Field> fieldMap = Arrays.stream(declaredFields).collect(Collectors.toMap(Field::getName, obj -> obj));
        for (String column : columns) {
            try {
                Field field = fieldMap.get(column);
                field.setAccessible(true);
                sqlChainContext.addParamValue(field.get(data));
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
        sqlBuf.append("(").append(columns.stream().map(c -> "?").collect(Collectors.joining(","))).append(")");
        return this;
    }

    protected DMLSqlGenerator deleteFrom(@NotNull Class<?> tableEntity) {
        connectTable("DELETE FROM ", tableEntity, null);
        return this;
    }

    protected DMLSqlGenerator update(@NotNull Class<?> tableEntity) {
        connectTable("UPDATE ", tableEntity, null);
        return this;
    }

    protected DMLSqlGenerator set(@NotNull SubSet... subSet) {
        SqlChainContext sqlChainContext = getSqlChainContext();
        if (!sqlChainContext.isExistSet()) {
            sqlBuf.append(" SET ");
            sqlChainContext.setExistSet(true);
        } else {
            sqlBuf.append(",");
        }
        sqlBuf.append(Arrays.stream(subSet).map(s -> s.apply(sqlChainContext)).collect(Collectors.joining(",")));
        return this;
    }

    @Override
    protected DMLSqlGenerator where(AbstractSub sqlWhere) {
        return (DMLSqlGenerator) super.where(sqlWhere);
    }


    public boolean exec(Connection connection) {
        boolean result = false;
        SqlOption sqlOption = getSqlChainContext().getSqlOption();

        PreparedStatement preparedStatement = null;
        try {
            if (sqlOption.getTransactionIsolation() != null) {
                connection.setTransactionIsolation(sqlOption.getTransactionIsolation().getValue());
            }
            String sql = toSql();
            preparedStatement = connection.prepareStatement(sql);

            // 占位符赋值
            List<Object> paramValueList = getSqlChainContext().getParamValueList();
            for (int i = 0; i < paramValueList.size(); i++) {
                Object data = paramValueList.get(i);
                preparedStatement.setObject((i + 1), data);
            }

            if (sqlOption.getSqlExecuteHook() != null) {
                sqlOption.getSqlExecuteHook().beforeRun(connection, getSqlChainContext());
            }
            log.debug("==> execute sql: {}", sql);
            if (log.isDebugEnabled() && !paramValueList.isEmpty()) {
                AtomicInteger count = new AtomicInteger(0);
                String paramInfo = paramValueList.stream().map(item -> String.format("param%s=%s", count.addAndGet(1), item))
                        .collect(Collectors.joining(", "));
                log.debug("==> {}", paramInfo);
            }

            int count = preparedStatement.executeUpdate();
            log.debug("<== affected row: {}", count);
            result = true;

            if (sqlOption.getSqlExecuteHook() != null) {
                sqlOption.getSqlExecuteHook().afterRun(connection, getSqlChainContext(), count);
            }
        } catch (Exception e) {
            log.error("execute error", e);
        } finally {
            try {
                // 关闭资源
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
                log.error("connection close error", e);
            }
        }
        return result;
    }

    public boolean exec() {
        Connection connection = null;
        try {
            connection = SqlConnectFactory.getConnection(getSqlChainContext().getSqlOption().getSourceName());
            return exec(connection);
        } catch (SQLException e) {
            log.error("get connection error", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Failed to close connection", e);
                }
            }
        }
        return false;
    }
}
