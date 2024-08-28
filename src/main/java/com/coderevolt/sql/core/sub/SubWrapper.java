package com.coderevolt.sql.core.sub;

import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.symbol.SqlLink;

import java.util.ArrayList;
import java.util.List;

public class SubWrapper extends AbstractSub {

    private final List<AbstractSub> abstractSubs = new ArrayList<>();

    SubWrapper(AbstractSub abstractSub) {
        this.abstractSubs.add(abstractSub);
    }

    public static SubWrapper wrapper(AbstractSub abstractSub) {
        return new SubWrapper(abstractSub);
    }

    @Override
    public String apply(SqlChainContext ctx) {
        abstractSubs.forEach(item -> {
            sqlBuf.append("(").append(item.apply(ctx)).append(")");
            if (item.link != null) {
                sqlBuf.append(" ").append(link.name()).append(" ");
            }
        });
        return toSql();
    }

    public SubWrapper wrap(AbstractSub abstractSub) {
        abstractSubs.add(abstractSub);
        return this;
    }

    @Override
    public SubWrapper or() {
        if (!abstractSubs.isEmpty()) {
            abstractSubs.get(abstractSubs.size() - 1).setLink(SqlLink.OR);
        }
        return this;
    }

    @Override
    public SubWrapper and() {
        if (!abstractSubs.isEmpty()) {
            abstractSubs.get(abstractSubs.size() - 1).setLink(SqlLink.AND);
        }
        return this;
    }
}
