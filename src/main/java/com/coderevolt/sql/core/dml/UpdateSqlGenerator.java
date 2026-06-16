package com.coderevolt.sql.core.dml;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.sql.attr.Column;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;
import com.coderevolt.sql.core.sub.SubSet;
import com.coderevolt.util.Assert;
import com.coderevolt.util.AtomicUtil;
import com.coderevolt.util.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UpdateSqlGenerator extends DMLSqlGenerator {

    private static final Logger log = LoggerFactory.getLogger(UpdateSqlGenerator.class);

    public UpdateSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    @Override
    public UpdateSqlGenerator update(Class<?> tableEntity) {
        return (UpdateSqlGenerator) super.update(tableEntity);
    }

    @Override
    public UpdateSqlGenerator set(SubSet... subSet) {
        return (UpdateSqlGenerator) super.set(subSet);
    }

    @Override
    public UpdateSqlGenerator where(AbstractSub sqlWhere) {
        return (UpdateSqlGenerator) super.where(sqlWhere);
    }

    @Override
    public UpdateSqlGenerator sql(String sql) {
        return (UpdateSqlGenerator) super.sql(sql);
    }

    public boolean updateById(Object data) {
        return genUpdateSqlExecutor(data).exec();
    }

    public boolean updateById(Object data, Connection connection) {
        return genUpdateSqlExecutor(data).exec(connection);
    }

    private UpdateSqlGenerator genUpdateSqlExecutor(Object data) {
        // 浅拷贝
        SqlOption sqlOptionClone = getSqlChainContext().getSqlOption().clone();
        SqlChainContext sqlChainContext = new SqlChainContext(sqlOptionClone);
        UpdateSqlGenerator copySqlGen = new UpdateSqlGenerator(sqlChainContext);
        copySqlGen.update(data.getClass());
        StringBuilder sqlBuf = copySqlGen.sqlBuf;
        sqlBuf.append(" SET ");

        Field idField = null;
        Column idColumn = null;
        Object idValue = null;

        Class<?> aClass = data.getClass();
        while (aClass != null && aClass != Object.class) {
            for (Field field : aClass.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Column column = field.getAnnotation(Column.class);
                    String fieldName = FieldUtil.underline(field.getName());
                    if (column != null) {
                        if (Column.ColumnType.ID == column.type()) {
                            Assert.isTrue(idColumn == null, "fount many id");
                            idField = field;
                            idColumn = column;
                            idValue = field.get(data);
                            Assert.isTrue(idValue != null, "id must not be null");
                        } else if (Column.DmlStrategy.IGNORE_COLUMN != column.dmlStrategy()){
                            if (Column.DmlStrategy.SET_NULL == column.dmlStrategy()) {
                                sqlBuf.append("@".equals(column.name()) ? fieldName : column.name()).append(" = ?,");
                                sqlChainContext.addParamValue(field.get(data));
                            } else if (Column.DmlStrategy.IGNORE_NULL == column.dmlStrategy() && field.get(data) != null) {
                                sqlBuf.append("@".equals(column.name()) ? fieldName : column.name()).append(" = ?,");
                                sqlChainContext.addParamValue(field.get(data));
                            }
                        }
                    } else if (field.get(data) != null) {
                        // 默认行为，属性为null不更新
                        sqlBuf.append(fieldName).append(" = ?,");
                        sqlChainContext.addParamValue(field.get(data));
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            aClass = aClass.getSuperclass();
        }
        Assert.isTrue(idField != null, "id not found");
        if (sqlBuf.charAt(sqlBuf.length() - 1) == ',') {
            sqlBuf.deleteCharAt(sqlBuf.length() - 1);
        }
        sqlBuf.append(" WHERE ").append("@".equals(idColumn.name()) ? FieldUtil.underline(idField.getName()) : idColumn.name()).append(" = ?");
        sqlChainContext.addParamValue(idValue);
        return copySqlGen;
    }

    public boolean updateByIdBatch(List<?> dataList) {
        String sourceName = getSqlChainContext().getSqlOption().getSourceName();
        Connection connection = null;
        try {
            connection = SqlConnectFactory.getConnection(sourceName);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return updateByIdBatch(dataList, connection);
    }

    public boolean updateByIdBatch(List<?> dataList, Connection connection) {
        if (dataList == null || dataList.isEmpty() || connection == null) {
            return false;
        }
        try {
            AtomicUtil.txExecute(connection, con -> {
                for (Object data : dataList) {
                    genUpdateSqlExecutor(data).exec(con);
                }
            });
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("connection close failed", e);
            }
        }
        return true;
    }
}
