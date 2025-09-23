package com.coderevolt.sql.config;

import com.coderevolt.sql.core.SqlChainContext;

import java.sql.Statement;

public interface SqlExecuteHook {

    /**
     * sql执行前调用
     * @param statement 会话
     * @param sqlChainContext sql执行链上下文
     */
    void beforeRun(Statement statement, SqlChainContext sqlChainContext);

    /**
     * sql执行后调用
     * @param statement
     * @param sqlChainContext
     * @param result 请求结果
     */
    void afterRun(Statement statement, SqlChainContext sqlChainContext, Object result);

}
