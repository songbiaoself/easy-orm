package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.symbol.SqlLink;

public abstract class AbstractSub {

    protected final StringBuilder sqlBuf = new StringBuilder();

    protected SqlLink link;

    protected void setLink(SqlLink link) {
        this.link = link;
    }

    public String toSql() {
        return sqlBuf.toString();
    }

    public AbstractSub sql(String sql) {
        this.sqlBuf.append(sql);
        return this;
    }

    protected AbstractSub or() {
        return this;
    }

    protected AbstractSub and() {
        return this;
    }

    public abstract String apply(SqlChainContext ctx);
}
