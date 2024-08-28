package com.coderevolt.sql.config;

import java.sql.Connection;

/**
 * sql执行链参数可以放在这个类
 */
public class SqlOption implements Cloneable {

    /**
     * 执行数据源
     */
    private String sourceName;

    /**
     * 隔离级别
     */
    private TransactionIsolationEnum transactionIsolation;

    /**
     * sql执行钩子
     */
    private SqlExecuteHook sqlExecuteHook;

    public SqlExecuteHook getSqlExecuteHook() {
        return sqlExecuteHook;
    }

    public void setSqlExecuteHook(SqlExecuteHook sqlExecuteHook) {
        this.sqlExecuteHook = sqlExecuteHook;
    }

    public static SqlOptionBuilder builder() {
        return new SqlOptionBuilder();
    }

    public TransactionIsolationEnum getTransactionIsolation() {
        return transactionIsolation;
    }

    public void setTransactionIsolation(TransactionIsolationEnum transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public SqlOption clone() {
        try {
            SqlOption sqlOptionClone = (SqlOption) super.clone();
            // 成员变量深拷贝：成员变量实现cloneable，给sqlOptionClone属性重新赋值
            return sqlOptionClone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * @see Connection#setTransactionIsolation(int)
     */
    public static enum TransactionIsolationEnum {

        TRANSACTION_NONE(0),

        TRANSACTION_READ_UNCOMMITTED(1),

        TRANSACTION_READ_COMMITTED(2),

        TRANSACTION_REPEATABLE_READ(4),

        TRANSACTION_SERIALIZABLE(8);

        private final int value;

        TransactionIsolationEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class SqlOptionBuilder {

        private static final SqlOption sqlOption = new SqlOption();

        public SqlOptionBuilder sourceName(String sourceName) {
            sqlOption.sourceName = sourceName;
            return this;
        }

        public SqlOptionBuilder transactionIsolation(TransactionIsolationEnum transactionIsolation) {
            sqlOption.transactionIsolation = transactionIsolation;
            return this;
        }

        public SqlOptionBuilder sqlExecuteHook(SqlExecuteHook sqlExecuteHook) {
            sqlOption.sqlExecuteHook = sqlExecuteHook;
            return this;
        }

        public SqlOption build() {
            return sqlOption;
        }

    }

}
