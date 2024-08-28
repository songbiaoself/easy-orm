package com.coderevolt.sql;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dml.DeleteSqlGenerator;
import com.coderevolt.sql.core.dml.InsertSqlGenerator;
import com.coderevolt.sql.core.dml.UpdateSqlGenerator;
import com.coderevolt.sql.core.dql.SelectSqlGenerator;

public class SqlExecutor {

    private final SqlOption sqlOption;

    private SqlExecutor(SqlOption sqlOption) {
        this.sqlOption = sqlOption;
    }

    public static SqlExecutor builder() {
        return builder(new SqlOption());
    }

    public static SqlExecutor builder(SqlOption sqlOption) {
        if (sqlOption.getSourceName() == null) {
            sqlOption.setSourceName(SqlConnectFactory.getDefaultSourceName());
        }
        return new SqlExecutor(sqlOption);
    }

    public InsertSqlGenerator insertChain() {
        return new InsertSqlGenerator(new SqlChainContext(sqlOption));
    }

    public UpdateSqlGenerator updateChain() {
        return new UpdateSqlGenerator(new SqlChainContext(sqlOption));
    }

    public DeleteSqlGenerator deleteChain() {
        return new DeleteSqlGenerator(new SqlChainContext(sqlOption));
    }

    public SelectSqlGenerator selectChain() {
        return new SelectSqlGenerator(new SqlChainContext(sqlOption));
    }

}
