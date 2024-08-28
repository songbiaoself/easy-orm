package com.coderevolt.sql.core.dml;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;
import com.coderevolt.util.Assert;
import com.coderevolt.util.SFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InsertSqlGenerator extends DMLSqlGenerator {

    private static final Logger log = LoggerFactory.getLogger(InsertSqlGenerator.class);

    public InsertSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    @SafeVarargs
    @Override
    public final <T> InsertSqlGenerator insertInto(Class<T> tableEntity, SFunction<T, ?>... fields) {
        return (InsertSqlGenerator) super.insertInto(tableEntity, fields);
    }

    @Override
    public InsertSqlGenerator values(Object data) {
        return (InsertSqlGenerator) super.values(data);
    }

    @Override
    public InsertSqlGenerator sql(String sql) {
        return (InsertSqlGenerator) super.sql(sql);
    }

    public InsertSqlGenerator values(List<?> dataList) {
        Assert.isNotEmpty(dataList, "dataList must not be empty");
        dataList.forEach(data -> {
            values(data);
            sqlBuf.append(",");
        });
        sqlBuf.deleteCharAt(sqlBuf.length() - 1);
        return this;
    }

    public InsertSqlGenerator select(SubSelectSqlGenerator subSelectSqlGenerator) {
        sqlBuf.append(" ").append(subSelectSqlGenerator.toSql());
        return this;
    }


}
