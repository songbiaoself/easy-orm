package com.coderevolt.sql.core.dml;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.sub.AbstractSub;

public class DeleteSqlGenerator extends DMLSqlGenerator {

    public DeleteSqlGenerator(SqlChainContext sqlChainContext) {
        super(sqlChainContext);
    }

    @Override
    public DeleteSqlGenerator deleteFrom(Class<?> tableEntity) {
        return (DeleteSqlGenerator) super.deleteFrom(tableEntity);
    }

    @Override
    public DeleteSqlGenerator where(AbstractSub sqlWhere) {
        return (DeleteSqlGenerator) super.where(sqlWhere);
    }

    @Override
    public DeleteSqlGenerator sql(String sql) {
        return (DeleteSqlGenerator) super.sql(sql);
    }
}
