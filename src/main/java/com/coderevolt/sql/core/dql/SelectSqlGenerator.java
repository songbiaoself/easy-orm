package com.coderevolt.sql.core.dql;

import com.coderevolt.doc.NotNull;
import com.coderevolt.doc.Nullable;
import com.coderevolt.sql.attr.Column;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubOrder;
import com.coderevolt.util.Assert;
import com.coderevolt.util.FieldUtil;
import com.coderevolt.util.SFunction;
import com.coderevolt.util.SelectFunction;

import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
                result.add(mapField(clazz, row));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    private static <V> V mapField(Class<V> clazz, Map<String, Object> map) throws InstantiationException, IllegalAccessException {
        if (clazz.isInstance(map)) {
            return (V) map;
        }
        if (clazz.isAssignableFrom(Number.class)) {
            // 兼容包装类
            Object value = map.values().iterator().next();
            if (clazz.isAssignableFrom(Integer.class)) {
                return (V) Integer.valueOf(value.toString());
            } else if (clazz.isAssignableFrom(Long.class)) {
                return (V) Long.valueOf(value.toString());
            } else if (clazz.isAssignableFrom(Double.class)) {
                return (V) Double.valueOf(value.toString());
            } else if (clazz.isAssignableFrom(Float.class)) {
                return (V) Float.valueOf(value.toString());
            } else if (clazz.isAssignableFrom(Short.class)) {
                return (V) Short.valueOf(value.toString());
            } else if (clazz.isAssignableFrom(Byte.class)) {
                return (V) Byte.valueOf(value.toString());
            } else {
                throw new IllegalStateException("convert error, unsupported number type :" + clazz);
            }
        }
        if (clazz.isAssignableFrom(String.class)) {
            return (V) map.values().iterator().next().toString();
        }
        if (clazz.isAssignableFrom(Boolean.class)) {
            return (V) Boolean.valueOf(map.values().iterator().next().toString());
        }
        if (isTemporalType(clazz)) {
            return (V) convertTemporalValue(map.values().iterator().next(), clazz);
        }

        V t = clazz.newInstance();
        Class<?> currentClass = clazz;
        while (currentClass != null && !Object.class.equals(currentClass)) {
            for (Field declaredField : currentClass.getDeclaredFields()) {
                Object value = null;
                Column column = declaredField.getAnnotation(Column.class);
                if (column != null && !"@".equals(column.name())) {
                    value = map.get(FieldUtil.camelCase(column.name()));
                } else {
                    value = map.get(declaredField.getName());
                }
                declaredField.setAccessible(true);
                try {
                    declaredField.set(t, convertTemporalValue(value, declaredField.getType()));
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
                        } else if (type == long.class || type == Long.class) {
                            declaredField.set(t, ((Number) value).longValue());
                        } else if (type == double.class || type == Double.class) {
                            declaredField.set(t, ((Number) value).doubleValue());
                        } else {
                            throw new IllegalStateException("convert error, unsupported number type :" + clazz);
                        }
                    } else {
                        throw e;
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return t;
    }

    private static boolean isTemporalType(Class<?> type) {
        return type == java.util.Date.class
                || type == java.sql.Date.class
                || type == Time.class
                || type == Timestamp.class
                || type == Instant.class
                || type == LocalDate.class
                || type == LocalTime.class
                || type == LocalDateTime.class
                || type == OffsetDateTime.class
                || type == ZonedDateTime.class;
    }

    private static Object convertTemporalValue(Object value, Class<?> targetType) {
        if (value == null || targetType.isInstance(value) || !isTemporalType(targetType)) {
            return value;
        }
        if (targetType == java.util.Date.class) {
            return toJavaUtilDate(value);
        }
        if (targetType == java.sql.Date.class) {
            return toSqlDate(value);
        }
        if (targetType == Time.class) {
            return toSqlTime(value);
        }
        if (targetType == Timestamp.class) {
            return toTimestamp(value);
        }
        if (targetType == Instant.class) {
            return toInstant(value);
        }
        if (targetType == LocalDate.class) {
            return toLocalDate(value);
        }
        if (targetType == LocalTime.class) {
            return toLocalTime(value);
        }
        if (targetType == LocalDateTime.class) {
            return toLocalDateTime(value);
        }
        if (targetType == OffsetDateTime.class) {
            return toOffsetDateTime(value);
        }
        if (targetType == ZonedDateTime.class) {
            return toZonedDateTime(value);
        }
        return value;
    }

    private static java.util.Date toJavaUtilDate(Object value) {
        if (value instanceof java.util.Date) {
            return new java.util.Date(((java.util.Date) value).getTime());
        }
        return java.util.Date.from(toInstant(value));
    }

    private static java.sql.Date toSqlDate(Object value) {
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof LocalDate) {
            return java.sql.Date.valueOf((LocalDate) value);
        }
        return java.sql.Date.valueOf(toLocalDate(value));
    }

    private static Time toSqlTime(Object value) {
        if (value instanceof Time) {
            return (Time) value;
        }
        if (value instanceof LocalTime) {
            return Time.valueOf((LocalTime) value);
        }
        return Time.valueOf(toLocalTime(value));
    }

    private static Timestamp toTimestamp(Object value) {
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof LocalDateTime) {
            return Timestamp.valueOf((LocalDateTime) value);
        }
        if (value instanceof Instant) {
            return Timestamp.from((Instant) value);
        }
        if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime());
        }
        return Timestamp.valueOf(toLocalDateTime(value));
    }

    private static Instant toInstant(Object value) {
        if (value instanceof Instant) {
            return (Instant) value;
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toInstant();
        }
        if (value instanceof java.util.Date) {
            return Instant.ofEpochMilli(((java.util.Date) value).getTime());
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant();
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (value instanceof LocalTime) {
            return ((LocalTime) value).atDate(LocalDate.ofEpochDay(0)).atZone(ZoneId.systemDefault()).toInstant();
        }
        if (value instanceof OffsetDateTime) {
            return ((OffsetDateTime) value).toInstant();
        }
        if (value instanceof ZonedDateTime) {
            return ((ZonedDateTime) value).toInstant();
        }
        throw new IllegalStateException("convert error, unsupported time type :" + value.getClass());
    }

    private static LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate();
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalDate();
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime().toLocalDate();
        }
        if (value instanceof java.util.Date || value instanceof Instant || value instanceof OffsetDateTime || value instanceof ZonedDateTime) {
            return toInstant(value).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        throw new IllegalStateException("convert error, unsupported time type :" + value.getClass());
    }

    private static LocalTime toLocalTime(Object value) {
        if (value instanceof LocalTime) {
            return (LocalTime) value;
        }
        if (value instanceof Time) {
            return ((Time) value).toLocalTime();
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalTime();
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime().toLocalTime();
        }
        if (value instanceof java.util.Date || value instanceof Instant || value instanceof OffsetDateTime || value instanceof ZonedDateTime) {
            return toInstant(value).atZone(ZoneId.systemDefault()).toLocalTime();
        }
        throw new IllegalStateException("convert error, unsupported time type :" + value.getClass());
    }

    private static LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate().atStartOfDay();
        }
        if (value instanceof Time) {
            return ((Time) value).toLocalTime().atDate(LocalDate.ofEpochDay(0));
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay();
        }
        if (value instanceof LocalTime) {
            return ((LocalTime) value).atDate(LocalDate.ofEpochDay(0));
        }
        if (value instanceof java.util.Date || value instanceof Instant || value instanceof OffsetDateTime || value instanceof ZonedDateTime) {
            return toInstant(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        throw new IllegalStateException("convert error, unsupported time type :" + value.getClass());
    }

    private static OffsetDateTime toOffsetDateTime(Object value) {
        if (value instanceof OffsetDateTime) {
            return (OffsetDateTime) value;
        }
        if (value instanceof ZonedDateTime) {
            return ((ZonedDateTime) value).toOffsetDateTime();
        }
        return toInstant(value).atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    private static ZonedDateTime toZonedDateTime(Object value) {
        if (value instanceof ZonedDateTime) {
            return (ZonedDateTime) value;
        }
        if (value instanceof OffsetDateTime) {
            return ((OffsetDateTime) value).toZonedDateTime();
        }
        return toInstant(value).atZone(ZoneId.systemDefault());
    }

    public <V> V one(Class<V> clazz) throws SQLException {
        List<Map<String, Object>> resultSet = execute();
        Assert.isTrue(resultSet.size() <= 1, "found many rows: " + resultSet.size());
        if (resultSet.size() == 1) {
            try {
                return mapField(clazz, resultSet.get(0));
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
