package com.coderevolt.sql.core.dql;

import com.coderevolt.doc.NotNull;
import com.coderevolt.doc.Nullable;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubOrder;
import com.coderevolt.util.Assert;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SelectSqlGenerator extends DQLSqlGenerator {

    public SelectSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    @SafeVarargs
    @Override
    public final <T> SelectSqlGenerator select(@Nullable SFunction<T, ?>... columns) {
        return (SelectSqlGenerator) super.select(columns);
    }

    @Override
    public SelectSqlGenerator select(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelect, @NotNull String alias) {
        return (SelectSqlGenerator) super.select(subSelect, alias);
    }

    @Override
    public <T> SelectSqlGenerator select(SFunction<T, ?> column, String alias) {
        return (SelectSqlGenerator) super.select(column, alias);
    }

    @Override
    public SelectSqlGenerator from(@NotNull Class<?> tableEntity, String alias) {
        return (SelectSqlGenerator) super.from(tableEntity, alias);
    }

    public SelectSqlGenerator from(@NotNull Class<?> tableEntity) {
        return from(tableEntity, null);
    }

    @Override
    public SelectSqlGenerator from(SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, String alias) {
        return (SelectSqlGenerator) super.from(subSelectFunc, alias);
    }

    @Override
    public SelectSqlGenerator leftJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.leftJoin(tableEntity, alias, subOn);
    }

    public SelectSqlGenerator leftJoin(@NotNull Class<?> tableEntity, AbstractSub subOn) {
        return leftJoin(tableEntity, null, subOn);
    }

    @Override
    public SelectSqlGenerator leftJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.leftJoin(subSelectFunc, alias, subOn);
    }

    public SelectSqlGenerator leftJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias) {
        return leftJoin(subSelectFunc, alias, null);
    }

    @Override
    public SelectSqlGenerator rightJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.rightJoin(tableEntity, alias, subOn);
    }

    public SelectSqlGenerator rightJoin(@NotNull Class<?> tableEntity, AbstractSub subOn) {
        return rightJoin(tableEntity, null, subOn);
    }

    @Override
    public SelectSqlGenerator rightJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.rightJoin(subSelectFunc, alias, subOn);
    }

    public SelectSqlGenerator rightJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias) {
        return rightJoin(subSelectFunc, alias, null);
    }

    @Override
    public SelectSqlGenerator innerJoin(@NotNull Class<?> tableEntity, String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.innerJoin(tableEntity, alias, subOn);
    }

    public SelectSqlGenerator innerJoin(@NotNull Class<?> tableEntity) {
        return innerJoin(tableEntity, null, null);
    }

    public SelectSqlGenerator innerJoin(@NotNull Class<?> tableEntity, String alias) {
        return innerJoin(tableEntity, alias, null);
    }

    public SelectSqlGenerator innerJoin(@NotNull Class<?> tableEntity, AbstractSub subOn) {
        return innerJoin(tableEntity, null, subOn);
    }

    @Override
    public SelectSqlGenerator innerJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias, AbstractSub subOn) {
        return (SelectSqlGenerator) super.innerJoin(subSelectFunc, alias, subOn);
    }

    public SelectSqlGenerator innerJoin(@NotNull SelectFunction<SqlChainContext, SubSelectSqlGenerator> subSelectFunc, @NotNull String alias) {
        return innerJoin(subSelectFunc, alias, null);
    }

    @SafeVarargs
    @Override
    public final <T> SelectSqlGenerator groupBy(SFunction<T, ?>... field) {
        return (SelectSqlGenerator) super.groupBy(field);
    }

    @Override
    public SelectSqlGenerator where(@NotNull AbstractSub sqlWhere) {
        return (SelectSqlGenerator) super.where(sqlWhere);
    }

    @Override
    public SelectSqlGenerator having(@NotNull AbstractSub subHaving) {
        return (SelectSqlGenerator) super.having(subHaving);
    }

    @Override
    public final SelectSqlGenerator order(@NotNull SubOrder... subOrder) {
        return (SelectSqlGenerator) super.order(subOrder);
    }

    @Override
    public SelectSqlGenerator limit(Integer start, Integer count) {
        return (SelectSqlGenerator) super.limit(start, count);
    }

    public SelectSqlGenerator limit(Integer count) {
        return limit(null, count);
    }

    @Override
    public SelectSqlGenerator sql(String sql) {
        return (SelectSqlGenerator) super.sql(sql);
    }

    public <V> List<V> list(Class<V> clazz) throws SQLException {
        List<Map<String, Object>> resultSet = execute();
        List<V> result = new ArrayList<>(resultSet.size());
        for (Map<String, Object> row : resultSet) {
            try {
                V t = clazz.newInstance();
                for (Field declaredField : clazz.getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    Object value = row.get(declaredField.getName());
                    try {
                        declaredField.set(t, value);
                    } catch (RuntimeException e) {
                        if (value instanceof Number) {
                            // 值强转
                            Class<?> type = declaredField.getType();
                            if (type == float.class || type == Float.class) {
                                declaredField.set(t, ((Number) value).floatValue());
                            } else if (type == int.class || type == Integer.class) {
                                declaredField.set(t, ((Number) value).intValue());
                            } else if (type == short.class || type == Short.class) {
                                declaredField.set(t, ((Number) value).shortValue());
                            } else if (type == byte.class || type == Byte.class) {
                                declaredField.set(t, ((Number) value).byteValue());
                            }
                        } else {
                            throw e;
                        }
                    }
                }
                result.add(t);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    public <V> V one(Class<V> clazz) throws SQLException {
        List<Map<String, Object>> resultSet = execute();
        Assert.isTrue(resultSet.size() <= 1, "found many rows: " + resultSet.size());
        if (resultSet.size() == 1) {
            Map<String, Object> map = resultSet.get(0);
            try {
                V t = clazz.newInstance();
                for (Field declaredField : clazz.getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    declaredField.set(t, map.get(declaredField.getName()));
                }
                return t;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    public Map<String, Object> map() throws SQLException {
        List<Map<String, Object>> resultSet = execute();
        Assert.isTrue(resultSet.size() <= 1, "found many rows: " + resultSet.size());
        if (resultSet.size() == 1) {
            return resultSet.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> listMap() throws SQLException {
        return execute();
    }


}
