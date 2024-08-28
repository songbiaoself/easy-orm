package com.coderevolt.sql;

import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SubSelectSqlGenerator;

public class SubSelectBuilder {

    public static SubSelectSqlGenerator create() {
        return create(new SqlOption());
    }

    public static SubSelectSqlGenerator create(SqlOption sqlOption) {
        return new SubSelectSqlGenerator(new SqlChainContext(sqlOption));
    }
}
