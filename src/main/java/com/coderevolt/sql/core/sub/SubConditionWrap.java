package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;

public class SubConditionWrap extends AbstractSub{

    private boolean cond;

    private AbstractSub sub;

    public SubConditionWrap(boolean cond, AbstractSub sub) {
        this.cond = cond;
        this.sub = sub;
    }

    @Override
    public String apply(SqlChainContext ctx) {
        if (cond) {
            this.sqlBuf.append(sub.apply(ctx));
            if (this.link != null) {
                this.sqlBuf.append(" ").append(link);
            }
        }
        return toSql();
    }
}
