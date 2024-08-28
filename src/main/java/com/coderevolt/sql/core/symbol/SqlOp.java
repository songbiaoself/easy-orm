package com.coderevolt.sql.core.symbol;

public class SqlOp {

    public enum M implements InterfaceSqlOp {
        EQ("="),
        LT("<"),
        LQ("<="),
        RT(">"),
        RQ(">="),
        NE("<>"),
        IN("IN"),
        BETWEEN("BETWEEN"),
        NOT_IN("NOT IN"),
        LIKE("LIKE");

        private final String value;

        M(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum L implements InterfaceSqlOp {
        EXISTS("EXISTS"),
        NOT_EXISTS("NOT EXISTS");

        private final String value;

        L(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum R implements InterfaceSqlOp {
        IS_NOT_NULL("IS NOT NULL"),
        IS_NULL("IS NULL");

        private final String value;

        R(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}
